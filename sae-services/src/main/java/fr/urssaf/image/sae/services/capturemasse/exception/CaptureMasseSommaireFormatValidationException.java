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
    * @param url
    *           url du fichier sommaire.xml
    */
   public CaptureMasseSommaireFormatValidationException(final String url) {
      super("Le format du fichier sommaire.xml " + url + " est invalide");
   }

}
