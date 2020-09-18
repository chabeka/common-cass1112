/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.exception;

/**
 * Exception concernant TraceExecutable
 * 
 */
public class TraceExecutableException extends Exception {

 private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public TraceExecutableException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception source
    */
   public TraceExecutableException(Exception exception) {
      super(exception);
   }

}
