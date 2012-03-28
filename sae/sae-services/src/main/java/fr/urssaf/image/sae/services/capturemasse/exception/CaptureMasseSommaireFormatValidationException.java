/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

/**
 * Exception levée lorsque le format du fichier sommaire.xml est invalide
 * 
 */
public class CaptureMasseSommaireFormatValidationException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param causeTechnique
    *           résumé succint de la cause de l'erreur
    * @param cause
    *           cause mère
    */
   public CaptureMasseSommaireFormatValidationException(
         final String causeTehnique, Throwable cause) {
      super(
            "Le fichier sommaire n'est pas valide ( "
                  + causeTehnique
                  + " ). Détails : Aucun document du sommaire ne sera intégré dans le SAE.",
            cause);
   }

}
