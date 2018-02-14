/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.tasklet;

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
 * <li>récupération des index des erreurs stockés dans le contexte du job</li>
 * <li>récupération des codes erreur stockés dans le contexte du job</li>
 * </ul>
 * 
 */
public abstract class AbstractCaptureMasseTasklet implements Tasklet {

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
    * @return la liste des codes erreurs stockée dans le contexte d'execution du
    *         job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<String> getCodesErreurListe(
         ChunkContext chunkContext) {
      ExecutionContext jobExecution = chunkContext.getStepContext()
            .getStepExecution().getJobExecution().getExecutionContext();
      return (ConcurrentLinkedQueue<String>) jobExecution
            .get(Constantes.CODE_EXCEPTION);
   }

   /**
    * @param chunkContext
    *           le contexte du chunk
    * @return la liste des index des erreurs stockée dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Integer> getIndexErreurListe(
         ChunkContext chunkContext) {
      ExecutionContext jobExecution = chunkContext.getStepContext()
            .getStepExecution().getJobExecution().getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_EXCEPTION);
   }

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
            .get(Constantes.DOC_EXCEPTION);
   }

   /**
    * @param chunkContext
    *           le contexte du chunk
    * @return la liste des index des références stockés dans le contexte
    *         d'execution du job
    */
   @SuppressWarnings("unchecked")
   protected final ConcurrentLinkedQueue<Integer> getIndexReferenceListe(
         ChunkContext chunkContext) {
      ExecutionContext jobExecution = chunkContext.getStepContext()
            .getStepExecution().getJobExecution().getExecutionContext();
      return (ConcurrentLinkedQueue<Integer>) jobExecution
            .get(Constantes.INDEX_REF_EXCEPTION);
   }
}
