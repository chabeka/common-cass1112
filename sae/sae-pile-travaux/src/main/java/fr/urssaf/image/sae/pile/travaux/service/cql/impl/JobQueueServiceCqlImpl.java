/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.cql.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobQueueServiceImpl;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupportCql;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

/**
 * @author AC75007648
 */
@Service
public class JobQueueServiceCqlImpl implements JobQueueCqlService {

  private final CuratorFramework curatorClient;

  private final JobClockSupport jobClockSupport;

  private final JobsQueueSupportCql jobsQueueSupportCql;

  private final JobHistorySupportCql jobHistorySupportCql;

  private final JobLectureCqlService jobLectureCqlService;

  private final JobRequestSupportCql jobRequestSupportCql;

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(JobQueueServiceImpl.class);

  @Autowired
  public JobQueueServiceCqlImpl(final JobRequestSupportCql jobRequestSupportCql,
                                final JobsQueueSupportCql jobsQueueSupportCql, final JobClockSupport jobClockSupport,
                                final JobHistorySupportCql jobHistorySupportCql, final JobLectureCqlService jobLectureCqlService,
                                final CuratorFramework curatorClient) {
    super();
    this.curatorClient = curatorClient;
    this.jobClockSupport = jobClockSupport;
    this.jobLectureCqlService = jobLectureCqlService;
    this.jobRequestSupportCql = jobRequestSupportCql;
    this.jobsQueueSupportCql = jobsQueueSupportCql;
    this.jobHistorySupportCql = jobHistorySupportCql;

  }

  @Override
  public void addJob(final JobToCreate jobToCreate) {
    // Timestamp de l'opération
    // Pas besoin de gérer le décalage ici : on ne fait que la création
    final long clock = jobClockSupport.currentCLock();

    // Ecriture dans la CF "JobRequest"
    this.jobRequestSupportCql.ajouterJobDansJobRequest(jobToCreate, clock);

    // Ecriture dans la CF "JobQueues"
    this.addJobsQueue(jobToCreate);

    // Ecriture dans la CF "JobHistory"
    final String messageTrace = "CREATION DU JOB";
    final UUID timestampTrace = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    this.jobHistorySupportCql.ajouterTrace(jobToCreate.getIdJob(),
                                           timestampTrace,
                                           messageTrace);
  }

  @Override
  public void reserveJob(final UUID idJob, final String hostname, final Date dateReservation)
      throws JobDejaReserveException, JobInexistantException, LockTimeoutException {

  }

  @Override
  public void startingJob(final UUID idJob, final Date dateDebutTraitement) throws JobInexistantException {

  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement) throws JobInexistantException {
    endingJob(idJob, succes, dateFinTraitement);
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement)
      throws JobInexistantException {
    this.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, -1);
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement,
                        final int nbDocumentTraite)
      throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  @Override
  public void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {
    // Ecriture dans la CF "JobHistory"
    this.jobHistorySupportCql
                             .ajouterTrace(jobUuid, timeUuid, description);
  }

  @Override
  public void renseignerPidJob(final UUID idJob, final Integer pid) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  @Override
  public void renseignerDocCountJob(final UUID idJob, final Integer nbDocs) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag, final String raison) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteJob(final UUID idJob) {
    // TODO Auto-generated method stub

  }

  @Override
  public void resetJob(final UUID idJob) throws JobNonReinitialisableException {
    // TODO Auto-generated method stub

  }

  @Override
  public List<String> getHosts() {
    return this.jobsQueueSupportCql.getHosts();
  }

  @Override
  public void addJobsQueue(final JobToCreate jobToCreate) {

  }

  @Override
  public void reserverJobDansJobsQueues(final UUID idJob, final String hostname, final String type, final Map<String, String> jobParameters) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteJobFromJobsQueues(final UUID idJob) {
    // TODO Auto-generated method stub

  }

  @Override
  public void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate, final String message) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteJobAndSemaphoreFromJobsQueues(final UUID idJob, final String codeTraitement) {
    // TODO Auto-generated method stub

  }

}
