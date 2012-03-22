package fr.urssaf.image.sae.ordonnanceur.exception;

/**
 * Exception levée lors de l'exécution de l'ordonnanceur
 * 
 * 
 */
public class OrdonnanceurRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           exception levée
    */
   public OrdonnanceurRuntimeException(Throwable cause) {
      super(cause);
   }

   /**
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           exception levée
    */
   public OrdonnanceurRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }

}
