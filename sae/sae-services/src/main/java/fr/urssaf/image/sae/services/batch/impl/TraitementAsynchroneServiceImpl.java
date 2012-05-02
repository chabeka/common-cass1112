package fr.urssaf.image.sae.services.batch.impl;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
                        parameters.getEcdeURL(), parameters.getUuid() });

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
}
