package fr.urssaf.image.sae.client.vi.exception;

/**
 * Exception levée lors d'une erreur lors de la signature du VI
 * 
 */
public class ViSignatureException extends RuntimeException {

   private static final long serialVersionUID = 1278929756286026464L;

   /**
    * Constructeur
    * 
    * @param cause
    *           erreur source
    */
   public ViSignatureException(Throwable cause) {
      super(cause);
   }

}
