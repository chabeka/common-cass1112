package fr.urssaf.image.sae.storage.model.storagedocument.filters;

/**
 * Classe concrète qui permet d'effectuer un filtre du type valeur métadonnée
 * n'appartient pas à un intervalle
 * 
 * 
 */
public class NotRangeFilter extends AbstractFilter {

   /**
    * Valeur minimum du filtre
    */
   private Object minValue;

   /**
    * Valeur maximum du filtre
    */
   private Object maxValue;

   /**
    * Construit un {@link NotRangeFilter }.
    * 
    * @param shortCode
    *           Code court de la métadonnée qui servira de filtre
    * @param longCode
    *           Code long de la métadonnée qui servira de filtre
    * @param minValue
    *           Valeur minimum du filtre
    * @param maxValue
    *           Valeur maximum du filtre
    */
   public NotRangeFilter(final String shortCode, final String longCode,
         final Object minValue, final Object maxValue) {
      super(shortCode, longCode);
      this.minValue = minValue;
      this.maxValue = maxValue;
   }

   /**
    * @return the minValue
    */
   public final Object getMinValue() {
      return minValue;
   }

   /**
    * @param minValue
    *           the minValue to set
    */
   public final void setMinValue(Object minValue) {
      this.minValue = minValue;
   }

   /**
    * @return the maxValue
    */
   public final Object getMaxValue() {
      return maxValue;
   }

   /**
    * @param maxValue
    *           the maxValue to set
    */
   public final void setMaxValue(Object maxValue) {
      this.maxValue = maxValue;
   }

}
