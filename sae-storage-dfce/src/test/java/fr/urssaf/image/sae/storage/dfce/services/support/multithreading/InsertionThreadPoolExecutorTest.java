package fr.urssaf.image.sae.storage.dfce.services.support.multithreading;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InsertionMasseRuntimeException;
import fr.urssaf.image.sae.storage.dfce.services.support.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.storage.dfce.services.support.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/appliContext-sae-storage-dfce-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class InsertionThreadPoolExecutorTest {

   @Autowired
   private InterruptionTraitementSupport interruptionSupport;

   @Autowired
   private InsertionService insertionService;

   private InterruptionTraitementConfig interruptionConfig;

   private InsertionThreadPoolExecutor poolExecutor;

   private static final StorageDocument DOCUMENT;

   private InsertionRunnable insertionRunnable;

   private JmxIndicator jmxIndicator;

   static {

      DOCUMENT = new StorageDocument();

   }

   @Before
   public void before() {

      interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(120);
      interruptionConfig.setStart("02:00:00");
      interruptionConfig.setTentatives(120);

      jmxIndicator = new JmxIndicator();

      poolExecutor = new InsertionThreadPoolExecutor(interruptionSupport,
            interruptionConfig, jmxIndicator);

      insertionRunnable = new InsertionRunnable(0, DOCUMENT, insertionService);

   }

   @After
   public void after() {

      EasyMock.reset(insertionService);
      EasyMock.reset(interruptionSupport);

   }

   @Test
   public void execute_success() throws InsertionServiceEx,
         InterruptionTraitementException {

      int count = 1000;

      jmxIndicator.setJmxCountDocument(count);

      jmxIndicator.setJmxCountDocument(count);

      // aucune interruption n'est programmée
      EasyMock.expect(
            interruptionSupport.hasInterrupted(EasyMock
                  .anyObject(DateTime.class), EasyMock
                  .anyObject(InterruptionTraitementConfig.class))).andReturn(
            false).times(count);

      // des insertions sont programmées
      EasyMock.expect(
            insertionService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andReturn(DOCUMENT)
            .times(count);

      EasyMock.replay(interruptionSupport);
      EasyMock.replay(insertionService);

      for (int i = 0; i < count; i++) {
         poolExecutor.execute(insertionRunnable);
      }

      // nécessaire pour le bon déroulement du test
      poolExecutor.waitFinishInsertion();

      InsertionMasseRuntimeException executeException = poolExecutor
            .getInsertionMasseException();
      Assert.assertNull("aucune exception de type " + executeException
            + " n'est attendue", poolExecutor.getInsertionMasseException());

      EasyMock.verify(interruptionSupport);
      EasyMock.verify(insertionService);

   }

   @Test
   public void execute_success_interruption()
         throws InterruptionTraitementException, InsertionServiceEx {

      int count = 1000;

      jmxIndicator.setJmxCountDocument(count);

      // une interruption est programmée
      EasyMock.expect(
            interruptionSupport.hasInterrupted(EasyMock
                  .anyObject(DateTime.class), EasyMock
                  .anyObject(InterruptionTraitementConfig.class))).andReturn(
            true).times(count);

      interruptionSupport.interruption(EasyMock.anyObject(DateTime.class),
            EasyMock.anyObject(InterruptionTraitementConfig.class), EasyMock
                  .anyObject(JmxIndicator.class));

      // des insertions sont programmées
      EasyMock.expect(
            insertionService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andReturn(DOCUMENT)
            .times(count);

      EasyMock.replay(interruptionSupport);
      EasyMock.replay(insertionService);

      for (int i = 0; i < count; i++) {
         poolExecutor.execute(insertionRunnable);
      }

      // nécessaire pour le bon déroulement du test
      poolExecutor.waitFinishInsertion();

      InsertionMasseRuntimeException executeException = poolExecutor
            .getInsertionMasseException();
      Assert.assertNull("aucune exception de type " + executeException
            + " n'est attendue", poolExecutor.getInsertionMasseException());

      EasyMock.verify(interruptionSupport);
      EasyMock.verify(insertionService);

   }

   @Test
   public void execute_failure_interruption()
         throws InterruptionTraitementException, InsertionServiceEx {

      int count = 1000;

      jmxIndicator.setJmxCountDocument(count);

      // une interruption est programmée
      EasyMock.expect(
            interruptionSupport.hasInterrupted(EasyMock
                  .anyObject(DateTime.class), EasyMock
                  .anyObject(InterruptionTraitementConfig.class))).andReturn(
            true).times(1, count);

      // l'interruption échoue
      InterruptionTraitementException interruptionException = EasyMock
            .createMockBuilder(InterruptionTraitementException.class)
            .withConstructor(InterruptionTraitementConfig.class,
                  Throwable.class).withArgs(interruptionConfig,
                  new ConnectionServiceEx()).createMock();

      interruptionSupport.interruption(EasyMock.anyObject(DateTime.class),
            EasyMock.anyObject(InterruptionTraitementConfig.class), EasyMock
                  .anyObject(JmxIndicator.class));

      EasyMock.expectLastCall().andThrow(interruptionException);

 // des insertions sont programmées
      EasyMock.expect(
            insertionService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andReturn(DOCUMENT)
            .times(0, count - 1);

      EasyMock.replay(interruptionSupport);
      EasyMock.replay(insertionService);

      for (int i = 0; i < count; i++) {
         poolExecutor.execute(insertionRunnable);
      }

      // nécessaire pour le bon déroulement du test
      poolExecutor.waitFinishInsertion();

      InsertionMasseRuntimeException executeException = poolExecutor
            .getInsertionMasseException();
      Assert
            .assertTrue(
                  "une exception de type "
                        + InterruptionTraitementException.class
                        + " est attendue",
                  executeException.getCause() instanceof InterruptionTraitementException);

      EasyMock.verify(interruptionSupport);
      EasyMock.verify(insertionService);

   }

   @Test
   public void execute_failure_insertion()
         throws InterruptionTraitementException, InsertionServiceEx {

      int count = 1000;

      jmxIndicator.setJmxCountDocument(count);

      // aucune interruption n'est programmée
      EasyMock.expect(
            interruptionSupport.hasInterrupted(EasyMock
                  .anyObject(DateTime.class), EasyMock
                  .anyObject(InterruptionTraitementConfig.class))).andReturn(
            false).times(1, count);

      // une des insertions échoue

      EasyMock.expect(
            insertionService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class))).andThrow(
            new InsertionServiceEx()).andReturn(DOCUMENT).times(0, count - 1);

      EasyMock.replay(interruptionSupport);
      EasyMock.replay(insertionService);

      for (int i = 0; i < count; i++) {
         poolExecutor.execute(insertionRunnable);
      }

      // nécessaire pour le bon déroulement du test
      poolExecutor.waitFinishInsertion();

      InsertionMasseRuntimeException executeException = poolExecutor
            .getInsertionMasseException();
      Assert.assertTrue("une exception de type "
            + InterruptionTraitementException.class + " est attendue",
            executeException.getCause() instanceof InsertionServiceEx);

      EasyMock.verify(interruptionSupport);
      EasyMock.verify(insertionService);
   }
}
