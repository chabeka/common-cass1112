/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.UUID;

/**
 * Classe de modèle de la CF "TraceRegSecuriteIndex"
 * 
 */
public class TraceRegSecuriteIndex {

   /** Identifiant de la trace */
   private UUID identifiant;

   /** Date de création de la trace */
   private Date timestamp;

   /** Contexte de la trace */
   private String contexte;

   /** code de l'événement */
   private String codeEvt;

   /** login de l'utilisateur */
   private String login;

   /** code du contrat de service */
   private String contrat;

   /**
    * @return l'Identifiant de la trace
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }

   /**
    * @param identifiant
    *           Identifiant de la trace
    */
   public final void setIdentifiant(UUID identifiant) {
      this.identifiant = identifiant;
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
   public final String getContrat() {
      return contrat;
   }

   /**
    * @param contrat
    *           code du contrat de service
    */
   public final void setContrat(String contrat) {
      this.contrat = contrat;
   }

}
