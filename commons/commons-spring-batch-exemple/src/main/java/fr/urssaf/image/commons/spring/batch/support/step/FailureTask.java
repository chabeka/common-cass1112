package fr.urssaf.image.commons.spring.batch.support.step;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FailureTask implements Tasklet {

   private static final Logger LOG = Logger.getLogger(FailureTask.class);

   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) {

      LOG.error("l'étape est un échec !");

      return RepeatStatus.FINISHED;
   }

}
