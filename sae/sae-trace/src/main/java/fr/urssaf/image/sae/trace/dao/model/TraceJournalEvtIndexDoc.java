/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Map;

/**
 * Classe de modèle de la CF TraceJournalEvtIndexDoc
 * 
 */
public class TraceJournalEvtIndexDoc extends TraceIndex {

   /**
    * Contexte de l'évenement
    */
   private String contexte;

   /**
    * Code du contrat de service
    */
   private String contratService;

   /**
    * Informations supplémentaires de la trace
    */
   private Map<String, Object> infos;

   /**
    * Constructeur par défaut
    */
   public TraceJournalEvtIndexDoc() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param traceJournal
    *           trace du journal des événements
    */
   public TraceJournalEvtIndexDoc(TraceJournalEvt traceJournal) {
      super(traceJournal);
      this.contexte = traceJournal.getContexte();
      this.contratService = traceJournal.getContratService();
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

   /**
    * @return the contratService
    */
   public final String getContratService() {
      return contratService;
   }

   /**
    * @param contratService
    *           the contratService to set
    */
   public final void setContratService(String contratService) {
      this.contratService = contratService;
   }

   /**
    * @return the infos
    */
   public final Map<String, Object> getInfos() {
      return infos;
   }

   /**
    * @param infos
    *           the infos to set
    */
   public final void setInfos(Map<String, Object> infos) {
      this.infos = infos;
   }
}
