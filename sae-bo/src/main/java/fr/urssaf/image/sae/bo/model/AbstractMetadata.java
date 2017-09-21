package fr.urssaf.image.sae.bo.model;

/**
 * Classe abstraite contenant les éléments communs des métadonnées.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>longCode : Le code long de la métadonnée.</li>
 * </ul>
 */
public class AbstractMetadata {
	/**
	 *	 le code long.
	 */
	private String longCode;

	/**
	 * @return Le code long de la métadonnée
	 */
	public final String getLongCode() {
		return longCode;
	}

	/**
	 * @param code
	 *            : Le code long de la métadonnée.
	 */
	public final void setLongCode(final String code) {
		this.longCode = code;
	}

	/**
	 * Construit un objet de type {@link AbstractMetadata}.
	 */
	public AbstractMetadata() {
		// Ici on fait rien
	}

	/**
	 * Construit un objet de type {@link AbstractMetadata}.
	 * 
	 * @param code
	 *            : Le code long de la métadonnée métier.
	 */

	public AbstractMetadata(final String code) {
		this.longCode = code;
	}

}
