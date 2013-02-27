package fr.urssaf.image.sae.services.capturemasse.support.traces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.util.HostnameUtil;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité.
 */
@Component
public class TracesSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TracesSupport.class);

   @Autowired
   private DispatcheurService dispatcheurService;

   /**
    * Ecrit une trace d'échec de capture de masse, le cas échéant
    * 
    * @param jobExecution
    *           le jobExecution de SpringBatch
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceEchecCaptureMasse(JobExecution jobExecution) {

      // Récupère la liste des exceptions survenues pendant le job
      List<Throwable> exceptions = jobExecution.getAllFailureExceptions();

      // Récupère le status du job
      BatchStatus batchStatus = jobExecution.getStatus();

      // Si le job est en erreur, et/ou que la liste des exceptions n'est pas
      // vide, on écrit une trace d'erreur de capture de masse
      if ((!BatchStatus.COMPLETED.equals(batchStatus))
            || (CollectionUtils.isNotEmpty(exceptions))) {

         // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
         // planter cette méthode
         try {

            // Instantiation de l'objet TraceToCreate
            TraceToCreate traceToCreate = new TraceToCreate();

            // Code de l'événement
            traceToCreate.setCodeEvt(Constantes.TRACE_CODE_EVT_ECHEC_CM);

            // Timestamp
            Date timestamp = new Date();
            traceToCreate.setTimestamp(timestamp);

            // Contexte
            traceToCreate.setContexte("captureMasse");

            // Le détail des exceptions survenues
            if (CollectionUtils.isNotEmpty(exceptions)) {
               String exceptionsAsString = buildStackTrace(exceptions);
               traceToCreate.setStracktrace(exceptionsAsString);
            }

            // Contrat de service et login
            setInfosAuth(traceToCreate);

            // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
            // ce code
            traceToCreate.getInfos().put("saeServeurHostname",
                  HostnameUtil.getHostname());
            traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

            // Info supplémentaire : Le status du job
            traceToCreate.getInfos().put("batchStatus", batchStatus.toString());

            // Info supplémentaire : Les paramètres du job
            addJobParametersDansInfos(jobExecution, traceToCreate.getInfos());

            // Appel du dispatcheur
            dispatcheurService.ajouterTrace(traceToCreate);

         } catch (Throwable ex) {
            LOGGER
                  .error(
                        "Une erreur s'est produite lors de l'écriture de la trace d'erreur de capture de masse",
                        ex);
         }

      }

   }

   private void addJobParametersDansInfos(JobExecution jobExecution,
         Map<String, Object> infos) {

      // On ajoute les paramètres du job sous la forme :
      // - clé de la map = jobParams.nom
      // - valeur associée = jobParams.valeur

      if ((jobExecution.getJobInstance() != null)
            && (jobExecution.getJobInstance().getJobParameters() != null)) {

         Map<String, JobParameter> jobParameters = jobExecution
               .getJobInstance().getJobParameters().getParameters();

         if (MapUtils.isNotEmpty(jobParameters)) {

            String nomParam;
            Object valeurParam;

            for (Map.Entry<String, JobParameter> entry : jobParameters
                  .entrySet()) {

               nomParam = entry.getKey();
               valeurParam = entry.getValue().getValue();

               infos.put(String.format("jobParams.%s", nomParam), valeurParam);

            }

         }

      }

   }

   private String buildStackTrace(List<Throwable> exceptions) {

      String result = StringUtils.EMPTY;

      if (CollectionUtils.isNotEmpty(exceptions)) {

         StringBuilder sBuilder = new StringBuilder();

         String stackTrace;
         
         Throwable throwable;
         for (int i=0;i<exceptions.size();i++) {
            throwable = exceptions.get(i);
            stackTrace = ExceptionUtils.getFullStackTrace(throwable);
            sBuilder.append(stackTrace);
            if (i<(exceptions.size()-1)) {
               sBuilder.append("\r\n");
            }
         }
         
         result = sBuilder.toString();

      }

      return result;

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
