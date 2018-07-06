/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.cql.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;
import fr.urssaf.image.sae.pile.travaux.service.cql.OperationPileTravauxCqlService;
import me.prettyprint.hector.api.Keyspace;

/**
 * Implémentation du service {@link OperationPileTravauxCqlService} Classe
 * singleton, accessible par l'annotation Autowired
 */
@Service
public class OperationPileTravauxServiceCqlImpl implements OperationPileTravauxCqlService {

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(OperationPileTravauxServiceCqlImpl.class);

  /**
   * Service permettant de réaliser des objets sur les jobs
   */
  @Autowired
  private JobQueueCqlService jobQueueCqlService;

  /**
   * Service permettant de réaliser les opérations de lecture sur les jobs
   */
  @Autowired
  private JobLectureCqlService jobLectureCqlService;

  /**
   * Keyspace utilisé
   */
  @Autowired
  private Keyspace keyspace;

  @Override
  public void purger(final Date dateMax) {
    if (dateMax == null) {
      throw new IllegalArgumentException(
                                         "La date maximum de suppression des jobs doit être renseignée");
    }

    final List<JobRequest> listeJobRequest = jobLectureCqlService.getJobsToDelete(keyspace, dateMax);

    int nbJobSupprimes = 0;
    for (final JobRequest jobRequest : listeJobRequest) {
      jobQueueCqlService.deleteJob(jobRequest.getIdJob());
      nbJobSupprimes++;
    }
    LOGGER.info("Nombre de travaux supprimés : {}", nbJobSupprimes);
  }

}
