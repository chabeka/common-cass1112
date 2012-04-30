package fr.urssaf.image.sae.metadata.rules;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * 
 * @author akenore
 * 
 */
@Component
public class MetadataIsArchivableRule extends
		AbstractLeafRule<SAEMetadata, MetadataReference> {
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	@Override
	public final boolean isSatisfiedBy(final SAEMetadata saeMetadata,
			final MetadataReference reference) {
		boolean result = false;
		if (reference.isArchivable()) {
			result = true;
		}
		return result;
	}

}
