/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.pile.travaux.modelcql;

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
public class JobHistoryCql {

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

}
