package fr.urssaf.image.parser_opencsv.application.exception;


public class CorrespondanceFormatException extends Exception {

   private static final long serialVersionUID = -6053353836651774753L;

   /**
    * @param message
    * @param cause
    */
   public CorrespondanceFormatException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * @param message
    */
   public CorrespondanceFormatException(final String message) {
      super(message);
   }

}
