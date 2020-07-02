package fr.urssaf.image.parser_opencsv.application.exception;

public class BNDScriptRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 908906353817707629L;

   /**
    * 
    */
   public BNDScriptRuntimeException() {
      super();
   }

   /**
    * @param message
    * @param cause
    */
   public BNDScriptRuntimeException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * @param message
    */
   public BNDScriptRuntimeException(final String message) {
      super(message);
   }

   /**
    * @param cause
    */
   public BNDScriptRuntimeException(final Throwable cause) {
      super(cause);
   }

}
