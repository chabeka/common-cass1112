package fr.urssaf.image.sae.bo.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Classe qui représente une erreur.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>code : Le code de l'erreur</li>
 * <li>message : Le Le message qui accompagne l'erreur.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */

public class SAEError {
	private String code;
	private String message;

	/**
	 * 
	 * @param code
	 *            : Le code l'erreur
	 */
	public final void setCode(final String code) {
		this.code = code;
	}

	/**
	 * 
	 * @return : Le code de l'erreur
	 */
	public final String getCode() {
		return code;
	}

	/**
	 * 
	 * @param message
	 *            : Le message qui accompagne l'erreur.
	 */
	public final void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return Le message qui accompagne l'erreur.
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * Construit un {@link SAEError}
	 */
	public SAEError() {
		// ici on ne fait rien
	}

	/**
	 * Construit un {@link SAEError}
	 * 
	 * @param code
	 *            : Le code de l'erreur
	 * @param message
	 *            : Le message qui accompagne l'erreur.
	 */
	public SAEError(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * {@inheritDoc}
	 */
	//CHECKSTYLE:OFF -- car la méthode toString peut être surchargée
	public  String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
		toStrBuilder.append("code erreur", getCode());
		toStrBuilder.append("message", getMessage());

		return toStrBuilder.toString();

	}
	// CHECKSTYLE:ON
}
