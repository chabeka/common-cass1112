package fr.urssaf.image.sae.integration.ihmweb.modele.ecde;

import java.util.ArrayList;
import java.util.List;

/***
 * 
 * Classe permettant de recuperer un tableau de EcdeSource <br>
 * à savoir EcdeSource[]
 * 
 */
@SuppressWarnings( { "PMD.MethodReturnsInternalArray",
      "PMD.ArrayIsStoredDirectly" })
public class EcdeSources {

   /*
    * Objet sources
    */
   private List<EcdeSource> sources = new ArrayList<EcdeSource>();

   /**
    * @return the sources
    */
   public final List<EcdeSource> getSources() {
      return sources;
   }

   /**
    * @param sources
    *           the sources to set
    */
   public final void setSources(List<EcdeSource> sources) {
      this.sources = sources;
   }

}
