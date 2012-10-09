package fr.urssaf.image.sae.services.executable.traitementmasse.exception;

/**
 * Exception levée lors de l’exécution d'un traitement de capture en masse
 * 
 * 
 */
public class TraitementMasseMainException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public TraitementMasseMainException(Throwable cause) {
      super(cause);
   }

}
