package fr.urssaf.image.sae.storage.model.storagedocument;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant la métadonnée.</BR> Elle contient les attributs
 * :
 * <ul>
 * <li>
 * shortCode : Le code court de la métadonnée</li>
 * <li>
 * longCode : Le code long de la métadonnée</li>
 * <li>
 * value : La valeur de la métadonnée</li>
 * </ul>
 */
public class StorageMetadata {
	// Les attributs
	private String shortCode;
	private Object value;

	/**
	 * @return Le code court de la métadonnée.
	 */
	public final String getShortCode() {
		return shortCode;
	}

	/**
	 * @param shortCode
	 *            : Le code court de la métadonnée
	 * 
	 */
	public final void setShortCode(final String shortCode) {
		this.shortCode = shortCode;
	}

	
	/**
	 * Retourne la valeur de la métadonnée
	 * 
	 * @return La valeur de la Métta donnée
	 */
	public final Object getValue() {
		return value;
	}

	/**
	 * Initialise la valeur de la métadonnée
	 * 
	 * @param value
	 *            : La valeur de la métadonnée
	 */
	public final void setValue(final Object value) {
		this.value = value;
	}

	/**
	 * Construit un {@link StorageMetadata }.
	 * 
	 * @param shortCode
	 *            : Le code court de la métadonnée
	 * @param value
	 *            : La valeur de la métadonnée
	 */
	public StorageMetadata(final String shortCode, final Object value) {
		this.shortCode = shortCode;
		this.value = value;
	}


	/**
	 * Construit un {@link StorageMetadata }.
	 * 
	 * @param shortCode
	 *            : Le code court de la métadonnée
	 * 
	 */
	public StorageMetadata(final String shortCode) {
		this(shortCode, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return new ToStringBuilder(this).append("code", shortCode)
				.append("value", value).toString();
	}
}
