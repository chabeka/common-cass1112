package fr.urssaf.image.sae.storage.exception;

/**
 * Exception à utiliser pour les erreurs de récupération qui peuvent être
 * récupérées.<BR/>
 */
public class RetrievalServiceEx extends StorageException {
	/**
	 * L'indentifiant unique de l'exception
	 */
	private static final long serialVersionUID = 537491939481933226L;

	/**
	 * Construit une nouvelle {@link RetrievalServiceEx }.
	 */
	public RetrievalServiceEx() {
		super();
	}

	/**
	 * Construit une nouvelle {@link RetrievalServiceEx } avec un message et
	 * une cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public RetrievalServiceEx(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link RetrievalServiceEx } avec un message.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 */
	public RetrievalServiceEx(final String message) {
		super(message);
	}
	/**
	 * Construit une nouvelle {@link RetrievalServiceEx } avec un message ,une
	 * cause données et un code erreur donné .
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 * @param codeErreur
	 *            : Le code d'erreur
	 */
	public RetrievalServiceEx(final String codeErreur, final String message,
			final Throwable cause) {
		super(message, cause);
		setCodeError(codeErreur);
	}
	
}
