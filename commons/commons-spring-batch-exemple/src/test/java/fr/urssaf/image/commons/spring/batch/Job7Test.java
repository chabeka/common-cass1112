package fr.urssaf.image.commons.spring.batch;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.SystemUtils;
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
      "/jobs/job-7.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class Job7Test {

   private static final File TMP_FILE;

   static {

      TMP_FILE = SystemUtils.getJavaIoTmpDir();

   }

   @Autowired
   private JobLauncherTestUtils jobLauncher;

   private JobParameters jobParameters;

   @Before
   public void before() {

      String input = "src/test/resources/data/bibliotheque.xml";

      String output1 = FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            "batch-exemple/livres1.txt");
      String output2 = FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            "batch-exemple/livres2.txt");

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("xml.input.location", new JobParameter(input));
      parameters.put("file.output.location", new JobParameter(output1));
      parameters.put("other.file.output.location", new JobParameter(output2));
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

      Assert.assertEquals("le nombre d'étapes exécutés est incorrect", 1,
            stepExecutions.size());

      StepExecution step1 = (StepExecution) CollectionUtils.get(stepExecutions,
            0);

      Assert.assertEquals(STEP_FAILURE_MESSSAGE, "XMLtoDoubleFile", step1
            .getStepName());
      Assert.assertEquals("le nombre d'items lus est incorrect", 10, step1
            .getReadCount());
      Assert.assertEquals("le nombre d'items écrits est incorrect", 10, step1
            .getWriteCount());
      Assert.assertTrue(
            "aucune exception ne doit être levée au cours de cette étape",
            step1.getFailureExceptions().isEmpty());

      Assert.assertEquals("la sortie du job est incorrecte", "COMPLETED",
            jobExecution.getExitStatus().getExitCode());

   }

}
