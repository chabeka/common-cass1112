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
 * Classe de modèle d'une trace du journal des événements
 * 
 */
public class TraceJournalEvt {

   /**
    * identifiant de la trace
    */
   private final UUID identifiant;

   /**
    * Date de création de la trace
    */
   private final Date timestamp;

   /**
    * Contexte de l'événement
    */
   private String contexte;

   /**
    * Code de l'événement
    */
   private String codeEvt;

   /**
    * Code du contrat de service
    */
   private String contratService;

   /**
    * Login de l'utilisateur
    */
   private String login;

   /**
    * Informations supplémentaires de la trace
    */
   private Map<String, Object> infos;

   /**
    * Liste des PAGMs
    */
   private List<String> pagms = new ArrayList<String>();

   /**
    * Constructeur
    * 
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
   public TraceJournalEvt(UUID idTrace, Date timestamp) {
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
   public TraceJournalEvt(TraceToCreate trace, List<String> listInfos,
         UUID idTrace, Date timestamp) {
      this.contexte = trace.getContexte();
      this.codeEvt = trace.getCodeEvt();
      this.contratService = trace.getContrat();
      if (CollectionUtils.isNotEmpty(trace.getPagms())) {
         this.pagms.addAll(trace.getPagms());
      }
      this.login = trace.getLogin();
      this.timestamp = timestamp;
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
    * @return the identifiant
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }

   /**
    * @return the timestamp
    */
   public final Date getTimestamp() {
      return timestamp;
   }

   /**
    * @return the contexte
    */
   public final String getContexte() {
      return contexte;
   }

   /**
    * @param contexte
    *           the contexte to set
    */
   public final void setContexte(String contexte) {
      this.contexte = contexte;
   }

   /**
    * @return the codeEvt
    */
   public final String getCodeEvt() {
      return codeEvt;
   }

   /**
    * @param codeEvt
    *           the codeEvt to set
    */
   public final void setCodeEvt(String codeEvt) {
      this.codeEvt = codeEvt;
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

   /**
    * @return les Pagms
    */
   public final List<String> getPagms() {
      return pagms;
   }

   /**
    * @param pagms
    *           les Pagms
    */
   public final void setPagms(List<String> pagms) {
      this.pagms = pagms;
   }

   /**
    * @return le login de l'utilisateur
    */
   public final String getLogin() {
      return login;
   }

   /**
    * @param login
    *           le login de l'utilisateur
    */
   public final void setLogin(String login) {
      this.login = login;
   }
}
