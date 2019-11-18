/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.service.thrift.JobLectureThriftService;
import fr.urssaf.image.sae.pile.travaux.utils.JobHistoryMapper;
import fr.urssaf.image.sae.pile.travaux.utils.JobRequestMapper;
import fr.urssaf.image.sae.pile.travaux.utils.JobsQueueMapper;
import me.prettyprint.hector.api.Keyspace;

/**
 * @author AC75007648
 */
@Service
public class JobLectureServiceImpl implements JobLectureService {

  private final String cfName = "jobsqueue";

  private final JobLectureCqlService jobLectureCqlService;

  private final JobLectureThriftService jobLectureThriftService;

  @Autowired
  public JobLectureServiceImpl(final JobLectureCqlService jobLectureCqlService, final JobLectureThriftService jobLectureThriftService) {
    super();
    this.jobLectureCqlService = jobLectureCqlService;
    this.jobLectureThriftService = jobLectureThriftService;
  }

  @Override
  public JobRequest getJobRequest(final UUID jobRequestUUID) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      final JobRequestCql jobRequestCql = jobLectureCqlService.getJobRequest(jobRequestUUID);
      // Vérifier que le job existe
      if (jobRequestCql != null) {
    	  return JobRequestMapper.mapJobRequestCqlToJobRequestThrift(jobRequestCql);
      }
      return JobRequestMapper.mapJobRequestCqlToJobRequestThrift(jobRequestCql);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getJobRequest(jobRequestUUID);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getJobRequest(jobRequestUUID);
    }
    return null;
  }

  @Override
  public Iterator<JobQueue> getUnreservedJobRequestIterator() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return JobsQueueMapper.mapIteratorJobQueueToIteratorJobQueueCql(jobLectureCqlService.getUnreservedJobRequestIterator());
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getUnreservedJobRequestIterator();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getUnreservedJobRequestIterator();
    }
    return null;
  }

  @Override
  public List<JobQueue> getNonTerminatedSimpleJobs(final String hostname) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return JobsQueueMapper.mapListJobQueueToListJobQueueCql(jobLectureCqlService.getNonTerminatedSimpleJobs(hostname));
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getNonTerminatedSimpleJobs(hostname);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getNonTerminatedSimpleJobs(hostname);
    }
    return null;
  }

  @Override
  public List<JobRequest> getNonTerminatedJobs(final String key) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      final List<JobRequest> listJobT = new ArrayList<>();
      final List<JobRequestCql> listRequestCql = jobLectureCqlService.getNonTerminatedJobs(key);
      for (final JobRequestCql job : listRequestCql) {
        listJobT.add(JobRequestMapper.mapJobRequestCqlToJobRequestThrift(job));
      }
      return listJobT;
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getNonTerminatedJobs(key);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getNonTerminatedJobs(key);
    }
    return null;
  }

  @Override
  public List<JobHistory> getJobHistory(final UUID idJob) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return JobHistoryMapper.mapListJobHistoryCqlToListJobHistory(jobLectureCqlService.getJobHistory(idJob).get(0));
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getJobHistory(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getJobHistory(idJob);
    }
    return null;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      final List<JobRequest> listJobT = new ArrayList<>();
      final List<JobRequestCql> listRequestCql = jobLectureCqlService.getAllJobs();
      for (final JobRequestCql job : listRequestCql) {
        listJobT.add(JobRequestMapper.mapJobRequestCqlToJobRequestThrift(job));
      }
      return listJobT;
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getAllJobs(keyspace);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getAllJobs(keyspace);
    }
    return null;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace, final int maxKeysToRead) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      final List<JobRequest> listJobT = new ArrayList<>();
      final List<JobRequestCql> listRequestCql = jobLectureCqlService.getAllJobs(maxKeysToRead);
      for (final JobRequestCql job : listRequestCql) {
        listJobT.add(JobRequestMapper.mapJobRequestCqlToJobRequestThrift(job));
      }
      return listJobT;
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getAllJobs(keyspace, maxKeysToRead);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getAllJobs(keyspace, maxKeysToRead);
    }
    return null;
  }

  @Override
  public List<JobRequest> getJobsToDelete(final Keyspace keyspace, final Date dateMax) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      final List<JobRequest> listJobT = new ArrayList<>();
      final List<JobRequestCql> listRequestCql = jobLectureCqlService.getJobsToDelete(dateMax);
      for (final JobRequestCql job : listRequestCql) {
        listJobT.add(JobRequestMapper.mapJobRequestCqlToJobRequestThrift(job));
      }
      return listJobT;
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getJobsToDelete(keyspace, dateMax);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getJobsToDelete(keyspace, dateMax);
    }
    return null;
  }

  @Override
  public boolean isJobResettable(final JobRequest job) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return jobLectureCqlService.isJobResettable(JobRequestMapper.mapJobRequestThriftToJobRequestCql(job));
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.isJobResettable(job);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.isJobResettable(job);
    }
    return false;
  }

  @Override
  public boolean isJobRemovable(final JobRequest job) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return jobLectureCqlService.isJobRemovable(JobRequestMapper.mapJobRequestThriftToJobRequestCql(job));
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.isJobRemovable(job);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.isJobRemovable(job);
    }
    return false;
  }

  @Override
  public JobRequest getJobRequestNotNull(final UUID uuidJob) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      final JobRequestCql cql = jobLectureCqlService.getJobRequestNotNull(uuidJob);
      return JobRequestMapper.mapJobRequestCqlToJobRequestThrift(cql);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getJobRequestNotNull(uuidJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getJobRequestNotNull(uuidJob);
    }
    return null;
  }

  @Override
  public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return jobLectureCqlService.getJobRequestIdByJobKey(jobKey);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return jobLectureThriftService.getJobRequestIdByJobKey(jobKey);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return jobLectureThriftService.getJobRequestIdByJobKey(jobKey);
    }
    return null;
  }

}
