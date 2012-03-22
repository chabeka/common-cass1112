/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.enrichissement.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;

/**
 * Item Processor pour l'enrichissement des métadonnées
 * 
 */
@Component
public class EnrichissementMetadonneeProcessor implements
      ItemProcessor<UntypedDocument, SAEDocument> {

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEDocument process(UntypedDocument item) throws Exception {
      // FIXME FBON - implémentation EnrichissementMetadonneeProcessor
      return null;
   }

}
