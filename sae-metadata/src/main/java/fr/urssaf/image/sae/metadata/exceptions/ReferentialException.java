package fr.urssaf.image.sae.metadata.exceptions;


/**
 * Exception à utiliser pour les erreurs lié au contrôle des métadonnées à
 * archiver.<BR/>
 * 
 */
public class ReferentialException extends ControlException {

	private static final long serialVersionUID = 5812830110677764248L;

	/**
	 * Construit une nouvelle {@link ReferentialException }.
	 */
	public ReferentialException() {
		super();
	}

	/**
	 * Construit une nouvelle {@link ReferentialException } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public ReferentialException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link ReferentialException }avec un message.
	 * 
	 * @param message
	 *            : Le message de l'erreur
	 */
	public ReferentialException(final String message) {
		super(message);
	}

	
}
