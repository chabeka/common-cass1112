/**
 * 
 */
package fr.urssaf.image.sae.webservices.modele;

/**
 * 
 * 
 */
public class WebServiceConfiguration {

   private boolean ancienWsActif;

   public WebServiceConfiguration(boolean ancienWsActif) {
      this.ancienWsActif = ancienWsActif;
   }

   /**
    * @return l'indicateur de support d'appel de l'ancien Ws
    */
   public final boolean isAncienWsActif() {
      return ancienWsActif;
   }

}
