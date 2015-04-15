package fr.urssaf.image.sae.storage.model.storagedocument.filters;

/**
 * Classe abstraite qui permet de filtrer la recherche par pagination
 * 
 * 
 */
public abstract class AbstractFilter {

   /**
    * Code court de la métadonnée qui servira de filtre
    */
   private String shortCode;

   /**
    * Construit un {@link AbstractFilter }.
    * 
    * @param shortCode
    *           Code court de la métadonnée qui servira de filtre
    */
   public AbstractFilter(final String shortCode) {
      this.shortCode = shortCode;
   }

   /**
    * @return the shortCode
    */
   public final String getShortCode() {
      return shortCode;
   }

   /**
    * @param shortCode
    *           the shortCode to set
    */
   public final void setShortCode(String shortCode) {
      this.shortCode = shortCode;
   }
}
