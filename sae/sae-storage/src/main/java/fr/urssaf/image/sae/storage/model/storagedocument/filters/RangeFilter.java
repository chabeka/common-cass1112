package fr.urssaf.image.sae.storage.model.storagedocument.filters;

/**
 * Classe concrète qui permet d'effectuer un filtre avec un range de valeur
 * 
 * 
 */
public class RangeFilter extends AbstractFilter {

   /**
    * Valeur minimum du filtre
    */
   private Object minValue;

   /**
    * Valeur maximum du filtre
    */
   private Object maxValue;

   /**
    * Construit un {@link RangeFilter }.
    * 
    * @param shortCode
    *           Code court de la métadonnée qui servira de filtre
    * @param minValue
    *           Valeur minimum du filtre
    * @param maxValue
    *           Valeur maximum du filtre
    */
   public RangeFilter(final String shortCode, final Object minValue,
         final Object maxValue) {
      super(shortCode);
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
