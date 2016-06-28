package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.CaptureMasseCompressionSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Item processor pour le contrôle des documents a compresser.
 * 
 */
@Component
public class ControleCompressionDocumentProcessor extends AbstractListener
      implements ItemProcessor<UntypedDocument, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleCompressionDocumentProcessor.class);
   
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
      
      final String path = (String) getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.SOMMAIRE_FILE);

      final File sommaire = new File(path);

      final File ecdeDirectory = sommaire.getParentFile();
      
      boolean compressionActive = captureMasseCompressionSupport.isDocumentToBeCompress(item, ecdeDirectory);
      if (compressionActive) {
         
         String cheminDocOnEcde = ecdeDirectory.getAbsolutePath() + File.separator
               + "documents" + File.separator + item.getFilePath();
         
         List<String> documentsToCompress = (List<String>) getStepExecution()
               .getJobExecution().getExecutionContext().get(
                     "documentsToCompress");
         if (documentsToCompress == null) {
            LOGGER.debug("{} - Pas de liste de documents a compresser, on la créé",
                  trcPrefix);
            documentsToCompress = new ArrayList<String>();
         }
         if (!documentsToCompress.contains(cheminDocOnEcde)) {
            LOGGER.debug(
                  "{} - Ajout du document {} dans la liste des documents a compresser",
                  trcPrefix, cheminDocOnEcde);
            documentsToCompress.add(cheminDocOnEcde);
         }
         getStepExecution().getJobExecution().getExecutionContext().put(
               "documentsToCompress", documentsToCompress);
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
