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
import org.easymock.EasyMock;
import org.junit.After;
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

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.service.ProcessorService;
import fr.urssaf.image.commons.spring.batch.service.ReaderService;
import fr.urssaf.image.commons.spring.batch.service.WriterService;
import fr.urssaf.image.commons.spring.batch.util.LivreEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-batch-test.xml",
      "/jobs/job-6.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class Job6Test {

   @Autowired
   private JobLauncherTestUtils jobLauncher;

   private JobParameters jobParameters;

   @Autowired
   private ProcessorService processorService;

   @Autowired
   private WriterService writerService;

   @Autowired
   private ReaderService readerService;

   private static final File TMP_FILE;

   static {

      TMP_FILE = SystemUtils.getJavaIoTmpDir();

   }

   @Before
   public void before() {

      String output = FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            "batch-exemple/livres_skip.txt");

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(ObjectUtils.toString(UUID
            .randomUUID())));
      parameters.put("file.output.location", new JobParameter(output));
      jobParameters = new JobParameters(parameters);

   }

   @After
   public void after() {

      EasyMock.reset(processorService, writerService, readerService);

   }

   private static final int COUNT_ELEMENTS = 10;

   private static final int COMMIT_INTERVAL = 3;

   private static void assertService(int readExpected, int writeExpected,
         JobExecution jobExecution) {

      Collection<StepExecution> stepExecutions = jobExecution
            .getStepExecutions();

      Assert.assertEquals("le nombre d'étapes exécutés est incorrect", 1,
            stepExecutions.size());

      StepExecution step1 = (StepExecution) CollectionUtils.get(stepExecutions,
            0);

      Assert.assertEquals("le nombre d'items lus est incorrect", readExpected,
            step1.getReadCount());
      Assert.assertEquals("le nombre d'items écrits est incorrect",
            writeExpected, step1.getWriteCount());
      Assert.assertTrue(
            "aucune exception ne doit être levée au cours de cette étape",
            step1.getFailureExceptions().isEmpty());

      Assert.assertEquals("la sortie du job est incorrecte", "COMPLETED",
            jobExecution.getExitStatus().getExitCode());

   }

   @Test
   public void jobExecution_success() {

      readerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS);

      processorService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS);

      writerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS);

      EasyMock.replay(processorService, writerService, readerService);

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.launchJob(jobParameters);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      assertService(COUNT_ELEMENTS, COUNT_ELEMENTS, jobExecution);

      EasyMock.verify(processorService, writerService, readerService);

   }

   @Test
   public void jobExecution_failure_reader() {

      int indexFailure = 5;

      // READER
      readerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(indexFailure).andThrow(
            new NestableRuntimeException("exception levée lors de la lecture"))
            .once().times(COUNT_ELEMENTS - indexFailure - 1);

      // PROCESSOR
      processorService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS - 1);

      // WRITER
      writerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS - 1);

      EasyMock.replay(processorService, writerService, readerService);

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.launchJob(jobParameters);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      assertService(COUNT_ELEMENTS - 1, COUNT_ELEMENTS - 1, jobExecution);

      EasyMock.verify(processorService, writerService, readerService);

   }

   @Test
   public void jobExecution_failure_processor() {

      int indexFailure = 5;

      // READER
      readerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS);

      // PROCESSOR
      processorService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(indexFailure).andThrow(
            new NestableRuntimeException("exception levée lors du processus"))
            .once().times(COUNT_ELEMENTS - indexFailure + 1);

      // WRITER
      writerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS - 1);

      EasyMock.replay(processorService, writerService, readerService);

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.launchJob(jobParameters);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      assertService(COUNT_ELEMENTS, COUNT_ELEMENTS - 1, jobExecution);

      EasyMock.verify(processorService, writerService, readerService);

   }

   @Test
   public void jobExecution_failure_writer() {

      int indexFailure = 5;

      // READER
      readerService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS);

      // PROCESSOR
      processorService.traitement(EasyMock.anyObject(Livre.class));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS + COMMIT_INTERVAL);

      // WRITER

      writerService.traitement(LivreEquals.compareTo(indexFailure));

      EasyMock.expectLastCall().andThrow(
            new NestableRuntimeException("exception levée lors de l'écriture"))
            .times(2);

      writerService.traitement(EasyMock
            .not(LivreEquals.compareTo(indexFailure)));

      EasyMock.expectLastCall().times(COUNT_ELEMENTS);

      EasyMock.replay(processorService, writerService, readerService);

      JobExecution jobExecution;
      try {
         jobExecution = jobLauncher.launchJob(jobParameters);
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      assertService(COUNT_ELEMENTS, COUNT_ELEMENTS - 1, jobExecution);

      EasyMock.verify(processorService, writerService, readerService);

   }

}
