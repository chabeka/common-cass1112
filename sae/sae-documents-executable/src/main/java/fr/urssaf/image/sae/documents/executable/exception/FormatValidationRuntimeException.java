package fr.urssaf.image.sae.documents.executable.exception;

/**
 * Erreur levée lors de la validation de format d'un fichier.
 */
public class FormatValidationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 8144415953374151234L;

   /**
    * Constructeur.
    * 
    * @param cause
    *           exception source
    */
   public FormatValidationRuntimeException(final Throwable cause) {
      super(cause);
   }
}
