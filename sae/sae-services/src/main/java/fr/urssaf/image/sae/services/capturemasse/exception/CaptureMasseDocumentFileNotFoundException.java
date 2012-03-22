/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

/**
 * Exception levée lorsque l'URL ECDE du fichier sommaire.xml est introuvable
 * 
 */
public class CaptureMasseDocumentFileNotFoundException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * constructeur
    * 
    * @param chemin
    *           chemin du fichier
    */
   public CaptureMasseDocumentFileNotFoundException(final String chemin) {
      super("Le document " + chemin + " est introuvable");
   }

}
