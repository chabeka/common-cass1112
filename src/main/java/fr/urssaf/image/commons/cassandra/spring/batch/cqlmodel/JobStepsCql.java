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
@Table(name = "jobstepscql")
public class JobStepsCql   implements Serializable, Comparable<JobStepsCql>{

   @PartitionKey
   @Column(name = "jobstepid")
   private Long jobStepId;

   @Column(name = "jobname")
   private String jobName;

   @Column(name = "stepname")
   private String stepName;

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
    * @return the stepName
    */
   public String getStepName() {
      return stepName;
   }

   /**
    * @param stepName
    *           the stepName to set
    */
   public void setStepName(final String stepName) {
      this.stepName = stepName;
   }

	@Override
	public int compareTo(JobStepsCql job) {
		return this.jobStepId.compareTo(job.getJobStepId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		result = prime * result + ((jobStepId == null) ? 0 : jobStepId.hashCode());
		result = prime * result + ((stepName == null) ? 0 : stepName.hashCode());
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
		JobStepsCql other = (JobStepsCql) obj;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		if (jobStepId == null) {
			if (other.jobStepId != null)
				return false;
		} else if (!jobStepId.equals(other.jobStepId))
			return false;
		if (stepName == null) {
			if (other.stepName != null)
				return false;
		} else if (!stepName.equals(other.stepName))
			return false;
		return true;
	}
	
	

}
