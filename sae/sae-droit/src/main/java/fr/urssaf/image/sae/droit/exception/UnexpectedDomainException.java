/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Exception levée lorsque un domaine est spécifié 
 * parmis les métas lors de l'archivation 
 * 
 */
public class UnexpectedDomainException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public UnexpectedDomainException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    * @param cause
    *           cause mère
    */
   public UnexpectedDomainException(String message, Throwable cause) {
      super(message, cause);
   }
}
