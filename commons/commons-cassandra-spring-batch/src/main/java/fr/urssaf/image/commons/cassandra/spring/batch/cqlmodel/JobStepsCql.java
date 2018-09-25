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
@Table(name = "jobsteps")
public class JobStepsCql {

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
   *          the jobStepId to set
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
   *          the jobName to set
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
   *          the stepName to set
   */
  public void setStepName(final String stepName) {
    this.stepName = stepName;
  }

}
