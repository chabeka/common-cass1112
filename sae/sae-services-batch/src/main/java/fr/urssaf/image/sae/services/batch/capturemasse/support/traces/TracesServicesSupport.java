package fr.urssaf.image.sae.services.batch.capturemasse.support.traces;

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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.HostnameUtil;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.StatutCaptureUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;
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
  public final void traceEchecCaptureMasse(final JobExecution jobExecution) {

    // On fait un try/catch(Throwable) pour la traçabilité ne fasse pas
    // planter cette méthode
    try {

      // Traces
      final String prefix = "traceEchecCaptureMasse()";
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
        final BatchStatus batchStatus = jobExecution.getStatus();
        LOGGER.debug("{} - BatchStatus : {}", prefix, batchStatus);

        // Instantiation de l'objet TraceToCreate
        final TraceToCreate traceToCreate = new TraceToCreate();

        // Code de l'événement
        traceToCreate.setCodeEvt(Constantes.TRACE_CODE_EVT_ECHEC_CM);

        // Contexte
        traceToCreate.setContexte(Constantes.CONTEXTE_CAPTURE_MASSE);

        // Le détail des exceptions survenues
        final String exceptionsAsString = buildStackTrace(jobExecution);
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

    } catch (final Exception ex) {
      LOGGER
      .error(
             "Une erreur s'est produite lors de l'écriture de la trace d'erreur de capture de masse",
             ex);
    }

  }

  private void addJobParametersDansInfos(final JobExecution jobExecution,
                                         final Map<String, Object> infos) {

    // On ajoute les paramètres du job sous la forme :
    // - clé de la map = jobParams.nom
    // - valeur associée = jobParams.valeur

    if (jobExecution.getJobInstance() != null
        && jobExecution.getJobInstance().getJobParameters() != null) {

      final Map<String, JobParameter> jobParameters = jobExecution
          .getJobInstance().getJobParameters().getParameters();

      if (MapUtils.isNotEmpty(jobParameters)) {

        String nomParam;
        Object valeurParam;

        for (final Map.Entry<String, JobParameter> entry : jobParameters
            .entrySet()) {

          nomParam = entry.getKey();
          valeurParam = entry.getValue().getValue();

          infos.put(String.format("jobParams.%s", nomParam), valeurParam);

        }

      }

    }

  }

  private String buildStackTrace(final JobExecution jobExecution) {

    // Traces
    final String prefix = "buildStackTrace()";
    LOGGER.debug("{} - Début", prefix);

    // Initialise le SpringBuilder
    final StringBuilder sBuilder = new StringBuilder();

    // Les exceptions SpringBatch
    LOGGER.debug("{} - Traitement des exceptions Spring Batch", prefix);
    final List<Throwable> exceptionsSpBatch = jobExecution
        .getAllFailureExceptions();
    int nbEx = sizeCollection(exceptionsSpBatch);
    sBuilder
            .append(String.format("Exception(s) Spring Batch : %s%n", nbEx));
    if (nbEx > 0) {
      LOGGER.debug("{} - {} exception(s) Spring Batch", prefix, nbEx);
      concatBatchErrorList(exceptionsSpBatch, sBuilder);
    } else {
      LOGGER.debug("{} - Aucune exception Spring Batch", prefix);
    }

    // Les exceptions "Document"
    LOGGER.debug("{} - Traitement des exceptions sur les documents", prefix);
    final ConcurrentLinkedQueue<String> messageExceptionsList = getErrorMessageList(jobExecution);
    nbEx = sizeCollection(messageExceptionsList);
    sBuilder.append(String.format("Exception(s) sur les documents : %s%n",
                                  nbEx));
    if (nbEx > 0) {
      LOGGER.debug("{} - {} exception(s) sur les documents", prefix, nbEx);
      concatErrorMessageList(messageExceptionsList, sBuilder);
    } else {
      LOGGER.debug("{} - Aucune exception sur les documents", prefix);
    }

    // Les exceptions survenues lors du rollback
    LOGGER.debug("{} - Traitement des exceptions du rollback", prefix);
    final ConcurrentLinkedQueue<String> messageRollExceptionList = getRollbackErrorMessageList(jobExecution);
    nbEx = sizeCollection(messageRollExceptionList);
    sBuilder.append(String.format("Exception(s) du rollback : %s%n", nbEx));
    if (nbEx > 0) {
      LOGGER.debug("{} - {} exception(s) du rollback", prefix, nbEx);
      concatErrorMessageList(messageRollExceptionList, sBuilder);
    } else {
      LOGGER.debug("{} - Aucune exception du rollback", prefix);
    }

    // Traces
    LOGGER.debug("{} - Fin", prefix);

    // Termine
    return sBuilder.toString();

  }

  private ConcurrentLinkedQueue<String> getErrorMessageList(
                                                            final JobExecution jobExecution) {
    return getErrorMessageListByType(jobExecution, Constantes.DOC_EXCEPTION);
  }

  private ConcurrentLinkedQueue<String> getRollbackErrorMessageList(
                                                                    final JobExecution jobExecution) {
    return getErrorMessageListByType(jobExecution, Constantes.ROLLBACK_EXCEPTION);
  }

  @SuppressWarnings("unchecked")
  private ConcurrentLinkedQueue<String> getErrorMessageListByType(
                                                                  final JobExecution jobExecution, final String cle) {
    ConcurrentLinkedQueue<String> listExceptions = null;
    if (jobExecution.getExecutionContext() != null
        && jobExecution.getExecutionContext().get(cle) != null) {
      listExceptions = (ConcurrentLinkedQueue<String>) jobExecution
          .getExecutionContext().get(cle);
    }
    return listExceptions;
  }

  private void concatErrorMessageList(final Iterable<String> errorMessageList,
                                      final StringBuilder sBuilder) {

    String exceptionMessage;

    for (final String errorMessage : errorMessageList) {
      exceptionMessage = errorMessage;
      sBuilder.append(exceptionMessage);
      sBuilder.append("\r\n");
    }

  }

  private void concatBatchErrorList(final Iterable<Throwable> exceptions,
                                    final StringBuilder sBuilder) {

    String stackTrace;
    for (final Throwable throwable : exceptions) {
      stackTrace = ExceptionUtils.getFullStackTrace(throwable);
      sBuilder.append(stackTrace);
      sBuilder.append("\r\n");
    }
  }

  private void setInfosAuth(final TraceToCreate traceToCreate) {

    if (SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal() != null) {

      final VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
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

  private int sizeCollection(final Collection<?> liste) {
    int result;
    if (liste == null) {
      result = 0;
    } else {
      result = liste.size();
    }
    return result;
  }

}
