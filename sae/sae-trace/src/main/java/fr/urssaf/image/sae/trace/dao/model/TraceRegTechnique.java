/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import fr.urssaf.image.sae.trace.commons.Constantes;
import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de modèle d'une trace du registre de surveillance technique
 * 
 */
public class TraceRegTechnique {

   /** Identifiant de la trace */
   private UUID identifiant;

   /** Date de création de la trace */
   private Date timestamp;

   /** Contexte de la trace */
   private String contexte;

   /** Trace technique de l'exception */
   private String stacktrace;

   /** code de l'événement */
   private String codeEvt;

   /** login de l'utilisateur */
   private String login;

   /** code du contrat de service */
   private String contrat;

   /** Le ou les PAGM */
   private List<String> pagms = new ArrayList<String>();

   /** informations supplémentaires de la trace */
   private Map<String, Object> infos;

   /**
    * Constructeur
    * 
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
   public TraceRegTechnique(UUID idTrace, Date timestamp) {
      this.identifiant = idTrace;
      this.timestamp = timestamp;
   }

   /**
    * Constructeur
    * 
    * @param trace
    *           trace d'origine
    * @param listInfos
    *           liste des informations supplémentaires à récupérer
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
   public TraceRegTechnique(TraceToCreate trace, List<String> listInfos,
         UUID idTrace, Date timestamp) {
      this.contexte = trace.getContexte();
      this.codeEvt = trace.getCodeEvt();
      this.contrat = trace.getContrat();
      if (CollectionUtils.isNotEmpty(trace.getPagms())) {
         this.pagms.addAll(trace.getPagms());
      }
      this.login = trace.getLogin();
      this.timestamp = timestamp;
      this.stacktrace = trace.getStracktrace();
      this.identifiant = idTrace;

      if (CollectionUtils.isNotEmpty(listInfos)
            && MapUtils.isNotEmpty(trace.getInfos())) {
         this.infos = new HashMap<String, Object>();
         for (String info : listInfos) {
            if (trace.getInfos().get(info) != null) {
               this.infos.put(info, trace.getInfos().get(info));
            }
         }

         // on récupère toutes les infos
         if (listInfos.size() == 1
               && Constantes.REG_ALL_INFOS.equals(listInfos.get(0))) {
            infos.putAll(trace.getInfos());
         }
      }
   }

   /**
    * @return l'Identifiant de la trace
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }

   /**
    * @return la Date de création de la trace
    */
   public final Date getTimestamp() {
      return timestamp;
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
    * @return la Trace technique de l'exception
    */
   public final String getStacktrace() {
      return stacktrace;
   }

   /**
    * @param stacktrace
    *           Trace technique de l'exception
    */
   public final void setStacktrace(String stacktrace) {
      this.stacktrace = stacktrace;
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
    * @return les informations supplémentaires de la trace
    */
   public final Map<String, Object> getInfos() {
      return infos;
   }

   /**
    * @param infos
    *           tinformations supplémentaires de la trace
    */
   public final void setInfos(Map<String, Object> infos) {
      this.infos = infos;
   }

}
