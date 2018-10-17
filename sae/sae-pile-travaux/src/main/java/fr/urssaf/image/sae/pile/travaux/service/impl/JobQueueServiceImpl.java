/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;
import fr.urssaf.image.sae.pile.travaux.service.thrift.JobQueueThriftService;

/**
 * @author AC75007648
 */
@Service
public class JobQueueServiceImpl implements JobQueueService {

  private final String cfName = "jobsqueue";

  private final JobQueueCqlService jobQueueCqlService;

  private final JobQueueThriftService jobQueueThriftService;

  @Autowired
  public JobQueueServiceImpl(final JobQueueCqlService jobQueueCqlService, final JobQueueThriftService jobQueueThriftService) {
    super();
    this.jobQueueCqlService = jobQueueCqlService;
    this.jobQueueThriftService = jobQueueThriftService;
  }

  @Override
  public void addJob(final JobToCreate jobToCreate) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.addJob(jobToCreate);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.addJob(jobToCreate);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void reserveJob(final UUID idJob, final String hostname, final Date dateReservation)
      throws JobDejaReserveException, JobInexistantException, LockTimeoutException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.reserveJob(idJob, hostname, dateReservation);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.reserveJob(idJob, hostname, dateReservation);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void startingJob(final UUID idJob, final Date dateDebutTraitement) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.startingJob(idJob, dateDebutTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.startingJob(idJob, dateDebutTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement)
      throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement,
                        final int nbDocumentTraite)
      throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, nbDocumentTraite);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.endingJob(idJob, succes, dateFinTraitement, message, codeTraitement, nbDocumentTraite);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.addHistory(jobUuid, timeUuid, description);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.addHistory(jobUuid, timeUuid, description);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void renseignerPidJob(final UUID idJob, final Integer pid) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.renseignerPidJob(idJob, pid);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.renseignerPidJob(idJob, pid);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void renseignerDocCountJob(final UUID idJob, final Integer nbDocs) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.renseignerDocCountJob(idJob, nbDocs);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.renseignerDocCountJob(idJob, nbDocs);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag, final String raison) throws JobInexistantException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.updateToCheckFlag(idJob, toCheckFlag, raison);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.updateToCheckFlag(idJob, toCheckFlag, raison);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void deleteJob(final UUID idJob) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.deleteJob(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.deleteJob(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void resetJob(final UUID idJob) throws JobNonReinitialisableException {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.resetJob(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.resetJob(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public List<String> getHosts() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return jobQueueCqlService.getHosts();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return jobQueueThriftService.getHosts();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public void addJobsQueue(final JobToCreate jobToCreate) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.addJobsQueue(jobToCreate);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.addJobsQueue(jobToCreate);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void reserverJobDansJobsQueues(final UUID idJob, final String hostname, final String type, final Map<String, String> jobParameters) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.reserverJobDansJobsQueues(idJob, hostname, type, jobParameters);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.reserverJobDansJobsQueues(idJob, hostname, type, jobParameters);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void deleteJobFromJobsQueues(final UUID idJob) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.deleteJobFromJobsQueues(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.deleteJobFromJobsQueues(idJob);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate, final String message) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.changerEtatJobRequest(idJob, stateJob, endingDate, message);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.changerEtatJobRequest(idJob, stateJob, endingDate, message);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

  @Override
  public void deleteJobAndSemaphoreFromJobsQueues(final UUID idJob, final String codeTraitement) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      this.jobQueueCqlService.deleteJobAndSemaphoreFromJobsQueues(idJob, codeTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      this.jobQueueThriftService.deleteJobAndSemaphoreFromJobsQueues(idJob, codeTraitement);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
  }

}
