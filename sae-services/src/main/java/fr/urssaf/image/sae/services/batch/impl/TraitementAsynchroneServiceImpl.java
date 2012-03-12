package fr.urssaf.image.sae.services.batch.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
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

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobInstanceDao;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.exception.JobInexistantException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;

/**
 * Implémentation du service {@link TraitementAsynchroneService}
 * 
 * 
 */
@Service
public class TraitementAsynchroneServiceImpl implements
      TraitementAsynchroneService {

   private final JobLauncher jobLauncher;

   private final CassandraJobInstanceDao jobInstanceDao;

   private final Job captureMasse;

   /**
    * 
    * @param jobLauncher
    *           exécuteur de traitement de masse
    * @param jobInstanceDao
    *           doa des instances de {@link JobInstance} pour cassandra
    * @param captureMasse
    *           job du traitement de capture en masse
    */
   @Autowired
   public TraitementAsynchroneServiceImpl(JobLauncher jobLauncher,
         CassandraJobInstanceDao jobInstanceDao,
         @Qualifier("capture_masse") Job captureMasse) {

      this.jobLauncher = jobLauncher;
      this.jobInstanceDao = jobInstanceDao;
      this.captureMasse = captureMasse;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final long ajouterJobCaptureMasse(String urlECDE, UUID uuid) {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("capture.masse.idtraitement", new JobParameter(uuid
            .toString()));
      parameters.put("capture.masse.sommaire", new JobParameter(urlECDE));
      JobParameters jobParameters = new JobParameters(parameters);

      JobInstance jobInstance = jobInstanceDao.createJobInstance(
            "capture_masse", jobParameters);

      return jobInstance.getId();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String lancerJob(long idJob) throws JobInexistantException {

      JobInstance jobInstance = jobInstanceDao.getJobInstance(idJob);

      if (jobInstance == null) {
         throw new JobInexistantException(idJob);
      }

      JobParameters jobParameters = jobInstance.getJobParameters();

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.run(captureMasse, jobParameters);
      } catch (JobExecutionAlreadyRunningException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobRestartException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobInstanceAlreadyCompleteException e) {
         throw new CaptureMasseRuntimeException(e);
      } catch (JobParametersInvalidException e) {
         throw new CaptureMasseRuntimeException(e);
      }

      return jobExecution.getExitStatus().getExitCode();

   }

}
