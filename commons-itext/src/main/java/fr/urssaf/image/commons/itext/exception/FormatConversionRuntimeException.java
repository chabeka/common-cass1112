package fr.urssaf.image.commons.itext.exception;

/**
 * Erreur levée lorsqu'une erreur inattendue se produit lors de la conversion
 * d'un fichier TIFF au format PDF.
 */
public class FormatConversionRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur.
    * 
    * @param cause
    *           exception source
    */
   public FormatConversionRuntimeException(final Throwable cause) {
      super(cause);
   }
}
