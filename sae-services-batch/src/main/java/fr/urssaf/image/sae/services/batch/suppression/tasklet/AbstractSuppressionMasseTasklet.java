/**
 * 
 */
package fr.urssaf.image.sae.services.batch.suppression.tasklet;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet mère fournissant les méthodes
 * <ul>
 * <li>récupération des erreurs stockées dans le contexte du job</li>
 * </ul>
 * 
 */
public abstract class AbstractSuppressionMasseTasklet implements Tasklet {

   /**
    * {@inheritDoc}
    */
   @Override
   public abstract RepeatStatus execute(StepContribution stepContribution,
         ChunkContext chunkContext)
         throws Exception;

   /**
    * @param chunkContext
    *           le contexte du chunk
    * @return la liste des exceptions des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Exception> getExceptionErreurListe(
         ChunkContext chunkContext) {
      ExecutionContext jobExecution = chunkContext.getStepContext()
            .getStepExecution().getJobExecution().getExecutionContext();
      return (ConcurrentLinkedQueue<Exception>) jobExecution
            .get(Constantes.SUPPRESSION_EXCEPTION);
   }
}
