package fr.urssaf.image.sae.mapping.exception;

/**
 * 
 */
public class InvalidSAETypeException extends Exception {

	private static final long serialVersionUID = 9100342806227683570L;

	/**
	 * Construit un objet de type {@link InvalidSAETypeException}
	 */
	public InvalidSAETypeException() {
		super();
	}

	/**
	 * Construit un objet de type {@link InvalidSAETypeException}
	 * 
	 * @param message
	 *            : Le message.
	 */
	public InvalidSAETypeException(final String message) {
		super(message);
	}

	/**
	 * Construit un objet de type {@link InvalidSAETypeException}
	 * 
	 * @param message
	 *            : Le message.
	 * @param cause
	 *            : La cause du dysfonctionnement.
	 */
	public InvalidSAETypeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un objet de type {@link InvalidSAETypeException}
	 * 
	 * @param cause
	 *            : La cause du dysfonctionnement.
	 */
	public InvalidSAETypeException(final Throwable cause) {
		super(cause);
	}

}
