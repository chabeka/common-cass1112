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
    * Code long de la métadonnée qui servira de filtre
    */
   private String longCode;

   /**
    * Construit un {@link AbstractFilter }.
    * 
    * @param shortCode
    *           Code court de la métadonnée qui servira de filtre
    * @param longCode
    *           Code long de la métadonnée qui servira de filtre
    */
   public AbstractFilter(final String shortCode, final String longCode) {
      this.shortCode = shortCode;
      this.longCode = longCode;
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

   /**
    * @return the longCode
    */
   public String getLongCode() {
      return longCode;
   }

   /**
    * @param longCode the longCode to set
    */
   public void setLongCode(String longCode) {
      this.longCode = longCode;
   }
}
