/**
 * 
 */
package fr.urssaf.image.sae.trace.exception;

/**
 * Exception runtime de traitement
 * 
 */
public class TraceRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 6514428262778393529L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public TraceRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public TraceRuntimeException(Exception exception) {
      super(exception);
   }

}
