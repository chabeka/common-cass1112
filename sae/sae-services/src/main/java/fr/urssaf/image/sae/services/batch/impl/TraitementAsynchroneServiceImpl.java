package fr.urssaf.image.sae.services.batch.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.dao.JobQueueDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.exception.JobInattenduException;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;

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

   private final JobLauncher jobLauncher;

   private final JobQueueDao jobQueueDao;

   private final JobQueueService jobQueueService;

   private final Job captureMasse;

   /**
    * 
    * @param jobLauncher
    *           exécuteur de traitement de masse
    * @param jobQueueDao
    *           dao des instances de {@link JobRequest} pour cassandra
    * @param jobQueueService
    *           service de la pile des travaux
    * @param captureMasse
    *           job du traitement de capture en masse
    */
   @Autowired
   public TraitementAsynchroneServiceImpl(JobLauncher jobLauncher,
         JobQueueDao jobQueueDao, JobQueueService jobQueueService,
         @Qualifier("capture_masse") Job captureMasse) {

      this.jobLauncher = jobLauncher;
      this.jobQueueDao = jobQueueDao;
      this.captureMasse = captureMasse;
      this.jobQueueService = jobQueueService;
   }

   /**
    * {@inheritDoc}<br>
    * <br>
    * 
    * 
    * 
    */
   @Override
   public final void ajouterJobCaptureMasse(String urlECDE, UUID uuid) {

      LOG
            .debug(
                  "{} - ajout d'un traitement de capture en masse avec le sommaire : {} pour  l'identifiant: {}",
                  new Object[] { "ajouterJobCaptureMasse()", urlECDE, uuid });

      String type = CAPTURE_MASSE_JN;
      String parametres = urlECDE;
      Date dateDemande = new Date();
      UUID idJob = uuid;
      jobQueueService.addJob(idJob, type, parametres, dateDemande);

   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final String lancerJob(UUID idJob) throws JobInexistantException,
         JobNonReserveException, JobInattenduException {

      JobRequest job = jobQueueDao.getJobRequest(idJob);

      if (job == null) {
         throw new JobInexistantException(idJob);
      }

      if (!CAPTURE_MASSE_JN.equals(job.getType())) {
         throw new JobInattenduException(job, CAPTURE_MASSE_JN);
      }

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("capture.masse.idtraitement", new JobParameter(idJob
            .toString()));

      // le paramètre stocké dans la pile des travaux correspond pour les
      // traitements de capture en masse à l'URL ECDE

      String urlECDE = job.getParameters();
      // TODO vérifiier que les paramètres sont corrects.
      parameters.put("capture.masse.sommaire", new JobParameter(urlECDE));
      JobParameters jobParameters = new JobParameters(parameters);

      // vérification que le job est bien réservé
      if (!JobState.RESERVED.equals(job.getState())) {
         throw new JobNonReserveException(idJob);
      }

      // démarrage du job et mise à jour de la pile des travaux
      jobQueueService.startingJob(idJob, new Date());

      // lancement du job abev spring batch
      JobExecution jobExecution = run(captureMasse, jobParameters);

      // le traitement est terminé
      // on met à jour la pile des travaux
      boolean succes = ExitStatus.COMPLETED
            .equals(jobExecution.getExitStatus()) ? true : false;

      jobQueueService.endingJob(idJob, succes, new Date());

      return jobExecution.getExitStatus().getExitCode();

   }

   protected final JobExecution run(Job job, JobParameters jobParameters) {

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.run(job, jobParameters);
      } catch (JobExecutionAlreadyRunningException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobRestartException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobInstanceAlreadyCompleteException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobParametersInvalidException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      return jobExecution;
   }

}
