/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de modèle d'une trace du registre de sécurité
 * 
 */
public class TraceRegSecurite {

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

   /** informations supplémentaires de la trace */
   private Map<String, Object> infos;

   /**
    * constructeur
    */
   public TraceRegSecurite() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param trace
    *           trace d'origine
    * @param listInfos
    *           la liste des informations supplémentaires à récupérer
    */
   public TraceRegSecurite(TraceToCreate trace, List<String> listInfos) {
      this.contexte = trace.getContexte();
      this.codeEvt = trace.getCodeEvt();
      this.contrat = trace.getContrat();
      this.login = trace.getLogin();
      this.timestamp = trace.getTimestamp();
      this.identifiant = TimeUUIDUtils.getTimeUUID(trace.getTimestamp()
            .getTime());

      if (CollectionUtils.isNotEmpty(listInfos)
            && MapUtils.isNotEmpty(trace.getInfos())) {
         this.infos = new HashMap<String, Object>();
         for (String info : listInfos) {
            if (trace.getInfos().get(info) != null) {
               this.infos.put(info, trace.getInfos().get(info));
            }
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
