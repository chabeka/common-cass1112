package fr.urssaf.image.sae.metadata.rules;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.SAEMetadataType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.constants.Constants;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette règle permet de determine si la valeur d'une métadonnée respect bien le
 * motif.
 * 
 * @author akenore
 */
@Component
public class UntypedMetadataValueTypeRule extends
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
	private boolean checkPattern(final UntypedMetadata uMetadata,
			final MetadataReference reference) {
		final SAEMetadataType saeType = SAEMetadataType.valueOf(reference
				.getType());
		boolean result = false;
		final String value = String.valueOf(uMetadata.getValue());
		final String pattern = reference.getPattern();
		switch (saeType) {
		case DATE:
			result = validate(value, pattern, Constants.DATE_PATTERN);
			break;
		case INTEGER:
			result = validate(value, pattern, Constants.NUMERIC_PATTERN);
			break;
		case BOOLEAN:
			result = validate(value, pattern, Constants.BOOLEAN_PATTERN);
			break;
		default:
			result = true;
		}
		return result;
	}

	/**
	 * 
	 * @param value
	 *            : La valeur à contrôler.
	 * @param pattern
	 *            : Le motif définit dans le reférentiel.
	 * @param defaultPattern
	 *            : Le motif par défaut.
	 * @return True si a valeur respecte le motif.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private boolean validate(final String value, final String pattern,
			final String defaultPattern) {
		boolean result = false;
		if (StringUtils.isNotEmpty(pattern)) {
			if (value.matches(pattern)) {
				result = true;
			}
		} else {
			if (value.matches(defaultPattern)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public final boolean isSatisfiedBy(final UntypedMetadata metaData,
			final MetadataReference referentiel) {
		return checkPattern(metaData, referentiel);
	}

}
