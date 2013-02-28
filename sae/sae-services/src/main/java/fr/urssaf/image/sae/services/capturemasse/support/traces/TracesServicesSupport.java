package fr.urssaf.image.sae.services.capturemasse.support.traces;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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
import fr.urssaf.image.sae.services.capturemasse.utils.StatutCaptureUtils;
import fr.urssaf.image.sae.services.util.HostnameUtil;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité.
 */
@Component
public class TracesServicesSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TracesServicesSupport.class);

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

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceEchecCaptureMasse()";
         LOGGER.debug("{} - Début", prefix);

         // Détermine si on considère la capture de masse en erreur ou non
         if (StatutCaptureUtils.isCaptureOk(jobExecution)) {

            LOGGER
                  .debug(
                        "{} - On ne va pas tracer d'événement d'échec la capture de masse",
                        prefix);

         } else {

            // Traces
            LOGGER
                  .debug(
                        "{} - On va tracer l'événement d'échec de la capture de masse",
                        prefix);

            // Récupère le status du job
            BatchStatus batchStatus = jobExecution.getStatus();
            LOGGER.debug("{} - BatchStatus : {}", prefix, batchStatus);

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
            String exceptionsAsString = buildStackTrace(jobExecution);
            traceToCreate.setStracktrace(exceptionsAsString);
            LOGGER.debug("{} - StackTrace : {}", prefix, exceptionsAsString);

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

         }

         // Traces
         LOGGER.debug("{} - Fin", prefix);

      } catch (Throwable ex) {
         LOGGER
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace d'erreur de capture de masse",
                     ex);
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

   private String buildStackTrace(JobExecution jobExecution) {

      // Traces
      String prefix = "buildStackTrace()";
      LOGGER.debug("{} - Début", prefix);

      // Initialise le SpringBuilder
      StringBuilder sBuilder = new StringBuilder();

      // Les exceptions SpringBatch
      LOGGER.debug("{} - Traitement des exceptions Spring Batch", prefix);
      List<Throwable> exceptionsSpBatch = jobExecution
            .getAllFailureExceptions();
      int nbEx = sizeCollection(exceptionsSpBatch);
      sBuilder
            .append(String.format("Exception(s) Spring Batch : %s\r\n", nbEx));
      if (nbEx > 0) {
         LOGGER.debug("{} - {} exception(s) Spring Batch", prefix, nbEx);
         addExceptions1(exceptionsSpBatch, sBuilder);
      } else {
         LOGGER.debug("{} - Aucune exception Spring Batch", prefix);
      }

      // Les exceptions "Document"
      LOGGER.debug("{} - Traitement des exceptions sur les documents", prefix);
      ConcurrentLinkedQueue<Exception> exceptionsDocs = getDocumentExceptions(jobExecution);
      nbEx = sizeCollection(exceptionsDocs);
      sBuilder.append(String.format("Exception(s) sur les documents : %s\r\n",
            nbEx));
      if (nbEx > 0) {
         LOGGER.debug("{} - {} exception(s) sur les documents", prefix, nbEx);
         addExceptions2(exceptionsDocs, sBuilder);
      } else {
         LOGGER.debug("{} - Aucune exception sur les documents", prefix);
      }

      // Les exceptions survenues lors du rollback
      LOGGER.debug("{} - Traitement des exceptions du rollback", prefix);
      ConcurrentLinkedQueue<Exception> exceptionsRoll = getRollbackExceptions(jobExecution);
      nbEx = sizeCollection(exceptionsRoll);
      sBuilder.append(String.format("Exception(s) du rollback : %s\r\n", nbEx));
      if (nbEx > 0) {
         LOGGER.debug("{} - {} exception(s) du rollback", prefix, nbEx);
         addExceptions2(exceptionsRoll, sBuilder);
      } else {
         LOGGER.debug("{} - Aucune exception du rollback", prefix);
      }

      // Traces
      LOGGER.debug("{} - Fin", prefix);

      // Termine
      return sBuilder.toString();

   }

   private ConcurrentLinkedQueue<Exception> getDocumentExceptions(
         JobExecution jobExecution) {
      return getListeExceptions(jobExecution, Constantes.DOC_EXCEPTION);
   }

   private ConcurrentLinkedQueue<Exception> getRollbackExceptions(
         JobExecution jobExecution) {
      return getListeExceptions(jobExecution, Constantes.ROLLBACK_EXCEPTION);
   }

   @SuppressWarnings("unchecked")
   private ConcurrentLinkedQueue<Exception> getListeExceptions(
         JobExecution jobExecution, String cle) {
      ConcurrentLinkedQueue<Exception> listExceptions = null;
      if ((jobExecution.getExecutionContext() != null)
            && (jobExecution.getExecutionContext().get(cle) != null)) {
         listExceptions = (ConcurrentLinkedQueue<Exception>) jobExecution
               .getExecutionContext().get(cle);
      }
      return listExceptions;
   }

   private void addExceptions2(Iterable<Exception> exceptions,
         StringBuilder sBuilder) {

      String stackTrace;

      for (Exception exception : exceptions) {

         stackTrace = ExceptionUtils.getFullStackTrace(exception);

         sBuilder.append(stackTrace);

         sBuilder.append("\r\n");

      }

   }

   private void addExceptions1(Iterable<Throwable> exceptions,
         StringBuilder sBuilder) {

      String stackTrace;

      for (Throwable throwable : exceptions) {

         stackTrace = ExceptionUtils.getFullStackTrace(throwable);

         sBuilder.append(stackTrace);

         sBuilder.append("\r\n");

      }

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

   private int sizeCollection(Collection<?> liste) {
      int result;
      if (liste == null) {
         result = 0;
      } else {
         result = liste.size();
      }
      return result;
   }

}
