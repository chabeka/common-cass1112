
package fr.urssaf.image.commons.commons.pronom.exemple.exception;

/**
 * Erreur sur le parcours du fichier de signatures.
 *
 */
public class SignatureParseException extends Exception {

    private static final long serialVersionUID = -6562570951231636750L;

    /**
     * Construit une SignatureParseException.
     * @param message le message pour l'exception.
     */
    public SignatureParseException(String message) {
        super(message);
    }

    
    /**
     * Construit une SignatureParseException.
     * @param message le message pour l'exception.
     * @param cause la cause de l'exception. 
     */
    public SignatureParseException(String message, Throwable cause) {
        super(message, cause);
    }
    

    /**
     * Construit une SignatureParseException.
     * @param cause le message pour l'exception.
     */
    public SignatureParseException(Throwable cause) {
        super(cause);
    }
}
