package fr.urssaf.image.sae.services.executable.capturemasse.exception;

/**
 * Exception levée lors de l’exécution du service d’archivage en masse
 * 
 * 
 */
public class CaptureMasseMainException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public CaptureMasseMainException(Throwable cause) {
      super(cause);
   }

}
