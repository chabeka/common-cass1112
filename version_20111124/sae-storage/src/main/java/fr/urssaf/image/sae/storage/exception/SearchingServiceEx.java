package fr.urssaf.image.sae.storage.exception;

/**
 * Exception à utiliser pour les erreurs de recherche qui peuvent être
 * récupérées.<BR/>
 */
public class SearchingServiceEx extends StorageException {

	/**
	 * L'identifiant unique de l'exception
	 */
	private static final long serialVersionUID = -298210295473447438L;

	/**
	 * Construit une nouvelle {@link SearchingServiceEx }.
	 */
	public SearchingServiceEx() {
		super();
	}

	/**
	 * Construit une nouvelle {@link SearchingServiceEx } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Message de l'erreur
	 * @param cause
	 *            : Cause de l'erreur
	 */
	public SearchingServiceEx(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link SearchingServiceEx } avec un message.
	 * 
	 * @param message
	 *            : Le message de l'erreur
	 */
	public SearchingServiceEx(final String message) {
		super(message);
	}
	/**
	 * Construit une nouvelle {@link SearchingServiceEx } avec un message ,une
	 * cause données et un code erreur donné .
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 * @param codeErreur
	 *            : Le code d'erreur
	 */
	public SearchingServiceEx(final String codeErreur, final String message,
			final Throwable cause) {
		super(message, cause);
		setCodeError(codeErreur);
	}
}
