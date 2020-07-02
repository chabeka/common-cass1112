package fr.urssaf.image.parser_opencsv.application.exception;


public class MetaFormatCSVException extends Exception {

   private static final long serialVersionUID = -6053353836651774753L;

   private static final String MESSAGE = "Le format de la Meta dans le CSV n'est pas correct";

   /**
    * 
    */
   public MetaFormatCSVException() {
      this(MESSAGE);
   }

   /**
    * @param message
    * @param cause
    */
   public MetaFormatCSVException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * @param message
    */
   public MetaFormatCSVException(final String message) {
      super(message);
   }

}
