package fr.urssaf.image.sae.storage.exception;

/**
 * Super classe des exceptions correspondant à des cas d'erreur <b>prévus et
 * contrôlés par l'appelé</b> (en tant que faisant partie de son "contrat" de
 * service), que <b>l'appelant doit catcher</b>.
 * 
 */
public class StorageException extends Exception {
	private String codeError;

	/**
	 * Identifiant unique qui caractérise l'excepion
	 */
	private static final long serialVersionUID = -6505532287814387009L;

	/**
	 * Construit une nouvelle {@link RetrievalServiceEx }.
	 */
	public StorageException() {
		super();
	}

	/**
	 * Construit une nouvelle {@link StorageException } avec un message.
	 * 
	 * @param message
	 *            Le message de l'erreur
	 */
	public StorageException(final String message) {
		super(message);
	}

	/**
	 * Construit une nouvelle {@link StorageException } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public StorageException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link StorageException } avec un message ,une
	 * cause données et un code erreur donné .
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 * @param codeErreur
	 *            : Le code d'erreur
	 */
	public StorageException(final String codeErreur, final String message,
			final Throwable cause) {
		super(message, cause);
		setCodeError(codeErreur);
	}

	/**
	 * @param codeError
	 *            : Le code erreur.
	 */
	public final void setCodeError(final String codeError) {
		this.codeError = codeError;
	}

	/**
	 * @return Le code erreur.
	 */
	public final String getCodeError() {
		return codeError;
	}
}
