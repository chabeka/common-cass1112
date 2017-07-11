package fr.urssaf.image.sae.storage.exception;

/**
 * Exception à utiliser pour les erreurs de connexion qui peuvent être
 * récupérées.<BR/>
 * 
 */
public class ConnectionServiceEx extends StorageException {


	/**
	 * L'identifiant unique de l'erreur
	 */
	private static final long serialVersionUID = -7786562625725866505L;

	/**
	 * Construit une nouvelle {@link ConnectionServiceEx }.
	 */
	public ConnectionServiceEx() {
		super();
	}

	/**
	 * Construit une nouvelle {@link ConnectionServiceEx } avec un message.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 */
	public ConnectionServiceEx(final String message) {
		super(message);
	}

	/**
	 * Construit une nouvelle {@link ConnectionServiceEx } avec un message et
	 * une cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public ConnectionServiceEx(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link ConnectionServiceEx } avec un message ,une
	 * cause données et un code erreur donné .
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 * @param codeErreur
	 *            : Le code d'erreur
	 */
	public ConnectionServiceEx(final String codeErreur, final String message,
			final Throwable cause) {
		super(message, cause);
		setCodeError(codeErreur);
	}
	
}
