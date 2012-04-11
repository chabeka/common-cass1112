package fr.urssaf.image.sae.cassandra.dao.model;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class JobModel {

   private UUID idJob;
   private String jobType;
   private String jobParam;
   private String jobState;
   private String serverName;
   private Date dateReceiptRequests;
   private Date launchDate;
   private Date finishDate;

   private boolean running;

   /**
    * @return the idJob
    */
   public UUID getIdJob() {
      return idJob;
   }

   /**
    * @param idJob
    *           the idJob to set
    */
   public void setIdJob(UUID idJob) {
      this.idJob = idJob;
   }

   /**
    * @return the running
    */
   public boolean isRunning() {
      if (!StringUtils.isEmpty(getServerName())) {
         running = true;
      }
      return running;
   }

   /**
    * @param running
    *           the running to set
    */
   public void setRunning(boolean running) {
      this.running = running;
   }

   /**
    * @return the launchDate
    */
   public Date getLaunchDate() {
      return launchDate;
   }

   /**
    * @param launchDate
    *           the launchDate to set
    */
   public void setLaunchDate(Date launchDate) {
      this.launchDate = launchDate;
   }

   /**
    * @return the lastSuccessfullRunDate
    */
   public Date getDateReceiptRequests() {
      return dateReceiptRequests;
   }

   /**
    * @param lastSuccessfullRunDate
    *           the lastSuccessfullRunDate to set
    */
   public void setDateReceiptRequests(Date dateReceiptRequests) {
      this.dateReceiptRequests = dateReceiptRequests;
   }

   /**
    * @return the serverName
    */
   public String getServerName() {
      return serverName;
   }

   /**
    * @param serverName
    *           the serverName to set
    */
   public void setServerName(String serverName) {
      this.serverName = serverName;
   }

   /**
    * @return the jobType
    */
   public String getJobType() {
      return jobType;
   }

   /**
    * @param jobType
    *           the jobType to set
    */
   public void setJobType(String jobType) {
      this.jobType = jobType;
   }

   /**
    * @return the jobParam
    */
   public String getJobParam() {
      return jobParam;
   }

   /**
    * @param jobParam
    *           the jobParam to set
    */
   public void setJobParam(String jobParam) {
      this.jobParam = jobParam;
   }

   /**
    * @return the jobState
    */
   public String getJobState() {
      return jobState;
   }

   /**
    * @param jobState
    *           the jobState to set
    */
   public void setJobState(String jobState) {
      this.jobState = jobState;
   }

   /**
    * @return the finishDate
    */
   public Date getFinishDate() {
      return finishDate;
   }

   /**
    * @param finishDate
    *           the finishDate to set
    */
   public void setFinishDate(Date finishDate) {
      this.finishDate = finishDate;
   }

   
   
}
