/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.exception;

/**
 * Exception levée lorsque le format d'une ligne est erroné
 * 
 */
public class LineFormatException extends RuntimeException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * @param message
    */
   public LineFormatException(String message) {
      super(message);
   }

}
