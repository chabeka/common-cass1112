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
public class SequencesCql implements Comparable<SequencesCql> {

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



  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (jobIdName == null ? 0 : jobIdName.hashCode());
    result = prime * result + (value == null ? 0 : value.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SequencesCql other = (SequencesCql) obj;
    if (jobIdName == null) {
      if (other.jobIdName != null) {
        return false;
      }
    } else if (!jobIdName.equals(other.jobIdName)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final SequencesCql o) {

    return getJobIdName().compareTo(o.getJobIdName());
  }

}
