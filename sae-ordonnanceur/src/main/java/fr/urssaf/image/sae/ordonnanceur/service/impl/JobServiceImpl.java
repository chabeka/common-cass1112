package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobExecutionDao;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobInstanceDao;
import fr.urssaf.image.sae.ordonnanceur.exception.JobDejaReserveException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobInexistantException;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;

/**
 * Implémentation du service {@link JobService}.<br>
 * <br>
 * la persistance des traitements s'appuie sur Spring Batch.<br>
 * L'implémentation de la persistance est fournié par Cassandra.
 * 
 * 
 * 
 */
public class JobServiceImpl implements JobService {

   private final CassandraJobExecutionDao jobExecutionDao;

   private final CassandraJobInstanceDao jobInstanceDao;

   /**
    * 
    * @param jobExecutionDao
    *           dao d'exécution des job
    * @param jobInstanceDao
    *           dao d'instanciation des job
    */
   @Autowired
   public JobServiceImpl(CassandraJobExecutionDao jobExecutionDao,
         CassandraJobInstanceDao jobInstanceDao) {

      this.jobExecutionDao = jobExecutionDao;
      this.jobInstanceDao = jobInstanceDao;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Collection<JobExecution> recupJobEnCours() {

      Collection<JobExecution> jobExecutions = jobExecutionDao
            .getRunningJobExecutions();

      return jobExecutions;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobInstance> recupJobsALancer() {

      List<JobInstance> jobInstances = jobInstanceDao
            .getUnreservedJobInstances();

      return jobInstances;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(long idJob) throws JobInexistantException,
         JobDejaReserveException {

      // récupération du nom de la machine
      String serverName;
      try {
         serverName = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

      // vérification que le job existe bien dans la table JobInstance
      JobInstance jobInstance = jobInstanceDao.getJobInstance(idJob);
      if (jobInstance == null) {

         throw new JobInexistantException(idJob);
      }

      // vérification que le job n'est pas déjà réservé
      String reservingServer = jobInstanceDao.getReservingServer(idJob);
      if (StringUtils.isBlank(reservingServer)) {

         // réservation du job
         jobInstanceDao.reserveJob(idJob, serverName);

      } else {

         // le job est déjà réservé
         throw new JobDejaReserveException(idJob, reservingServer);

      }

   }

}
