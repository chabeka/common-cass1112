package fr.urssaf.image.sae.trace.model;

/**
 * Valeur du type de trace HIST_EVT : historique des évenements CYCLE_VIE :
 * cycle de vie des archives SECURITE : registre de sécurité EXPLOITATION :
 * registre d'exploitation TECHNIQUE : registre technique
 */
public enum TraceType {

   HIST_EVT("Historique des événements DFCE"), CYCLE_VIE(
         "Historique du cycle de vie des archives DFCE"), JOURNAL_EVT(
         "Journal des événements SAE"), SECURITE("Registre de sécurité"), EXPLOITATION(
         "Registre d'exploitation"), TECHNIQUE(
         "Registre de surveillance technique");

   private String description;

   TraceType(String description) {
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
