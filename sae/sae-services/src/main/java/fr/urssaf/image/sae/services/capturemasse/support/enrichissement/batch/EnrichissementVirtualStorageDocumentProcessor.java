/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Enrichissement des métadonnées de l'objet {@link VirtualStorageDocument}
 * 
 */
@Component
public class EnrichissementVirtualStorageDocumentProcessor implements
      ItemProcessor<VirtualStorageDocument, VirtualStorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EnrichissementVirtualStorageDocumentProcessor.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public VirtualStorageDocument process(VirtualStorageDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      LOGGER.debug("{} - fin", trcPrefix);
      // TODO Auto-generated method stub
      return null;
   }

}
