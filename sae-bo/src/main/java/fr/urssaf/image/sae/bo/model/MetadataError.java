
package fr.urssaf.image.sae.bo.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Classe qui réprésente un erreur lié à une métadonneé.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>longCode : Le code long de la métadonnée.</li>
 * </ul>
 */
public class MetadataError extends SAEError {

	private String longCode;

	/**
	 * 
	 * @param code
	 *            : Le code long de la métadonnée
	 */
	public final void setLongCode(final String code) {
		this.longCode = code;
	}

	/**
	 * 
	 * @return Le code long de la métadonnée
	 */
	public final String getLongCode() {
		return longCode;
	}

	/**
	 * Construit un {@link MetadataError}
	 */
	public MetadataError() {
		super();
	}

	/**
	 * 
	 * @param code
	 *            : Le code de l'erreur
	 * @param lCode
	 *            : Le code long de la métadonnée
	 * @param message
	 *            : Le message qui accompagne l'erreur.
	 */
	public MetadataError(final String code, final String lCode,
			final String message) {
		super(code, message);
		this.longCode = lCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public final  String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
		toStrBuilder.append("code erreur", getCode());
		toStrBuilder.append("code long", longCode);
		toStrBuilder.append("message", getMessage());

		return toStrBuilder.toString();

	}
}
