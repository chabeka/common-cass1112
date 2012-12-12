/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.exception;

/**
 * Erreur Runtime
 * 
 */
public class TraceExecutableRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 6669561838838721268L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public TraceExecutableRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception source
    */
   public TraceExecutableRuntimeException(Exception exception) {
      super(exception);
   }

}
