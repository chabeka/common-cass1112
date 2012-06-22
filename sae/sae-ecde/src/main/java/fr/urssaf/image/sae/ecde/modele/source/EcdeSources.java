package fr.urssaf.image.sae.ecde.modele.source;




/***
 * 
 * Classe permettant de recuperer un tableau de EcdeSource
 * <br> à savoir EcdeSource[]
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
      return sources;
   }

   /**
    * @param sources the sources to set
    */
   public final void setSources(EcdeSource[] sources) {
      this.sources = sources;
   }
   

}
