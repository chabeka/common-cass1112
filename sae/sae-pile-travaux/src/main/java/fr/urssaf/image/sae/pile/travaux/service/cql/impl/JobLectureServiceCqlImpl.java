/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.cql.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupportCql;
import me.prettyprint.hector.api.Keyspace;

/**
 * @author AC75007648
 */
@Service
public class JobLectureServiceCqlImpl implements JobLectureCqlService {

  private final JobRequestSupportCql jobRequestSupportCql;

  private final JobsQueueSupportCql jobsQueueSupportCql;

  private final JobHistorySupportCql jobHistorySupportCql;

  private static final int MAX_ALL_JOBS = 200;

  @Autowired
  public JobLectureServiceCqlImpl(final JobHistorySupportCql jobHistorySupportCql, final JobRequestSupportCql jobRequestSupportCql,
                                  final JobsQueueSupportCql jobsQueueSupportCql) {

    this.jobHistorySupportCql = jobHistorySupportCql;
    this.jobRequestSupportCql = jobRequestSupportCql;
    this.jobsQueueSupportCql = jobsQueueSupportCql;
  }

  @Override
  public JobRequest getJobRequest(final UUID jobRequestUUID) {

    return jobRequestSupportCql.getJobRequest(jobRequestUUID);
  }

  @Override
  public Iterator<JobQueueCql> getUnreservedJobRequestIterator() {
    return jobsQueueSupportCql.getUnreservedJobRequest();
  }

  @Override
  public List<JobQueueCql> getNonTerminatedSimpleJobs(final String hostname) {
    return jobsQueueSupportCql.getNonTerminatedSimpleJobs(hostname);
  }

  @Override
  public List<JobRequest> getNonTerminatedJobs(final String key) {

    final List<JobQueueCql> listJQ = getNonTerminatedSimpleJobs(key);

    final List<JobRequest> jobRequests = new ArrayList<JobRequest>();

    for (final JobQueueCql jobQueue : listJQ) {
      final JobRequest jobRequest = this.getJobRequest(jobQueue.getIdJob());
      jobRequests.add(jobRequest);
    }

    return jobRequests;

  }

  @Override
  public List<JobHistoryCql> getJobHistory(final UUID idJob) {

    final List<JobHistoryCql> listJH = new ArrayList<>();
    final Iterator<JobHistoryCql> it = jobHistorySupportCql.getJobHistory(idJob);

    while (it.hasNext()) {
      listJH.add(it.next());
    }
    return listJH;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace) {

    final List<JobRequest> listJR = new ArrayList<>();
    final Iterator<JobRequest> it = jobRequestSupportCql.findAll();

    while (it.hasNext() && listJR.size() < MAX_ALL_JOBS) {
      listJR.add(it.next());
    }
    return listJR;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace, int maxKeysToRead) {

    final List<JobRequest> listJR = new ArrayList<>();
    final Iterator<JobRequest> it = jobRequestSupportCql.findAll();

    while (it.hasNext() && maxKeysToRead > 0) {
      listJR.add(it.next());
      maxKeysToRead--;
    }
    return listJR;
  }

  @Override
  public List<JobRequest> getJobsToDelete(final Keyspace keyspace, final Date dateMax) {

    final List<JobRequest> listJR = new ArrayList<>();
    final Iterator<JobRequest> it = jobRequestSupportCql.findAll();

    while (it.hasNext()) {
      final JobRequest jobRequest = it.next();
      // On peut obtenir un jobRequest null dans le cas d'un jobRequest effacé

      if (jobRequest != null && (jobRequest.getCreationDate().before(dateMax)
          || DateUtils.isSameDay(jobRequest.getCreationDate(), dateMax))) {
        listJR.add(jobRequest);
      }
    }
    return listJR;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isJobResettable(final JobRequest job) {
    final String state = job.getState().toString();
    if (state.equals(JobState.RESERVED) || state.equals(JobState.STARTING)) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isJobRemovable(final JobRequest job) {
    final String state = job.getState().toString();
    if (JobState.CREATED.equals(state) || JobState.STARTING.equals(state)
        || JobState.RESERVED.equals(state)) {
      return true;
    }
    return false;
  }

  @Override
  public JobRequest getJobRequestNotNull(final UUID uuidJob) throws JobInexistantException {
    final JobRequest jobRequest = this.getJobRequest(uuidJob);
    if (jobRequest == null) {
      throw new JobInexistantException(uuidJob);
    }
    return jobRequest;
  }

  @Override
  public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
    return jobRequestSupportCql.getJobRequestIdByJobKey(jobKey);
  }

}
