/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

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
public class JobStepCql {

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

}
