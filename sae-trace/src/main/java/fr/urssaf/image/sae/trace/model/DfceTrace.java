package fr.urssaf.image.sae.trace.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Trace générique créée par DFCE
 * 
 * 
 */
public class DfceTrace {
   /**
    * Attributs de la trace
    */
   private Map<String, String> attributs;

   /**
    * Identifiant unique du document concerné par la trace
    */
   private UUID docUuid;

   /**
    * Date de création de la trace
    */
   private Date dateEvt;

   /**
    * Type de la trace
    */
   private String typeEvt;

   /**
    * Login de l'utilisateur
    */
   private String login;

   /**
    * @return the attributs
    */
   public final Map<String, String> getAttributs() {
      return attributs;
   }

   /**
    * @param attributs
    *           the attributs to set
    */
   public final void setAttributs(Map<String, String> attributs) {
      this.attributs = attributs;
   }

   /**
    * @return the docUuid
    */
   public final UUID getDocUuid() {
      return docUuid;
   }

   /**
    * @param docUuid
    *           the docUuid to set
    */
   public final void setDocUuid(UUID docUuid) {
      this.docUuid = docUuid;
   }

   /**
    * @return the dateEvt
    */
   public final Date getDateEvt() {
      return getDateCopy(dateEvt);
   }

   /**
    * @param dateEvt
    *           the dateEvt to set
    */
   public final void setDateEvt(Date dateEvt) {
      this.dateEvt = getDateCopy(dateEvt);
   }

   /**
    * @return the typeEvt
    */
   public final String getTypeEvt() {
      return typeEvt;
   }

   /**
    * @param typeEvt
    *           the typeEvt to set
    */
   public final void setTypeEvt(String typeEvt) {
      this.typeEvt = typeEvt;
   }

   /**
    * @return the login
    */
   public final String getLogin() {
      return login;
   }

   /**
    * @param login
    *           the login to set
    */
   public final void setLogin(String login) {
      this.login = login;
   }

   private Date getDateCopy(Date date) {
      Date tDate = null;
      if (date != null) {
         tDate = new Date(date.getTime());
      }
      return tDate;
   }
}
