package fr.urssaf.image.sae.storage.model.storagedocument.filters;

/**
 * Classe concrète qui permet d'effectuer un filtre du type valeur métadonnées
 * différente d'une valeur
 * 
 * 
 */
public class NotValueFilter extends AbstractFilter {

   /**
    * Construit un {@link NotValueFilter }.
    * 
    * @param shortCode
    *           Code court de la métadonnée qui servira de filtre
    * @param longCode
    *           Code long de la métadonnée qui servira de filtre
    * @param value
    *           Valeur du filtre
    */
   public NotValueFilter(final String shortCode, final String longCode,
         final Object value) {
      super(shortCode, longCode);
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
