package fr.urssaf.image.sae.services.exception.format.validation;


/**
 * Exception levée lorsque de la validation d'un fichier.
 * 
 * Erreur technique : Exemple FileNotFoundException, ValidatorInitialisationException
 * 
 */
public class FormatValidationRuntimeException extends RuntimeException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   /**
    * Constructeur
    * @param message message d'origine
    */
   public FormatValidationRuntimeException(String message){
      super(message);
   }
   
   /**
    * Constructeur
    * @param cause cause d'origine
    */
   public FormatValidationRuntimeException(Throwable cause){
      super(cause);
   }
   
   /**
    * Constructeur
    * @param cause cause d'origine
    */
   public FormatValidationRuntimeException(String message, Throwable cause){
      super(message, cause);
   }
}
