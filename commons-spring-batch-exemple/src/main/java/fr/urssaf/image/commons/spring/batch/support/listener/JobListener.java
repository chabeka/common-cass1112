package fr.urssaf.image.commons.spring.batch.support.listener;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Component
public class JobListener {

   private static final Logger LOG = Logger.getLogger(JobListener.class);

   @BeforeJob
   public void beforeJob(JobExecution jobExecution) {

      LOG.debug("demarrage du job: " + jobExecution.getJobId());
   }

   @AfterJob
   public void afterJob(JobExecution jobExecution) {

      LOG.debug("fin job: " + jobExecution.getJobId() + " "
            + jobExecution.getExitStatus().getExitCode());
   }
}
