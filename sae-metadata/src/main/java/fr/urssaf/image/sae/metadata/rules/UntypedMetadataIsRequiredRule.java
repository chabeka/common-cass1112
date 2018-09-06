package fr.urssaf.image.sae.metadata.rules;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette règle permet de determiner si la valeur d'une métadonnée obligatoire
 * est vide ou non.
 */
@Component
public class UntypedMetadataIsRequiredRule extends
AbstractLeafRule<UntypedMetadata, MetadataReference> {
   /**
    * Contrôle des métadonnées.
    * 
    * @param uMetadata
    *            : La métadonnée du SAE
    * @param reference
    *            : La métadonnée du référentiel
    * @return True si a valeur respecte le motif.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   private boolean checkRequiredValue(final UntypedMetadata uMetadata,
         final MetadataReference reference) {
      final boolean required = reference.isRequiredForStorage();
      boolean result = true;
      final String value = String.valueOf(uMetadata.getValue());

      if (required && StringUtils.isEmpty(value)) {
         result = false;
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */

   @Override
   public final boolean isSatisfiedBy(final UntypedMetadata metaData,
         final MetadataReference referentiel) {
      return checkRequiredValue(metaData, referentiel);
   }


}
