package fr.urssaf.image.sae.lotinstallmaj.exception;

/**
 * Exception à lever dans le traitement du JAR Executable. 
 *
 */
public final class MajLotGeneralException extends Exception {
   
   private static final long serialVersionUID = 1L;
   
   /**
    * Construit une nouvelle {@link MajLotGeneralException} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public MajLotGeneralException(final String message, final Throwable cause) {
      super(message, cause);
   }
   
   /**
    * Construit une nouvelle {@link MajLotGeneralException} avec un message.
    * 
    * @param message
    *           : Le message d'erreur
    */
   public MajLotGeneralException(final String message) {
      super(message);
   }

}
