package fr.urssaf.image.commons.spring.batch.support.step;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
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
public class LogTask implements Tasklet, InitializingBean {

   private static final Logger LOG = Logger.getLogger(LogTask.class);

   @Override
   public void afterPropertiesSet() {

      Assert
            .state(StringUtils.isNotBlank(message)
                  | StringUtils.isNotBlank(exception),
                  "les propriétés 'message' ou 'exception' sont requises mais pas les deux!");

   }

   private String message;

   public void setMessage(String message) {
      this.message = message;
   }

   private String exception;

   public void setException(String exception) {
      this.exception = exception;
   }

   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) {

      if (exception != null) {
         LOG.error(exception);
         throw new NestableRuntimeException(exception);
      }

      LOG.debug(message);

      return RepeatStatus.FINISHED;
   }

}
