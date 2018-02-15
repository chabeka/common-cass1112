package fr.urssaf.image.sae.storage.exception;

/**
 * Exception à utiliser pour les erreurs de gestion de la corbeille.<BR/>
 * 
 */

public class RecycleBinServiceEx extends StorageException {

	/**
	 * L'identifiant unique de l'exception
	 */
   private static final long serialVersionUID = 9099711432275582562L;
   
   /**
	 * Construit une nouvelle {@link RecycleBinServiceEx }.
	 */
	public RecycleBinServiceEx() {
		super();
	}

	/**
	 * Construit une nouvelle {@link RecycleBinServiceEx } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public RecycleBinServiceEx(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link RecycleBinServiceEx }avec un message.
	 * 
	 * @param message
	 *            : Le message de l'erreur
	 */
	public RecycleBinServiceEx(final String message) {
		super(message);
	}
	/**
	 * Construit une nouvelle {@link RecycleBinServiceEx } avec un message ,une
	 * cause données et un code erreur donné .
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 * @param codeErreur
	 *            : Le code d'erreur
	 */
	public RecycleBinServiceEx(final String codeErreur, final String message,
			final Throwable cause) {
		super(message, cause);
		setCodeError(codeErreur);
	}
	
}
