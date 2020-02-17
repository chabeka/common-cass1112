package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeAPIService;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;
import fr.urssaf.image.sae.pile.travaux.service.thrift.JobQueueThriftService;

/**
 * Implémentation du service {@link JobQueueService}
 * 
 * 
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

  private final String cfName = "jobsqueue";

  private final JobQueueCqlService jobQueueCqlService;

  private final JobQueueThriftService jobQueueThriftService;

  private final ModeAPIService modeApiService;

  @Autowired
  public JobQueueServiceImpl(final JobQueueCqlService jobQueueCqlService,
                             final JobQueueThriftService jobQueueThriftService,
                             final ModeAPIService modeApiService) {
    super();
    this.jobQueueCqlService = jobQueueCqlService;
    this.jobQueueThriftService = jobQueueThriftService;
    this.modeApiService = modeApiService;
  }

  @Override
  public void addJob(final JobToCreate jobToCreate) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.addJob(jobToCreate);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.addJob(jobToCreate);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.addJob(jobToCreate);
      jobQueueThriftService.addJob(jobToCreate);
    }
  }

  @Override
  public void reserveJob(final UUID idJob, final String hostname, final Date dateReservation)
      throws JobDejaReserveException, JobInexistantException, LockTimeoutException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.reserveJob(idJob, hostname, dateReservation);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.reserveJob(idJob, hostname, dateReservation);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.reserveJob(idJob, hostname, dateReservation);
      jobQueueThriftService.reserveJob(idJob, hostname, dateReservation);
    }
  }

  @Override
  public void startingJob(final UUID idJob, final Date dateDebutTraitement) throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.startingJob(idJob, dateDebutTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.startingJob(idJob, dateDebutTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.startingJob(idJob, dateDebutTraitement);
      jobQueueThriftService.startingJob(idJob, dateDebutTraitement);
    }
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement) throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement);
      jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement);
    }
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement)
      throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement);
      jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement);
    }
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement,
                        final int nbDocumentTraite)
                            throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, nbDocumentTraite);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, nbDocumentTraite);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, nbDocumentTraite);
      jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, nbDocumentTraite);
    }
  }

  @Override
  public void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.addHistory(jobUuid, timeUuid, description);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.addHistory(jobUuid, timeUuid, description);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.addHistory(jobUuid, timeUuid, description);
      jobQueueThriftService.addHistory(jobUuid, timeUuid, description);
    }
  }

  @Override
  public void renseignerPidJob(final UUID idJob, final Integer pid) throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.renseignerPidJob(idJob, pid);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.renseignerPidJob(idJob, pid);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.renseignerPidJob(idJob, pid);
      jobQueueThriftService.renseignerPidJob(idJob, pid);
    }
  }

  @Override
  public void renseignerDocCountJob(final UUID idJob, final Integer nbDocs) throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.renseignerDocCountJob(idJob, nbDocs);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.renseignerDocCountJob(idJob, nbDocs);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.renseignerDocCountJob(idJob, nbDocs);
      jobQueueThriftService.renseignerDocCountJob(idJob, nbDocs);
    }
  }

  @Override
  public void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag, final String raison) throws JobInexistantException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.updateToCheckFlag(idJob, toCheckFlag, raison);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.updateToCheckFlag(idJob, toCheckFlag, raison);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.updateToCheckFlag(idJob, toCheckFlag, raison);
      jobQueueThriftService.updateToCheckFlag(idJob, toCheckFlag, raison);
    }
  }

  @Override
  public void deleteJob(final UUID idJob) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.deleteJob(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.deleteJob(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.deleteJob(idJob);
      jobQueueThriftService.deleteJob(idJob);
    }
  }

  @Override
  public void resetJob(final UUID idJob) throws JobNonReinitialisableException {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.resetJob(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.resetJob(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.resetJob(idJob);
      jobQueueThriftService.resetJob(idJob);
    }
  }

  @Override
  public List<String> getHosts() {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      return jobQueueCqlService.getHosts();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      return jobQueueThriftService.getHosts();
    }
    return null;
  }

  @Override
  public void addJobsQueue(final JobToCreate jobToCreate) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.addJobsQueue(jobToCreate);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.addJobsQueue(jobToCreate);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.addJobsQueue(jobToCreate);
      jobQueueThriftService.addJobsQueue(jobToCreate);
    }
  }

  @Override
  public void reserverJobDansJobsQueues(final UUID idJob, final String hostname, final String type, final Map<String, String> jobParameters) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.reserverJobDansJobsQueues(idJob, hostname, type, jobParameters);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.reserverJobDansJobsQueues(idJob, hostname, type, jobParameters);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.reserverJobDansJobsQueues(idJob, hostname, type, jobParameters);
      jobQueueThriftService.reserverJobDansJobsQueues(idJob, hostname, type, jobParameters);
    }
  }

  @Override
  public void deleteJobFromJobsQueues(final UUID idJob) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.deleteJobFromJobsQueues(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.deleteJobFromJobsQueues(idJob);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.deleteJobFromJobsQueues(idJob);
      jobQueueThriftService.deleteJobFromJobsQueues(idJob);
    }
  }

  @Override
  public void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate, final String message) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.changerEtatJobRequest(idJob, stateJob, endingDate, message);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.changerEtatJobRequest(idJob, stateJob, endingDate, message);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.changerEtatJobRequest(idJob, stateJob, endingDate, message);
      jobQueueThriftService.changerEtatJobRequest(idJob, stateJob, endingDate, message);
    }
  }

  @Override
  public void deleteJobAndSemaphoreFromJobsQueues(final UUID idJob, final String codeTraitement) {
    final String modeApi = modeApiService.getModeAPI(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      jobQueueCqlService.deleteJobAndSemaphoreFromJobsQueues(idJob, codeTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      jobQueueThriftService.deleteJobAndSemaphoreFromJobsQueues(idJob, codeTraitement);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      jobQueueCqlService.deleteJobAndSemaphoreFromJobsQueues(idJob, codeTraitement);
      jobQueueThriftService.deleteJobAndSemaphoreFromJobsQueues(idJob, codeTraitement);
    }
  }

}
