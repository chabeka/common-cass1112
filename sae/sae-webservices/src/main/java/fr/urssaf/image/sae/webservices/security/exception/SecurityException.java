package fr.urssaf.image.sae.webservices.security.exception;


/**
 * Exception de sécurité non prévue, et qui ne devrait pas se produire
 * (pour les exceptions techniques, levées par exemple lors des TU)
 */
public class SecurityException extends RuntimeException {


   private static final long serialVersionUID = 5359407119760747541L;
   
   
   /**
    * Constructeur
    */
   public SecurityException() {
     super();
   }
   
   
   
   /**
    * Constructeur
    * 
    * @param message le message de l'exception
    */
   public SecurityException(String message) {
     super(message);
      }

   
   /**
    * Constructeur
    * 
    * @param message le message de l'exception
    * @param cause l'origine de l'exception
    */
   public SecurityException(String message, Throwable cause) {
       super(message, cause);
   }

   
   /**
    * Constructeur
    * 
    * @param cause l'origine de l'exception
    */
   public SecurityException(Throwable cause) {
       super(cause);
   }
   
   
}
