/**
 * 
 */
package fr.urssaf.image.sae.utils;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.MapStepExecutionDao;

/**
 * 
 * 
 */
public class SaeStepExecutionDao extends MapStepExecutionDao {

   /**
    * 
    */
   public static final String ERREUR_SAUVEGARDE = "Erreur lors de la sauvegarde";
   
   private String stepToFail;

   /**
    * {@inheritDoc}<br>
    * <b>Cette méthode fonctionnera dans la plupart des cas, sauf quand elle
    * atteindra la step correspondant à la step to fail.</b>
    */
   @Override
   public void saveStepExecution(StepExecution stepExecution) {

      if (stepExecution.getStepName().equals(stepToFail)) {
         throw new Error(ERREUR_SAUVEGARDE);
      } else {
         super.saveStepExecution(stepExecution);
      }
   }

   /**
    * @return the stepToFail
    */
   public final String getStepToFail() {
      return stepToFail;
   }

   /**
    * @param stepToFail
    *           the stepToFail to set
    */
   public final void setStepToFail(String stepToFail) {
      this.stepToFail = stepToFail;
   }

}
