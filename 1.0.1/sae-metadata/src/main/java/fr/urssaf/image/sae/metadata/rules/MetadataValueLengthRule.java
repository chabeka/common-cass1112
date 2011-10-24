package fr.urssaf.image.sae.metadata.rules;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette règle permet de determine si la longueur de la valeur d'une métadonnée
 * est inférieur ou égal à la longueur définie.
 * 
 * @author akenore
 * 
 */
@Component
public class MetadataValueLengthRule extends
		AbstractLeafRule<SAEMetadata, MetadataReference> {
	/**
	 * {@inheritDoc}
	 */

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public final boolean isSatisfiedBy(final SAEMetadata saeMetadata,
			final MetadataReference metadataReference) {
		boolean result = true;
		final int length = metadataReference.getLength();
		final String value = String.valueOf(saeMetadata.getValue());
		if ((length > -1) && StringUtils.isNotEmpty(value)
				&& value.trim().length() > length) {
			result = false;
		}
		return result;
	}
}
