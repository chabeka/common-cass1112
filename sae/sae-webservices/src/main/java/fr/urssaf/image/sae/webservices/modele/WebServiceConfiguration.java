/**
 * 
 */
package fr.urssaf.image.sae.webservices.modele;

/**
 * configuration du webservice
 * 
 */
public class WebServiceConfiguration {

   private final boolean ancienWsActif;

   /**
    * 
    * @param ancienWsActif
    *           booleen indiquant si l'ancien mode de fonctionnement WS est
    *           actif ou non
    */
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
