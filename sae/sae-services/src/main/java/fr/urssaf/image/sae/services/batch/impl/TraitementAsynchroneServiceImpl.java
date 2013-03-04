package fr.urssaf.image.sae.services.batch.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.service.impl.skip.SaeDroitServiceSkipImpl;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.exception.JobInattenduException;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.batch.model.CaptureMasseParametres;
import fr.urssaf.image.sae.services.batch.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.support.TraitementExecutionSupport;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du service {@link TraitementAsynchroneService}
 * 
 * 
 */
@Service
public class TraitementAsynchroneServiceImpl implements
      TraitementAsynchroneService {

   private static final Logger LOG = LoggerFactory
         .getLogger(TraitementAsynchroneServiceImpl.class);

   /**
    * Nom du job d'un traitement de capture en masse
    */
   public static final String CAPTURE_MASSE_JN = "capture_masse";

   private static final String TRC_LANCER = "lancerJob";

   private final JobLectureService jobLectureService;

   private final JobQueueService jobQueueService;

   private TraitementExecutionSupport captureMasse;

   /**
    * 
    * @param jobLectureService
    *           service de lecture de la pile des travaux
    * @param jobQueueService
    *           service de la pile des travaux
    */
   @Autowired
   public TraitementAsynchroneServiceImpl(JobLectureService jobLectureService,
         JobQueueService jobQueueService) {

      this.jobLectureService = jobLectureService;
      this.jobQueueService = jobQueueService;

   }

   /**
    * 
    * @param captureMasse
    *           traitement de capture en masse
    */
   @Autowired
   @Qualifier("captureMasseTraitement")
   public final void setCaptureMasse(TraitementExecutionSupport captureMasse) {
      this.captureMasse = captureMasse;
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    * 
    * 
    */
   @Override
   public final void ajouterJobCaptureMasse(CaptureMasseParametres parameters) {
      
         LOG
         .debug(
               "{} - ajout d'un traitement de capture en masse avec le sommaire : {} pour  l'identifiant: {}",
               new Object[] { "ajouterJobCaptureMasse()",
                     getEcdeUrl(parameters),parameters.getUuid()});


      String type = CAPTURE_MASSE_JN;

      String parametres = parameters.getEcdeURL();
      Date dateDemande = new Date();
      UUID idJob = parameters.getUuid();

      JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType(type);
      job.setParameters(parametres);
      job.setCreationDate(dateDemande);
      job.setClientHost(parameters.getClientHost());
      job.setSaeHost(parameters.getSaeHost());
      job.setDocCount(parameters.getNbreDocs());
      job.setVi(parameters.getVi());
      job.setJobParameters(parameters.getJobParameters());
      jobQueueService.addJob(job);

   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void lancerJob(UUID idJob) throws JobInexistantException,
         JobNonReserveException {

      JobRequest job = jobLectureService.getJobRequest(idJob);

      // vérification que le traitement existe bien dans la pile des travaux
      if (job == null) {
         throw new JobInexistantException(idJob);
      }
      
      LOG.debug("{} - récupération du VI", TRC_LANCER);
      VIContenuExtrait viExtrait = job.getVi();
      AuthenticationToken token;

      if (viExtrait == null) {
         
         LOG.debug("{} - le Vi est null, on met toutes les autorisations", TRC_LANCER);
         
         List<String> pagms = new ArrayList<String>();
         pagms.add("ACCES_FULL_PAGM");
         SaeDroitServiceSkipImpl impl = new SaeDroitServiceSkipImpl();
         SaeDroits saeDroits = new SaeDroits();
         try {
            saeDroits = impl.loadSaeDroits("CS_ANCIEN_SYSTEME", pagms);
         } catch (ContratServiceNotFoundException e) {
            LOG.warn("impossible de créer un accès total");
         } catch (PagmNotFoundException e) {
            LOG.warn("impossible de créer un accès total");
         }
         viExtrait = new VIContenuExtrait();
         viExtrait.setCodeAppli("aucun contrat de service");
         viExtrait.setIdUtilisateur("aucun contrat de service");
         viExtrait.setSaeDroits(saeDroits);

      }

      String[] roles = viExtrait.getSaeDroits().keySet().toArray(new String[0]);
      token = AuthenticationFactory.createAuthentication(viExtrait
            .getIdUtilisateur(), viExtrait, roles, viExtrait.getSaeDroits());
      LOG.debug("{} - initialisation du contexte de sécurité", TRC_LANCER);
      AuthenticationContext.setModeHeritage();
      AuthenticationContext.setAuthenticationToken(token);

      // vérification que le type de traitement existe bien
      // pour l'instant seul la capture en masse existe
      if (!CAPTURE_MASSE_JN.equals(job.getType())) {
         throw new JobInattenduException(job, CAPTURE_MASSE_JN);
      }

      // vérification que le job est bien réservé
      if (!JobState.RESERVED.equals(job.getState())) {
         throw new JobNonReserveException(idJob);
      }

      // récupération du PID
      String processName = java.lang.management.ManagementFactory
            .getRuntimeMXBean().getName();
      String pid = null;

      if (processName.contains("@")) {
         pid = processName.split("@")[0];

         LOG.debug("PID = " + pid);

         jobQueueService.renseignerPidJob(idJob, Integer.valueOf(pid));

      } else {
         LOG.info("impossible de récupérer le pid");
      }

      // démarrage du job et mise à jour de la pile des travaux
      jobQueueService.startingJob(idJob, new Date());

      // Ajout d'une trace
      UUID timeUuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      jobQueueService.addHistory(idJob, timeUuid, "LANCEMENT DU JOB.");

      ExitTraitement exitTraitement;
      try {

         // appel de l'implémentation de l'exécution du traitement de capture en
         // masse
         exitTraitement = captureMasse.execute(job);

      } catch (Exception e) {

         LOG.warn("Erreur grave lors de l'exécution  du traitement.", e);

         exitTraitement = new ExitTraitement();
         exitTraitement.setSucces(false);
         exitTraitement.setExitMessage(e.getMessage());

      }

      LOG.debug(
            "{} - le traitement n°{} est terminé {}. Message de sortie : {}.",
            new Object[] {
                  "lancerJob()",
                  job.getIdJob(),
                  BooleanUtils.toString(exitTraitement.isSucces(),
                        "avec succès", "sur un échec"),
                  exitTraitement.getExitMessage() });

      // le traitement est terminé
      // on met à jour la pile des travaux
      jobQueueService.endingJob(idJob, exitTraitement.isSucces(), new Date(),
            exitTraitement.getExitMessage());

   }
   
   private String getEcdeUrl(CaptureMasseParametres parameters){
      String url = StringUtils.EMPTY;
      if(StringUtils.isNotBlank(parameters.getEcdeURL())){
         url =parameters.getEcdeURL();         
      }else{
         url =parameters.getJobParameters().get(Constantes.ECDE_URL);
      }
      
      return url;
   }
}
