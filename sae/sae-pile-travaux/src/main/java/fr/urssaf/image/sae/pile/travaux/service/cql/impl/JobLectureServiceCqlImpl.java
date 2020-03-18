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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.cql.impl.JobHistoryDaoCqlImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.impl.JobRequestDaoCqlImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.impl.JobsQueueDaoCqlImpl;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.support.JobHistorySupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupportCql;
import fr.urssaf.image.sae.pile.travaux.support.JobsQueueSupportCql;

/**
 * @author AC75007648
 */
@Service
public class JobLectureServiceCqlImpl implements JobLectureCqlService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobLectureServiceCqlImpl.class);

  private  JobRequestSupportCql jobRequestSupportCql;

  private  JobsQueueSupportCql jobsQueueSupportCql;

  private JobHistorySupportCql jobHistorySupportCql;

  private static final int MAX_ALL_JOBS = 200;

  public JobLectureServiceCqlImpl() {
    super();
  }

  @Autowired
  public JobLectureServiceCqlImpl(final JobRequestSupportCql jobRequestSupportCql, final JobsQueueSupportCql jobsQueueSupportCql,
                                  final JobHistorySupportCql jobHistorySupportCql) {

    this.jobHistorySupportCql = jobHistorySupportCql;
    this.jobRequestSupportCql = jobRequestSupportCql;
    this.jobsQueueSupportCql = jobsQueueSupportCql;
  }

  /**
   * 
   */
  public JobLectureServiceCqlImpl(final CassandraCQLClientFactory ccf) {

    final IJobHistoryDaoCql jobHistoryDaoCql = new JobHistoryDaoCqlImpl(ccf);
    jobHistoryDaoCql.setCcf(ccf);
    final JobHistorySupportCql jobHistorySupportCql = new JobHistorySupportCql();
    jobHistorySupportCql.setJobHistoryDaoCql(jobHistoryDaoCql);

    final IJobRequestDaoCql jobRequestDaoCql = new JobRequestDaoCqlImpl(ccf);
    jobRequestDaoCql.setCcf(ccf);
    final JobRequestSupportCql jobRequestSupportCql = new JobRequestSupportCql();
    jobRequestSupportCql.setJobRequestDaoCql(jobRequestDaoCql);

    final IJobsQueueDaoCql jobsQueueDaoCql = new JobsQueueDaoCqlImpl(ccf);
    jobsQueueDaoCql.setCcf(ccf);
    final JobsQueueSupportCql jobsQueueSupportCql = new JobsQueueSupportCql();
    jobsQueueSupportCql.setJobsQueueDaoCql(jobsQueueDaoCql);

    this.jobHistorySupportCql = jobHistorySupportCql;
    this.jobRequestSupportCql = jobRequestSupportCql;
    this.jobsQueueSupportCql = jobsQueueSupportCql;
  }

  @Override
  public JobRequestCql getJobRequest(final UUID jobRequestUUID) {

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
  public List<JobRequestCql> getNonTerminatedJobs(final String key) {

    final List<JobQueueCql> listJQ = getNonTerminatedSimpleJobs(key);

    final List<JobRequestCql> jobRequests = new ArrayList<>();

    for (final JobQueueCql jobQueue : listJQ) {
      final JobRequestCql jobRequest = getJobRequest(jobQueue.getIdJob());
      if (jobRequest != null) {
        jobRequests.add(jobRequest);
      }
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
  public List<JobRequestCql> getAllJobs() {

    final List<JobRequestCql> listJR = new ArrayList<>();
    final Iterator<JobRequestCql> it = jobRequestSupportCql.findAll();

    while (it.hasNext() && listJR.size() < MAX_ALL_JOBS) {
      listJR.add(it.next());
    }
    return listJR;
  }

  @Override
  public List<JobRequestCql> getAllJobs(int maxKeysToRead) {

    final List<JobRequestCql> listJR = new ArrayList<>();
    final Iterator<JobRequestCql> it = jobRequestSupportCql.findAll();

    while (it.hasNext() && maxKeysToRead > 0) {
      listJR.add(it.next());
      maxKeysToRead--;
    }
    return listJR;
  }

  @Override
  public List<JobRequestCql> getJobsToDelete(final Date dateMax) {

    final List<JobRequestCql> listJR = new ArrayList<>();
    final Iterator<JobRequestCql> it = jobRequestSupportCql.findAll();

    while (it.hasNext()) {
      final JobRequestCql jobRequest = it.next();
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
  public final boolean isJobResettable(final JobRequestCql job) {
    if (JobState.RESERVED.name().equals(job.getState()) || JobState.STARTING.name().equals(job.getState())) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isJobRemovable(final JobRequestCql job) {
    if (JobState.CREATED.name().equals(job.getState()) || JobState.STARTING.name().equals(job.getState())
        || JobState.RESERVED.name().equals(job.getState())) {
      return true;
    }
    return false;
  }

  @Override
  public JobRequestCql getJobRequestNotNull(final UUID uuidJob) throws JobInexistantException {
    final JobRequestCql jobRequest = getJobRequest(uuidJob);
    if (jobRequest == null) {
      throw new JobInexistantException(uuidJob);
    }
    return jobRequest;
  }

  @Override
  public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
    return jobRequestSupportCql.getJobRequestIdByJobKey(jobKey);
  }

  @Override
  public long getJobRequestColunmWriteTime(final UUID id, final String columnName) {
    return jobRequestSupportCql.getJobRequestColunmWriteTime(id, columnName);

  }

}
