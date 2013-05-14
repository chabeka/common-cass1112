package fr.urssaf.image.sae.metadata.rules;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * 
 * Régle permettant de vérifier que la métadonnée est modifiable
 * 
 */
@Component
public class MetadataIsModifiableRule extends
      AbstractLeafRule<UntypedMetadata, MetadataReference> {
   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   @Override
   public final boolean isSatisfiedBy(final UntypedMetadata saeMetadata,
         final MetadataReference reference) {
      return Boolean.TRUE.equals(reference.isModifiable());
   }

}
