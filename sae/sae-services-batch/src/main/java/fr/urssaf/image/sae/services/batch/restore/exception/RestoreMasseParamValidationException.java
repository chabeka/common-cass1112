package fr.urssaf.image.sae.services.batch.restore.exception;

/**
 * Exception levée lorsque l'identifiant de suppression est invalide
 * 
 */
public class RestoreMasseParamValidationException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause mère
    */
   public RestoreMasseParamValidationException(Throwable cause) {
      super("Aucun document correspondant à l'identifiant de suppression ne sera restoré dans la Ged Nationale", cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           résumé succint de la cause de l'erreur
    * @param cause
    *           cause mère
    */
   public RestoreMasseParamValidationException(String message,
         Throwable cause) {
      super("Aucun document correspondant à l'identifiant de suppresion ne sera restoré dans la Ged Nationale ("
            + message + ").", cause);
   }
}
