package fr.urssaf.image.sae.lotinstallmaj.exception;

/**
 * Exception à lever dans le traitement du JAR Executable. 
 *
 */
public final class MajLotRestartTomcatException extends Exception {

   private static final long serialVersionUID = 1L;

   private static final String MESSAGE = "DFCE a été mis jour. Vous devez redémarrer le Serveur Tomcat de la Webapp DFCE ";

   /**
    * Construit une nouvelle {@link MajLotRestartTomcatException} avec un message et
    * une cause données.
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public MajLotRestartTomcatException(final String message, final Throwable cause) {
      super(message, cause);
   }

   /**
    * Construit une nouvelle {@link MajLotRestartTomcatException} avec un message.
    * 
    * @param message
    *           : Le message d'erreur
    */
   public MajLotRestartTomcatException(final String message) {
      super(message);
   }

   public MajLotRestartTomcatException() {
      super(MESSAGE);
   }

}
