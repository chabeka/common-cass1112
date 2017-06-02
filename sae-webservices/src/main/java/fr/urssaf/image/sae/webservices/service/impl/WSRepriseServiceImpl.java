package fr.urssaf.image.sae.webservices.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Reprise;
import fr.cirtil.www.saeservice.RepriseResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
import fr.urssaf.image.sae.webservices.exception.DeblocageAxisFault;
import fr.urssaf.image.sae.webservices.exception.RepriseAxisFault;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;
import fr.urssaf.image.sae.webservices.service.WSRepriseService;
import fr.urssaf.image.sae.webservices.util.HostnameUtil;

/**
 * Classe d'implémentation de l'interface {@link WSRepriseService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC et
 * l'annotation @Autowired.
 */
@Service
public class WSRepriseServiceImpl implements WSRepriseService {

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

   @Autowired
   private TraitementAsynchroneService traitementService;

   @Override
   public RepriseResponse reprise(Reprise request, String callerIP)
         throws RepriseAxisFault, JobInexistantException {
      String prefixeTrc = "deblocage()";
      LOG.debug("{} - Début", prefixeTrc);

      // Récuperer les paramètres du job à reprendre
      UuidType uuid = request.getReprise().getUuid();
      UUID uuidJobAReprendre = UUID.fromString(uuid.getUuidType());
      UUID uuidJobReprise = null;
      LOG.debug("{} - UUID du job à reprendre: {}", prefixeTrc, uuid);

      try {
         // Récuperer le job
         JobRequest jobRequest = jobLectureService
               .getJobRequestNotNull(uuidJobAReprendre);

         if (JobState.FAILURE.name().equals(jobRequest.getState().toString())) {
            LOG.debug(
                  "{} - ajout d'un traitement de reprise de masse du job: {}",
                  new Object[] { "ajouterJob()", uuid });
            String hName = HostnameUtil.getHostname();
            // Récupération de l'UUID du traitement
            String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);
            // Récupération de l'UUID du traitement
            uuidJobReprise = UUID.fromString(contextLog);

            // Charger l'uuid du job à reprendre dans les paramètres de la
            // reprise
            Map<String, String> jobParams = new HashMap<String, String>();
            jobParams.put(Constantes.UUID_JOB_A_Reprendre,
                  uuidJobAReprendre.toString());

            VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
                  .getContext().getAuthentication().getPrincipal();
            
            TraitemetMasseParametres parametres = new TraitemetMasseParametres(
                  jobParams, uuidJobReprise, TYPES_JOB.reprise_masse, hName,
                  callerIP, null, extrait);
            traitementService.ajouterJobReprise(parametres);
         } else {
            LOG.warn(
                  "{} - échec de reprise du job {} - ce job ne peut pas être repris à cause de son état",
                  new Object[] { prefixeTrc, uuid });
            throw new DeblocageAxisFault("RepriseAxisFault",
                  "Erreur de reprise: le job ne peut pas être repris à cause de son état");
         }
      } catch (JobInexistantException e) {
         LOG.warn("{} - échec de reprise du job {} - ce job n'existe plus",
               new Object[] { prefixeTrc, uuid });
      } catch (AccessDeniedException e) {
         throw e;
      } catch (Exception e) {
         throw new RepriseAxisFault("ErreurInterneReprise", e.getMessage(), e);
      }

      return ObjectStorageResponseFactory.createRepriseResponse(uuidJobReprise
            .toString());
   }

}
