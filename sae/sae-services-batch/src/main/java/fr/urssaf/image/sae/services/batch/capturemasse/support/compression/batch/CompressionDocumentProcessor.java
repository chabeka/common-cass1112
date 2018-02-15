package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.batch;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.CaptureMasseCompressionSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Item processor pour compresser les documents.
 * 
 */
@Component
public class CompressionDocumentProcessor extends AbstractListener implements
      ItemProcessor<UntypedDocument, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(CompressionDocumentProcessor.class);

   @Autowired
   private CaptureMasseCompressionSupport captureMasseCompressionSupport;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public UntypedDocument process(UntypedDocument item) throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      try {
         final String path = (String) getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.SOMMAIRE_FILE);

         final File sommaire = new File(path);

         final File ecdeDirectory = sommaire.getParentFile();

         String cheminDocOnEcde = ecdeDirectory.getAbsolutePath()
               + File.separator + "documents" + File.separator
               + item.getFilePath();

         List<String> documentsToCompress = (List<String>) getStepExecution()
               .getJobExecution().getExecutionContext()
               .get("documentsToCompress");

         Map<String, CompressedDocument> documentsCompressed = (Map<String, CompressedDocument>) getStepExecution()
               .getJobExecution().getExecutionContext()
               .get("documentsCompressed");

         // on verifie que le document courant est a compresser ou non
         // et s'il n'a pas deja ete compresse
         if (documentsToCompress != null
               && documentsToCompress.contains(cheminDocOnEcde)
               && (documentsCompressed == null || !documentsCompressed
                     .containsKey(cheminDocOnEcde))) {

            // dans ce cas, on compresse le document
            CompressedDocument compressedDoc = captureMasseCompressionSupport
                  .compresserDocument(item, ecdeDirectory);

            if (documentsCompressed == null) {
               LOGGER.debug(
                     "{} - Pas de map de documents compresses, on la créé",
                     trcPrefix);
               documentsCompressed = new HashMap<String, CompressedDocument>();
            }
            LOGGER.debug(
                  "{} - Ajout du document {} dans la map des documents compresses",
                  trcPrefix, cheminDocOnEcde);
            documentsCompressed.put(cheminDocOnEcde, compressedDoc);
            getStepExecution().getJobExecution().getExecutionContext()
                  .put("documentsCompressed", documentsCompressed);
         }
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getExceptionErreurListe().add(new Exception(e.getMessage()));
         } else {
            throw e;
         }

      }

      LOGGER.debug("{} - fin", trcPrefix);
      return item;
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
