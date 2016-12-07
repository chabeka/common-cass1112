package fr.urssaf.image.sae.batch.documents.executable.exception;

/**
 * Erreur levée quand au moins un paramètre obligatoire est manquant ou vide.
 */
public class ParametreRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -7650012673945394957L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ParametreRuntimeException(String message) {
      super(message);
   }

}
