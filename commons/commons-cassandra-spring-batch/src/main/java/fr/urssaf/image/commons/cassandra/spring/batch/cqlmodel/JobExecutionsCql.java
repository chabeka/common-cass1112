/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "jobexecutions")
public class JobExecutionsCql {

  /**
   * correspond soit au nom du job
   */
  @PartitionKey(0)
  @Column(name = "jobname")
  private String jobName;

  @ClusteringColumn
  @Column(name = "jobexecutionid")
  private Long jobExecutionId;

  private String value;

  /**
   *
   */
  public JobExecutionsCql() {
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
   * @return the firstkey
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * @param firstkey
   *          the firstkey to set
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

}
