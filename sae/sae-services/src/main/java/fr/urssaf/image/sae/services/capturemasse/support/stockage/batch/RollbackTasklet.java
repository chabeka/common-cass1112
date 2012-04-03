/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;

/**
 * Tasklet pour le rollback
 * 
 */
@Component
public class RollbackTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackTasklet.class);

   private static final String COUNT_READ = "countRead";

   private static final String TRC_ROLLBACK = "rollbacktasklet()";

   @Autowired
   private RollbackSupport support;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings( { "unchecked", "PMD.AvoidThrowingRawExceptionTypes" })
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) {

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

         UUID strDocumentID = listIntegDocs.get(countRead.intValue());

         support.rollback(strDocumentID);

         countRead = countRead + 1;

         LOGGER.debug("{} - Rollback du document #{}/{} ({})", new Object[] {
               TRC_ROLLBACK, countRead, listIntegDocs.size(), strDocumentID });

         mapStep.put(COUNT_READ, countRead);

         if (countRead.intValue() == listIntegDocs.size()) {
            status = RepeatStatus.FINISHED;
         } else {
            status = RepeatStatus.CONTINUABLE;
         }

      } catch (Exception e) {

         String idTraitement = (String) chunkContext.getStepContext()
               .getJobParameters().get(Constantes.ID_TRAITEMENT);

         String errorMessage = MessageFormat.format(
               "{0} - Une exception a été levée lors du rollback : {1}",
               TRC_ROLLBACK, idTraitement);

         LOGGER.error(errorMessage, e.getCause());

         LOGGER
               .error(

                     "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                     idTraitement);

         status = RepeatStatus.FINISHED;
      }

      return status;
   }

}
