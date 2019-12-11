/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import java.io.Serializable;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "jobinstancetojobexecutioncql")
public class JobInstanceToJobExecutionCql implements Serializable, Comparable<JobInstanceToJobExecutionCql>{

   @PartitionKey
   @Column(name = "jobinstanceid")
   private Long jobInstanceId;

   @Column(name = "jobexecutionid")
   private Long jobExecutionId;

   private String value;

   /**
   *
   */
   public JobInstanceToJobExecutionCql() {
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
    * @return the jobInstanceId
    */
   public Long getJobInstanceId() {
      return jobInstanceId;
   }

   /**
    * @param jobInstanceId
    *           the jobInstanceId to set
    */
   public void setJobInstanceId(final Long jobInstanceId) {
      this.jobInstanceId = jobInstanceId;
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
	public int compareTo(JobInstanceToJobExecutionCql job) {
		return (""+this.jobExecutionId + this.jobInstanceId).compareTo(""+job.getJobExecutionId()+job.getJobInstanceId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobExecutionId == null) ? 0 : jobExecutionId.hashCode());
		result = prime * result + ((jobInstanceId == null) ? 0 : jobInstanceId.hashCode());
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
		JobInstanceToJobExecutionCql other = (JobInstanceToJobExecutionCql) obj;
		if (jobExecutionId == null) {
			if (other.jobExecutionId != null)
				return false;
		} else if (!jobExecutionId.equals(other.jobExecutionId))
			return false;
		if (jobInstanceId == null) {
			if (other.jobInstanceId != null)
				return false;
		} else if (!jobInstanceId.equals(other.jobInstanceId))
			return false;
		return true;
	}

}
