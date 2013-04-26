package fr.urssaf.image.sae.rnd.modele;

/**
 * Type possible des types de document
 * 
 *
 */
public enum TypeCode {
   ARCHIVABLE_AED("Type de document archivable AED"), 
   NON_ARCHIVABLE_AED("Type de document non archivable AED"),
   TEMPORAIRE("Type de document temporaire");

   private String description;

   TypeCode(String description) {
      this.description = description;
   }

   /**
    * 
    * @return le nom
    */
   public String getValue() {
      return name();
   }

   /**
    * 
    * @param value
    *           La valeur
    */
   public void setValue(String value) {
   }

   /**
    * 
    * @return le libellé
    */
   public String getDescription() {
      return description;
   }

   /**
    * 
    * @param description
    *           Le libellé
    */
   public void setDescription(String description) {
      this.description = description;
   }
}
