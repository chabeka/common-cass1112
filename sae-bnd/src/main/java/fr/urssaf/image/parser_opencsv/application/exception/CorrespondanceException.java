package fr.urssaf.image.parser_opencsv.application.exception;


public class CorrespondanceException extends Exception {

   private static final long serialVersionUID = -8055064813325648506L;

   private static final String MESSAGE = "Aucune Correspondance RND trouvé pour le Code ";

   public CorrespondanceException(final String code) {
      super(MESSAGE + code);
   }

   public CorrespondanceException(final String message, final String code) {
      super(message + code);
   }

   public CorrespondanceException(final String message, final Throwable cause) {
      super(message, cause);
   }

   public CorrespondanceException(final Throwable cause) {
      super(cause);
   }

}
