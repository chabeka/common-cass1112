package fr.urssaf.image.sae.storage.model.storagedocument.filters;


/**
 * Classe concrète qui permet d'effectuer un filtre avec une valeur
 * 
 * 
 */
public class ValueFilter extends AbstractFilter {

   /**
    * Construit un {@link ValueFilter }.
    * 
    * @param shortCode
    *           Code court de la métadonnée qui servira de filtre
    * @param value
    *           Valeur du filtre
    */
   public ValueFilter(final String shortCode, final Object value) {
      super(shortCode);
      this.value = value;
   }

   /**
    * Valeur du filtre
    */
   private Object value;

   /**
    * @return the value
    */
   public final Object getValue() {
      return value;
   }

   /**
    * @param value
    *           the value to set
    */
   public final void setValue(Object value) {
      this.value = value;
   }

}
