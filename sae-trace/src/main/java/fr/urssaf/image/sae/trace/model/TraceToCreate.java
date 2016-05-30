/**
 * 
 */
package fr.urssaf.image.sae.trace.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Formulaire d'une trace à créer. Ce modèle sert essentiellement au dispatcheur
 * de traces.
 * 
 */
public class TraceToCreate {

   private static final int BUFFER_MIN_SIZE = 26;

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
   private String contrat;

   /** Le ou les PAGM */
   private List<String> pagms = new ArrayList<String>();

   /** Informations supplémentaires sur la trace */
   private Map<String, Object> infos = new HashMap<String, Object>();

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
   public final String getContrat() {
      return contrat;
   }

   /**
    * @param contrat
    *           Code du contrat de service
    */
   public final void setContrat(String contrat) {
      this.contrat = contrat;
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

   /**
    * Le ou les PAGM
    * 
    * @return Le ou les PAGM
    */
   public final List<String> getPagms() {
      return pagms;
   }

   /**
    * Le ou les PAGM
    * 
    * @param pagms
    *           Le ou les PAGM
    */
   public final void setPagms(List<String> pagms) {
      this.pagms = pagms;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {

      StringBuffer buffer = new StringBuffer(BUFFER_MIN_SIZE);
      buffer.append("code evenement:");
      buffer.append(this.codeEvt);

      if (StringUtils.isNotBlank(this.action)) {
         buffer.append(";action:");
         buffer.append(this.action);
      }

      if (StringUtils.isNotBlank(this.contexte)) {
         buffer.append(";contexte:");
         buffer.append(this.contexte);
      }

      if (StringUtils.isNotBlank(this.contrat)) {
         buffer.append(";contrat:");
         buffer.append(this.contrat);
      }

      if (CollectionUtils.isNotEmpty(this.pagms)) {
         buffer.append(";pagms:");
         buffer.append(this.pagms);
      }

      if (StringUtils.isNotBlank(this.login)) {
         buffer.append(";login:");
         buffer.append(this.login);
      }

      if (StringUtils.isNotBlank(this.stracktrace)) {
         buffer.append(";stacktrace:");
         buffer.append(this.stracktrace);
      }

      if (MapUtils.isNotEmpty(this.infos)) {
         List<String> keys = new ArrayList<String>(this.infos.keySet());
         Collections.sort(keys);
         for (String key : keys) {
            buffer.append(';');
            buffer.append(key);
            buffer.append(':');
            buffer.append(this.infos.get(key));
         }
      }

      return buffer.toString();
   }

}
