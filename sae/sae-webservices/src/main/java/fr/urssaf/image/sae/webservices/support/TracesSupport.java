package fr.urssaf.image.sae.webservices.support;

import java.util.Date;

import org.apache.axis2.context.MessageContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité.
 */
@Component
public class TracesSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(TracesSupport.class);

   @Autowired
   private DispatcheurService dispatcheurService;

   /**
    * Ecrit les traces de l'échec d'une opération de service web
    * 
    * @param msgCtx
    *           le contexte Axis2
    * @param soapRequest
    *           la requête SOAP envoyée par le client
    * @param exception
    *           l'exception qui a amenée à l'échec de l'opération
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceEchecWs(MessageContext msgCtx, String soapRequest,
         Exception exception) {

      // On fait un try catch Throwable pour empêcher qu'un plantage de la
      // traçabilité fasse planter la suite des opérations
      try {

         // Récupère le nom de l'opération en cours
         String nomOperation = msgCtx.getAxisOperation().getName()
               .getLocalPart();

         // Le timestamp
         Date timestamp = new Date();

         // L'adresse IP du client
         String ipClient;
         if (msgCtx.getProperty(MessageContext.REMOTE_ADDR) == null) {
            ipClient = StringUtils.EMPTY;
         } else {
            ipClient = (String) msgCtx.getProperty(MessageContext.REMOTE_ADDR);
         }

         // Trace selon l'opération
         String codeEvt = getCodeEvtPourTrace(nomOperation);
         if (StringUtils.isNotEmpty(codeEvt)) {
            traceEchec(codeEvt, nomOperation, timestamp, soapRequest,
                  exception, ipClient);
         }

      } catch (Throwable ex) {
         LOG
               .error(
                     "Une erreur est survenue lors de la traçabilité de l'échec d'une opération WS",
                     ex);
      }

   }

   private String getCodeEvtPourTrace(String nomOperation) {

      String result = StringUtils.EMPTY;

      if ("PingSecure".equals(nomOperation)) {
         result = "WS_PING_SECURE|KO";
      } else if ("archivageUnitaire".equals(nomOperation)
            || "archivageUnitairePJ".equals(nomOperation)) {
         result = "WS_CAPTURE_UNITAIRE|KO";
      } else if ("archivageMasse".equals(nomOperation)
            || "archivageMasseAvecHash".equals(nomOperation)) {
         result = "WS_CAPTURE_MASSE|KO";
      } else if ("consultation".equals(nomOperation)
            || "consultationMTOM".equals(nomOperation)) {
         result = "WS_CONSULTATION|KO";
      } else if ("recherche".equals(nomOperation)) {
         result = "WS_RECHERCHE|KO";
      }

      return result;

   }

   protected final void traceEchec(String codeEvt, String nomOperation,
         Date timestamp, String soapRequest, Exception exception,
         String clientIP) {

      // Instantiation de l'objet TraceToCreate
      TraceToCreate traceToCreate = new TraceToCreate();

      // Code de l'événement
      traceToCreate.setCodeEvt(codeEvt);

      // Timestamp
      traceToCreate.setTimestamp(timestamp);

      // Contexte
      traceToCreate.setContexte(nomOperation);

      // Le détail de l'exception survenue
      if (exception != null) {
         traceToCreate.setStracktrace(ExceptionUtils
               .getFullStackTrace(exception));
      }

      // Contrat de service et login
      setInfosAuth(traceToCreate);

      // Info supplémentaire : Message SOAP de request
      traceToCreate.getInfos().put("soapRequest", soapRequest);

      // Info supplémentaire : Hostname et IP du serveur sur lequel tourne ce
      // code
      traceToCreate.getInfos().put("saeServeurHostname",
            HostnameUtil.getHostname());
      traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

      // Info supplémentaire : IP du client
      traceToCreate.getInfos().put("clientIP", clientIP);

      // Appel du dispatcheur
      dispatcheurService.ajouterTrace(traceToCreate);

   }

   private void setInfosAuth(TraceToCreate traceToCreate) {

      if ((SecurityContextHolder.getContext() != null)
            && (SecurityContextHolder.getContext().getAuthentication() != null)
            && (SecurityContextHolder.getContext().getAuthentication()
                  .getPrincipal() != null)) {

         VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
               .getContext().getAuthentication().getPrincipal();

         // Le code du Contrat de Service
         traceToCreate.setContrat(extrait.getCodeAppli());

         // Le ou les PAGM
         if (CollectionUtils.isNotEmpty(extrait.getPagms())) {
            traceToCreate.getPagms().addAll(extrait.getPagms());
         }

         // Le login utilisateur
         traceToCreate.setLogin(extrait.getIdUtilisateur());

      }

   }

}
