package fr.urssaf.image.sae.ecde.modele.source;

import java.util.Arrays;

/***
 * 
 * Classe permettant de recuperer un tableau de EcdeSource <br>
 * à savoir EcdeSource[]
 * 
 */
public class EcdeSources {

   /*
    * Objet sources
    */
   private EcdeSource[] sources;

   /**
    * @return the sources
    */
   public final EcdeSource[] getSources() {
      return Arrays.copyOf(sources, sources.length);
   }

   /**
    * @param sources
    *           the sources to set
    */
   public final void setSources(EcdeSource[] sources) {
      this.sources = Arrays.copyOf(sources, sources.length);
   }

}
