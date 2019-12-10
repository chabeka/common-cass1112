package fr.urssaf.image.sae.trace.model;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;


@Table(name = "jobexecutioncql")
public class JobExecutionCqlForMig implements Serializable, Comparable<JobExecutionCqlForMig>{

   // Colonnes de JobExecution

   @PartitionKey
   @Column(name = "jobexecutionid")
   private Long jobExecutionId;

   private Long jobInstanceId;

   private String jobName;

   private Date creationTime;

   private ByteBuffer executionContext;
   
   private Integer version;

   private Date startTime;

   private Date endTime;

   private BatchStatus status;

   private String exitCode;

   private String exitMessage;

   private Date lastUpdated;

   /**
   *
   */
   public JobExecutionCqlForMig() {
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
    * @return the creationTime
    */
   public Date getCreationTime() {
      return creationTime;
   }

   /**
    * @param creationTime
    *           the creationTime to set
    */
   public void setCreationTime(final Date creationTime) {
      this.creationTime = creationTime;
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

	public ByteBuffer getExecutionContext() {
		return executionContext;
	}
	
	public void setExecutionContext(ByteBuffer executionContext) {
		this.executionContext = executionContext;
	}

	@Override
	public int compareTo(JobExecutionCqlForMig job) {
		return getJobExecutionId().compareTo(job.getJobExecutionId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((executionContext == null) ? 0 : executionContext.hashCode());
		result = prime * result + ((exitCode == null) ? 0 : exitCode.hashCode());
		result = prime * result + ((exitMessage == null) ? 0 : exitMessage.hashCode());
		result = prime * result + ((jobExecutionId == null) ? 0 : jobExecutionId.hashCode());
		result = prime * result + ((jobInstanceId == null) ? 0 : jobInstanceId.hashCode());
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		JobExecutionCqlForMig other = (JobExecutionCqlForMig) obj;
		if (creationTime == null) {
			if (other.creationTime != null)
				return false;
		} else if (!creationTime.equals(other.creationTime))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (executionContext == null) {
			if (other.executionContext != null)
				return false;
		} else if (!executionContext.equals(other.executionContext))
			return false;
		if (exitCode == null) {
			if (other.exitCode != null)
				return false;
		} else if (!exitCode.equals(other.exitCode))
			return false;
		if (exitMessage == null) {
			if (other.exitMessage != null)
				return false;
		} else if (!exitMessage.equals(other.exitMessage))
			return false;
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
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (status != other.status)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	

}

