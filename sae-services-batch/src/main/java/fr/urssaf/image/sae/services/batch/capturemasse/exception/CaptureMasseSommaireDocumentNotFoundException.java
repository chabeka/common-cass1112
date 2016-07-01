/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.exception;

/**
 * Exception quand le fichier d'un document du sommaire n'existe pas dans l'ECDE
 * 
 */
public class CaptureMasseSommaireDocumentNotFoundException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param cheminRelatif
    *           chemin relatif du document
    */
   public CaptureMasseSommaireDocumentNotFoundException(final String cheminRelatif) {
      super("Le fichier document " + cheminRelatif + " est introuvable");
   }

}
