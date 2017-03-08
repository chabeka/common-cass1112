package fr.urssaf.image.sae.format.identification.exceptions;


/**
 * Impossible d’instancier l’identificateur. 
 * 
 */
public class IdentifierInitialisationException extends Exception {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public IdentifierInitialisationException(String message) {
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
   public IdentifierInitialisationException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
