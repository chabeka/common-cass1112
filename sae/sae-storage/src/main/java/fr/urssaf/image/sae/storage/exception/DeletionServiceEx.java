package fr.urssaf.image.sae.storage.exception;

/**
 * Exception à utiliser pour les erreurs de suppression qui peuvent être
 * récupérées.<BR/>
 * 
 */

public class DeletionServiceEx extends StorageException {

	/**
	 * L'indentifiant unique de l'exception
	 */
	private static final long serialVersionUID = 2518811610910367626L;

	/**
	 * Construit une nouvelle {@link DeletionServiceEx }.
	 */
	public DeletionServiceEx() {
		super();
	}

	/**
	 * Construit une nouvelle {@link DeletionServiceEx } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public DeletionServiceEx(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link DeletionServiceEx }avec un message.
	 * 
	 * @param message
	 *            : Le message de l'erreur
	 */
	public DeletionServiceEx(final String message) {
		super(message);
	}
	/**
	 * Construit une nouvelle {@link DeletionServiceEx } avec un message ,une
	 * cause données et un code erreur donné .
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 * @param codeErreur
	 *            : Le code d'erreur
	 */
	public DeletionServiceEx(final String codeErreur, final String message,
			final Throwable cause) {
		super(message, cause);
		setCodeError(codeErreur);
	}
	
}
