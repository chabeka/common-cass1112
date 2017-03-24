package fr.urssaf.image.commons.spring.batch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-batch-test.xml",
      "/jobs/job-8.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class Job8Test {

   @Autowired
   private JobLauncherTestUtils jobLauncher;

   private JobParameters jobParameters;

   @Before
   public void before() {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(ObjectUtils.toString(UUID
            .randomUUID())));
      jobParameters = new JobParameters(parameters);

   }

   @Test
   public void jobExecution_success() {

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.launchJob(jobParameters);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      Collection<StepExecution> stepExecutions = jobExecution
            .getStepExecutions();

      Assert.assertEquals("le nombre d'étapes exécutés est incorrect", 1,
            stepExecutions.size());

      StepExecution loopStep = (StepExecution) CollectionUtils.get(
            stepExecutions, 0);

      Assert.assertEquals("le nombre d'items lus est incorrect", 10, loopStep
            .getReadCount());

      Assert.assertEquals("la sortie du job est incorrecte", "COMPLETED",
            jobExecution.getExitStatus().getExitCode());

   }

}
