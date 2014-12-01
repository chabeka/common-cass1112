/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.capturemasse.support.enrichissement.EnrichissementMetadonneeSupport;

/**
 * Item Processor pour l'enrichissement des métadonnées
 * 
 */
@Component
public class EnrichissementMetadonneeProcessor implements
      ItemProcessor<UntypedDocument, SAEDocument> {

   @Autowired
   private EnrichissementMetadonneeSupport support;

   @Autowired
   private MappingDocumentService documentService;
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEDocument process(final UntypedDocument item) throws Exception {

      final SAEDocument saeDocument = documentService
            .untypedDocumentToSaeDocument(item);

      support.enrichirMetadonnee(saeDocument);

      return saeDocument;
   }

}
