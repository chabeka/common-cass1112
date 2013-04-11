package fr.urssaf.image.sae.trace.model;

/**
 * Différents types de journaux existants
 * 
 * 
 */
public enum JournalType {

   JOURNAL_EVENEMENT_DFCE("Journal des événements DFCE"), JOURNAL_EVENEMENT_SAE(
         "Journal des événements SAE"), JOURNAL_CYCLE_VIE(
         "Journal du cycle de vie des archives DFCE");

   private String description;

   JournalType(String description) {
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
    * @return le libellé
    */
   public String getDescription() {
      return description;
   }

   /**
    * 
    * @param description
    *           le libellé
    */
   public void setDescription(String description) {
      this.description = description;
   }

}
