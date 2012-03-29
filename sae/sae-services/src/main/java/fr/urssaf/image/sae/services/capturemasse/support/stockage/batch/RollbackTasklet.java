/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;

/**
 * Tasklet pour le rollback
 * 
 */
@Component
public class RollbackTasklet implements Tasklet {

   private static final String COUNT_READ = "countRead";

   @Autowired
   private RollbackSupport support;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final ExecutionContext mapStep = chunkContext.getStepContext()
            .getStepExecution().getExecutionContext();
      final Map<String, Object> mapJob = chunkContext.getStepContext()
            .getJobExecutionContext();

      Integer countRead = (Integer) mapStep.get(COUNT_READ);
      final List<UUID> listIntegDocs = (List<UUID>) mapJob
            .get(Constantes.INTEG_DOCS);

      if (countRead == null) {
         countRead = Integer.valueOf(0);
      }

      RepeatStatus status;

      try {
         support.rollback(listIntegDocs.get(countRead.intValue()));

         countRead = countRead + 1;

         mapStep.put(COUNT_READ, countRead);

         if (countRead.intValue() == listIntegDocs.size()) {
            status = RepeatStatus.FINISHED;
         } else {
            status = RepeatStatus.CONTINUABLE;
         }
      
      } catch (CaptureMasseRuntimeException e) {
         CaptureMasseSommaireDocumentException exception = new CaptureMasseSommaireDocumentException(
               0, e);
         mapStep.put(Constantes.DOC_EXCEPTION, exception);

         status = RepeatStatus.FINISHED;
      }

      return status;
   }

}
