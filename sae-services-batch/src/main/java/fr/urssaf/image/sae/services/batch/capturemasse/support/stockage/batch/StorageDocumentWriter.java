/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.model.CaptureMasseControlResult;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionCapturePoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item writer de l'écriture des documents dans DFCE
 */
@Component
public class StorageDocumentWriter extends AbstractDocumentWriterListener
implements
ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentWriter.class);

   @Autowired
   private InsertionCapturePoolThreadExecutor poolExecutor;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   @Autowired
   private TracesControlesSupport tracesSupport;

   private static final String TRC_INSERT = "StorageDocumentWriter()";

   private int docIndexInWriter = 0;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends StorageDocument> items)
         throws Exception {

      Runnable command;

      for (final StorageDocument storageDocument : Utils.nullSafeIterable(items)) {
         boolean isdocumentATraite = true;
         if (isModePartielBatch()) {
            isdocumentATraite = isDocumentATraite(docIndexInWriter);
         }

         // Si le document n'est pas en erreur, on traite, sinon on passe au
         // suivant.
         if (isdocumentATraite) {
            command = new InsertionRunnable(docIndexInWriter, storageDocument, this);

            try {
               poolExecutor.execute(command);
            }
            catch (final Exception ex) {
               // Reprise - Si traitement déjà réaliser par le traitement nominal, on déclare le document comme traité.
               if (isModePartielBatch()) {
                  // En mode partiel, on ajoute l'exception à la liste des exceptions et on continue le traitement des documents.
                  getCodesErreurListe().add(Constantes.ERR_BUL002);
                  getIndexErreurListe().add(docIndexInWriter);
                  final String message = ex.getMessage();
                  getErrorMessageList().add(message);
                  LOGGER.warn(message, ex);
               }
               else {
                  throw ex;
               }
            }

         }

         docIndexInWriter++;
         LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
               TRC_INSERT,
               "Queue : " + poolExecutor.getQueue().size() + " - Total : " + poolExecutor.getTaskCount() + " - Actifs : "
                     + poolExecutor.getActiveCount());
      }

   }

   /**
    * {@inheritDoc}
    */
   // Attention : cette méthode est appelée depuis un pool de thread. Elle ne doit pas utiliser docIndexInWriter dont la valeur change
   @Override
   public UUID launchTraitement(final AbstractStorageDocument storageDocument, final int docIndex)
         throws Exception {
      try {
         final StorageDocument document = insertStorageDocument((StorageDocument) storageDocument);
         final UUID uuid = document != null ? document.getUuid() : null;
         return uuid;
      }
      catch (final TraitementRepriseAlreadyDoneException ex) {
         throw ex;
      }
      catch (final Exception ex) {
         if (isModePartielBatch()) {
            synchronized (this) {
               getCodesErreurListe().add(Constantes.ERR_BUL002);
               getIndexErreurListe().add(docIndex);
               getErrorMessageList().add(ex.getMessage());
               LOGGER.warn("Une erreur est survenue lors de la persistance de document",
                     ex);
               return null;
            }
         } else {
            throw ex;
         }
      }
   }

   /**
    * Persistance du document
    *
    * @param storageDocument
    *           document à sauvegarder
    * @return le document avec l'uuid renseigné
    * @throws InsertionServiceEx
    *            Exception levée lors de la persistance
    * @throws TraitementRepriseAlreadyDoneException
    */
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument)
               throws InsertionServiceEx,
               TraitementRepriseAlreadyDoneException {

      try {
         final StorageDocument retour = serviceProvider
               .getStorageDocumentService()
               .insertStorageDocument(
                     storageDocument);

         // trace les éventuelles erreurs d'identification ou de validation
         traceErreurIdentOrValid(storageDocument, retour);

         // ajout le fichier d'origine en pièce jointe
         // si le document a été compressé
         ajoutFichierOrigineSiNecessaire(storageDocument, retour);

         return retour;
      }
      catch (final InsertionIdGedExistantEx except) {

         try {
            if (isRepriseActifBatch()
                  && verificationTraitementReprise(storageDocument)) {
               throw new TraitementRepriseAlreadyDoneException(except);
            }
         }
         catch (final RetrievalServiceEx e) {
            // Do nothing exception levé juste après
         }

         throw new InsertionServiceEx("SAE-ST-INS001",
               except.getMessage(),
               except);

      }
      catch (final Exception except) {

         throw new InsertionServiceEx("SAE-ST-INS001",
               except.getMessage(),
               except);

      }

   }

   /**
    * Vérifie que la reprise est dans un cas d'erreur ou un cas de reprise d'un
    * document déjà traité.
    *
    * @param storageDocument
    *           Document
    * @return True si cas passant, false sinon.
    * @throws RetrievalServiceEx
    * @{@link RetrievalServiceEx}
    */
   private boolean verificationTraitementReprise(final StorageDocument storageDocument)
         throws RetrievalServiceEx {
      boolean retour = false;
      final String idJobEnCours = getStepExecution().getJobParameters()
            .getString(Constantes.ID_TRAITEMENT);
      final List<StorageMetadata> listeMetadatas = storageDocument.getMetadatas();

      if (idJobEnCours != null && !idJobEnCours.isEmpty()) {
         // On recherche la metadonnée idTraitementInterne dans la liste des
         // métadonnées du document.
         StorageMetadata metadata = retrieveMetadonneeByList(listeMetadatas,
               StorageTechnicalMetadatas.ID_TRAITEMENT_MASSE_INTERNE
               .getShortCode());

         if (metadata == null) {
            // Si on ne trouve pas la métadonnée idTraitementInterne, on
            // l'ajoute à la liste des métadonnées.
            listeMetadatas.add(new StorageMetadata(
                  StorageTechnicalMetadatas.ID_TRAITEMENT_MASSE_INTERNE
                  .getShortCode()));
         }

         final StorageMetadata metadataIdGed = retrieveMetadonneeByList(
               listeMetadatas,
               StorageTechnicalMetadatas.IDGED.getShortCode());

         final UUID uuid = UUID.fromString(metadataIdGed.getValue().toString());

         // On met à jour les metadatas du document
         final UUIDCriteria uuidCriteria = new UUIDCriteria(uuid, listeMetadatas);
         final List<StorageMetadata> listeMetadatasRetrieve = serviceProvider
               .getStorageDocumentService()
               .retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);

         // On vérifie qu'elle se trouve bien dans la liste mise à jour.
         metadata = retrieveMetadonneeByList(listeMetadatasRetrieve,
               StorageTechnicalMetadatas.ID_TRAITEMENT_MASSE_INTERNE
               .getShortCode());
         if (metadata != null
               && idJobEnCours.equals(metadata.getValue().toString())) {
            // On alimente l'uuid du document pour ne pas bloquer le reste du
            // traitement.
            storageDocument.setUuid(uuid);
            // Si la métadonnée existe, la vérification est OK.
            retour = true;
         }

      }

      return retour;
   }

   /**
    * Permet de retouver une metadonnée à partir d'une liste de metadonées.
    *
    * @param listeMetadatas
    *           Liste métadonnées
    * @param shortCode
    *           Code à retrouver
    * @return La metadonnée recherchée
    */
   private StorageMetadata retrieveMetadonneeByList(
         final List<StorageMetadata> listeMetadatas, final String shortCode) {
      if (shortCode != null && !shortCode.isEmpty()) {
         for (final StorageMetadata storageMetadata : Utils
               .nullSafeIterable(listeMetadatas)) {
            final StorageTechnicalMetadatas technical = Utils
                  .technicalMetadataFinder(storageMetadata.getShortCode());

            if (shortCode.equals(technical.getShortCode())) {
               return storageMetadata;
            }
         }
      }
      return null;
   }

   /**
    * Methode permettant d'ajouter le fichier d'origine si le document qui a été
    * stocké en piece principale et un document issu de la compression.
    *
    * @param storageDocument
    *           document insere
    * @param retour
    *           document principal
    * @throws FileNotFoundException
    *            Exception levée si le fichier n'existe pas.
    * @throws StorageDocAttachmentServiceEx
    *            Exception levée lors d'une erreur d'ajout de pièce jointe
    */
   @SuppressWarnings("unchecked")
   private void ajoutFichierOrigineSiNecessaire(
         final StorageDocument storageDocument, final StorageDocument retour)
               throws FileNotFoundException, StorageDocAttachmentServiceEx {
      final String trcPrefix = "ajoutFichierOrigineSiNecessaire";

      if (getStepExecution() != null) {
         // recupere la map des documents compressés
         final Map<String, CompressedDocument> documentsCompressed = (Map<String, CompressedDocument>) getStepExecution()
               .getJobExecution()
               .getExecutionContext()
               .get("documentsCompressed");

         if (documentsCompressed != null) {
            LOGGER.debug("{} - Clé de la map des documents compresses : {}",
                  trcPrefix,
                  documentsCompressed.keySet());
            LOGGER.debug("{} - Recherche du document : {}",
                  trcPrefix,
                  storageDocument.getFilePath());
            String pathOriginalFile = null;
            String originalName = null;
            // on parcours la liste des documents compressés pour savoir si le
            // storage document
            // est un document compressé (il a ete remplacé par le
            // replacementDocumentProcessor)
            // si c'est le cas, il faut stocké l'original en pièce jointe
            for (final Entry<String, CompressedDocument> entry : documentsCompressed
                  .entrySet()) {
               final CompressedDocument doc = entry.getValue();
               LOGGER.debug("{} - Analyse du document : {}",
                     trcPrefix,
                     doc.getFilePath());
               if (doc.getFilePath().equals(storageDocument.getFilePath())) {
                  // on a trouvé le nom du fichier dans les listes des fichiers
                  // compressés
                  // donc, c'est le document compressés qui a été stockés
                  // on doit donc recuperer le chemin vers l'original
                  // pour l'ajouter en piece jointe
                  pathOriginalFile = entry.getKey();
                  originalName = doc.getOriginalName();
                  break;
               }
            }
            if (pathOriginalFile != null) {
               LOGGER.debug("{} - Document original trouvé : {}",
                     trcPrefix,
                     pathOriginalFile);
               // calcul du nom du fichier et de l'extension
               final String docName = FilenameUtils.getBaseName(originalName);
               final String extension = FilenameUtils.getExtension(originalName);
               final DataHandler contenu = new DataHandler(new FileDataSource(
                     new File(pathOriginalFile)));
               // on a un doc original qu'on va mettre en piece jointe
               serviceProvider.getStorageDocumentService()
               .addDocumentAttachment(retour.getUuid(),
                     docName,
                     extension,
                     contenu);
            } else {
               LOGGER.debug("{} - Document original non trouvé", trcPrefix);
            }
         } else {
            LOGGER.debug("{} - La map des documents compresses n'existe pas dans le contexte spring",
                  trcPrefix);
         }
      }
   }

   @SuppressWarnings("unchecked")
   private void traceErreurIdentOrValid(final StorageDocument storageDocument,
         final StorageDocument retour) {
      final String trcPrefix = "traceErreurIdentOrValid";
      LOGGER.debug("{} - début", trcPrefix);

      if (getStepExecution() != null) {
         // récupére la map de resultat de controle de capture de masse
         final Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) getStepExecution()
               .getJobExecution()
               .getExecutionContext()
               .get("mapCaptureControlResult");
         if (map == null) {
            LOGGER.debug(
                  "{} - Map des résultat de controle non présente dans le contexte d'éxécution",
                  trcPrefix);
         } else {
            LOGGER.debug("{} - Map des résultat de controle récupéré",
                  trcPrefix);
            // recupére le résultat de controle de capture de masse du document
            // archive
            final CaptureMasseControlResult resultat = map.get(storageDocument
                  .getFilePath());
            if (resultat == null) {
               LOGGER.debug("{} - Résultat de controle non présent dans la map pour la key {}",
                     trcPrefix,
                     storageDocument.getFilePath());
            } else {
               LOGGER.debug("{} - Récupération OK du résultat de controle pour le document",
                     trcPrefix);

               // Récupère le nom de l'opération du WS
               final String contexte = Constantes.CONTEXTE_CAPTURE_MASSE;

               // Récupère le format du fichier
               final String formatFichier = findMetadataValue("ffi",
                     storageDocument.getMetadatas());

               LOGGER.debug("{} - Format de fichier : {}",
                     trcPrefix,
                     formatFichier);

               // Récupère l'identifiant de traitement unitaire
               final String idTraitement = findMetadataValue(
                     StorageTechnicalMetadatas.ID_TRAITEMENT_MASSE_INTERNE
                     .getShortCode(),
                     storageDocument.getMetadatas());

               LOGGER.debug("{} - Identifiant du traitement : {}",
                     trcPrefix,
                     idTraitement);

               if (resultat.isIdentificationActivee()
                     && resultat.isIdentificationEchecMonitor()) {
                  tracesSupport.traceErreurIdentFormatFichier(contexte,
                        formatFichier,
                        resultat.getIdFormatReconnu(),
                        retour
                        .getUuid()
                        .toString()
                        .toString(),
                        idTraitement);
               }

               if (resultat.isValidationActivee()
                     && resultat.isValidationEchecMonitor()) {
                  tracesSupport.traceErreurValidFormatFichier(contexte,
                        formatFichier,
                        resultat.getDetailEchecValidation(),
                        retour.getUuid().toString(),
                        idTraitement);
               }
            }
         }
      }
      LOGGER.debug("{} - fin", trcPrefix);
   }

   private String findMetadataValue(final String metaName,
         final List<StorageMetadata> metadatas) {
      int metaIndex = 0;
      String valeur = null;
      boolean trouve = false;

      do { // récupération de la valeur de la métadonnéé "FormatFichier"
         final StorageMetadata metadata = metadatas.get(metaIndex);
         if (StringUtils.equalsIgnoreCase(metadata.getShortCode(), metaName)) {
            trouve = true;
            valeur = (String) metadata.getValue();
         }
         metaIndex++;
      } while (!trouve && metaIndex < metadatas.size());

      return valeur;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final StorageServiceProvider getServiceProvider() {
      return serviceProvider;
   }
}
