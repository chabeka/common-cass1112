package fr.urssaf.image.sae.format.referentiel.exceptions;

/**
 * Erreur levée quand au moins un paramètre obligatoire est manquant ou vide
 * 
 */
public class UnknownParameterException extends RuntimeException {

   private static final long serialVersionUID = 5431010446702527384L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public UnknownParameterException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    * @param cause
    *           cause de l'erreur
    */
   public UnknownParameterException(String message, Throwable cause) {
      super(message, cause);
   }

}
