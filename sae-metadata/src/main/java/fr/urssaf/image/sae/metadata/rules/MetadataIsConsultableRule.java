package fr.urssaf.image.sae.metadata.rules;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 *
 */
@Component
public class MetadataIsConsultableRule extends AbstractLeafRule<SAEMetadata, MetadataReference> {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	@Override
	public final boolean isSatisfiedBy(final SAEMetadata saeMetadata,
			final MetadataReference reference) {
		boolean result = false;
		if (reference.isConsultable()) {
				result = true;
		}
		return result;
	}
	
}
