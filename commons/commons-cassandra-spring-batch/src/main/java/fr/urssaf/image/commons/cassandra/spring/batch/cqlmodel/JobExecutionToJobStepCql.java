/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import java.io.Serializable;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "jobexecutiontojobstepcql")
public class JobExecutionToJobStepCql  implements Serializable, Comparable<JobExecutionToJobStepCql>{

  @PartitionKey
  @Column(name = "jobexecutionid")
  private Long jobExecutionId;

  @ClusteringColumn
  @Column(name = "jobstepid")
  private Long jobStepId;

  private String value;

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
   * @return the jobStepId
   */
  public Long getJobStepId() {
    return jobStepId;
  }

  /**
   * @param jobStepId
   *           the jobStepId to set
   */
  public void setJobStepId(final Long jobStepId) {
    this.jobStepId = jobStepId;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value
   *           the value to set
   */
  public void setValue(final String value) {
    this.value = value;
  }

  @Override
  public int compareTo(final JobExecutionToJobStepCql job) {
    return ("" + jobExecutionId + jobStepId).compareTo("" + job.getJobExecutionId() + job.getJobStepId());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (jobExecutionId == null ? 0 : jobExecutionId.hashCode());
    result = prime * result + (jobStepId == null ? 0 : jobStepId.hashCode());
    result = prime * result + (value == null ? 0 : value.hashCode());
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
    final JobExecutionToJobStepCql other = (JobExecutionToJobStepCql) obj;
    if (jobExecutionId == null) {
      if (other.jobExecutionId != null) {
        return false;
      }
    } else if (!jobExecutionId.equals(other.jobExecutionId)) {
      return false;
    }
    if (jobStepId == null) {
      if (other.jobStepId != null) {
        return false;
      }
    } else if (!jobStepId.equals(other.jobStepId)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

}
