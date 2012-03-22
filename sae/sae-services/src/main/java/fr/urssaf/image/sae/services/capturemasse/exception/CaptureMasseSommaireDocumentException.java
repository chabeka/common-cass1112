/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

import org.springframework.util.Assert;

/**
 * Exception levée par un document du fichier sommaire.xml lors du traitement de
 * capture de masse
 * 
 */
public class CaptureMasseSommaireDocumentException extends Exception {

   private static final long serialVersionUID = 1L;

   private final int index;

   /**
    * Constructeur
    * 
    * @param index
    *           index du document dans le fichier sommaire.xml
    * @param cause
    *           cause de l'exception de contrôle
    */
   public CaptureMasseSommaireDocumentException(final int index,
         final Exception cause) {

      super(cause);

      Assert.notNull(cause, "l'exception mère ne doit pas être à null");

      this.index = index;
   }

   /**
    * @return the index
    */
   public final int getIndex() {
      return index;
   }

}
