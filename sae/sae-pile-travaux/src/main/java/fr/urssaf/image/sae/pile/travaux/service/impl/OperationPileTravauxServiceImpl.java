/**
 * 
 */
package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.Date;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;

/**
 * Implémentation du service {@link OperationPileTravauxService} Classe
 * singleton, accessible par l'annotation Autowired
 * 
 */
@Service
public class OperationPileTravauxServiceImpl implements
      OperationPileTravauxService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(OperationPileTravauxServiceImpl.class);
   
   private static final int MAX_ALL_JOBS = 20000;

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

   /**
    * Keyspace utilisé
    */
   @Autowired
   private Keyspace keyspace;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void purger(Date dateMax) {

      if (dateMax == null) {
         throw new IllegalArgumentException(
               "La date maximum de suppression des jobs doit être renseignée");
      }

      List<JobRequest> listeJobRequest = jobLectureService.getAllJobs(keyspace, MAX_ALL_JOBS);

      int nbJobSupprimes = 0;
      for (JobRequest jobRequest : listeJobRequest) {
         Date dateCreation = jobRequest.getCreationDate();
         if (dateCreation.before(dateMax)
               || DateUtils.isSameDay(dateCreation, dateMax)) {
            jobQueueService.deleteJob(jobRequest.getIdJob());
            nbJobSupprimes++;
         }
      }
      LOGGER.info("Nombre de travaux supprimés : {}", nbJobSupprimes);

   }

}
