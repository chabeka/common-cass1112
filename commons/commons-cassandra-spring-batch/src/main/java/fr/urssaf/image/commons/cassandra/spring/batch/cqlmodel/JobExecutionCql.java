/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe créée pour corriger un problème de déserialisation
 * de la colonne executionContext veant de la table thrift
 */
@Table(name = "jobexecutioncql")
public class JobExecutionCql {

  // Colonnes de JobExecution

  @PartitionKey
  @Column(name = "jobexecutionid")
  private Long jobExecutionId;

  private Long jobInstanceId;

  private String jobName;

  private Date creationTime;

  private ExecutionContext executionContext = new ExecutionContext();

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
  public JobExecutionCql() {
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
