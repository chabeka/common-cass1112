package fr.urssaf.image.sae.webservices.service.impl;

import java.util.HashMap;
import java.util.List;
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
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.aspect.BuildOrClearMDCAspect;
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
      String prefixeTrc = "reprise()";
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
         
         // Reprise impossible pour le job de reprise 
         if(jobRequest.getType().equals(TYPES_JOB.reprise_masse.name())){
            LOG.warn(
                  "{} - échec de reprise du job {} - Le job de reprise ne peut pas être repris",
                  new Object[] { prefixeTrc, uuidJobAReprendre });
            throw new RepriseAxisFault("ErreurInterneReprise",
                  "Le job de reprise ne peut pas être repris");
         } else if (JobState.FAILURE.name().equals(jobRequest.getState().toString())) {
            LOG.debug(
                  "{} - ajout d'un traitement de reprise de masse du job: {}",
                  new Object[] { "ajouterJob()", uuid });
            String hName = HostnameUtil.getHostname();
            // Récupération de l'UUID du traitement
            String contextLog = MDC.get(BuildOrClearMDCAspect.LOG_CONTEXTE);
            // Récupération de l'UUID du traitement
            uuidJobReprise = UUID.fromString(contextLog);
            
            VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
                  .getContext().getAuthentication().getPrincipal();

            // Vérification des droits pour le traitement reprise
            List<String> pagmsReprise = extrait.getPagms();
            List<String> pagmsJobAReprendre = jobRequest.getVi().getPagms();
            
            // Contrôle CS du traitement de reprise
            boolean checkAccessRepriseCS = true;
            if (!jobRequest.getVi().getCodeAppli()
                  .equals(extrait.getCodeAppli())) {
               checkAccessRepriseCS = false;
               LOG.warn(
                     "{} - Erreur de Reprise: Le traitement de reprise doit avoir le même contrat de service que celui du traitement à reprendre",
                     prefixeTrc);
               throw new RepriseAxisFault("ErreurInterneReprise",
                     "Le traitement de reprise doit avoir le même contrat de service que celui du traitement à reprendre");
            }
            // Contrôle PAGM de reprise
            if (checkAccessRepriseCS) {
               for (String pagmAReprendre : pagmsJobAReprendre) {
                  if (!pagmsReprise.contains(pagmAReprendre)) {
                     LOG.warn(
                           "{} - Erreur PAGM de reprise: Le traitement de reprise doit avoir un PAGM équivalent à celui du traitement à reprendre",
                          prefixeTrc);
                     throw new RepriseAxisFault("ErreurInterneReprise",
                           "Le traitement de reprise doit avoir un PAGM équivalent à celui du traitement à reprendre");
                  }
               }
            }
            
            // Charger l'uuid du job à reprendre dans les paramètres de la
            // reprise
            Map<String, String> jobParams = new HashMap<String, String>();
            jobParams.put(Constantes.ID_TRAITEMENT_A_REPRENDRE,
                  uuidJobAReprendre.toString());
            jobParams.put(Constantes.HEURE_REPRISE,
                  Long.toString(System.currentTimeMillis()));

            TraitemetMasseParametres parametres = new TraitemetMasseParametres(
                  jobParams, uuidJobReprise, TYPES_JOB.reprise_masse, hName,
                  callerIP, null, extrait);
            traitementService.ajouterJobReprise(parametres);
         } else {
            LOG.warn(
                  "{} - échec de reprise du job {} - ce job ne peut pas être repris à cause de son état",
                  new Object[] { prefixeTrc, uuid });
            throw new RepriseAxisFault("ErreurInterneReprise",
                  "Erreur de reprise: le job ne peut pas être repris à cause de son état");
         }
      } catch (JobInexistantException e) {
         LOG.warn("{} - échec de reprise du traitement {} - ce job n'existe plus en base",
               new Object[] { prefixeTrc, uuid });
         throw new RepriseAxisFault("ErreurInterneReprise", e.getMessage(), e);
      } catch (AccessDeniedException e) {
         throw new RepriseAxisFault("ErreurInterneReprise", e.getMessage(), e);
      } catch (Exception e) {
         throw new RepriseAxisFault("ErreurInterneReprise", e.getMessage(), e);
      }

      return ObjectStorageResponseFactory.createRepriseResponse(uuidJobReprise
            .toString());
   }

}
