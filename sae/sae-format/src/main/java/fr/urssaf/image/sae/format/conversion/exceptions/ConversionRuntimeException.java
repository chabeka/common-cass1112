package fr.urssaf.image.sae.format.conversion.exceptions;


/**
 * Erreur inattendu lors de la conversion. 
 * 
 */
public class ConversionRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ConversionRuntimeException(String message) {
      super(message);
   }
   
   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    * @param cause
    *           cause          
    */
   public ConversionRuntimeException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
