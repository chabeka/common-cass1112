/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle de la CF TraceJournalEvtindex
 */
@Table(name = "TraceJournalEvtIndex")
public class TraceJournalEvtIndex extends TraceIndex {

   /**
    * Contexte de l'évenement
    */
   private String contexte;

   /**
    * Code du contrat de service
    */
   private String contratService;

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
  public TraceJournalEvtIndex(final TraceJournalEvt exploitation) {
      super(exploitation);
      this.contexte = exploitation.getContexte();
      this.contratService = exploitation.getContratService();
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
  public final void setContexte(final String contexte) {
      this.contexte = contexte;
   }

   /**
    * @return the contratService
    */
   public String getContratService() {
      return contratService;
   }

   /**
   * @param contratService
   *          the contratService to set
    */
  public void setContratService(final String contratService) {
      this.contratService = contratService;
   }

}
