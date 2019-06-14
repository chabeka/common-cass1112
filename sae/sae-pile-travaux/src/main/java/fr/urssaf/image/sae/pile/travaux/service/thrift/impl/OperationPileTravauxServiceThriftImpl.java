/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.thrift.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;
import fr.urssaf.image.sae.pile.travaux.service.thrift.OperationPileTravauxThriftService;
import me.prettyprint.hector.api.Keyspace;

/**
 * Implémentation du service {@link OperationPileTravauxService} Classe
 * singleton, accessible par l'annotation Autowired
 */
@Service
public class OperationPileTravauxServiceThriftImpl implements
                                                   OperationPileTravauxThriftService {

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(OperationPileTravauxServiceThriftImpl.class);

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
  public final void purger(final Date dateMax) {

    if (dateMax == null) {
      throw new IllegalArgumentException(
                                         "La date maximum de suppression des jobs doit être renseignée");
    }

    final List<JobRequest> listeJobRequest = jobLectureService.getJobsToDelete(keyspace, dateMax);

    int nbJobSupprimes = 0;
    for (final JobRequest jobRequest : listeJobRequest) {
      jobQueueService.deleteJob(jobRequest.getIdJob());
      nbJobSupprimes++;
    }
    LOGGER.info("Nombre de travaux supprimés : {}", nbJobSupprimes);

  }

}
