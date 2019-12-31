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
@Table(name = "jobexecutionsrunningcql")
public class JobExecutionsRunningCql   implements Serializable, Comparable<JobExecutionsRunningCql>{

  // ** correspond soit au nom du job, soit au mot clé « _all » */
  // ** qui permet de parcourir les exécutions tout job confondu. */
  @PartitionKey
  @Column(name = "jobname")
  private String jobName;

  @ClusteringColumn
  @Column(name = "jobexecutionid")
  private Long jobExecutionId;

  private String value;

  /**
  *
  */
  public JobExecutionsRunningCql() {
  }

  /**
   * @return the jobExecutionId
   */
  public Long getJobExecutionId() {
    return jobExecutionId;
  }

  /**
   * @param jobExecutionId
   *          the jobExecutionId to set
   */
  public void setJobExecutionId(final Long jobExecutionId) {
    this.jobExecutionId = jobExecutionId;
  }

  /**
   * @return the job name
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * @param jobName
   *          the jobname to set
   */
  public void setJobName(final String jobName) {
    this.jobName = jobName;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(final String value) {
    this.value = value;
  }

	@Override
	public int compareTo(JobExecutionsRunningCql job) {
		return (getJobName()+getJobExecutionId()).compareTo(job.getJobName()+job.getJobExecutionId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobExecutionId == null) ? 0 : jobExecutionId.hashCode());
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobExecutionsRunningCql other = (JobExecutionsRunningCql) obj;
		if (jobExecutionId == null) {
			if (other.jobExecutionId != null)
				return false;
		} else if (!jobExecutionId.equals(other.jobExecutionId))
			return false;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}