package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.service.JobService;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;
import fr.urssaf.image.sae.pile.travaux.dao.JobQueueDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

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

   private final JobQueueDao jobQueueDao;

   private final JobQueueService jobQueueService;

   /**
    * 
    * @param jobQueueDao
    *           dao de la pile des jobs
    * @param jobQueueService
    *           service de la pile des jobs
    */
   @Autowired
   public JobServiceImpl(JobQueueDao jobQueueDao,
         JobQueueService jobQueueService) {

      this.jobQueueDao = jobQueueDao;
      this.jobQueueService = jobQueueService;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SimpleJobRequest> recupJobEnCours() {

      String hostname;
      try {
         hostname = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new OrdonnanceurRuntimeException(e);
      }
      List<SimpleJobRequest> jobRequests = jobQueueDao
            .getNonTerminatedSimpleJobs(hostname);

      return jobRequests;

   }

   /**
    * {@inheritDoc}
    */

   @Override
   public final List<SimpleJobRequest> recupJobsALancer() {

      @SuppressWarnings("unchecked")
      List<SimpleJobRequest> jobRequests = IteratorUtils.toList(jobQueueDao
            .getUnreservedJobRequestIterator());

      return jobRequests;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(UUID idJob) throws JobDejaReserveException,
         JobInexistantException {

      // récupération du nom de la machine
      String hostname;
      try {
         hostname = HostUtils.getLocalHostName();
      } catch (UnknownHostException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

      try {
         jobQueueService.reserveJob(idJob, hostname, new Date());
      } catch (LockTimeoutException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

   }

}
