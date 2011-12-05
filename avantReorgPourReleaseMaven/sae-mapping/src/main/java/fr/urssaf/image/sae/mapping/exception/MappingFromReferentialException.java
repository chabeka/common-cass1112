package fr.urssaf.image.sae.mapping.exception;
/**
 * 
 * @author akenore
 *
 */
public class MappingFromReferentialException extends Exception {

	private static final long serialVersionUID = 9100342806227683570L;

	/**
	 * Construit un objet de type {@link MappingFromReferentialException}
	 */
	public MappingFromReferentialException() {
		super();
	}

	/**
	 * Construit un objet de type {@link MappingFromReferentialException}
	 * 
	 * @param message
	 *            : Le message.
	 */
	public MappingFromReferentialException(final String message) {
		super(message);
	}

	/**
	 * Construit un objet de type {@link MappingFromReferentialException}
	 * 
	 * @param message
	 *            : Le message.
	 * @param cause
	 *            : La cause du dysfonctionnement.
	 */
	public MappingFromReferentialException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un objet de type {@link MappingFromReferentialException}
	 * 
	 * @param cause
	 *            : La cause du dysfonctionnement.
	 */
	public MappingFromReferentialException(final Throwable cause) {
		super(cause);
	}

}
