/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

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
   public CaptureMasseSommaireFileNotFoundException(String url) {
      super("Le fichier sommaire " + url + " est introuvable");
   }

}
