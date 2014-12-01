/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport;

/**
 * Contrôle des {@link UntypedVirtualDocument}
 * 
 */
@Component
public class ControleSommaireDocumentsVirtuelsProcessor implements
      ItemProcessor<UntypedVirtualDocument, UntypedVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleSommaireDocumentsVirtuelsProcessor.class);

   @Autowired
   private CaptureMasseControleSupport controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedVirtualDocument process(UntypedVirtualDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      controleSupport.controleSAEMetadatas(item);

      LOGGER.debug("{} - fin", trcPrefix);

      return item;
   }

}
