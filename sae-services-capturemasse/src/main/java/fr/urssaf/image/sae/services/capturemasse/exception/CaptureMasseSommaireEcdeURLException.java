/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

/**
 * Exception levée lorsque l'URL ECDE du fichier sommaire.xml estincorrecte
 * 
 */
public class CaptureMasseSommaireEcdeURLException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param url
    *           url du sommaire
    */
   public CaptureMasseSommaireEcdeURLException(final String url) {
      super("l'URL ECDE " + url + " est incorrecte ");
   }

}
