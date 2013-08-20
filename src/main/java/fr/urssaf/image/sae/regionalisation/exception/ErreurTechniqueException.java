package fr.urssaf.image.sae.regionalisation.exception;

import java.io.IOException;

/**
 * Erreur levée lors d'une erreur technique
 * 
 * 
 */
public class ErreurTechniqueException extends RuntimeException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private int lineNumber = -1;

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public ErreurTechniqueException(Throwable cause) {
      super(cause);
   }

   /**
    * @param lineNumber
    *           numéro de ligne où se situe l'erreur
    * @param cause
    *           cause de l'exception
    */
   public ErreurTechniqueException(int lineNumber, IOException cause) {
      super(cause);
      this.lineNumber = lineNumber;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Throwable#getMessage()
    */
   @Override
   public final String getMessage() {
      String message = "";

      if (lineNumber != -1) {
         message = "Erreur à la ligne " + lineNumber + "\n";
      }
      return message + super.getMessage();
   }

}
