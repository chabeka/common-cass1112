package fr.urssaf.image.sae.ecde.util.test;

import java.io.File;
import java.net.URI;

/**
 * Objet utilisé pour la capture de masse.
 * <br>
 * En effet, il permet de renvoyer comme valeur URL ECDE du repertoire document
 * <br>ainsi l'emplacement du répertoire documents(chemin du répertoire)
 * 
 */
public class EcdeTestDocument {

   private URI urlEcdeDocument;
   private File repEcdeDocuments;
   /**
    * @return the urlEcdeDocument
    */
   public final URI getUrlEcdeDocument() {
      return urlEcdeDocument;
   }
   /**
    * @param urlEcdeDocument the urlEcdeDocument to set
    */
   public final void setUrlEcdeDocument(URI urlEcdeDocument) {
      this.urlEcdeDocument = urlEcdeDocument;
   }
   /**
    * @return the repEcdeDocuments
    */
   public final File getRepEcdeDocuments() {
      return repEcdeDocuments;
   }
   /**
    * @param repEcdeDocuments the repEcdeDocuments to set
    */
   public final void setRepEcdeDocuments(File repEcdeDocuments) {
      this.repEcdeDocuments = repEcdeDocuments;
   }
   
}
