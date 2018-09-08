/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * 
 * 
 */
public class PrmdNotFoundException extends Exception {

   private static final long serialVersionUID = -2787691464718590500L;

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public PrmdNotFoundException(String message) {
      super(message);
   }
   
   /**
    * constructeur
    * 
    * @param cause
    *           cause de l'exception
    */
   public PrmdNotFoundException(Throwable cause) {
      super(cause);
   }
   
   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause de l'exception       
    */
   public PrmdNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
