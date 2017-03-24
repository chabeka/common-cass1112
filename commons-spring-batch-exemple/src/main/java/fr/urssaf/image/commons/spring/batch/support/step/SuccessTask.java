package fr.urssaf.image.commons.spring.batch.support.step;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class SuccessTask implements Tasklet {

   private static final Logger LOG = Logger.getLogger(SuccessTask.class);

   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) {

      LOG.debug("l'étape est un succès !");

      return RepeatStatus.FINISHED;
   }

}
