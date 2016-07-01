package fr.urssaf.image.sae.metadata.rules;

import org.springframework.stereotype.Component;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette règle permet de determine si la métadonnées d'un document de SAE à
 * belle et bien une correspondance dans le référentiel des métadonnées.
 */
@Component
public class UntypedMetadataExistingRule extends
		AbstractLeafRule<UntypedMetadata, MetadataReference> {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	@Override
	public final boolean isSatisfiedBy(final UntypedMetadata untypedMetadata,
			final MetadataReference metaDataReference) {
		boolean result = true;
		if (metaDataReference == null) {
			result = false;
		}
		return result;
	}
}
