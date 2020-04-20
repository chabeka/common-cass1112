/**
 * 
 */
package fr.urssaf.image.sae.jobs.executable.model;

/**
 * Classe de configuration des traitements concernant les jobs
 * 
 */
public class JobsConfiguration {

   /**
    * Durée de conservation des jobs (en jours)
    */
   private int jobsDureeConservation;

   /**
    * @return the jobsDureeConservation
    */
   public final int getJobsDureeConservation() {
      return jobsDureeConservation;
   }

   /**
    * @param jobsDureeConservation the jobsDureeConservation to set
    */
   public final void setJobsDureeConservation(int jobsDureeConservation) {
      this.jobsDureeConservation = jobsDureeConservation;
   }
   
   
}
