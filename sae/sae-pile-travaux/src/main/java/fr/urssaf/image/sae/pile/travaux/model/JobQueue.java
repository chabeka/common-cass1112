package fr.urssaf.image.sae.pile.travaux.model;

import java.util.Map;
import java.util.UUID;

/**
 * Propriétés principales des traitements dans la pile des travaux. Les
 * propriétés sont :
 * <ul>
 * <li><code>idJob</code>: identifiant unique du traitement</li>
 * <li><code>type</code>: type de traitement</li>
 * <li><code>parameters</code>: paramètres du traitement</li>
 * </ul>
 * 
 * 
 */
public class JobQueue {

   private UUID idJob;

   private String type;

   @Deprecated
   private String parameters;
   
   private Map<String, String> jobParameters;

   /**
    * @return the idJob
    */
   public final UUID getIdJob() {
      return idJob;
   }

   /**
    * @param idJob
    *           the idJob to set
    */
   public final void setIdJob(UUID idJob) {
      this.idJob = idJob;
   }

   /**
    * @return the type
    */
   public final String getType() {
      return type;
   }

   /**
    * @param type
    *           the type to set
    */
   public final void setType(String type) {
      this.type = type;
   }

   /**
    * @return the parameters
    */
   @Deprecated
   public final String getParameters() {
      return parameters;
   }

   /**
    * @param parameters
    *           the parameters to set
    */
   @Deprecated
   public final void setParameters(String parameters) {
      this.parameters = parameters;
   }

   public Map<String, String> getJobParameters() {
      return jobParameters;
   }

   public void setJobParameters(Map<String, String> jobParameters) {
      this.jobParameters = jobParameters;
   }

}
