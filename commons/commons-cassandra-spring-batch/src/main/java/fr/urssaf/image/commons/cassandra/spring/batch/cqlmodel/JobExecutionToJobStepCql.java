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
@Table(name = "jobexecutiontojobstepcql")
public class JobExecutionToJobStepCql {

   @PartitionKey
   @Column(name = "jobexecutionid")
   private Long jobExecutionId;

   @ClusteringColumn
   @Column(name = "jobstepid")
   private Long jobStepId;

   private String value;

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
    * @return the value
    */
   public String getValue() {
      return value;
   }

   /**
    * @param value
    *           the value to set
    */
   public void setValue(final String value) {
      this.value = value;
   }

}
