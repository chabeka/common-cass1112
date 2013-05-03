/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;

/**
 * enrichissement des métadonnées de l'objet {@link UntypedDocument}
 * 
 */
@Component
public class EnrichissementVirtualMetadonneeProcessor implements
      ItemProcessor<UntypedVirtualDocument, SAEVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EnrichissementVirtualStorageDocumentProcessor.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public SAEVirtualDocument process(UntypedVirtualDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      LOGGER.debug("{} - fin", trcPrefix);
      // TODO Auto-generated method stub
      return null;
   }

}
