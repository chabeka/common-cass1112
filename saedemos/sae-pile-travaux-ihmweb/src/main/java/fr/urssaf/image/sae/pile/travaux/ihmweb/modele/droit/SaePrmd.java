/**
 * 
 */
package fr.urssaf.image.sae.pile.travaux.ihmweb.modele.droit;

import java.util.Map;

/**
 * Prmd contenu dans le context de sécurité
 * 
 */
public class SaePrmd {

   /** PRMD d'un droit du SAE */
   private Prmd prmd;

   /**
    * valeurs des paramètres dynamiques du PRMD. Il peut être vide si le PRMD
    * n'est pas dynamique
    */
   private Map<String, String> values;

   /**
    * @return le PRMD d'un droit du SAE
    */
   public final Prmd getPrmd() {
      return prmd;
   }

   /**
    * @param prmd
    *           PRMD d'un droit du SAE
    */
   public final void setPrmd(Prmd prmd) {
      this.prmd = prmd;
   }

   /**
    * @return les valeurs des paramètres dynamiques du PRMD
    */
   public final Map<String, String> getValues() {
      return values;
   }

   /**
    * @param values
    *           valeurs des paramètres dynamiques du PRMD
    */
   public final void setValues(Map<String, String> values) {
      this.values = values;
   }

}
