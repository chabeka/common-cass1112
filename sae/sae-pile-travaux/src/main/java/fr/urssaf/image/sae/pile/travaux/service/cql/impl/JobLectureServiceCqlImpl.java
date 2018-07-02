/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.cql.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.servicecql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupportCql;
import me.prettyprint.hector.api.Keyspace;

/**
 * @author AC75007648
 */
@Service
public class JobLectureServiceCqlImpl implements JobLectureCqlService {

  private final JobHistorySupportCql jobHistorySupportCql;

  @Autowired
  public JobLectureServiceCqlImpl(final JobHistorySupportCql jobHistorySupportCql) {
    super();
    this.jobHistorySupportCql = jobHistorySupportCql;
  }

  @Override
  public JobRequest getJobRequest(final UUID jobRequestUUID) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterator<JobQueue> getUnreservedJobRequestIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobQueue> getNonTerminatedSimpleJobs(final String hostname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobRequest> getNonTerminatedJobs(final String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobHistory> getJobHistory(final UUID idJob) {
    return null;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace, final int maxKeysToRead) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobRequest> getJobsToDelete(final Keyspace keyspace, final Date dateMax) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isJobResettable(final JobRequest job) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isJobRemovable(final JobRequest job) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public JobRequest getJobRequestNotNull(final UUID uuidJob) throws JobInexistantException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
    // TODO Auto-generated method stub
    return null;
  }

}
