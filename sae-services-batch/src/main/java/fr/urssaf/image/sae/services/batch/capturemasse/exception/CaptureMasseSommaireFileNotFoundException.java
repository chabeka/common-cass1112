/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.exception;

/**
 * Exception levée lorsque l'URL ECDE du fichier sommaire.xml est introuvable
 * 
 */
public class CaptureMasseSommaireFileNotFoundException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeurl
    * 
    * @param url
    *           url du fichier sommaire.xml
    */
   public CaptureMasseSommaireFileNotFoundException(final String url) {
      super("Le fichier sommaire " + url + " est introuvable");
   }

}
