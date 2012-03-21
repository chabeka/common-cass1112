package fr.urssaf.image.sae.services.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoopTask implements Tasklet, InitializingBean {

   @Override
   public void afterPropertiesSet() {

      Assert.state(count > 0,
            "la propriété 'count' doit étre égal au moins à 1");

   }

   private int count;

   public void setCount(int count) {
      this.count = count;
   }

   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) {

      RepeatStatus repeatStatus;

      int chunckCount = chunkContext.getStepContext().getStepExecution()
            .getReadCount();

      if (chunckCount < count) {

         repeatStatus = RepeatStatus.CONTINUABLE;

         chunkContext.getStepContext().getStepExecution().setReadCount(
               ++chunckCount);

      } else {

         repeatStatus = RepeatStatus.FINISHED;

      }

      return repeatStatus;
   }

}
