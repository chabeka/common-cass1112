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
@Table(name = "jobinstancesbynamecql")
public class JobInstancesByNameCql {

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

}
