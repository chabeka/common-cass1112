/**
 * 
 */
package fr.urssaf.image.sae.utils;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.dao.MapJobExecutionDao;

/**
 * 
 * 
 */
public class SaeJobExecutionDao extends MapJobExecutionDao {

   /**
    * Etapes disponibles
    */
   public enum StepAvailable {
      BEFORE, AFTER
   };

   public static final String ERREUR_LEVEE = "erreur de jobExecutionDao";

   private StepAvailable stepToFail;

   private int index = 0;

   /**
    * {@inheritDoc}<br>
    * <b>Cette méthode fonctionnera dans la plupart des cas, sauf quand elle
    * atteindra la step correspondant à la step to fail.</b>
    */
   @Override
   public final void saveJobExecution(JobExecution jobExecution) {

      if (stepToFail != null && StepAvailable.BEFORE.equals(stepToFail)) {
         throw new RuntimeException("erreur de jobExecutionDao");
      } else {
         super.saveJobExecution(jobExecution);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateJobExecution(JobExecution jobExecution) {
      if (stepToFail != null && StepAvailable.AFTER.equals(stepToFail)
            && index == 1) {
         throw new RuntimeException("erreur de jobExecutionDao");
      } else {
         index++;
         super.updateJobExecution(jobExecution);
      }
   }

   /**
    * @param stepToFail
    *           the stepToFail to set
    */
   public final void setStepToFail(StepAvailable stepToFail) {
      this.stepToFail = stepToFail;
   }

}
