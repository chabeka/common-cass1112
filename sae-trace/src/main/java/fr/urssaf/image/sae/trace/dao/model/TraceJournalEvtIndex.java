/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;


/**
 * Classe de modèle de la CF TraceJournalEvtindex
 * 
 */
public class TraceJournalEvtIndex extends TraceIndex {

   /**
    * Contexte de l'évenement
    */
   private String contexte;

   /**
    * Constructeur par défaut
    */
   public TraceJournalEvtIndex() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param exploitation
    *           trace d'exploitation
    */
   public TraceJournalEvtIndex(TraceJournalEvt exploitation) {
      super(exploitation);
      this.contexte = exploitation.getContexte();
   }


   /**
    * @return le contexte de l'événement
    */
   public final String getContexte() {
      return contexte;
   }

   /**
    * @param contexte
    *           le contexte de l'événement
    */
   public final void setContexte(String contexte) {
      this.contexte = contexte;
   }

}
