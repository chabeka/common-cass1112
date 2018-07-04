/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.cql.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.JobNonReinitialisableException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.service.cql.JobQueueCqlService;

/**
 * @author AC75007648
 */
public class JobQueueServiceCqlImpl implements JobQueueCqlService {

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#addJob(fr.urssaf.image.sae.pile.travaux.model.JobToCreate)
   */
  @Override
  public void addJob(final JobToCreate jobToCreate) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#reserveJob(java.util.UUID, java.lang.String, java.util.Date)
   */
  @Override
  public void reserveJob(final UUID idJob, final String hostname, final Date dateReservation)
      throws JobDejaReserveException, JobInexistantException, LockTimeoutException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#startingJob(java.util.UUID, java.util.Date)
   */
  @Override
  public void startingJob(final UUID idJob, final Date dateDebutTraitement) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#endingJob(java.util.UUID, boolean, java.util.Date)
   */
  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#endingJob(java.util.UUID, boolean, java.util.Date, java.lang.String, java.lang.String)
   */
  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement)
      throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#endingJob(java.util.UUID, boolean, java.util.Date, java.lang.String, java.lang.String, int)
   */
  @Override
  public void endingJob(final UUID idJob, final boolean succes, final Date dateFinTraitement, final String message, final String codeTraitement,
                        final int nbDocumentTraite)
      throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#addHistory(java.util.UUID, java.util.UUID, java.lang.String)
   */
  @Override
  public void addHistory(final UUID jobUuid, final UUID timeUuid, final String description) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#renseignerPidJob(java.util.UUID, java.lang.Integer)
   */
  @Override
  public void renseignerPidJob(final UUID idJob, final Integer pid) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#renseignerDocCountJob(java.util.UUID, java.lang.Integer)
   */
  @Override
  public void renseignerDocCountJob(final UUID idJob, final Integer nbDocs) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#updateToCheckFlag(java.util.UUID, java.lang.Boolean, java.lang.String)
   */
  @Override
  public void updateToCheckFlag(final UUID idJob, final Boolean toCheckFlag, final String raison) throws JobInexistantException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#deleteJob(java.util.UUID)
   */
  @Override
  public void deleteJob(final UUID idJob) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#resetJob(java.util.UUID)
   */
  @Override
  public void resetJob(final UUID idJob) throws JobNonReinitialisableException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#getHosts()
   */
  @Override
  public List<String> getHosts() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#addJobsQueue(fr.urssaf.image.sae.pile.travaux.model.JobToCreate)
   */
  @Override
  public void addJobsQueue(final JobToCreate jobToCreate) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#reserverJobDansJobsQueues(java.util.UUID, java.lang.String, java.lang.String, java.util.Map)
   */
  @Override
  public void reserverJobDansJobsQueues(final UUID idJob, final String hostname, final String type, final Map<String, String> jobParameters) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#deleteJobFromJobsQueues(java.util.UUID)
   */
  @Override
  public void deleteJobFromJobsQueues(final UUID idJob) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#changerEtatJobRequest(java.util.UUID, java.lang.String, java.util.Date, java.lang.String)
   */
  @Override
  public void changerEtatJobRequest(final UUID idJob, final String stateJob, final Date endingDate, final String message) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.pile.travaux.servicecql.JobQueueCqlService#deleteJobAndSemaphoreFromJobsQueues(java.util.UUID, java.lang.String)
   */
  @Override
  public void deleteJobAndSemaphoreFromJobsQueues(final UUID idJob, final String codeTraitement) {
    // TODO Auto-generated method stub

  }

}
