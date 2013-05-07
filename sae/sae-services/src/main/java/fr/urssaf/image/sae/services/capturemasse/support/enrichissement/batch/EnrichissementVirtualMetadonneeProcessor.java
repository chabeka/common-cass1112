/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedVirtualDocument;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport;

/**
 * enrichissement des métadonnées de l'objet {@link UntypedDocument}
 * 
 */
@Component
public class EnrichissementVirtualMetadonneeProcessor implements
      ItemProcessor<UntypedVirtualDocument, SAEVirtualDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(EnrichissementVirtualStorageDocumentProcessor.class);

   @Autowired
   private EnrichissementMetadonneeSupport metadonneeSupport;

   @Autowired
   private MappingDocumentService documentService;

   /**
    * {@inheritDoc}
    */
   @Override
   public SAEVirtualDocument process(UntypedVirtualDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      SAEVirtualDocument saeVirtualDocument = documentService
            .untypedVirtualDocumentToSaeVirtualDocument(item);
      metadonneeSupport.enrichirMetadonneesVirtuelles(saeVirtualDocument);

      LOGGER.debug("{} - fin", trcPrefix);
      
      return saeVirtualDocument;
   }

}
