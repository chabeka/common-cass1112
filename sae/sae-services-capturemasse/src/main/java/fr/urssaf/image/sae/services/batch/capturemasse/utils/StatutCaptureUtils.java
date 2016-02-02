package fr.urssaf.image.sae.services.batch.capturemasse.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

/**
 * Classe utilitaire pour déterminer le statut réussite/échec d'une capture de
 * masse
 */
public final class StatutCaptureUtils {

   private StatutCaptureUtils() {
      // Constructeur privé
   }

   /**
    * Renvoie true si la capture de masse est considérée comme réussie
    * 
    * @param jobExecution
    *           le JobExecution SpringBatch
    * @return true si la capture de masse est considérée comme réussie, false
    *         dans le cas contraire
    */
   public static boolean isCaptureOk(JobExecution jobExecution) {

      List<StepExecution> list = new ArrayList<StepExecution>(jobExecution
            .getStepExecutions());
      boolean traitementOK = ExitStatus.COMPLETED.equals(jobExecution
            .getExitStatus());

      int index = 0;
      String stepName;
      List<String> failedSteps = Arrays.asList("finBloquant", "finErreur",
            "finErreurVirtuel");
      while (traitementOK && index < list.size()) {
         stepName = list.get(index).getStepName();
         if (failedSteps.contains(stepName)) {
            traitementOK = false;
         }
         index++;
      }

      return traitementOK;

   }

}
