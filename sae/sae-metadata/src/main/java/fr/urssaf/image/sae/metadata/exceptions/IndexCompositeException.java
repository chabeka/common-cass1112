package fr.urssaf.image.sae.metadata.exceptions;


/**
 * Exception à utiliser pour les erreurs lié aux indexes composites<BR/>
 * 
 */
public class IndexCompositeException extends ControlException {

	private static final long serialVersionUID = 5812830110677764248L;

	/**
	 * Construit une nouvelle {@link IndexCompositeException }.
	 */
	public IndexCompositeException() {
		super();
	}

	/**
	 * Construit une nouvelle {@link IndexCompositeException } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public IndexCompositeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link IndexCompositeException }avec un message.
	 * 
	 * @param message
	 *            : Le message de l'erreur
	 */
	public IndexCompositeException(final String message) {
		super(message);
	}

	
}
