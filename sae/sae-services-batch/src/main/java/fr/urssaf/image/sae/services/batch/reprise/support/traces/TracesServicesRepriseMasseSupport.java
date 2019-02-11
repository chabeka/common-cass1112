package fr.urssaf.image.sae.services.batch.reprise.support.traces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.HostnameUtil;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Classe de support pour écrire les traces via la brique de traçabilité
 */
@Component
public class TracesServicesRepriseMasseSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TracesServicesRepriseMasseSupport.class);

   /**
    * Services du dispatcheur de la trace
    */
   @Autowired
   private DispatcheurService dispatcheurService;

   /**
    * Ecrit une trace d'échec de reprise de masse, le cas échéant
    * 
    * @param jobExecution
    *           le jobExecution de SpringBatch
    */
   @SuppressWarnings("PMD.AvoidCatchingThrowable")
   public final void traceEchecRepriseMasse(JobExecution jobExecution) {

      // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
      // planter cette méthode
      try {

         // Traces
         String prefix = "traceEchecReprise  Masse()";
         LOGGER.debug("{} - Début", prefix);

         if (ExitStatus.COMPLETED.equals(jobExecution.getExitStatus())) {

            LOGGER.debug(
                  "{} - On ne va pas tracer d'événement d'échec la reprise de masse",
                  prefix);

         } else {

            // Traces
            LOGGER.debug(
                  "{} - On va tracer l'événement d'échec de la reprise de masse",
                  prefix);

            // Récupère le status du job
            BatchStatus batchStatus = jobExecution.getStatus();
            LOGGER.debug("{} - BatchStatus : {}", prefix, batchStatus);

            // Instantiation de l'objet TraceToCreate
            TraceToCreate traceToCreate = new TraceToCreate();

            // Code de l'événement
            traceToCreate.setCodeEvt(Constantes.TRACE_CODE_EVT_ECHEC_REPRISE);

            // Contexte
            traceToCreate.setContexte(Constantes.CONTEXTE_REPRISE_MASSE);

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

      } catch (Exception ex) {
         LOGGER.error(
               "Une erreur s'est produite lors de l'écriture de la trace d'erreur de reprise de masse",
               ex);
      }

   }

   /**
    * Ajout des paramètres du job dans la map infos
    * 
    * @param jobExecution
    *           Job execution object
    * @param infos
    *           Map infos
    */
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

   /**
    * Construction de la stack trace
    * 
    * @param jobExecution
    *           Job execution object
    * @return Stack trace
    */
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
         concatBatchErrorList(exceptionsSpBatch, sBuilder);
      } else {
         LOGGER.debug("{} - Aucune exception Spring Batch", prefix);
      }
      
      // Les exceptions survenues lors du rollback
      LOGGER.debug("{} - Traitement des exceptions du rollback", prefix);
      ConcurrentLinkedQueue<String> exceptionsRoll = getRollbackExceptions(jobExecution);
      nbEx = sizeCollection(exceptionsRoll);
      sBuilder.append(String.format("Exception(s) du rollback : %s\r\n", nbEx));
      if (nbEx > 0) {
         LOGGER.debug("{} - {} exception(s) du rollback", prefix, nbEx);
         concatErrorMessageList(exceptionsRoll, sBuilder);
      } else {
         LOGGER.debug("{} - Aucune exception du rollback", prefix);
      }

      // Traces
      LOGGER.debug("{} - Fin", prefix);

      // Termine
      return sBuilder.toString();

   }

   /**
    * Concaténation des exceptions dans la trace
    * @param errorMessageList
    * @param sBuilder
    */
   private void concatErrorMessageList(Iterable<String> errorMessageList,
         StringBuilder sBuilder) {

      String exceptionMessage;

      for (String errorMessage : errorMessageList) {
         exceptionMessage = errorMessage;
         sBuilder.append(exceptionMessage);
         sBuilder.append("\r\n");
      }

   }

   /**
    * Concaténation de exceptions technique du batch dans la trace
    * @param exceptions
    * @param sBuilder
    */
   private void concatBatchErrorList(Iterable<Throwable> exceptions,
         StringBuilder sBuilder) {

      String stackTrace;
      for (Throwable throwable : exceptions) {
         stackTrace = ExceptionUtils.getFullStackTrace(throwable);
         sBuilder.append(stackTrace);
         sBuilder.append("\r\n");
      }
   }
      
   private ConcurrentLinkedQueue<String> getRollbackExceptions(
         JobExecution jobExecution) {
      return getErrorMessageListByType(jobExecution, Constantes.ROLLBACK_EXCEPTION);
   }
   
   @SuppressWarnings("unchecked")
   private ConcurrentLinkedQueue<String> getErrorMessageListByType(
         JobExecution jobExecution, String cle) {
      ConcurrentLinkedQueue<String> listExceptions = null;
      if ((jobExecution.getExecutionContext() != null)
            && (jobExecution.getExecutionContext().get(cle) != null)) {
         listExceptions = (ConcurrentLinkedQueue<String>) jobExecution
               .getExecutionContext().get(cle);
      }
      return listExceptions;
   }

   /**
    * Ajout des informations d'authentification dans le trace.
    * 
    * * @pa traceToCreate Objet trace..e.
    */
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

   /**
    * Taille d'une collection.
    * 
    * @pa liste liste d'objet
    * 
    * @return La taille d'une collect
    */
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
