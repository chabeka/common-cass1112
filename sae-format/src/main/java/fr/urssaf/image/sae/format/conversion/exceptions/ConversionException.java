package fr.urssaf.image.sae.format.conversion.exceptions;


/**
 * Erreur de paramètrage de la conversion. 
 * 
 */
public class ConversionException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ConversionException(String message) {
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
   public ConversionException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
