package fr.urssaf.image.commons.itext.exception;

/**
 * Erreur levée lors d'une erreur d'extraction d'image.
 */
public class ExtractionException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur.
    * 
    * @param cause
    *           exception source
    */
   public ExtractionException(final Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur.
    * 
    * @param message
    *           message
    */
   public ExtractionException(final String message) {
      super(message);
   }
}
