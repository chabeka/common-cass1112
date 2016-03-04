package fr.urssaf.image.sae.services.batch.restore.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * Tasklet bidon qui sera supprimé lors de l'implementation du service de restore.
 *
 */
@Component
public class EmptyTasklet implements Tasklet {
   
   private static final Logger LOGGER = LoggerFactory
         .getLogger(EmptyTasklet.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      
      LOGGER.info("execution du step {} pour le job {}", new String[] { chunkContext.getStepContext().getStepName(), chunkContext.getStepContext().getJobName() });
      return RepeatStatus.FINISHED;
   }

}
