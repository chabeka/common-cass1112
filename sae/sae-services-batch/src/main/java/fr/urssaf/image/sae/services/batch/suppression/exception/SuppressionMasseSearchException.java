package fr.urssaf.image.sae.services.batch.suppression.exception;

/**
 * Exception levée lorsque la recherche des documents pour la suppression.
 * 
 */
public class SuppressionMasseSearchException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause mère
    */
   public SuppressionMasseSearchException(Throwable cause) {
      super("Aucun document correspondant à la requête ne sera supprimé dans la Ged Nationale", cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           résumé succint de la cause de l'erreur
    * @param cause
    *           cause mère
    */
   public SuppressionMasseSearchException(String message,
         Throwable cause) {
      super("Aucun document correspondant à la requête ne sera supprimé dans la Ged Nationale ("
            + message + ").", cause);
   }
}
