package fr.urssaf.image.sae.webservices.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSDeblocageService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;

/**
 * Classe d'implémentation de l'interface {@link WSDeblocageService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired.
 */
@Service
public class WSDeblocageServiceImpl implements WSDeblocageService {

  private static final Logger LOG = LoggerFactory
      .getLogger(WSTransfertMasseServiceImpl.class);

  /**
   * Service permettant de réaliser des objets sur les jobs
   */
  @Autowired
  private JobQueueService jobQueueService;

  /**
   * Service permettant de réaliser les opérations de lecture sur les jobs
   */
  @Autowired
  private JobLectureService jobLectureService;

  /*
   * Service pour ajouter la trace
   */
  @Autowired
  private DispatcheurService dispatcheurService;

  /**
   * {@inheritDoc}
   * 
   * @throws JobInexistantException
   */
  @Override
  public DeblocageResponse deblocage(final Deblocage request, final String callerIP)
      throws DeblocageAxisFault, JobInexistantException {
    final String prefixeTrc = "deblocage()";
    LOG.debug("{} - Début", prefixeTrc);

    // Récupérer les paramètres du job à débloquer
    final UuidType uuid = request.getDeblocage().getUuid();
    final UUID uuidJob = UUID.fromString(uuid.getUuidType());
    String etatJob = StringUtils.EMPTY;
    LOG.debug("{} - UUID du job: {}", prefixeTrc, uuid);

    try {
      // Récupérer le job
      final JobRequest jobRequest = jobLectureService
          .getJobRequestNotNull(uuidJob);

      // Si code traitement existant dans les paramètres du job
      String codeTraitement = StringUtils.EMPTY;
      if (jobRequest.getJobParameters() != null
          && !jobRequest.getJobParameters().isEmpty()) {
        codeTraitement = jobRequest.getJobParameters()
            .get(
                 Constantes.CODE_TRAITEMENT);
      }
      String stateRequest = "";
      if (jobRequest.getState() != null) {
        stateRequest = jobRequest.getState().toString();
      }

      if (JobState.FAILURE.name().equals(stateRequest)) {
        // Si déblocage modification de masse
        if (StringUtils.isNotBlank(codeTraitement)) {
          jobQueueService.deleteJobAndSemaphoreFromJobsQueues(uuidJob,
                                                              codeTraitement);
          // Récupérer la date effective de fin du traitement
          final Date endingDate = jobRequest.getEndingDate();
          // Passer le job à l'état ABORT
          jobQueueService.changerEtatJobRequest(uuidJob,
                                                JobState.ABORT.name(),
                                                endingDate,
                                                null);
          // Ajouter une trace DEBLOCAGE|OK en mettant l'id du traitement de masse dans les infos
          ajouterTraceDeblocage(uuidJob, endingDate, stateRequest);
        } else {
          LOG.warn("{} - échec de déblocage du job {} - ce job ne correspond pas à un traitement de modification de masse", prefixeTrc, uuid);
          throw new DeblocageAxisFault("ErreurInterneDeblocage",
              "Le job ne correspond pas à un traitement de modification de masse");
        }
      } else if (JobState.RESERVED.name().equals(stateRequest)
          || JobState.STARTING.name().equals(stateRequest)) {
        jobQueueService.deleteJobFromJobsQueues(uuidJob);
        // Passer le job à l'état FAILURE
        final Date dateFailure = new Date();
        jobQueueService.changerEtatJobRequest(uuidJob,
                                              JobState.FAILURE.name(),
                                              dateFailure,
                                              null);
        // Ajouter une trace DEBLOCAGE|OK en mettant l'id du traitement de masse dans les infos
        ajouterTraceDeblocage(uuidJob, dateFailure, stateRequest);
      } else {
        LOG.warn("{} - échec de déblocage du job {} - ce job ne peut pas être débloqué à cause de son état", prefixeTrc, uuid);
        throw new DeblocageAxisFault("ErreurInterneDeblocage",
            "Le job ne peut pas être débloqué à cause de son état");
      }
      // Récupérer l'état du job après déblocage
      final JobRequest job = jobLectureService.getJobRequest(uuidJob);
      etatJob = job.getState().toString();
    }
    catch (final JobInexistantException e) {
      LOG.warn("{} - échec de déblocage du job {} - ce job n'existe plus", prefixeTrc, uuid);
      throw new DeblocageAxisFault("ErreurInterneDeblocage",
                                   e.getMessage(),
                                   e);
    }
    catch (final Exception e) {
      throw new DeblocageAxisFault("ErreurInterneDeblocage",
                                   e.getMessage(),
                                   e);
    }

    return ObjectStorageResponseFactory.createDeblocageResponse(
                                                                uuid.getUuidType(),
                                                                etatJob);
  }

  /**
   * @param uuidJob
   * @param name
   * @param endingDate
   */
  private void ajouterTraceDeblocage(final UUID uuidJob, final Date endingDate, final String stateBefore) {
    // Instantiation de l'objet TraceToCreate
    final TraceToCreate traceToCreate = new TraceToCreate();

    // Code de l'événement
    traceToCreate.setCodeEvt("WS_DEBLOCAGE|OK");

    // Contexte
    traceToCreate.setContexte("Deblocage");

    // Info supplémentaire : Hostname et IP du serveur sur lequel tourne ce
    // code
    traceToCreate.getInfos()
    .put("uuidJob",
         uuidJob);
    traceToCreate.getInfos()
    .put("endingDate",
         endingDate);
    traceToCreate.getInfos().put("saeServeurHostname",
                                 HostnameUtil.getHostname());
    traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());
    traceToCreate.getInfos().put("stateBeforeUnlock", stateBefore);
    setInfosAuth(traceToCreate);
    // Appel du dispatcheur
    dispatcheurService.ajouterTrace(traceToCreate);

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
}
