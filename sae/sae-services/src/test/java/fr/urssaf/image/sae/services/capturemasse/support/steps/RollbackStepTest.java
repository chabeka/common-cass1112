/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
      "/applicationContext-sae-services-dfce-mock.xml",
      "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class RollbackStepTest {

   private static final String STEP_NAME = "rollback";

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   @Qualifier("deletionService")
   private DeletionService deletionService;

   @After
   public void after() {

      EasyMock.reset(deletionService);
   }

   private JobParameters jobParameters;

   @Before
   public void before() {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(ObjectUtils.toString(UUID
            .randomUUID())));
      jobParameters = new JobParameters(parameters);

   }

   @Test
   public void rollback_success() throws DeletionServiceEx {

      ConcurrentLinkedQueue<UUID> listIntegDocs = new ConcurrentLinkedQueue<UUID>();

      listIntegDocs.add(UUID.randomUUID());
      listIntegDocs.add(UUID.randomUUID());
      listIntegDocs.add(UUID.randomUUID());

      deletionService.deleteStorageDocument(EasyMock.anyObject(UUID.class));

      EasyMock.expectLastCall().times(listIntegDocs.size());

      deletionService.setDeletionServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));

      EasyMock.expectLastCall().times(listIntegDocs.size());

      EasyMock.replay(deletionService);

      ExecutionContext executionContext = new ExecutionContext();
      executionContext.put(Constantes.INTEG_DOCS, listIntegDocs);

      JobExecution execution = launcher.launchStep(STEP_NAME, jobParameters,
            executionContext);

      StepExecution rollbackStep = (StepExecution) CollectionUtils.get(
            execution.getStepExecutions(), 0);

      Assert.assertEquals("le nom de l'étape est incorrect", STEP_NAME,
            rollbackStep.getStepName());

      Assert.assertEquals("le nombre d'items lus est inattendu", listIntegDocs
            .size(), rollbackStep.getReadCount());

      Assert.assertEquals("le nombre de commit est inattendu", listIntegDocs
            .size(), rollbackStep.getCommitCount());

      Assert.assertEquals("le nombre d'items écrit est inattendu",
            listIntegDocs.size(), rollbackStep.getWriteCount());

      EasyMock.verify(deletionService);

   }

   @Test
   public void rollback_success_integrated_documents_empty()
         throws DeletionServiceEx {

      ConcurrentLinkedQueue<UUID> listIntegDocs = new ConcurrentLinkedQueue<UUID>();

      ExecutionContext executionContext = new ExecutionContext();
      executionContext.put(Constantes.INTEG_DOCS, listIntegDocs);

      JobExecution execution = launcher.launchStep(STEP_NAME, jobParameters,
            executionContext);

      StepExecution rollbackStep = (StepExecution) CollectionUtils.get(
            execution.getStepExecutions(), 0);

      Assert.assertEquals("le nom de l'étape est incorrect", STEP_NAME,
            rollbackStep.getStepName());

      Assert.assertEquals("le nombre d'items lus est inattendu", listIntegDocs
            .size(), rollbackStep.getReadCount());

      Assert.assertEquals("le nombre de commit est inattendu", 1, rollbackStep
            .getCommitCount());

      Assert.assertEquals("le nombre d'items écrit est inattendu",
            listIntegDocs.size(), rollbackStep.getWriteCount());

   }

   @Test
   public void rollback_failure() throws DeletionServiceEx {

      ConcurrentLinkedQueue<UUID> listIntegDocs = new ConcurrentLinkedQueue<UUID>();

      listIntegDocs.add(UUID.randomUUID());
      listIntegDocs.add(UUID.randomUUID());
      listIntegDocs.add(UUID.randomUUID());

      deletionService.deleteStorageDocument(EasyMock.anyObject(UUID.class));

      Exception expectedException = new DeletionServiceEx(
            "une exception a lieu dans le rollback");

      EasyMock.expectLastCall().once().andThrow(expectedException);

      deletionService.setDeletionServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));

      EasyMock.expectLastCall().anyTimes();

      EasyMock.replay(deletionService);

      ExecutionContext executionContext = new ExecutionContext();
      executionContext.put(Constantes.INTEG_DOCS, listIntegDocs);

      JobExecution execution = launcher.launchStep(STEP_NAME, jobParameters,
            executionContext);

      StepExecution rollbackStep = (StepExecution) CollectionUtils.get(
            execution.getStepExecutions(), 0);

      Assert.assertEquals("le nom de l'étape est incorrect", STEP_NAME,
            rollbackStep.getStepName());

      Assert.assertEquals("le nombre d'items lus est inattendu", 1,
            rollbackStep.getReadCount());

      Assert.assertEquals("le nombre de commit est inattendu", 2, rollbackStep
            .getCommitCount());

      Assert.assertEquals("le nombre d'items écrit est inattendu", 1,
            rollbackStep.getWriteCount());

      EasyMock.verify(deletionService);
   }

}
