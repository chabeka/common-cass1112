package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.multithreading;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.StorageDocumentWriter;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionIdGedExistantEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml",
      "/applicationContext-sae-services-batch-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class InsertionPoolThreadExecutorTest {

  private InsertionCapturePoolThreadExecutor poolExecutor;

  @Autowired
  private InterruptionTraitementMasseSupport interruptionSupport;

  @Autowired
  @Qualifier("storageDocumentService")
  private StorageDocumentService storageDocumentService;

  @Autowired
  private StorageDocumentWriter writer;

  private InterruptionTraitementConfig interruptionConfig;

  private InsertionRunnable insertionRunnable;

  private static final StorageDocument DOCUMENT;

  static {

    DOCUMENT = new StorageDocument();
    DOCUMENT.setFilePath("path");
    DOCUMENT.setUuid(UUID.randomUUID());

  }

  @Before
  public void before() {

    interruptionConfig = new InterruptionTraitementConfig();
    interruptionConfig.setDelay(120);
    interruptionConfig.setStart("02:00:00");
    interruptionConfig.setTentatives(120);

    final InsertionPoolConfiguration poolConfiguration = new InsertionPoolConfiguration();
    poolConfiguration.setCorePoolSize(20);

    poolExecutor = new InsertionCapturePoolThreadExecutor(poolConfiguration,
                                                          interruptionSupport,
                                                          interruptionConfig);

    insertionRunnable = new InsertionRunnable(0, DOCUMENT, writer, 0);
  }

  @After
  public void after() {

    EasyMock.reset(storageDocumentService);
    EasyMock.reset(interruptionSupport);

  }

  @Test
  public void execute_success() throws InsertionServiceEx,
      InsertionIdGedExistantEx {

    final int count = 1000;

    // des insertions sont programmées
    EasyMock
            .expect(
                    storageDocumentService.insertStorageDocument(EasyMock
                                                                         .anyObject(StorageDocument.class)))
            .andReturn(DOCUMENT)
            .times(count);

    try {
      interruptionSupport.verifyInterruptedProcess(interruptionConfig);
      EasyMock.expectLastCall().times(count);
    }
    catch (final InterruptionTraitementException e) {
      Assert.fail("L'exception ne doit pas être levé ici");
    }

    EasyMock.replay(interruptionSupport);
    EasyMock.replay(storageDocumentService);

    for (int i = 0; i < count; i++) {
      poolExecutor.execute(insertionRunnable);
    }

    // nécessaire pour le bon déroulement du test
    poolExecutor.shutdown();
    poolExecutor.waitFinishInsertion();

    final InsertionMasseRuntimeException executeException = poolExecutor
                                                                        .getInsertionMasseException();
    Assert.assertNull("aucune exception de type " + executeException
        + " n'est attendue", poolExecutor.getInsertionMasseException());

    Assert.assertEquals("le nombre d'insertion de document est inattendu",
                        count,
                        poolExecutor.getIntegratedDocuments().size());

    EasyMock.verify(interruptionSupport);
    EasyMock.verify(storageDocumentService);

  }

  @Test
  public void execute_success_interruption() throws InsertionServiceEx,
      InterruptionTraitementException, InsertionIdGedExistantEx {

    final int count = 1000;

    try {
      interruptionSupport.verifyInterruptedProcess(interruptionConfig);
      EasyMock.expectLastCall().times(count);
    }
    catch (final InterruptionTraitementException e) {
      Assert.fail("L'exception ne doit pas être levé ici");
    }

    // des insertions sont programmées
    EasyMock
            .expect(
                    storageDocumentService.insertStorageDocument(EasyMock
                                                                         .anyObject(StorageDocument.class)))
            .andReturn(DOCUMENT)
            .times(count);

    EasyMock.replay(interruptionSupport);
    EasyMock.replay(storageDocumentService);

    for (int i = 0; i < count; i++) {
      poolExecutor.execute(insertionRunnable);
      // Recalcul l'UUID du document
      DOCUMENT.setUuid(UUID.randomUUID());
    }

    // nécessaire pour le bon déroulement du test
    poolExecutor.shutdown();
    poolExecutor.waitFinishInsertion();

    final InsertionMasseRuntimeException executeException = poolExecutor
                                                                        .getInsertionMasseException();
    Assert.assertNull("aucune exception de type " + executeException
        + " n'est attendue", poolExecutor.getInsertionMasseException());

    EasyMock.verify(interruptionSupport);
    EasyMock.verify(storageDocumentService);

  }

  @Test
  public void execute_failure_interruption()
      throws InterruptionTraitementException, InsertionServiceEx,
      InsertionIdGedExistantEx {

    final int count = 1000;

    // l'interruption échoue
    final InterruptionTraitementException interruptionException = EasyMock
                                                                          .createMockBuilder(InterruptionTraitementException.class)
                                                                          .withConstructor(InterruptionTraitementConfig.class,
                                                                                           Throwable.class)
                                                                          .withArgs(interruptionConfig, new ConnectionServiceEx())
                                                                          .createMock();
    try {
      interruptionSupport.verifyInterruptedProcess(interruptionConfig);
      EasyMock.expectLastCall().andThrow(interruptionException).times(1, count);
    }
    catch (final InterruptionTraitementException e) {
      Assert.fail("L'exception ne doit pas être levé ici");
    }

    // des insertions sont programmées
    EasyMock
            .expect(
                    storageDocumentService.insertStorageDocument(EasyMock
                                                                         .anyObject(StorageDocument.class)))
            .andReturn(DOCUMENT)
            .times(0, count - 1);

    EasyMock.replay(interruptionSupport);
    EasyMock.replay(storageDocumentService);

    for (int i = 0; i < count; i++) {
      poolExecutor.execute(insertionRunnable);
    }

    // nécessaire pour le bon déroulement du test
    poolExecutor.shutdown();
    poolExecutor.waitFinishInsertion();

    final InsertionMasseRuntimeException executeException = poolExecutor
                                                                        .getInsertionMasseException();
    Assert.assertTrue(
                      "une exception de type " + InterruptionTraitementException.class
                          + " est attendue",
                      executeException.getCause() instanceof InterruptionTraitementException);

    EasyMock.verify(interruptionSupport);
    EasyMock.verify(storageDocumentService);

  }

  @Test
  public void execute_failure_insertion()
      throws InterruptionTraitementException, InsertionServiceEx,
      InsertionIdGedExistantEx {

    final int count = 1000;

    try {
      interruptionSupport.verifyInterruptedProcess(interruptionConfig);
      EasyMock.expectLastCall().times(1, count);
    }
    catch (final InterruptionTraitementException e) {
      Assert.fail("L'exception ne doit pas être levé ici");
    }

    // une des insertions échoue

    EasyMock
            .expect(
                    storageDocumentService.insertStorageDocument(EasyMock
                                                                         .anyObject(StorageDocument.class)))
            .andThrow(new InsertionServiceEx("Exception volontaire pour test d'execution en echec."))
            .andReturn(DOCUMENT)
            .times(0, count - 1);

    EasyMock.replay(interruptionSupport);
    EasyMock.replay(storageDocumentService);

    for (int i = 0; i < count; i++) {
      poolExecutor.execute(insertionRunnable);
    }

    // nécessaire pour le bon déroulement du test
    poolExecutor.shutdown();
    poolExecutor.waitFinishInsertion();

    final InsertionMasseRuntimeException executeException = poolExecutor
                                                                        .getInsertionMasseException();
    Assert.assertTrue("une exception de type "
        + InterruptionTraitementException.class + " est attendue",
                      executeException.getCause() instanceof InsertionServiceEx);

    EasyMock.verify(interruptionSupport);
    EasyMock.verify(storageDocumentService);
  }

}
