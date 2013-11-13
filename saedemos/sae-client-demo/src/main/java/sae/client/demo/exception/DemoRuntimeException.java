package sae.client.demo.exception;

/**
 * Exception Runtime pour le jar de démo
 */
public class DemoRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -624810046377374441L;

   /**
    * Constructeur
    * 
    * @param cause
    *           la cause mère
    */
   public DemoRuntimeException(Throwable cause) {
      super(cause);
   }

}
