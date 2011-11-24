package fr.urssaf.image.sae.services.exception.capture;

/**
 * Exception levée lorsque le serveur reçoit une demande d'insertion en masse
 * alors qu'il est en train d'exécuter une autre.
 * 
 * @author akenore
 * 
 */
public class ServerBusyEx extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 *            : Le message
	 */
	public ServerBusyEx(String message) {
		super(message);
	}

	public ServerBusyEx() {
		super();
	}
}
