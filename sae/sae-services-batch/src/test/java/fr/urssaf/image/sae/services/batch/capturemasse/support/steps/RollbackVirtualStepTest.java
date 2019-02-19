/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading.InsertionPoolThreadVirtualExecutor;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
      "/applicationContext-sae-services-capturemasse-test-mock-deletion.xml",
      "/applicationContext-sae-services-batch-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class RollbackVirtualStepTest {

  private static final String STEP_NAME = "rollbackVirtuel";

  @Autowired
  private JobLauncherTestUtils launcher;

  @Autowired
  @Qualifier("deletionService")
  private DeletionService deletionService;
  
  @Autowired
  private InsertionPoolThreadVirtualExecutor executor;

  @After
  public void after() throws Exception {

    EasyMock.reset(deletionService);
    
  }

  private JobParameters jobParameters;

  @Before
  public void before() throws Exception {
      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(ObjectUtils.toString(UUID
            .randomUUID())));
      jobParameters = new JobParameters(parameters);

  }
  
  private void setSecurityContext() {
      // initialisation du contexte de sécurité
      final VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      viExtrait.setPagms(Arrays.asList("TU_PAGM1", "TU_PAGM2"));

      final SaeDroits saeDroits = new SaeDroits();
      final List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      final Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      final String[] roles = new String[] { "archivage_masse", "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      saeDroits.put("recherche", saePrmds);
      
      viExtrait.setSaeDroits(saeDroits);
      final AuthenticationToken token = AuthenticationFactory.createAuthentication(
                                                                                   viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
}

  @Test
  public void rollback_success() throws DeletionServiceEx {

	setSecurityContext();
	  
    final UUID refUuid = UUID.randomUUID();

    // Liste des documents intégrés
    final CaptureMasseVirtualDocument doc1 = new CaptureMasseVirtualDocument();
    doc1.setReferenceUUID(refUuid);
    doc1.setUuid(UUID.randomUUID());
    doc1.setIndex(0);
    final CaptureMasseVirtualDocument doc2 = new CaptureMasseVirtualDocument();
    doc2.setReferenceUUID(refUuid);
    doc2.setUuid(UUID.randomUUID());
    doc2.setIndex(0);
    final CaptureMasseVirtualDocument doc3 = new CaptureMasseVirtualDocument();
    doc3.setReferenceUUID(refUuid);
    doc3.setUuid(UUID.randomUUID());
    doc3.setIndex(0);
    executor.getIntegratedDocuments().add(doc1);
    executor.getIntegratedDocuments().add(doc2);
    executor.getIntegratedDocuments().add(doc3);

    deletionService.deleteStorageDocument(EasyMock.anyObject(UUID.class));

    EasyMock.expectLastCall().anyTimes();

    EasyMock.replay(deletionService);

    final ExecutionContext executionContext = new ExecutionContext();
    executionContext.put(Constantes.NB_INTEG_DOCS, 3);
    executionContext.put(Constantes.DOC_COUNT, 3);

    final JobExecution execution = launcher.launchStep(STEP_NAME,
                                                       jobParameters,
                                                       executionContext);

    final StepExecution rollbackStep = (StepExecution) CollectionUtils.get(
                                                                           execution.getStepExecutions(),
                                                                           0);

    Assert.assertEquals("le nom de l'étape est incorrect",
                        STEP_NAME,
                        rollbackStep.getStepName());

    Assert.assertEquals("le nombre d'items lus est inattendu",
                        3,
                        rollbackStep.getReadCount());

    // Assert.assertEquals("le nombre de commit est inattendu", 3,
    // rollbackStep
    // .getCommitCount());

    Assert.assertEquals("le nombre d'items écrit est inattendu",
                        3,
                        rollbackStep.getWriteCount());

    EasyMock.verify(deletionService);

  }

  @Test
  public void rollback_success_integrated_documents_empty()
      throws DeletionServiceEx {

	  setSecurityContext();
    // ConcurrentLinkedQueue<UUID> listIntegDocs = new
    // ConcurrentLinkedQueue<UUID>();

    final ExecutionContext executionContext = new ExecutionContext();
    // executionContext.put(Constantes.INTEG_DOCS, listIntegDocs);
    executionContext.put(Constantes.NB_INTEG_DOCS, 3);
    executionContext.put(Constantes.DOC_COUNT, 3);

    final JobExecution execution = launcher.launchStep(STEP_NAME,
                                                       jobParameters,
                                                       executionContext);

    final StepExecution rollbackStep = (StepExecution) CollectionUtils.get(
                                                                           execution.getStepExecutions(),
                                                                           0);

    Assert.assertEquals("le nom de l'étape est incorrect",
                        STEP_NAME,
                        rollbackStep.getStepName());

    // Assert.assertEquals("le nombre d'items lus est inattendu",
    // listIntegDocs.size(), rollbackStep.getReadCount());
    Assert.assertEquals("le nombre d'items lus est inattendu",
                        0,
                        rollbackStep.getReadCount());

    Assert.assertEquals("le nombre de commit est inattendu",
                        1,
                        rollbackStep
                                    .getCommitCount());

    // Assert.assertEquals("le nombre d'items écrit est inattendu",
    // listIntegDocs.size(), rollbackStep.getWriteCount());
    Assert.assertEquals("le nombre d'items écrit est inattendu",
                        0,
                        rollbackStep.getWriteCount());

  }
  
  @Test
  public void rollback_failure() throws DeletionServiceEx {

    final UUID refUuid = UUID.randomUUID();

    // Liste des documents intégrés
    final CaptureMasseVirtualDocument doc1 = new CaptureMasseVirtualDocument();
    doc1.setReferenceUUID(refUuid);
    doc1.setUuid(UUID.randomUUID());
    doc1.setIndex(0);
    final CaptureMasseVirtualDocument doc2 = new CaptureMasseVirtualDocument();
    doc2.setReferenceUUID(refUuid);
    doc2.setUuid(UUID.randomUUID());
    doc2.setIndex(1);
    final CaptureMasseVirtualDocument doc3 = new CaptureMasseVirtualDocument();
    doc3.setReferenceUUID(refUuid);
    doc3.setUuid(UUID.randomUUID());
    doc3.setIndex(2);
    executor.getIntegratedDocuments().add(doc1);
    executor.getIntegratedDocuments().add(doc2);
    executor.getIntegratedDocuments().add(doc3);

    deletionService.deleteStorageDocument(EasyMock.anyObject(UUID.class));

    final Exception expectedException = new DeletionServiceEx(
                                                              "une exception a lieu dans le rollback");

    EasyMock.expectLastCall().once().andThrow(expectedException);

    EasyMock.expectLastCall().anyTimes();

    EasyMock.replay(deletionService);

    final ExecutionContext executionContext = new ExecutionContext();
    // executionContext.put(Constantes.INTEG_DOCS, listIntegDocs);
    executionContext.put(Constantes.NB_INTEG_DOCS, 3);

    final JobExecution execution = launcher.launchStep(STEP_NAME,
                                                       jobParameters,
                                                       executionContext);

    final StepExecution rollbackStep = (StepExecution) CollectionUtils.get(
                                                                           execution.getStepExecutions(),
                                                                           0);

    Assert.assertEquals("le nom de l'étape est incorrect",
                        STEP_NAME,
                        rollbackStep.getStepName());

    Assert.assertEquals("le nombre d'items lus est inattendu",
                        1,
                        rollbackStep.getReadCount());

    Assert.assertEquals("le nombre de commit est inattendu",
                        2,
                        rollbackStep
                                    .getCommitCount());

    Assert.assertEquals("le nombre d'items écrit est inattendu",
                        1,
                        rollbackStep.getWriteCount());

    EasyMock.verify(deletionService);
    
    executor.getIntegratedDocuments().remove(doc1);
    executor.getIntegratedDocuments().remove(doc2);
    executor.getIntegratedDocuments().remove(doc3);
  }

}
