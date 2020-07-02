package fr.urssaf.image.parser_opencsv.application.exception;

public class CountNbrePageFileException extends Exception {

   private static final long serialVersionUID = -6742864489692762161L;

   private static final String MESSAGE = "Le nombre de page du binaire n'a pas pu être calculé";

   public CountNbrePageFileException() {
      super(MESSAGE);
   }

   public CountNbrePageFileException(final Throwable cause) {
      super(MESSAGE, cause);
   }

}
