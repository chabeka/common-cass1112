package fr.urssaf.image.commons.spring.batch.support.step;

import org.apache.commons.lang.exception.NestableRuntimeException;
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
public class PauseTask implements Tasklet, InitializingBean {

   @Override
   public void afterPropertiesSet() {

      Assert.state(pause >= 10,
            "la pause doit être au moins de 10 millisecondes");

   }

   private long pause;

   public void setPause(long pause) {
      this.pause = pause;
   }

   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) {

      try {
         Thread.sleep(pause);
      } catch (InterruptedException e) {
        throw new NestableRuntimeException(e);
      }

      return RepeatStatus.FINISHED;
   }
}
