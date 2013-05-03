/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;

/**
 * Contrôle des {@link SAEVirtualDocument}
 * 
 */
@Component
public class ControleStorageVirtualDocumentProcessor implements
      ItemProcessor<SAEVirtualDocument, SAEVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleStorageVirtualDocumentProcessor.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public SAEVirtualDocument process(SAEVirtualDocument item) throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      LOGGER.debug("{} - fin", trcPrefix);
      // TODO Auto-generated method stub
      return null;
   }

}
