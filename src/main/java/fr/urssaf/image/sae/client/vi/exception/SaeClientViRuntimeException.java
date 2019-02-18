package fr.urssaf.image.sae.client.vi.exception;

/**
 * Exception Runtime générale pour l'artéfact
 * 
 */
public class SaeClientViRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -6427318565752987479L;

   /**
    * Constructeur
    * 
    * @param cause
    *           l'exception mère
    */
   public SaeClientViRuntimeException(Throwable cause) {
      super(cause);
   }

}
