package fr.urssaf.image.sae.documents.executable.exception;

/**
 * Erreur levée lors de la validation de format d'un fichier.
 */
public class PurgeRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 6514428262778393529L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public PurgeRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param exception
    *           exception mère
    */
   public PurgeRuntimeException(Exception exception) {
      super(exception);
   }

   /**
    * @param message
    *           message de l'erreur
    * @param exception
    *           exception mère
    */
   public PurgeRuntimeException(String message, Exception exception) {
      super(message, exception);
   }
}
