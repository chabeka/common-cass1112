/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * 
 * 
 */
public class PagmfNotFoundException extends RuntimeException {

   private static final long serialVersionUID = -2787691464718590500L;

   /**
    * constructeur
    * 
    * @param message
    *           message de l'exception
    */
   public PagmfNotFoundException(String message) {
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
   public PagmfNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
   
   /**
    * constructeur
    * 
    * @param cause
    *           cause mère
    */
   public PagmfNotFoundException(Throwable cause) {
      super(cause);
   }
}
