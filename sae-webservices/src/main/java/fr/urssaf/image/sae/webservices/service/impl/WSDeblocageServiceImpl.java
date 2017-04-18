package fr.urssaf.image.sae.webservices.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Deblocage;
import fr.cirtil.www.saeservice.DeblocageResponse;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSDeblocageService;

/**
 * Classe d'implémentation de l'interface {@link WSDeblocageService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC et l'annotation @Autowired.
 */
@Service
public class WSDeblocageServiceImpl implements WSDeblocageService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSTransfertMasseServiceImpl.class);

   @Autowired
   private OperationPileTravauxService operationPileTravaux;

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

   /**
    * 
    * {@inheritDoc}
    * 
    * @throws JobInexistantException
    */
   @Override
   public final DeblocageResponse deblocage(Deblocage request, String callerIP)
         throws DeblocageAxisFault {
      String prefixeTrc = "deblocage()";
      LOG.debug("{} - Début", prefixeTrc);

      // Récuperer les paramètres du job à débloquer
      String uuid = request.getDeblocage().getUuid();
      UUID uuidJob = UUID.fromString(uuid);
      String etatJob = StringUtils.EMPTY;
      LOG.debug("{} - UUID du job: {}", prefixeTrc, uuid);

      try {
         // Recuperer le job
         JobRequest jobRequest = jobLectureService
               .getJobRequestNotNull(uuidJob);

         // Si code traitement existant dans les params du job
         String codeTraitement = StringUtils.EMPTY;
         if (jobRequest.getJobParameters() != null
               && !jobRequest.getJobParameters().isEmpty()) {
            codeTraitement = jobRequest.getJobParameters().get(
                  Constantes.CODE_TRAITEMENT);
         }

         if (JobState.FAILURE.name().equals(jobRequest.getState().toString())) {
            // Si déblocage modification de masse
            if (StringUtils.isNotBlank(codeTraitement)) {
               jobQueueService.deleteJobAndSemaphoreFromJobsQueues(uuidJob,
                     codeTraitement);
               Date abortDate = new Date();
               // Passer le job à l'état ABORT
               jobQueueService.changerEtatJobRequest(uuidJob,
                     JobState.ABORT.name(), abortDate, null);
            } else {
               LOG.warn("{} - échec de déblocage du job {} - ce job ne correspond pas à un traitement de modification de masse",
                     new Object[] { prefixeTrc, uuid });
               throw new DeblocageAxisFault(
                     "DeblocageAxisFault",
                     "Erreur de déblocage: le job ne correspond pas à un traitement de modification de masse");
            }
         } else if (JobState.RESERVED.name().equals(
               jobRequest.getState().toString())
               || JobState.STARTING.name().equals(
                     jobRequest.getState().toString())) {
            jobQueueService.deleteJobFromJobsQueues(uuidJob);
            // Passer le job à l'état FAILURE
            Date dateFailure = new Date();
            jobQueueService.changerEtatJobRequest(uuidJob,
                  JobState.FAILURE.name(), dateFailure, null);
         } else {
            LOG.warn("{} - échec de déblocage du job {} - ce job ne peut pas être débloqué à cause de son état",
                  new Object[] { prefixeTrc, uuid });
            throw new DeblocageAxisFault(
                  "DeblocageAxisFault",
                  "Erreur de déblocage: le job ne peut pas être débloqué à cause de son état");
         }
         // Récupérer l'état du job apèrs déblocage
         JobRequest job = jobLectureService.getJobRequest(uuidJob);
         etatJob = job.getState().toString();
      } catch (JobInexistantException e) {
         LOG.warn("{} - échec de déblocage du job {} - ce job n'existe plus",
               new Object[] { prefixeTrc, uuid });
      } catch (AccessDeniedException e) {
         throw e;
      } catch (Exception e) {
         throw new DeblocageAxisFault("ErreurInterneDeblocage", e.getMessage(),
               e);
      }

      return ObjectStorageResponseFactory
            .createDeblocageResponse(uuid, etatJob);
   }

}
