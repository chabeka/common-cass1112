package fr.urssaf.image.parser_opencsv.application.exception;


public class HashInexistantException extends Exception {

   private static final long serialVersionUID = -6742864489692762161L;

   private static final String MESSAGE = "Le Hash n'est pas renseigné";

   public HashInexistantException() {
      super(MESSAGE);
   }

   public HashInexistantException(final Throwable cause) {
      super(MESSAGE, cause);
   }

}
