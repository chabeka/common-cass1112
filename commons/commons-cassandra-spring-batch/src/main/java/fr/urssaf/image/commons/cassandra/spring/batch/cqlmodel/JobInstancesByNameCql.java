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
@Table(name = "jobinstancesbynamecql")
public class JobInstancesByNameCql  implements Serializable, Comparable<JobInstancesByNameCql> {

   @PartitionKey
   @Column(name = "jobname")
   private String jobName;

   @Column(name = "jobinstanceid")
   private Long jobInstanceId;

   /**
    * @return the jobName
    */
   public String getJobName() {
      return jobName;
   }

   /**
    * @param jobName
    *           the jobName to set
    */
   public void setJobName(final String jobName) {
      this.jobName = jobName;
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

	@Override
	public int compareTo(JobInstancesByNameCql job) {
		return (this.jobName+this.jobInstanceId).compareTo(job.getJobName()+job.getJobInstanceId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobInstanceId == null) ? 0 : jobInstanceId.hashCode());
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
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
		JobInstancesByNameCql other = (JobInstancesByNameCql) obj;
		if (jobInstanceId == null) {
			if (other.jobInstanceId != null)
				return false;
		} else if (!jobInstanceId.equals(other.jobInstanceId))
			return false;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		return true;
	}

}
