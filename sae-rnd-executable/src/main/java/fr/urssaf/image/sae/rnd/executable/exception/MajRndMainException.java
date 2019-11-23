package fr.urssaf.image.sae.rnd.executable.exception;

/**
 * Exception levée lors de l’exécution d'un traitement de capture en masse
 * 
 * 
 */
public class MajRndMainException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public MajRndMainException(Throwable cause) {
      super(cause);
   }

}
