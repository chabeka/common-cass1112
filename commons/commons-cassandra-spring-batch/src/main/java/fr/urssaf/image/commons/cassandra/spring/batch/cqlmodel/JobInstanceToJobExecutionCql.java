/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "jobinstancetojobexecution")
public class JobInstanceToJobExecutionCql {

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
   *          the jobExecutionId to set
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
   *          the jobInstanceId to set
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
   *          the value to set
   */
  public void setValue(final String value) {
    this.value = value;
  }

}
