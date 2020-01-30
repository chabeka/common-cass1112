package fr.urssaf.image.sae.pile.travaux.modelcql;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;

/**
 * Traitement dans la pile des travaux. Les propriétés sont.
 * <ul>
 * <li><code>idJob</code>: identifiant unique du traitement</li>
 * <li><code>type</code>: type de traitement</li>
 * <li><code>parameters</code>: paramètres du traitement</li>
 * <li><code>state</code>: état du traitement</li>
 * <li><code>reservedBy</code>: hostname du serveur ayant réservé la demande</li>
 * <li><code>creationDate</code>: date/heure d'arrivée de la demande</li>
 * <li><code>reservationDate</code>: date/heure de réservation</li>
 * <li><code>startingDate</code>: date/heure de début de traitement</li>
 * <li><code>endingDate</code>: date/heure de fin de traitement</li>
 * <li><code>message</code>: message de compte-rendu du traitement. Exemple :
 * message d'erreur</li>
 * <li><code>toCheckFlag</code>: flag pour indiquer que le traitement est à
 * vérifier</li>
 * <li><code>toCheckFlagRaison</code>: raison pour laquelle le traitement est à
 * vérifier</li>
 * <li><code>clientHost</code>: host du client qui demande le traitement de
 * masse</li>
 * <li><code>saeHost</code>: host du serveur de l'ordonnanceur qui exécute le
 * traitement de masse</li>
 * <li><code>docCount</code>: nombre de documents à traiter pour une capture en
 * masse uniquement</li>
 * <li><code>pid</code>: PID du processus qui exécute le traitement de masse</li>
 * </ul>
 */
@Table(name = "jobrequestcql")
public class JobRequestCql implements Serializable, Comparable<JobRequestCql> {

  @PartitionKey
  @Id
  private UUID idJob;

  private String type;

  private String parameters;

  private String state;

  private String reservedBy;

  private Date creationDate;

  private Date reservationDate;

  private Date startingDate;

  private Date endingDate;

  private String message;

  private String saeHost;

  private String clientHost;

  private Integer docCount;

  private Integer docCountTraite;

  private Integer pid;

  private Boolean toCheckFlag;

  private String toCheckFlagRaison;

  @Column(name = "viextrait")
  private VIContenuExtrait vi;

  private Map<String, String> jobParameters;

  private byte[] jobKey;

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
   * @return the parameters
   */
  public final String getParameters() {
    return parameters;
  }

  /**
   * @param parameters
   *           the parameters to set
   */
  public final void setParameters(final String parameters) {
    this.parameters = parameters;
  }

  /**
   * @return the state of the jobRequest
   */
  public final String getState() {
    return state;
  }

  /**
   * @param state
   *           the state to set
   */
  public final void setState(final String state) {
    this.state = state;
  }

  /**
   * @return the reservedBy
   */
  public final String getReservedBy() {
    return reservedBy;
  }

  /**
   * @param reservedBy
   *           the reservedBy to set
   */
  public final void setReservedBy(final String reservedBy) {
    this.reservedBy = reservedBy;
  }

  /**
   * @return the creationDate
   */
  public final Date getCreationDate() {
    // On ne renvoie pas la date directement, car c'est un objet mutable
    return creationDate == null ? null : new Date(creationDate.getTime());
  }

  /**
   * @param creationDate
   *           the creationDate to set
   */
  public final void setCreationDate(final Date creationDate) {
    this.creationDate = creationDate == null ? null : new Date(creationDate
                                                               .getTime());
  }

  /**
   * @return the reservationDate
   */
  public final Date getReservationDate() {
    // On ne renvoie pas la date directement, car c'est un objet mutable
    return reservationDate == null ? null : new Date(reservationDate
                                                     .getTime());
  }

  /**
   * @param reservationDate
   *           the reservationDate to set
   */
  public final void setReservationDate(final Date reservationDate) {
    this.reservationDate = reservationDate == null ? null : new Date(
                                                                     reservationDate.getTime());
  }

  /**
   * @return the startingDate
   */
  public final Date getStartingDate() {
    // On ne renvoie pas la date directement, car c'est un objet mutable
    return startingDate == null ? null : new Date(startingDate.getTime());
  }

  /**
   * @param startingDate
   *           the startingDate to set
   */
  public final void setStartingDate(final Date startingDate) {
    this.startingDate = startingDate == null ? null : new Date(startingDate
                                                               .getTime());
  }

  /**
   * @return the endingDate
   */
  public final Date getEndingDate() {
    // On ne renvoie pas la date directement, car c'est un objet mutable
    return endingDate == null ? null : new Date(endingDate.getTime());
  }

  /**
   * @param endingDate
   *           the endingDate to set
   */
  public final void setEndingDate(final Date endingDate) {
    this.endingDate = endingDate == null ? null : new Date(endingDate
                                                           .getTime());
  }

  /**
   * @param message
   *           : message de compte-rendu du traitement
   */
  public final void setMessage(final String message) {
    this.message = message;
  }

  /**
   * @return message de compte-rendu du traitement
   */
  public final String getMessage() {
    return message;
  }

  /**
   * @return le nom de machine ou l'IP de la machine SAE ayant traité la
   *         demande
   */
  public final String getSaeHost() {
    return saeHost;
  }

  /**
   * @param saeHost
   *           le nom de machine ou l'IP de la machine SAE ayant traité la
   *           demande
   */
  public final void setSaeHost(final String saeHost) {
    this.saeHost = saeHost;
  }

  /**
   * @return le nom de machine ou l'IP de la machine cliente ayant traité la
   *         demande
   */
  public final String getClientHost() {
    return clientHost;
  }

  /**
   * @param clientHost
   *           le nom de machine ou l'IP de la machine cliente ayant traité la
   *           demande
   */
  public final void setClientHost(final String clientHost) {
    this.clientHost = clientHost;
  }

  /**
   * @return le nombre de documents présents dans le fichier sommaire
   */
  public final Integer getDocCount() {
    return docCount;
  }

  /**
   * @param docCount
   *           le nombre de documents présents dans le fichier sommaire
   */
  public final void setDocCount(final Integer docCount) {
    this.docCount = docCount;
  }

  /**
   * @return le process ID du traitement
   */
  public final Integer getPid() {
    return pid;
  }

  /**
   * @param pid
   *           le process ID du traitement
   */
  public final void setPid(final Integer pid) {
    this.pid = pid;
  }

  /**
   * @return the toCheckFlag
   */
  public final Boolean getToCheckFlag() {
    return toCheckFlag;
  }

  /**
   * @param toCheckFlag
   *           the toCheckFlag to set
   */
  public final void setToCheckFlag(final Boolean toCheckFlag) {
    this.toCheckFlag = toCheckFlag;
  }

  /**
   * @return the toCheckFlagRaison
   */
  public final String getToCheckFlagRaison() {
    return toCheckFlagRaison;
  }

  /**
   * @param toCheckFlagRaison
   *           the toCheckFlagRaison to set
   */
  public final void setToCheckFlagRaison(final String toCheckFlagRaison) {
    this.toCheckFlagRaison = toCheckFlagRaison;
  }

  /**
   * @return le contenu du VI
   */
  public final VIContenuExtrait getVi() {
    return vi;
  }

  /**
   * @param viExtrait
   *           le contenu du VI
   */
  public final void setVi(final VIContenuExtrait viExtrait) {
    vi = viExtrait;
  }

  /**
   * @return jobParameters
   *         Objet contenant tous les paramètres du job
   */
  public final Map<String, String> getJobParameters() {
    return jobParameters;
  }

  /**
   * @param jobParameters
   *           Objet contenant tous les paramètres du job
   */
  public final void setJobParameters(final Map<String, String> jobParameters) {
    this.jobParameters = jobParameters;
  }

  /**
   * Getter pour jobKey
   *
   * @return the jobKey
   */
  public byte[] getJobKey() {
    return jobKey;
  }

  /**
   * Setter pour jobKey
   *
   * @param jobKey
   *           the jobKey to set
   */
  public void setJobKey(final byte[] jobKey) {
    this.jobKey = jobKey;
  }

  /**
   * Getter pour docCountTraite
   *
   * @return the docCountTraite
   */
  public Integer getDocCountTraite() {
    return docCountTraite;
  }

  /**
   * Setter pour docCountTraite
   *
   * @param docCountTraite
   *           the docCountTraite to set
   */
  public void setDocCountTraite(final Integer docCountTraite) {
    this.docCountTraite = docCountTraite;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final JobRequestCql job) {
    return (TimeUUIDUtils.getTimeFromUUID(idJob) + "").compareTo(TimeUUIDUtils.getTimeFromUUID(job.getIdJob()) + "");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (clientHost == null ? 0 : clientHost.hashCode());
    result = prime * result + (creationDate == null ? 0 : creationDate.hashCode());
    result = prime * result + (docCount == null ? 0 : docCount.hashCode());
    result = prime * result + (docCountTraite == null ? 0 : docCountTraite.hashCode());
    result = prime * result + (endingDate == null ? 0 : endingDate.hashCode());
    result = prime * result + (idJob == null ? 0 : idJob.hashCode());
    result = prime * result + Arrays.hashCode(jobKey);
    result = prime * result + (jobParameters == null ? 0 : jobParameters.hashCode());
    result = prime * result + (message == null ? 0 : message.hashCode());
    result = prime * result + (parameters == null ? 0 : parameters.hashCode());
    result = prime * result + (pid == null ? 0 : pid.hashCode());
    result = prime * result + (reservationDate == null ? 0 : reservationDate.hashCode());
    result = prime * result + (reservedBy == null ? 0 : reservedBy.hashCode());
    result = prime * result + (saeHost == null ? 0 : saeHost.hashCode());
    result = prime * result + (startingDate == null ? 0 : startingDate.hashCode());
    result = prime * result + (state == null ? 0 : state.hashCode());
    result = prime * result + (toCheckFlag == null ? 0 : toCheckFlag.hashCode());
    result = prime * result + (toCheckFlagRaison == null ? 0 : toCheckFlagRaison.hashCode());
    result = prime * result + (type == null ? 0 : type.hashCode());
    result = prime * result + (vi == null ? 0 : vi.hashCode());
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
    final JobRequestCql other = (JobRequestCql) obj;
    if (clientHost == null) {
      if (other.clientHost != null) {
        return false;
      }
    } else if (!clientHost.equals(other.clientHost)) {
      return false;
    }
    if (creationDate == null) {
      if (other.creationDate != null) {
        return false;
      }
    } else if (!creationDate.equals(other.creationDate)) {
      return false;
    }
    if (docCount == null) {
      if (other.docCount != null) {
        return false;
      }
    } else if (!docCount.equals(other.docCount)) {
      return false;
    }
    if (docCountTraite == null) {
      if (other.docCountTraite != null) {
        return false;
      }
    } else if (!docCountTraite.equals(other.docCountTraite)) {
      return false;
    }
    if (endingDate == null) {
      if (other.endingDate != null) {
        return false;
      }
    } else if (!endingDate.equals(other.endingDate)) {
      return false;
    }
    if (idJob == null) {
      if (other.idJob != null) {
        return false;
      }
    } else if (!idJob.equals(other.idJob)) {
      return false;
    }
    if (!Arrays.equals(jobKey, other.jobKey)) {
      return false;
    }
    if (jobParameters == null) {
      if (other.jobParameters != null) {
        return false;
      }
    } else if (!jobParameters.equals(other.jobParameters)) {
      return false;
    }
    if (message == null) {
      if (other.message != null) {
        return false;
      }
    } else if (!message.equals(other.message)) {
      return false;
    }
    if (parameters == null) {
      if (other.parameters != null) {
        return false;
      }
    } else if (!parameters.equals(other.parameters)) {
      return false;
    }
    if (pid == null) {
      if (other.pid != null) {
        return false;
      }
    } else if (!pid.equals(other.pid)) {
      return false;
    }
    if (reservationDate == null) {
      if (other.reservationDate != null) {
        return false;
      }
    } else if (!reservationDate.equals(other.reservationDate)) {
      return false;
    }
    if (reservedBy == null) {
      if (other.reservedBy != null) {
        return false;
      }
    } else if (!reservedBy.equals(other.reservedBy)) {
      return false;
    }
    if (saeHost == null) {
      if (other.saeHost != null) {
        return false;
      }
    } else if (!saeHost.equals(other.saeHost)) {
      return false;
    }
    if (startingDate == null) {
      if (other.startingDate != null) {
        return false;
      }
    } else if (!startingDate.equals(other.startingDate)) {
      return false;
    }
    if (state == null) {
      if (other.state != null) {
        return false;
      }
    } else if (!state.equals(other.state)) {
      return false;
    }
    if (toCheckFlag == null) {
      if (other.toCheckFlag != null) {
        return false;
      }
    } else if (!toCheckFlag.equals(other.toCheckFlag)) {
      return false;
    }
    if (toCheckFlagRaison == null) {
      if (other.toCheckFlagRaison != null) {
        return false;
      }
    } else if (!toCheckFlagRaison.equals(other.toCheckFlagRaison)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    if (vi == null) {
      if (other.vi != null) {
        return false;
      }
    } else if (!vi.equals(other.vi)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "JobRequestCql [idJob=" + idJob + ", type=" + type + ", parameters=" + parameters + ", state=" + state + ", reservedBy=" + reservedBy
        + ", creationDate=" + creationDate + ", reservationDate=" + reservationDate + ", startingDate=" + startingDate + ", endingDate=" + endingDate
        + ", message=" + message + ", saeHost=" + saeHost + ", clientHost=" + clientHost + ", docCount=" + docCount + ", docCountTraite=" + docCountTraite
        + ", pid=" + pid + ", toCheckFlag=" + toCheckFlag + ", toCheckFlagRaison=" + toCheckFlagRaison + ", vi=" + vi + ", jobParameters=" + jobParameters
        + ", jobKey=" + Arrays.toString(jobKey) + "]";
  }

}
