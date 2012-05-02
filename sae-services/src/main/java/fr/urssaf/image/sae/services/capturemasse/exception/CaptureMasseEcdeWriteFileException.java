/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

/**
 * 
 * 
 */
public class CaptureMasseEcdeWriteFileException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param url
    *           url du répertoire de traitement
    */
   public CaptureMasseEcdeWriteFileException(final String url) {
      super("Le SAE ne dispose pas des droits "
            + "d'écriture dans le répertoire de traitement ECDE " + url);
   }

}
