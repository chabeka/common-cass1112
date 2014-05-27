package fr.urssaf.image.sae.metadata.test.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe réprésentant une metadonnées de test
 */
@XStreamAlias("metadata")
public class MockMetadata {
	private String longCode;
	private String shortCode;
	private String value;
	private String type;

	/**
	 * @return Le code long de la métadonnées.
	 */
	public final String getLongCode() {
		return longCode;
	}

	/**
	 * @param longCode
	 *            : Le code long de la métadonnées.
	 */
	public final void setLongCode(final String longCode) {
		this.longCode = longCode;
	}

	/**
	 * @return Le code court de la métadonnées.
	 */
	public final String getShortCode() {
		return shortCode;
	}

	/**
	 * @param shortCode
	 *            Le code court de la métadonnées.
	 */
	public final void setShortCode(final String shortCode) {
		this.shortCode = shortCode;
	}

	/**
	 * @return La valeur de la métadonnées.
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            : La valeur de la métadonnées
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return Le type de la métadonnée.
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @param type
	 *            : Le type de la métadonnée.
	 */
	public final void setType(final String type) {
		this.type = type;
	}

}
