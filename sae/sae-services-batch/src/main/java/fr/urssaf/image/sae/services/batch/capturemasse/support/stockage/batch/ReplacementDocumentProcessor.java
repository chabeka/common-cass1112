package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Item processor pour remplacer le document d'origine par le document compressé
 * si le document a été compressé.
 *
 */
@Component
public class ReplacementDocumentProcessor extends AbstractListener implements
      ItemProcessor<StorageDocument, StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ReplacementDocumentProcessor.class);

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public StorageDocument process(StorageDocument item) throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      try {

         // Si il y a déjà eu une erreur sur un document en mode partiel, on ne
         // cherche pas à continuer sur ce document
         if (!(isModePartielBatch() && getIndexErreurListe().contains(
               getStepExecution().getExecutionContext().getInt(
                     Constantes.CTRL_INDEX)))) {

            final String path = (String) getStepExecution().getJobExecution()
                  .getExecutionContext().get(Constantes.SOMMAIRE_FILE);

            final File sommaire = new File(path);

            final File ecdeDirectory = sommaire.getParentFile();

            String cheminDocOnEcde;
            if (!item.getFilePath().contains(ecdeDirectory.getAbsolutePath())) {
               cheminDocOnEcde = ecdeDirectory.getAbsolutePath()
                     + File.separator + "documents" + File.separator
                     + item.getFilePath();
            } else {
               cheminDocOnEcde = item.getFilePath();
            }
            LOGGER.debug("{} - chemin on ecde: {}", trcPrefix, cheminDocOnEcde);

            // recupere la map des documents compressés
            Map<String, CompressedDocument> documentsCompressed = (Map<String, CompressedDocument>) getStepExecution()
                  .getJobExecution().getExecutionContext()
                  .get("documentsCompressed");
            if (documentsCompressed == null) {
               LOGGER.debug(
                     "{} - La map des documents compresses n'existe pas dans le contexte spring",
                     trcPrefix);
            } else {
               LOGGER.debug("{} - Clé de la map des documents compresses : {}",
                     trcPrefix, documentsCompressed.keySet());
            }

            // test si le document courant a ete compressé
            if (documentsCompressed != null
                  && documentsCompressed.containsKey(cheminDocOnEcde)) {
               // le document courant fait bien parti de la liste des documents
               // compressés
               // on va donc remplacer les informations
               final CompressedDocument compressedDoc = documentsCompressed
                     .get(cheminDocOnEcde);
               // le nom
               LOGGER.debug(
                     "remplacement du nom du fichier {} par {}",
                     new String[] { item.getFileName(),
                           compressedDoc.getFileName() });
               item.setFileName(compressedDoc.getFileName());
               LOGGER.debug(
                     "remplacement du chemin du fichier {} par {}",
                     new String[] { item.getFilePath(),
                           compressedDoc.getFilePath() });
               item.setFilePath(compressedDoc.getFilePath());
               // le contenu du fichier
               LOGGER.debug("remplacement du contenu du fichier par {}",
                     new String[] { compressedDoc.getFilePath() });
               InputStreamSource streamSource = new InputStreamSource(
                     new FileInputStream(compressedDoc.getFilePath()));
               item.setContent(new DataHandler(streamSource));
               // le hash
               final StorageMetadata metaHash = findStorageMetadata(
                     item.getMetadatas(),
                     StorageTechnicalMetadatas.HASH.getShortCode());
               if (metaHash != null) {
                  LOGGER.debug("remplacement du hash {} par {}", new Object[] {
                        metaHash.getValue(), compressedDoc.getHash() });
                  metaHash.setValue(compressedDoc.getHash());
               }
            }
         }
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getErrorMessageList().add(e.getMessage());
            LOGGER.warn("Une erreur est survenue lors de remplacement de document compressé",
                  e);
         } else {
            throw e;
         }
      }
      LOGGER.debug("{} - fin", trcPrefix);
      return item;
   }

   /**
    * Methode de recherche de l'objet storageMetadata a partir du code court.
    * 
    * @param metadonnees
    *           liste des metadonnées
    * @param shortCode
    *           code court de la metadonnee recherchee
    * @return StorageMetadata
    */
   private StorageMetadata findStorageMetadata(
         final List<StorageMetadata> metadonnees, final String shortCode) {
      StorageMetadata retour = null;
      if (metadonnees != null) {
         for (StorageMetadata metadonnee : metadonnees) {
            if (metadonnee.getShortCode().equals(shortCode)) {
               retour = metadonnee;
               break;
            }
         }
      }
      return retour;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // rien à faire
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

}
