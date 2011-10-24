package fr.urssaf.image.sae.bo.model.bo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;

/**
 * Classe représentant un document c'est-à-dire un tableau de byte correspondant
 * au contenu du document et la liste des métadonnées(liste de paire(code,
 * valeur)dont les valeurs sont typées).<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>shortCode : Le code court de la métadonnée.</li>
 * <li>value : La valeur de la métadonnée.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class SAEMetadata extends AbstractMetadata {
	private String shortCode;
	private Object value;

	/**
	 * @param shortCode
	 *            : Le code court de la métadonnée.
	 */
	public final void setShortCode(final String shortCode) {
		this.shortCode = shortCode;
	}

	/**
	 * @return Le code court de la métadonnée
	 */
	public final String getShortCode() {
		return shortCode;
	}

	/**
	 * @param value
	 *            : La valeur de la métadonnée.
	 */
	public final void setValue(final Object value) {
		this.value = value;
	}

	/**
	 * @return La valeur de la métadonnée.
	 */
	public final Object getValue() {
		return value;
	}

	/**
	 * Construit un objet de type {@link SAEMetadata}
	 */
	public SAEMetadata() {
		super();
	}

	/**
	 * Construit un objet de type {@link SAEMetadata}
	 * 
	 * @param longCode
	 *            : Le long court de la métadonnée métier.
	 * @param value
	 *            : La valeur de la métadonnée.
	 */
	public SAEMetadata(final String longCode, final Object value) {
		super(longCode);
		this.value = value;
	}

	/**
	 * Construit un objet de type {@link SAEMetadata}
	 * 
	 * @param longCode
	 *            : Le long court de la métadonnée métier.
	 * @param shortCode
	 *            : Le code court de la métadonnée métier.
	 * @param value
	 *            : La valeur de la métadonnée.
	 */
	public SAEMetadata(final String longCode, final String shortCode,
			final Object value) {
		super(longCode);
		this.shortCode = shortCode;
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStrBuilder.append("code court", shortCode);
		toStrBuilder.append("code long", getLongCode());
		toStrBuilder.append("value", getValue());
		return toStrBuilder.toString();

	}
}
