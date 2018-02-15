package fr.urssaf.image.commons.itext.exception;

/**
 * Erreur levée lors d'une erreur de conversion se produit.
 */
public class FormatConversionException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur.
    * 
    * @param cause
    *           exception source
    */
   public FormatConversionException(final Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur.
    * 
    * @param message
    *           message
    */
   public FormatConversionException(final String message) {
      super(message);
   }
}
