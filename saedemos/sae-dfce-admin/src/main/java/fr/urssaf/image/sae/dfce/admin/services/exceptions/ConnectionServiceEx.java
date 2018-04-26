package fr.urssaf.image.sae.dfce.admin.services.exceptions;

/**
 * Exception levée par une méthode de la l’interface StorageConnectionService <BR />
 * 
 
 */
public class ConnectionServiceEx extends DfceAdminException {

	/**
	 * L'identifiant unique de l'erreur
	 */
	private static final long serialVersionUID = -7786562625725866505L;

	/**
	 * Construit un {@link ConnectionServiceEx }
	 */
	public ConnectionServiceEx() {
		super();
	}

	/**
	 *  Construit un {@link ConnectionServiceEx }
	 * 
	 * @param message
	 *            : Le message d'erreur
	 */
	public ConnectionServiceEx(final String message) {
		super(message);
	}

	/**
	 *  Construit un {@link ConnectionServiceEx }
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public ConnectionServiceEx(final String message, final Throwable cause) {
		super(message, cause);
	}

}
