package fr.urssaf.image.sae.dfce.admin.services.exceptions;

/**
 * Exception levée par une méthode de la l’interface BaseAdministrationService <BR />
 * 
 */
public class BaseAdministrationServiceEx extends DfceAdminException {

	/**
	 * L'identifiant unique de l'erreur
	 */
	private static final long serialVersionUID = -7786562625725866505L;

	/**
	 * Construit un {@link BaseAdministrationServiceEx }
	 */
	public BaseAdministrationServiceEx() {
		super();
	}

	/**
	 * Construit un {@link BaseAdministrationServiceEx }
	 * 
	 * @param message
	 *            : Le message d'erreur
	 */
	public BaseAdministrationServiceEx(final String message) {
		super(message);
	}

	/**
	 * Construit un {@link BaseAdministrationServiceEx }
	 * 
	 * @param message
	 *            : Le message d'erreur
	 * @param cause
	 *            : La cause de l'erreur
	 */
	public BaseAdministrationServiceEx(final String message,
			final Throwable cause) {
		super(message, cause);
	}

}
