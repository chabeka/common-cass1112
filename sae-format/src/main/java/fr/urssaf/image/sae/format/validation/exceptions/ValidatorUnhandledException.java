package fr.urssaf.image.sae.format.validation.exceptions;

/**
 * Exception remontée lorsque la validation d'un document ne peut être effectuée
 * à cause d'un plantage du validator.
 */
public class ValidatorUnhandledException extends Exception {

   private static final long serialVersionUID = 398985123758266287L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause
    */
   public ValidatorUnhandledException(Throwable cause) {
      super(cause);
   }
}
