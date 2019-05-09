package fr.urssaf.image.commons.itext.exception;

/**
 * Erreur levée lors d'une erreur de paramétrage de la conversion en PDF.
 */
public class FormatConversionParametrageException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur.
    * 
    * @param message
    *           message
    */
   public FormatConversionParametrageException(final String message) {
      super(message);
   }
}
