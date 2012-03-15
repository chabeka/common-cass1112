package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.pile.travaux.dao.JobQueueDao;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

/**
 * Implémentation du service {@link JobQueueService}
 * 
 * 
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

   @Autowired
   private CuratorFramework curatorClient;

   @Autowired
   private JobQueueDao jobQueueDao;

   private static final Logger LOG = LoggerFactory.getLogger(JobQueueServiceImpl.class);
   
   /**
    * Time-out du lock, en secondes
    */
   private static final int LOCK_TIME_OUT = 20;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addJob(UUID idJob, String type, String parametres,
         Date dateDemande) {
      JobRequest jobRequest = new JobRequest();
      jobRequest.setIdJob(idJob);
      jobRequest.setType(type);
      jobRequest.setParameters(parametres);
      jobRequest.setCreationDate(dateDemande);
      jobRequest.setState(JobState.CREATED);
      jobQueueDao.saveJobRequest(jobRequest);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void endingJob(UUID idJob, boolean succes,
         Date dateFinTraitement) {
      // Récupération du jobRequest
      JobRequest jobRequest = jobQueueDao.getJobRequest(idJob);
      Assert.notNull(jobRequest, "JobRequest d'id " + idJob + " non trouvé");
      // On modifie la date de fin de traitement, et l'état
      jobRequest.setEndingDate(dateFinTraitement);
      jobRequest.setState(succes?JobState.SUCCESS:JobState.FAILURE);
      // On persiste
      jobQueueDao.updateJobRequest(jobRequest);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void reserveJob(UUID idJob, String hostname,
         Date dateReservation) throws JobDejaReserveException,
         JobInexistantException, LockTimeoutException {
      
      Assert.notNull(idJob, "L'id du job ne doit pas être null");
      
      ZookeeperMutex mutex = new ZookeeperMutex(curatorClient, "/JobRequest/" + idJob);
      try {
         if (!mutex.acquire(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
            throw new LockTimeoutException(
                  "Erreur lors de la tentative d'acquisition du lock pour le jobRequest " + idJob
                        + " : on n'a pas obtenu le lock au bout de " + LOCK_TIME_OUT + " secondes.");
         }
         // On a le lock.
         // Récupération du jobRequest
         JobRequest jobRequest = jobQueueDao.getJobRequest(idJob);
         if (jobRequest == null) throw new JobInexistantException(idJob);

         if (jobRequest.getReservedBy() != null && !jobRequest.getReservedBy().isEmpty()) {
            throw new JobDejaReserveException(idJob, jobRequest.getReservedBy());
         }
         
         // On écrit la réservation dans cassandra
         jobQueueDao.reserveJobRequest(jobRequest, hostname, dateReservation);

         // On vérifie qu'on a toujours le lock. Si oui, la réservation a réellement fonctionné
         checkLock(mutex, idJob, hostname);

      } finally {
         mutex.release();
      }
   }

   /**
    * Après la réservation d'un job, on vérifie que le lock est encore valide
    * @param mutex      Le mutex utilisé pour le lock
    * @param idJob      Id du job réservé
    * @param hostname   Nom du serveur qui tente la réservation
    * @throws JobDejaReserveException  Si le lock n'est plus valide et qu'on s'est fait subtilisé le job 
    */
   private void checkLock(ZookeeperMutex mutex, UUID idJob, String hostname) throws JobDejaReserveException {
      // On vérifie qu'on a toujours le lock. Si oui, la réservation a
      // réellement fonctionné
      if (mutex.isObjectStillLocked(LOCK_TIME_OUT, TimeUnit.SECONDS)) {
         // C'est bon, le job est réellement réservé
         return;
      } else {
         // On a sûrement été déconnecté de zookeeper. C'est un cas qui ne
         // devrait jamais arriver.
         String message = "Erreur lors de la tentative d'acquisition du lock pour le jobRequest "
               + idJob + ". Problème de connexion zookeeper ?";
         LOG.error(message);

         // On regarde si le job a été réservé par un autre serveur
         JobRequest jobRequest = jobQueueDao.getJobRequest(idJob);
         String currentHostname = jobRequest.getReservedBy();
         if (currentHostname.equals(hostname)) {
            // On a été déconnecté de zookeeper, mais pour autant, le job nous a
            // été attribué.
            return;
         } else if (currentHostname != null && !currentHostname.isEmpty()
               && !currentHostname.equals(hostname)) {
            throw new JobDejaReserveException(idJob, currentHostname);
         }
      }
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.LongVariable")
   public final void startingJob(UUID idJob, Date dateDebutTraitement) throws JobInexistantException {
      // Récupération du jobRequest
      JobRequest jobRequest = jobQueueDao.getJobRequest(idJob);
      if (jobRequest == null) throw new JobInexistantException(idJob);
      
      // On modifie la date de début de traitement, et l'état
      jobRequest.setStartingDate(dateDebutTraitement);
      jobRequest.setState(JobState.STARTING);
      // On persiste
      jobQueueDao.updateJobRequest(jobRequest);
   }

}
