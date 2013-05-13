/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementStorageDocumentSupport;
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

   @Autowired
   private EnrichissementStorageDocumentSupport documentSupport;
   
   private String uuid;

   /**
    * initialisation avant le début du Step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {
      this.uuid = stepExecution.getJobParameters().getString(
            Constantes.ID_TRAITEMENT);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final VirtualStorageDocument process(VirtualStorageDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      documentSupport.enrichirVirtualDocument(item, uuid);
      
      LOGGER.debug("{} - fin", trcPrefix);
      
      return item;
   }

}
