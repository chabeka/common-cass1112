package fr.urssaf.image.sae.integration.ihmweb.exception;


/**
 * Exception générale Runtime 
 */
public class IntegrationRuntimeException extends RuntimeException {

   
   private static final long serialVersionUID = 1081518128233592632L;
   

   /**
    * Constructeur
    * 
    * @param cause la cause de l'exception
    */
   public IntegrationRuntimeException(Throwable cause) {
      super(cause);
   }
   
   
   /**
    * Constructeur
    * 
    * @param message le message de l'exception
    */
   public IntegrationRuntimeException(String message) {
      super(message);
   }
   
   
   /**
    * Constructeur
    * 
    * @param message le message de l'exception
    * @param cause la cause de l'exception
    */
   public IntegrationRuntimeException(String message,Throwable cause) {
      super(message,cause);
   }
   
}
