/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.exception;

/**
 * Exception levée lorsque le format du fichier sommaire.xml est invalide
 * 
 */
public class CaptureMasseSommaireFormatValidationException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cause
    *           cause mère
    */
   public CaptureMasseSommaireFormatValidationException(Throwable cause) {
      super("Aucun document du sommaire ne sera intégré dans le SAE.", cause);
   }

   /**
    * Constructeur
    * 
    * @param message
    *           résumé succint de la cause de l'erreur
    * @param cause
    *           cause mère
    */
   public CaptureMasseSommaireFormatValidationException(String message,
         Throwable cause) {
      super("Aucun document du sommaire ne sera intégré dans le SAE ("
            + message + ").", cause);
   }

}
