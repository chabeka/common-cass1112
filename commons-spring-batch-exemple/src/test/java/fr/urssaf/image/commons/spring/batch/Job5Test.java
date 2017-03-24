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
      "/jobs/job-5.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class Job5Test {

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

   private static final String STEP_FAILURE_MESSSAGE = "le nom de l'étape est incorrect";

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

      Assert.assertEquals("le nombre d'étapes exécutés est incorrect", 5,
            stepExecutions.size());

      // StepExecution stepA = (StepExecution)
      // CollectionUtils.get(stepExecutions,
      // 0);
      //
      // Assert.assertEquals(STEP_FAILURE_MESSSAGE, "stepA",
      // stepA.getStepName());
      //
      // StepExecution stepB = (StepExecution)
      // CollectionUtils.get(stepExecutions,
      // 1);
      //
      // Assert.assertEquals(STEP_FAILURE_MESSSAGE, "stepB",
      // stepB.getStepName());
      //
      // StepExecution stepC = (StepExecution)
      // CollectionUtils.get(stepExecutions,
      // 2);
      //
      // Assert.assertEquals(STEP_FAILURE_MESSSAGE, "stepC",
      // stepC.getStepName());
      //
      // StepExecution stepD = (StepExecution)
      // CollectionUtils.get(stepExecutions,
      // 3);
      //
      // Assert.assertEquals(STEP_FAILURE_MESSSAGE, "stepD",
      // stepD.getStepName());

      StepExecution stepE = (StepExecution) CollectionUtils.get(stepExecutions,
            4);

      Assert.assertEquals(STEP_FAILURE_MESSSAGE, "stepE", stepE.getStepName());

      Assert.assertEquals("la sortie du job est incorrecte", "COMPLETED",
            jobExecution.getExitStatus().getExitCode());

   }

}
