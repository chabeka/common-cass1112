/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

/**
 * Méthodes de base pour les listener
 * 
 */
public abstract class AbstractListener {

   private StepExecution stepExecution;

   /**
    * initialisation de variables
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void beforeStep(final StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      specificInitOperations();
   }

   /**
    * Méthode déclenchée à la fin du step
    * 
    * @param stepExecution le stepExecution
    * @return le status de sortie
    */
   @AfterStep
   public final ExitStatus afterStep(StepExecution stepExecution) {
      this.stepExecution = stepExecution;
      return specificAfterStepOperations();
   }

   /**
    * @return la liste des codes erreurs stockée dans le contexte d'execution du
    *         job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<String> getCodesErreurListe() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.CODE_EXCEPTION);
   }

   /**
    * @return la liste des index des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Integer> getIndexErreurListe() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_EXCEPTION);
   }

   /**
    * @return la liste des exceptions des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Exception> getExceptionErreurListe() {
      ExecutionContext jobExecution = getStepExecution().getJobExecution()
            .getExecutionContext();
      return (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.DOC_EXCEPTION);
   }

   /**
    * Opérations supplémentaires à réaliser lors de l'initialisation du
    * listener. Initialisation du stepExecution déjà réalisée
    */
   protected abstract void specificInitOperations();

   /**
    * Opérations supplémentaires à réaliser lors de la fin du step.
    * 
    * @return le code status de retour
    */
   protected abstract ExitStatus specificAfterStepOperations();

   /**
    * @return le StepExecution
    */
   protected final StepExecution getStepExecution() {
      return stepExecution;
   }
}
