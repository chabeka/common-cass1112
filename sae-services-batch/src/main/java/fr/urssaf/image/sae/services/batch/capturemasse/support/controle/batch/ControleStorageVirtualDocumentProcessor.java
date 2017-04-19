/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;

/**
 * Contrôle des {@link SAEVirtualDocument}
 * 
 */
@Component
public class ControleStorageVirtualDocumentProcessor implements
      ItemProcessor<SAEVirtualDocument, SAEVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleStorageVirtualDocumentProcessor.class);

   @Autowired
   private CaptureMasseControleSupport controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEVirtualDocument process(SAEVirtualDocument item) throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      controleSupport.controleSAEVirtualDocumentStockage(item);

      LOGGER.debug("{} - fin", trcPrefix);
      return item;
   }

}