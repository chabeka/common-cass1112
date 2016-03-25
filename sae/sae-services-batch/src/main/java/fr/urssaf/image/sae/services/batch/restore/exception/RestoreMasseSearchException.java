package fr.urssaf.image.sae.services.batch.restore.exception;

/**
 * Exception levée lorsque la recherche des documents pour un restore de masse.
 * 
 */
public class RestoreMasseSearchException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause mère
    */
   public RestoreMasseSearchException(Throwable cause) {
      super("Aucun document correspondant à l'identifiant de traitement ne sera restoré dans la Ged Nationale", cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           résumé succint de la cause de l'erreur
    * @param cause
    *           cause mère
    */
   public RestoreMasseSearchException(String message,
         Throwable cause) {
      super("Aucun document correspondant à l'identifiant de traitement ne sera restoré dans la Ged Nationale ("
            + message + ").", cause);
   }
}
