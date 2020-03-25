/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import java.io.Serializable;
import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.item.ExecutionContext;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Cette classe repesente l'objet qui sera enregistré dans cassandra.<br>
 * Les colonnes de la table correspondent aux attributs de la classe.<br>
 * La classe n'a rien à avoir avec la classe spring {@link JobStep}
 * portant à peu près le même nom.
 */
@Table(name = "jobstepcql")
public class JobStepCql implements Serializable, Comparable<JobStepCql> {

  // Colonnes de JobStep
  /** Clé de partionnement **/
  @PartitionKey
  @Column(name = "jobstepexecutionid")
  private Long jobStepExecutionId;

  @Column(name = "jobexecutionid")
  private Long jobExecutionId;

  private Integer version;

  private String name;

  private volatile Date startTime;

  private volatile Date endTime;

  private BatchStatus status;

  private int commitCount;

  private int readCount;

  private int filterCount;

  private int writeCount;

  private int readSkipCount;

  private int writeSkipCount;

  private int processSkipCount;

  private int rollbackCount;

  private String exitCode;

  private String exitMessage;

  private volatile Date lastUpdated;

  private volatile ExecutionContext executionContext = new ExecutionContext();

  /**
   *
   */
  public JobStepCql() {
  }

  /**
   * @return the jobStepExecutionId
   */
  public Long getJobStepExecutionId() {
    return jobStepExecutionId;
  }

  /**
   * @param jobStepExecutionId
   *           the jobStepExecutionId to set
   */
  public void setJobStepExecutionId(final Long jobStepExecutionId) {
    this.jobStepExecutionId = jobStepExecutionId;
  }

  /**
   * @return the jobExecutionId
   */
  public Long getJobExecutionId() {
    return jobExecutionId;
  }

  /**
   * @param jobExecutionId
   *           the jobExecutionId to set
   */
  public void setJobExecutionId(final Long jobExecutionId) {
    this.jobExecutionId = jobExecutionId;
  }

  /**
   * @return the version
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * @param version
   *           the version to set
   */
  public void setVersion(final Integer version) {
    this.version = version;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *           the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return the startTime
   */
  public Date getStartTime() {
    return startTime;
  }

  /**
   * @param startTime
   *           the startTime to set
   */
  public void setStartTime(final Date startTime) {
    this.startTime = startTime;
  }

  /**
   * @return the endTime
   */
  public Date getEndTime() {
    return endTime;
  }

  /**
   * @param endTime
   *           the endTime to set
   */
  public void setEndTime(final Date endTime) {
    this.endTime = endTime;
  }

  /**
   * @return the status
   */
  public BatchStatus getStatus() {
    return status;
  }

  /**
   * @param status
   *           the status to set
   */
  public void setStatus(final BatchStatus status) {
    this.status = status;
  }

  /**
   * @return the commitCount
   */
  public int getCommitCount() {
    return commitCount;
  }

  /**
   * @param commitCount
   *           the commitCount to set
   */
  public void setCommitCount(final int commitCount) {
    this.commitCount = commitCount;
  }

  /**
   * @return the readCount
   */
  public int getReadCount() {
    return readCount;
  }

  /**
   * @param readCount
   *           the readCount to set
   */
  public void setReadCount(final int readCount) {
    this.readCount = readCount;
  }

  /**
   * @return the filterCount
   */
  public int getFilterCount() {
    return filterCount;
  }

  /**
   * @param filterCount
   *           the filterCount to set
   */
  public void setFilterCount(final int filterCount) {
    this.filterCount = filterCount;
  }

  /**
   * @return the writeCount
   */
  public int getWriteCount() {
    return writeCount;
  }

  /**
   * @param writeCount
   *           the writeCount to set
   */
  public void setWriteCount(final int writeCount) {
    this.writeCount = writeCount;
  }

  /**
   * @return the readSkipCount
   */
  public int getReadSkipCount() {
    return readSkipCount;
  }

  /**
   * @param readSkipCount
   *           the readSkipCount to set
   */
  public void setReadSkipCount(final int readSkipCount) {
    this.readSkipCount = readSkipCount;
  }

  /**
   * @return the writeSkipCount
   */
  public int getWriteSkipCount() {
    return writeSkipCount;
  }

  /**
   * @param writeSkipCount
   *           the writeSkipCount to set
   */
  public void setWriteSkipCount(final int writeSkipCount) {
    this.writeSkipCount = writeSkipCount;
  }

  /**
   * @return the processSkipCount
   */
  public int getProcessSkipCount() {
    return processSkipCount;
  }

  /**
   * @param processSkipCount
   *           the processSkipCount to set
   */
  public void setProcessSkipCount(final int processSkipCount) {
    this.processSkipCount = processSkipCount;
  }

  /**
   * @return the rollbackCount
   */
  public int getRollbackCount() {
    return rollbackCount;
  }

  /**
   * @param rollbackCount
   *           the rollbackCount to set
   */
  public void setRollbackCount(final int rollbackCount) {
    this.rollbackCount = rollbackCount;
  }

  /**
   * @return the exitCode
   */
  public String getExitCode() {
    return exitCode;
  }

  /**
   * @param exitCode
   *           the exitCode to set
   */
  public void setExitCode(final String exitCode) {
    this.exitCode = exitCode;
  }

  /**
   * @return the exitMessage
   */
  public String getExitMessage() {
    return exitMessage;
  }

  /**
   * @param exitMessage
   *           the exitMessage to set
   */
  public void setExitMessage(final String exitMessage) {
    this.exitMessage = exitMessage;
  }

  /**
   * @return the lastUpdated
   */
  public Date getLastUpdated() {
    return lastUpdated;
  }

  /**
   * @param lastUpdated
   *           the lastUpdated to set
   */
  public void setLastUpdated(final Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /**
   * @return the executionContext
   */
  public ExecutionContext getExecutionContext() {
    return executionContext;
  }

  /**
   * @param executionContext
   *           the executionContext to set
   */
  public void setExecutionContext(final ExecutionContext executionContext) {
    this.executionContext = executionContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final JobStepCql job) {
    return name.compareTo(job.getName());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + commitCount;
    result = prime * result + (endTime == null ? 0 : endTime.hashCode());
    result = prime * result + (executionContext == null ? 0 : executionContext.hashCode());
    result = prime * result + (exitCode == null ? 0 : exitCode.hashCode());
    result = prime * result + (exitMessage == null ? 0 : exitMessage.hashCode());
    result = prime * result + filterCount;
    result = prime * result + (jobExecutionId == null ? 0 : jobExecutionId.hashCode());
    result = prime * result + (lastUpdated == null ? 0 : lastUpdated.hashCode());
    result = prime * result + (name == null ? 0 : name.hashCode());
    result = prime * result + processSkipCount;
    result = prime * result + readCount;
    result = prime * result + readSkipCount;
    result = prime * result + rollbackCount;
    result = prime * result + (startTime == null ? 0 : startTime.hashCode());
    result = prime * result + (status == null ? 0 : status.hashCode());
    result = prime * result + (version == null ? 0 : version.hashCode());
    result = prime * result + writeCount;
    result = prime * result + writeSkipCount;
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
    final JobStepCql other = (JobStepCql) obj;
    if (commitCount != other.commitCount) {
      return false;
    }
    if (endTime == null) {
      if (other.endTime != null) {
        return false;
      }
    } else if (!endTime.equals(other.endTime)) {
      return false;
    }
    if (executionContext == null) {
      if (other.executionContext != null) {
        return false;
      }
    } else if (!executionContext.equals(other.executionContext)) {
      return false;
    }
    if (exitCode == null) {
      if (other.exitCode != null) {
        return false;
      }
    } else if (!exitCode.equals(other.exitCode)) {
      return false;
    }
    if (exitMessage == null) {
      if (other.exitMessage != null) {
        return false;
      }
    } else if (!exitMessage.equals(other.exitMessage)) {
      return false;
    }
    if (filterCount != other.filterCount) {
      return false;
    }
    if (jobExecutionId == null) {
      if (other.jobExecutionId != null) {
        return false;
      }
    } else if (!jobExecutionId.equals(other.jobExecutionId)) {
      return false;
    }
    if (lastUpdated == null) {
      if (other.lastUpdated != null) {
        return false;
      }
    } else if (!lastUpdated.equals(other.lastUpdated)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (processSkipCount != other.processSkipCount) {
      return false;
    }
    if (readCount != other.readCount) {
      return false;
    }
    if (readSkipCount != other.readSkipCount) {
      return false;
    }
    if (rollbackCount != other.rollbackCount) {
      return false;
    }
    if (startTime == null) {
      if (other.startTime != null) {
        return false;
      }
    } else if (!startTime.equals(other.startTime)) {
      return false;
    }
    if (status != other.status) {
      return false;
    }
    if (version == null) {
      if (other.version != null) {
        return false;
      }
    } else if (!version.equals(other.version)) {
      return false;
    }
    if (writeCount != other.writeCount) {
      return false;
    }
    if (writeSkipCount != other.writeSkipCount) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "JobStepCql [jobStepExecutionId=" + jobStepExecutionId + ", jobExecutionId=" + jobExecutionId + ", version=" + version + ", name=" + name
        + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", commitCount=" + commitCount + ", readCount=" + readCount
        + ", filterCount=" + filterCount + ", writeCount=" + writeCount + ", readSkipCount=" + readSkipCount + ", writeSkipCount=" + writeSkipCount
        + ", processSkipCount=" + processSkipCount + ", rollbackCount=" + rollbackCount + ", exitCode=" + exitCode + ", exitMessage=" + exitMessage
        + ", lastUpdated=" + lastUpdated + ", executionContext=" + executionContext + "]";
  }


}
