package fr.urssaf.image.sae.services.batch.suppression.exception;

/**
 * Exception levée lorsque la requete lucene est invalide
 * 
 */
public class SuppressionMasseRequeteValidationException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause mère
    */
   public SuppressionMasseRequeteValidationException(Throwable cause) {
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
   public SuppressionMasseRequeteValidationException(String message,
         Throwable cause) {
      super("Aucun document correspondant à la requête ne sera supprimé dans la Ged Nationale ("
            + message + ").", cause);
   }
}
