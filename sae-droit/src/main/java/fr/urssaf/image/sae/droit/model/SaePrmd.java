/**
 * 
 */
package fr.urssaf.image.sae.droit.model;

import java.util.Map;

import fr.urssaf.image.sae.droit.dao.model.Prmd;

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

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof SaePrmd) {
         SaePrmd saePrmd = (SaePrmd) obj;
         areEquals = prmd.getBean().equals(saePrmd.getPrmd().getBean())
               && prmd.getCode().equals(saePrmd.getPrmd().getCode())
               && prmd.getDescription().equals(
                     saePrmd.getPrmd().getDescription())
               && prmd.getLucene().equals(saePrmd.getPrmd().getLucene())
               && prmd.getMetadata().equals(saePrmd.getPrmd().getMetadata());

         if (!(values == null && saePrmd.getValues() == null)) {
            if (values != null && saePrmd.getValues() != null) {
               areEquals = areEquals
                     && values.keySet().size() == saePrmd.getValues().keySet()
                           .size()
                     && values.keySet().containsAll(
                           saePrmd.getValues().keySet());
            }
         }

      }

      return areEquals;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

}
