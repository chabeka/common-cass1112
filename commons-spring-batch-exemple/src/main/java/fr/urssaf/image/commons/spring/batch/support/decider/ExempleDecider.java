package fr.urssaf.image.commons.spring.batch.support.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExempleDecider implements JobExecutionDecider, InitializingBean {

   private String decision;

   @Override
   public void afterPropertiesSet() {

      Assert.hasText(decision, "'decision' is required");

   }

   public void setDecision(String decision) {
      this.decision = decision;
   }

   @Override
   public FlowExecutionStatus decide(JobExecution jobExecution,
         StepExecution stepExecution) {

      return new FlowExecutionStatus(decision);

   }

}
