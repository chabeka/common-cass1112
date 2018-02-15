package fr.urssaf.image.sae.lotinstallmaj.exception;

/**
 * Exception à lever dans le cas d'erreurs que l'on ne souhaite<br>
 * pas traiter ou que l'on ne peut pas traiter.
 * 
 */
public final class MajLotRuntimeException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause de l'exception
    */
   public MajLotRuntimeException(Throwable cause) {
      super(cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    */
   public MajLotRuntimeException(String message) {
      super(message);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           le message de l'exception
    * @param cause
    *           cause de l'exception
    */
   public MajLotRuntimeException(String message, Throwable cause) {
      super(message, cause);
   }

}
