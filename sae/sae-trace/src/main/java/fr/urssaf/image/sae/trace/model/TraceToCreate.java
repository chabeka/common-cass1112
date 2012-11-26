/**
 * 
 */
package fr.urssaf.image.sae.trace.model;

import java.util.Date;
import java.util.Map;

/**
 * Formulaire d'une trace à créer. Ce modèle sert essentiellement au dispatcheur
 * de traces.
 * 
 */
public class TraceToCreate {

   /** Date de création de la trace */
   private Date timestamp;

   /** Contexte de la trace */
   private String contexte;

   /** Code de l'événement */
   private String codeEvt;

   /** Action à entreprendre */
   private String action;

   /** Trace technique de l'exception */
   private String stracktrace;

   /** Login de l'utilisateur */
   private String login;

   /** Code du contrat de service */
   private String cs;

   /** Informations supplémentaires sur la trace */
   private Map<String, Object> infos;

   /**
    * @return la Date de création de la trace
    */
   public final Date getTimestamp() {
      return timestamp;
   }

   /**
    * @param timestamp
    *           Date de création de la trace
    */
   public final void setTimestamp(Date timestamp) {
      this.timestamp = timestamp;
   }

   /**
    * @return le Contexte de la trace
    */
   public final String getContexte() {
      return contexte;
   }

   /**
    * @param contexte
    *           Contexte de la trace
    */
   public final void setContexte(String contexte) {
      this.contexte = contexte;
   }

   /**
    * @return le Code de l'événement
    */
   public final String getCodeEvt() {
      return codeEvt;
   }

   /**
    * @param codeEvt
    *           Code de l'événement
    */
   public final void setCodeEvt(String codeEvt) {
      this.codeEvt = codeEvt;
   }

   /**
    * @return l'Action à entreprendre
    */
   public final String getAction() {
      return action;
   }

   /**
    * @param action
    *           Action à entreprendre
    */
   public final void setAction(String action) {
      this.action = action;
   }

   /**
    * @return la Trace technique de l'exception
    */
   public final String getStracktrace() {
      return stracktrace;
   }

   /**
    * @param stracktrace
    *           Trace technique de l'exception
    */
   public final void setStracktrace(String stracktrace) {
      this.stracktrace = stracktrace;
   }

   /**
    * @return le Login de l'utilisateur
    */
   public final String getLogin() {
      return login;
   }

   /**
    * @param login
    *           Login de l'utilisateur
    */
   public final void setLogin(String login) {
      this.login = login;
   }

   /**
    * @return le Code du contrat de service
    */
   public final String getCs() {
      return cs;
   }

   /**
    * @param cs
    *           Code du contrat de service
    */
   public final void setCs(String cs) {
      this.cs = cs;
   }

   /**
    * @return les Informations supplémentaires sur la trace
    */
   public final Map<String, Object> getInfos() {
      return infos;
   }

   /**
    * @param infos
    *           Informations supplémentaires sur la trace
    */
   public final void setInfos(Map<String, Object> infos) {
      this.infos = infos;
   }

}
