package fr.urssaf.image.sae.metadata.rules;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette règle permet de determine si la valeur d'une métadonnée définie comme
 * obligatoire est bien enrichie.
 * 
 * @author akenore
 * 
 */
@Component
public class MetadataValueIsRequiredRule extends
		AbstractLeafRule<SAEMetadata, MetadataReference> {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	@Override
	public final boolean isSatisfiedBy(final SAEMetadata saeMetadata,
			final MetadataReference reference) {
		boolean result = false;
		if (saeMetadata.getValue() != null) {
				result = true;
		}
		return result;
	}

}
