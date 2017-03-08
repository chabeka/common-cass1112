package fr.urssaf.image.sae.integrationinstall.exception;

/**
 * Exception à lever dans le cas d'erreurs que l'on ne souhaite<br>
 * pas traiter ou que l'on ne peut pas traiter.
 * 
 */
public final class IntegrationInstRuntimeException extends RuntimeException {

   /**
    * SUID
    */
   private static final long serialVersionUID = -2207955362314881105L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause de l'exception
    */
   public IntegrationInstRuntimeException(Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    */
   public IntegrationInstRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    * @param cause
    *           cause de l'exception
    */
   public IntegrationInstRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }

}
