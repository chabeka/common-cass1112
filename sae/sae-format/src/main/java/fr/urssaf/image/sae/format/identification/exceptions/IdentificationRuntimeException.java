package fr.urssaf.image.sae.format.identification.exceptions;

/**
 * Une erreur s’est produite au niveau de l’outil d’identification. 
 * 
 */
public class IdentificationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public IdentificationRuntimeException(String message) {
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
   public IdentificationRuntimeException(String message, Throwable cause) {
      super(message,cause);
   }
   
}
