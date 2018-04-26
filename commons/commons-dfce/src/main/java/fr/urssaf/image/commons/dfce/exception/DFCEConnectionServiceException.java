/**
 * 
 */
package fr.urssaf.image.commons.dfce.exception;

/**
 * Exception levée lors de la connection à DFCe.
 */
public class DFCEConnectionServiceException extends RuntimeException {

   /**
    * SUID
    */
   private static final long serialVersionUID = -4495097927866687639L;

   /**
    * Constructeur
    */
   public DFCEConnectionServiceException(String message) {
      super(message);
   }

   /**
    * Constructeur
    *
    * @param message
    * @param cause
    */
   public DFCEConnectionServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructeur
    *
    * @param cause
    */
   public DFCEConnectionServiceException(Throwable cause) {
      super(cause);
   }

}
