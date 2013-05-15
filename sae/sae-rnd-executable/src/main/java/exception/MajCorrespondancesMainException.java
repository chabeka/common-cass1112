package exception;

/**
 * Exception levée lors de l’exécution d'un traitement de capture en masse
 * 
 * 
 */
public class MajCorrespondancesMainException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * 
    * @param cause
    *           cause de l'exception
    */
   public MajCorrespondancesMainException(Throwable cause) {
      super(cause);
   }

}
