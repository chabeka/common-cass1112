/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Classe de modèle d'une trace du registre de sécurité
 * 
 */
public class TraceRegSecurite {

   /** Identifiant de la trace */
   private UUID id;

   /** Date de création de la trace */
   private Date timestamp;

   /** Contexte de la trace */
   private String contexte;

   /** code de l'événement */
   private String codeEvt;

   /** login de l'utilisateur */
   private String login;

   /** code du contrat de service */
   private String cs;

   /** informations supplémentaires de la trace */
   private Map<String, String> infos;

   /**
    * @return l'Identifiant de la trace
    */
   public final UUID getId() {
      return id;
   }

   /**
    * @param id
    *           Identifiant de la trace
    */
   public final void setId(UUID id) {
      this.id = id;
   }

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
    * @return le code de l'événement
    */
   public final String getCodeEvt() {
      return codeEvt;
   }

   /**
    * @param codeEvt
    *           code de l'événement
    */
   public final void setCodeEvt(String codeEvt) {
      this.codeEvt = codeEvt;
   }

   /**
    * @return le login de l'utilisateur
    */
   public final String getLogin() {
      return login;
   }

   /**
    * @param login
    *           login de l'utilisateur
    */
   public final void setLogin(String login) {
      this.login = login;
   }

   /**
    * @return le code du contrat de service
    */
   public final String getCs() {
      return cs;
   }

   /**
    * @param cs
    *           code du contrat de service
    */
   public final void setCs(String cs) {
      this.cs = cs;
   }

   /**
    * @return les informations supplémentaires de la trace
    */
   public final Map<String, String> getInfos() {
      return infos;
   }

   /**
    * @param infos
    *           tinformations supplémentaires de la trace
    */
   public final void setInfos(Map<String, String> infos) {
      this.infos = infos;
   }

}
