package fr.urssaf.image.sae.services.batch.impl;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

   private final JobQueueDao jobQueueDao;

   private final JobQueueService jobQueueService;

   private TraitementExecutionSupport captureMasse;

   /**
    * 
    * @param jobQueueDao
    *           dao des instances de {@link JobRequest} pour cassandra
    * @param jobQueueService
    *           service de la pile des travaux
    */
   @Autowired
   public TraitementAsynchroneServiceImpl(JobQueueDao jobQueueDao,
         JobQueueService jobQueueService) {

      this.jobQueueDao = jobQueueDao;
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
   public final void lancerJob(UUID idJob) throws JobInexistantException,
         JobNonReserveException {

      JobRequest job = jobQueueDao.getJobRequest(idJob);

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

      // démarrage du job et mise à jour de la pile des travaux
      jobQueueService.startingJob(idJob, new Date());

      boolean succes = false;
      try {

         // appel de l'implémentation de l'exécution du traitement de capture en
         // masse
         succes = captureMasse.execute(job);

      } catch (Exception e) {

         LOG.warn("Erreur grave lors de l'exécution  du traitement.", e);

         succes = false;

      } finally {

         // le traitement est terminé
         // on met à jour la pile des travaux

         jobQueueService.endingJob(idJob, succes, new Date());
      }

   }
}
