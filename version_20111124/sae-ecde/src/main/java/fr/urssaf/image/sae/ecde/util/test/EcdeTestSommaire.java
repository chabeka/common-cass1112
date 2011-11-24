package fr.urssaf.image.sae.ecde.util.test;

import java.io.File;
import java.net.URI;

/**
 * Objet utilisé pour la capture de masse.
 * <br>
 * En effet, il permet de renvoyer comme valeur URL ECDE
 * <br>ainsi l'emplacement où doit se trouver le fichier sommaire.xml
 */
public class EcdeTestSommaire {
   
   private URI urlEcde;
   private File repEcde;
   
   /**
    * @return the urlEcde
    */
   public final URI getUrlEcde() {
      return urlEcde;
   }
   /**
    * @param urlEcde the urlEcde to set
    */
   public final void setUrlEcde(URI urlEcde) {
      this.urlEcde = urlEcde;
   }
   /**
    * @return the repEcde
    */
   public final File getRepEcde() {
      return repEcde;
   }
   /**
    * @param repEcde the repEcde to set
    */
   public final void setRepEcde(File repEcde) {
      this.repEcde = repEcde;
   }
   

}
