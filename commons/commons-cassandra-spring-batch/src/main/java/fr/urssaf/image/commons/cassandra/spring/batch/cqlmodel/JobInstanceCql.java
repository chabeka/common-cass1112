/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.batch.core.JobParameters;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "jobinstancecql")
public class JobInstanceCql  implements Serializable, Comparable<JobInstanceCql> {

  @PartitionKey
  @Column(name = "jobinstanceid")
  private Long jobInstanceId;

  @Column(name = "jobname")
  private String jobName;

  @Column(name = "jobParameters")
  private JobParameters jobparameters;

  @Column(name = "jobkey")
  private byte[] jobKey;

  private Integer version;

  @Column(name = "reservedby")
  private String reservedBy;

  /**
   * Constructeur
   */
  public JobInstanceCql() {
    super();
  }

  /**
   * @return the jobInstanceId
   */
  public Long getJobInstanceId() {
    return jobInstanceId;
  }

  /**
   * @param jobInstanceId
   *          the jobInstanceId to set
   */
  public void setJobInstanceId(final Long jobInstanceId) {
    this.jobInstanceId = jobInstanceId;
  }

  /**
   * @return the jobparameters
   */
  public JobParameters getJobparameters() {
    return jobparameters;
  }

  /**
   * @param jobparameters
   *          the jobparameters to set
   */
  public void setJobparameters(final JobParameters jobparameters) {
    this.jobparameters = jobparameters;
  }

  /**
   * @return the jobKey
   */
  public byte[] getJobKey() {
    return jobKey;
  }

  /**
   * @param jobKey
   *          the jobKey to set
   */
  public void setJobKey(final byte[] jobKey) {
    this.jobKey = jobKey;
  }

  /**
   * @return the version
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * @param version
   *          the version to set
   */
  public void setVersion(final Integer version) {
    this.version = version;
  }

  /**
   * @return the reservedBy
   */
  public String getReservedBy() {
    return reservedBy;
  }

  /**
   * @param reservedBy
   *          the reservedBy to set
   */
  public void setReservedBy(final String reservedBy) {
    this.reservedBy = reservedBy;
  }

  /**
   * @return the jobName
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * @param jobName
   *          the jobName to set
   */
  public void setJobName(final String jobName) {
    this.jobName = jobName;
  }

  @Override
  public int compareTo(final JobInstanceCql job) {
    return jobInstanceId.compareTo(job.getJobInstanceId());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (jobInstanceId == null ? 0 : jobInstanceId.hashCode());
    result = prime * result + Arrays.hashCode(jobKey);
    result = prime * result + (jobName == null ? 0 : jobName.hashCode());
    result = prime * result + (jobparameters == null ? 0 : jobparameters.hashCode());
    result = prime * result + (version == null ? 0 : version.hashCode());
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
    final JobInstanceCql other = (JobInstanceCql) obj;
    if (jobInstanceId == null) {
      if (other.jobInstanceId != null) {
        return false;
      }
    } else if (!jobInstanceId.equals(other.jobInstanceId)) {
      return false;
    }
    if (!Arrays.equals(jobKey, other.jobKey)) {
      return false;
    }
    if (jobName == null) {
      if (other.jobName != null) {
        return false;
      }
    } else if (!jobName.equals(other.jobName)) {
      return false;
    }
    if (jobparameters == null) {
      if (other.jobparameters != null) {
        return false;
      }
    } else if (!jobparameters.equals(other.jobparameters)) {
      return false;
    }
    if (version == null) {
      if (other.version != null) {
        return false;
      }
    } else if (!version.equals(other.version)) {
      return false;
    }
    return true;
  }


}
