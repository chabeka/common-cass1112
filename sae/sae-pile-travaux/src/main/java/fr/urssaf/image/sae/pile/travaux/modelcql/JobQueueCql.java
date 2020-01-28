package fr.urssaf.image.sae.pile.travaux.modelcql;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Propriétés principales des traitements dans la pile des travaux. Les
 * propriétés sont :
 * <ul>
 * <li><b><code>jobsituation</code></b>: l'état du traitemnt est:
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
@Table(name = "jobqueuecql")
public class JobQueueCql implements Serializable, Comparable<JobQueueCql> {

  @PartitionKey
  private String key;

  @ClusteringColumn
  private UUID idJob;

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
   *           the idJob to set
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
   *           the type to set
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
   *           les parametres du job
   */
  public final void setJobParameters(final Map<String, String> jobParameters) {
    this.jobParameters = jobParameters;
  }

  /**
   * @return the jobsituation
   */
  public String getKey() {
    return key;
  }

  /**
   * @param jobsituation
   *           the jobsituation to set
   */
  public void setKey(final String key) {
    this.key = key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final JobQueueCql job) {
    return (key + idJob + "").compareTo(job.getKey() + job.getIdJob() + "");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (idJob == null ? 0 : idJob.hashCode());
    result = prime * result + (jobParameters == null ? 0 : jobParameters.hashCode());
    result = prime * result + (key == null ? 0 : key.hashCode());
    result = prime * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final JobQueueCql other = (JobQueueCql) obj;
    if (idJob == null) {
      if (other.idJob != null) {
        return false;
      }
    } else if (!idJob.equals(other.idJob)) {
      return false;
    }
    if (jobParameters == null) {
      if (other.jobParameters != null) {
        return false;
      }
    } else if (!jobParameters.equals(other.jobParameters)) {
      return false;
    }
    if (key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    return true;
  }

}
