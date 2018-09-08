package fr.urssaf.image.sae.format.conversion.exceptions;


/**
 * Erreur de paramètrage de la conversion. 
 * 
 */
public class ConversionParametrageException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ConversionParametrageException(String message) {
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
   public ConversionParametrageException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
