package fr.urssaf.image.sae.format.conversion.exceptions;


/**
 * Impossible d’instancier le convertisseur. 
 * 
 */
public class ConvertisseurInitialisationException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ConvertisseurInitialisationException(String message) {
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
   public ConvertisseurInitialisationException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
