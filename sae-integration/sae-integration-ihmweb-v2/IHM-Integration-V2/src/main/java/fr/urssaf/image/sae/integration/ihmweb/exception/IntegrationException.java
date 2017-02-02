/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.exception;

/**
 * 
 * 
 */
public class IntegrationException extends Exception {

   /**
    * serialVersionUID
    */
   private static final long serialVersionUID = 2433481600962006340L;

   /**
    * Constructeur
    * 
    * @param cause
    *           la cause de l'exception
    */
   public IntegrationException(Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    * @param cause
    *           la cause de l'exception
    */
   public IntegrationException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'erreur
    */
   public IntegrationException(String message) {
      super(message);
   }

}
