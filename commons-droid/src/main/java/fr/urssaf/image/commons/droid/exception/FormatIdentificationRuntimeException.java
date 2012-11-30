package fr.urssaf.image.commons.droid.exception;

public class FormatIdentificationRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -831717734825912969L;

   public FormatIdentificationRuntimeException(Throwable cause) {
      super(cause);
   }
   
   public FormatIdentificationRuntimeException(String message) {
      super(message);
   }
   
}
