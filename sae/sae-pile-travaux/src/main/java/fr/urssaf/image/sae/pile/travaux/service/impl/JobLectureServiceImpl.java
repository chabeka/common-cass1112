/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.impl;

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
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobLectureCqlService;
import fr.urssaf.image.sae.pile.travaux.service.thrift.JobLectureThriftService;
import fr.urssaf.image.sae.pile.travaux.utils.JobHistoryMapper;
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
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      final JobRequest jobRequest = this.jobLectureCqlService.getJobRequest(jobRequestUUID);
      // Vérifier que le job existe
      if (jobRequest == null) {
        try {
          throw new JobInexistantException(jobRequestUUID);
        }
        catch (final JobInexistantException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      return this.jobLectureCqlService.getJobRequest(jobRequestUUID);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getJobRequest(jobRequestUUID);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public Iterator<JobQueue> getUnreservedJobRequestIterator() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return JobsQueueMapper.mapIteratorJobQueueToIteratorJobQueueCql(this.jobLectureCqlService.getUnreservedJobRequestIterator());
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getUnreservedJobRequestIterator();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobQueue> getNonTerminatedSimpleJobs(final String hostname) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return JobsQueueMapper.mapListJobQueueToListJobQueueCql(this.jobLectureCqlService.getNonTerminatedSimpleJobs(hostname));
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getNonTerminatedSimpleJobs(hostname);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobRequest> getNonTerminatedJobs(final String key) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.getNonTerminatedJobs(key);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getNonTerminatedJobs(key);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobHistory> getJobHistory(final UUID idJob) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return JobHistoryMapper.mapListJobHistoryCqlToListJobHistory(this.jobLectureCqlService.getJobHistory(idJob).get(0));
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getJobHistory(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.getAllJobs(keyspace);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getAllJobs(keyspace);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobRequest> getAllJobs(final Keyspace keyspace, final int maxKeysToRead) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.getAllJobs(keyspace, maxKeysToRead);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getAllJobs(keyspace, maxKeysToRead);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<JobRequest> getJobsToDelete(final Keyspace keyspace, final Date dateMax) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.getJobsToDelete(keyspace, dateMax);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getJobsToDelete(keyspace, dateMax);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public boolean isJobResettable(final JobRequest job) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.isJobResettable(job);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.isJobResettable(job);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return false;
  }

  @Override
  public boolean isJobRemovable(final JobRequest job) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.isJobRemovable(job);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.isJobRemovable(job);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return false;
  }

  @Override
  public JobRequest getJobRequestNotNull(final UUID uuidJob) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.getJobRequestNotNull(uuidJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getJobRequestNotNull(uuidJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public UUID getJobRequestIdByJobKey(final byte[] jobKey) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.jobLectureCqlService.getJobRequestIdByJobKey(jobKey);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.jobLectureThriftService.getJobRequestIdByJobKey(jobKey);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

}
