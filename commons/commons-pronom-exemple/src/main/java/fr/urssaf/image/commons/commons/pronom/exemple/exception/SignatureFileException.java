
package fr.urssaf.image.commons.commons.pronom.exemple.exception;


/**
 * Exception levée lorsque qu'il y a une erreur sur le fichier des signatures.
 * 
 */
public class SignatureFileException extends Exception {

    private static final long serialVersionUID = 5878068551833875L;

    private final ErrorCode errorCode;

    /**
     * Construction de l'exception avec un message d erreur.
     * 
     * @param message
     *            the message
     * @param filePath
     * @param errorCode
     *            the error code
     */
    public SignatureFileException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Construction de l'exception avec un message d erreur.
     * 
     * @param message
     *            the message
     * @param filePath
     * @param cause
     *            the cause of the exception
     * @param errorCode
     *            the error code
     */
    public SignatureFileException(String message, Throwable cause,
            ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * @return le message d erreur
     */
    public final ErrorCode getErrorCode() {
        return errorCode;
    }

}
