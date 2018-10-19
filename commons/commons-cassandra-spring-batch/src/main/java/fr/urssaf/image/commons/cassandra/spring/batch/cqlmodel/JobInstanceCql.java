/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel;

import org.springframework.batch.core.JobParameters;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "jobinstancecql")
public class JobInstanceCql {

   @PartitionKey
   @Column(name = "jobinstanceid")
   private Long jobInstanceId;

   @Column(name = "jobname")
   private String jobName;

   @Column(name = "jobParameters")
   private JobParameters jobparameters;

   @Column(name = "jobkey")
   private byte[] jobKey;

   private Integer version;

   @Column(name = "reservedby")
   private String reservedBy;

   /**
    * @param jobInstanceId
    * @param jobparameters
    * @param jobKey
    */
   public JobInstanceCql() {
      super();
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
    * @return the jobparameters
    */
   public JobParameters getJobparameters() {
      return jobparameters;
   }

   /**
    * @param jobparameters
    *           the jobparameters to set
    */
   public void setJobparameters(final JobParameters jobparameters) {
      this.jobparameters = jobparameters;
   }

   /**
    * @return the jobKey
    */
   public byte[] getJobKey() {
      return jobKey;
   }

   /**
    * @param jobKey
    *           the jobKey to set
    */
   public void setJobKey(final byte[] jobKey) {
      this.jobKey = jobKey;
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
    * @return the reservedBy
    */
   public String getReservedBy() {
      return reservedBy;
   }

   /**
    * @param reservedBy
    *           the reservedBy to set
    */
   public void setReservedBy(final String reservedBy) {
      this.reservedBy = reservedBy;
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

}
