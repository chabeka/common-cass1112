package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Propriétés principales des traitements dans la pile des travaux. Les
 * propriétés sont :
 * <ul>
 * <li><b><code>jobstate</code></b>: l'état du traitemnt est:
 * <ul>
 * <li>soit en "<b>jobsWaiting</b>"</li>
 * <li>ou en cours de traitemnt sur un serveur "<b>serveurHostName</b>"</li>
 * <li>ou en blocage "<b>semaphore_CODE_TRAITEMENT</b>"</li>
 * </ul>
 * </li>
 * <li><b><code>idJob</code></b>: identifiant unique du traitement</li>
 * <li><b><code>type</code></b>: type de traitement</li>
 * <li><b><code>parameters</code></b>: paramètres du traitement</li>
 * </ul>
 */
@Table(name = "jobqueue")
public class JobQueueCql {

  @PartitionKey
  private UUID idJob;

  private String jobsituation;

  private String type;

  private Map<String, String> jobParameters;

  /**
   * @return the idJob
   */
  public final UUID getIdJob() {
    return idJob;
  }

  /**
   * @param idJob
   *          the idJob to set
   */
  public final void setIdJob(final UUID idJob) {
    this.idJob = idJob;
  }

  /**
   * @return the type
   */
  public final String getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public final void setType(final String type) {
    this.type = type;
  }

  /**
   * @return les parametres du job
   */
  public final Map<String, String> getJobParameters() {
    return jobParameters;
  }

  /**
   * @param jobParameters
   *          les parametres du job
   */
  public final void setJobParameters(final Map<String, String> jobParameters) {
    this.jobParameters = jobParameters;
  }

  /**
   * @return the jobsituation
   */
  public String getJobsituation() {
    return jobsituation;
  }

  /**
   * @param jobsituation
   *          the jobsituation to set
   */
  public void setJobsituation(final String jobsituation) {
    this.jobsituation = jobsituation;
  }

}
