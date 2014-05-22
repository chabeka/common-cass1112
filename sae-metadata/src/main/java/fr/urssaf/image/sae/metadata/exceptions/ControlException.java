package fr.urssaf.image.sae.metadata.exceptions;


/**
 * Super classe des exceptions correspondant à des cas d'erreur <b>prévus et
 * contrôlés par l'appelé</b> (en tant que faisant partie de son "contrat" de
 * service), que <b>l'appelant doit catcher</b>.
 * 
 */
public class ControlException extends Exception {

	/**
	 * L'indentifiant unique de l'exception
	 */
	private static final long serialVersionUID = 5297382465265127785L;

	
	
	/**
	 * Construit une nouvelle {@link ControlException }.
	 */
	public ControlException() {
		super();
	}

	/**
	 * Construit une nouvelle {@link ControlException } avec un message et une
	 * cause données.
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public ControlException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit une nouvelle {@link ControlException }avec un message.
	 * 
	 * @param message
	 *            : Le message de l'erreur
	 */
	public ControlException(final String message) {
		super(message);
	}

	
}
