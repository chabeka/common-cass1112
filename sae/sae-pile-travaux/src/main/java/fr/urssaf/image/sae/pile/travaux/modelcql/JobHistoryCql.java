/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.pile.travaux.modelcql;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Modèle de l'historique d'un traitement.<br>
 * <ul>
 * <li><code>trace</code>: message de la trace</li>
 * <li><code>date</code>: date de la trace</li>
 * </ul>
 */
@Table(name = "jobhistorycql")
public class JobHistoryCql implements Serializable, Comparable<JobHistoryCql> {

  /** identifiant du job */
  @PartitionKey
  UUID idjob;

  /** date de la trace : message de la trace */
  Map<UUID, String> trace;

  /**
   *
   */
  public JobHistoryCql() {
    super();
  }

  /**
   * @return the idjob
   */
  public UUID getIdjob() {
    return idjob;
  }

  /**
   * @param idjob
   *          the idjob to set
   */
  public void setIdjob(final UUID idjob) {
    this.idjob = idjob;
  }

  /**
   * @return the trace
   */
  public Map<UUID, String> getTrace() {
    return trace;
  }

  /**
   * @param trace
   *          the trace to set
   */
  public void setTrace(final Map<UUID, String> trace) {
    this.trace = trace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final JobHistoryCql job) {
    return ("" + idjob).compareTo(job.getIdjob() + "");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (idjob == null ? 0 : idjob.hashCode());
    result = prime * result + (trace == null ? 0 : trace.hashCode());
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
    final JobHistoryCql other = (JobHistoryCql) obj;
    if (idjob == null) {
      if (other.idjob != null) {
        return false;
      }
    } else if (!idjob.equals(other.idjob)) {
      return false;
    }
    if (trace == null) {
      if (other.trace != null) {
        return false;
      }
    } else if (!trace.equals(other.trace)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "JobHistoryCql [idjob=" + idjob + ", trace=" + trace + "]";
  }

}
