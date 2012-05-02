package fr.urssaf.image.sae.ecde.exception;
/**
 * Exception levée lorsqu'une URL ECDE ne respecte pas le format d'une URL ECDE
 * 
 * @see EcdeGeneralException
 * 
 * */

public class EcdeBadURLFormatException extends EcdeGeneralException {
   
   private static final long serialVersionUID = 1L;

   /**
    * Constructor
    * 
    *
    * @param message cause
    */
   public EcdeBadURLFormatException(String message) {
      super(message);
   }
   
}