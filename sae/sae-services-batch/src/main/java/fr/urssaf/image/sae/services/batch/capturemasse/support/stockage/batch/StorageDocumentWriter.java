/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataHandler;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.model.CaptureMasseControlResult;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item writer de l'écriture des documents dans DFCE
 * 
 */
@Component
public class StorageDocumentWriter extends AbstractDocumentWriterListener
      implements ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentWriter.class);

   @Autowired
   private InsertionPoolThreadExecutor poolExecutor;

   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider serviceProvider;

   @Autowired
   private TracesControlesSupport tracesSupport;

   private static final String TRC_INSERT = "StorageDocumentWriter()";
   private static final String CATCH = "AvoidCatchingThrowable";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends StorageDocument> items)
         throws Exception {

      Runnable command;
      int index = 0;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {

         command = new InsertionRunnable(getStepExecution().getReadCount()
               + index, storageDocument, this);

         poolExecutor.execute(command);

         LOGGER.debug("{} - nombre de documents en attente dans le pool : {}",
               TRC_INSERT, poolExecutor.getQueue().size());

         index++;

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
    */
   @SuppressWarnings(CATCH)
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {

      try {
         final StorageDocument retour = serviceProvider
               .getStorageDocumentService().insertStorageDocument(
                     storageDocument);

         // trace les éventuelles erreurs d'identification ou de validation
         traceErreurIdentOrValid(storageDocument, retour);
         
         // ajout le fichier d'origine en pièce jointe
         // si le document a été compressé
         ajoutFichierOrigineSiNecessaire(retour);         

         return retour;
      } catch (Exception except) {

         throw new InsertionServiceEx("SAE-ST-INS001", except.getMessage(),
               except);

         /* nous sommes obligés de récupérer les throwable pour les erreurs DFCE */
      } catch (Throwable except) {

         throw new InsertionServiceEx("SAE-ST-INS001", except.getMessage(),
               except);

      }

   }

   /**
    * Methode permettant d'ajouter le fichier d'origine si le document qui a été
    * stocké en piece principale et un document issu de la compression.
    * 
    * @param retour
    *           document principal
    * @throws FileNotFoundException
    *            Exception levée si le fichier n'existe pas.
    * @throws StorageDocAttachmentServiceEx
    *            Exception levée lors d'une erreur d'ajout de pièce jointe
    */
   @SuppressWarnings("unchecked")
   private void ajoutFichierOrigineSiNecessaire(final StorageDocument retour)
         throws FileNotFoundException, StorageDocAttachmentServiceEx {

      if (getStepExecution() != null) {
         // recupere la map des documents compressés
         Map<String, CompressedDocument> documentsCompressed = (Map<String, CompressedDocument>) getStepExecution()
               .getJobExecution().getExecutionContext().get("documentsCompressed");
   
         if (documentsCompressed != null) {
            String pathOriginalFile = null;
            String originalName = null;
            // on parcours la liste des documents compressés pour savoir si le
            // storage document
            // est un document compressé (il a ete remplacé par le
            // replacementDocumentProcessor)
            // si c'est le cas, il faut stocké l'original en pièce jointe
            for (Entry<String, CompressedDocument> entry : documentsCompressed
                  .entrySet()) {
               CompressedDocument doc = entry.getValue();
               if (doc.getFileName().equals(retour.getFileName())) {
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
               // calcul du nom du fichier et de l'extension
               String docName = FilenameUtils.getBaseName(originalName);
               String extension = FilenameUtils.getExtension(originalName);
               InputStreamSource streamSource = new InputStreamSource(
                     new FileInputStream(pathOriginalFile));
               // on a un doc original qu'on va mettre en piece jointe
               serviceProvider.getStorageDocumentService().addDocumentAttachment(
                     retour.getUuid(), docName, extension,
                     new DataHandler(streamSource));
            }
         }
      }
   }

   @SuppressWarnings("unchecked")
   private void traceErreurIdentOrValid(final StorageDocument storageDocument,
         final StorageDocument retour) {
      String trcPrefix = "traceErreurIdentOrValid";
      LOGGER.debug("{} - début", trcPrefix);

      if (getStepExecution() != null) {
         // récupére la map de resultat de controle de capture de masse
         Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) getStepExecution()
               .getJobExecution().getExecutionContext().get(
                     "mapCaptureControlResult");
         if (map == null) {
            LOGGER
                  .debug(
                        "{} - Map des résultat de controle non présente dans le contexte d'éxécution",
                        trcPrefix);
         } else {
            LOGGER.debug("{} - Map des résultat de controle récupéré",
                  trcPrefix);
            // recupére le résultat de controle de capture de masse du document
            // archive
            CaptureMasseControlResult resultat = map.get(storageDocument
                  .getFilePath());
            if (resultat == null) {
               LOGGER
                     .debug(
                           "{} - Résultat de controle non présent dans la map pour la key {}",
                           trcPrefix, storageDocument.getFilePath());
            } else {
               LOGGER
                     .debug(
                           "{} - Récupération OK du résultat de controle pour le document",
                           trcPrefix);

               // Récupère le nom de l'opération du WS
               String contexte = Constantes.CONTEXTE_CAPTURE_MASSE;

               // Récupère le format du fichier
               String formatFichier = findMetadataValue("ffi", storageDocument
                     .getMetadatas());

               LOGGER.debug("{} - Format de fichier : {}", trcPrefix,
                     formatFichier);

               // Récupère l'identifiant de traitement unitaire
               String idTraitement = findMetadataValue("iti", storageDocument
                     .getMetadatas());

               LOGGER.debug("{} - Identifiant du traitement : {}", trcPrefix,
                     idTraitement);

               if (resultat.isIdentificationActivee()
                     && resultat.isIdentificationEchecMonitor()) {
                  tracesSupport.traceErreurIdentFormatFichier(contexte,
                        formatFichier, resultat.getIdFormatReconnu(), retour
                              .getUuid().toString().toString(), idTraitement);
               }

               if (resultat.isValidationActivee()
                     && resultat.isValidationEchecMonitor()) {
                  tracesSupport.traceErreurValidFormatFichier(contexte,
                        formatFichier, resultat.getDetailEchecValidation(),
                        retour.getUuid().toString(), idTraitement);
               }
            }
         }
      }
      LOGGER.debug("{} - fin", trcPrefix);
   }

   private String findMetadataValue(String metaName,
         List<StorageMetadata> metadatas) {
      int index = 0;
      String valeur = null;
      boolean trouve = false;

      do { // récupération de la valeur de la métadonnéé "FormatFichier"
         StorageMetadata metadata = metadatas.get(index);
         if (StringUtils.equalsIgnoreCase(metadata.getShortCode(), metaName)) {
            trouve = true;
            valeur = (String) metadata.getValue();
         }
         index++;
      } while (!trouve && index < metadatas.size());

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
