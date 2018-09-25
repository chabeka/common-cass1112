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
@Table(name = "sequences")
public class SequencesCql {

  @PartitionKey
  @Column(name = "jobidname")
  private String jobIdName;

  private Long value;

  /**
   *
   */
  public SequencesCql() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @return the jobIdName
   */
  public String getJobIdName() {
    return jobIdName;
  }

  /**
   * @param jobIdName
   *          the jobIdName to set
   */
  public void setJobIdName(final String jobIdName) {
    this.jobIdName = jobIdName;
  }

  /**
   * @return the value
   */
  public Long getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(final Long value) {
    this.value = value;
  }

}
