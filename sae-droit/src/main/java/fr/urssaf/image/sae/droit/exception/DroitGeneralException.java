/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * Classe mère des exceptions non runtime levée par sae-droit
 * 
 */
public class DroitGeneralException extends Exception {

   private static final long serialVersionUID = 5421108891055222031L;

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public DroitGeneralException(String message) {
      super(message);
   }

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    * @param cause
    *           cause mère
    */
   public DroitGeneralException(String message, Throwable cause) {
      super(message, cause);
   }
   
   /**
    * constructeur
    * 
    * @param cause
    *           cause mère
    */
   public DroitGeneralException(Throwable cause) {
      super(cause);
   }

}
