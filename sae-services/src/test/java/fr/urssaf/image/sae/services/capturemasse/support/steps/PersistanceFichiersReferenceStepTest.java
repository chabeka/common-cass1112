/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.SaeListCaptureMasseReferenceFile;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageReference;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-test.xml",
      "/applicationContext-sae-services-integration-test.xml" })
public class PersistanceFichiersReferenceStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private StorageServiceProvider provider;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService documentService;

   @Autowired
   private SaeListCaptureMasseReferenceFile saeListCaptureMasseReferenceFile;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
      AuthenticationContext.setAuthenticationToken(token);
   }

   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);

      EasyMock.reset(provider, documentService);

      saeListCaptureMasseReferenceFile.clear();

      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException exception) {
         // nothing to do
      }
   }

   @Test
   public void testReaderReturnError() throws IOException, ConnectionServiceEx {

      initReader();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_format_failure.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);

      ExecutionContext context = new ExecutionContext();
      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      JobExecution execution = launcher.launchStep(
            "persistanceFichiersReference", jobParameters, context);

      context = execution.getExecutionContext();

      Assert.assertNotNull("Une exception doit etre presente dans le context",
            context.get(Constantes.DOC_EXCEPTION));

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

      EasyMock.verify(provider);

   }

   @Test
   public void testProcessorReturnError() throws IOException,
         ConnectionServiceEx {

      initReader();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      ExecutionContext contextParam = new ExecutionContext();
      contextParam.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
            .toString());
      contextParam.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      contextParam.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep(
            "persistanceFichiersReference", jobParameters, contextParam);
      ExecutionContext context = execution.getExecutionContext();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

      EasyMock.verify(provider);

   }

   @Test
   public void testConnectionError() throws IOException, ConnectionServiceEx {

      initConnectionError();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      ExecutionContext contextParam = new ExecutionContext();
      contextParam.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
            .toString());
      contextParam.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      contextParam.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep(
            "persistanceFichiersReference", jobParameters, contextParam);

      Assert.assertEquals("le status de sortie doit etre correct",
            new ExitStatus("FAILED_NO_ROLLBACK").getExitCode(), execution
                  .getExitStatus().getExitCode());

      ExecutionContext context = execution.getExecutionContext();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

      EasyMock.verify(provider);

   }

   @Test
   public void testCloseConnectionError() throws IOException,
         ConnectionServiceEx, InsertionServiceEx {

      initCloseConnectionError();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      File documents = new File(ecdeTestSommaire.getRepEcde(), "documents");
      documents.mkdir();
      File attestation = new File(documents, "attestation1.pdf");
      ClassPathResource resPdf = new ClassPathResource("PDF/doc1.PDF");
      FileOutputStream fosPdf = new FileOutputStream(attestation);

      IOUtils.copy(resPdf.getInputStream(), fosPdf);

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);

      ExecutionContext context = new ExecutionContext();

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      JobExecution execution = launcher.launchStep(
            "persistanceFichiersReference", jobParameters, context);

      Assert.assertEquals("le status de sortie doit etre correct",
            ExitStatus.FAILED.getExitCode(), execution.getExitStatus()
                  .getExitCode());

      ExecutionContext execContext = execution.getExecutionContext();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) execContext
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

      EasyMock.verify(provider, documentService);

   }

   @Test
   public void testWriteError() throws IOException, ConnectionServiceEx,
         InsertionServiceEx {

      initWriterError();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);

      ExecutionContext context = new ExecutionContext();

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      JobExecution execution = launcher.launchStep(
            "persistanceFichiersReference", jobParameters, context);

      context = execution.getExecutionContext();

      Assert.assertNotNull("Une exception doit etre presente dans le context",
            context.get(Constantes.DOC_EXCEPTION));

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

      EasyMock.verify(provider, documentService);

   }

   @Test
   public void testWriteSuccess() throws IOException, ConnectionServiceEx,
         InsertionServiceEx {

      initSuccess();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      File documents = new File(ecdeTestSommaire.getRepEcde(), "documents");
      documents.mkdir();
      File attestation = new File(documents, "attestation1.pdf");
      ClassPathResource resPdf = new ClassPathResource("PDF/doc1.PDF");
      FileOutputStream fosPdf = new FileOutputStream(attestation);

      IOUtils.copy(resPdf.getInputStream(), fosPdf);

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);

      ExecutionContext context = new ExecutionContext();

      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      context.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      context.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
      context.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      JobExecution execution = launcher.launchStep(
            "persistanceFichiersReference", jobParameters, context);

      context = execution.getExecutionContext();

      Assert.assertEquals("le step doit etre en succes", ExitStatus.COMPLETED
            .getExitCode(), execution.getExitStatus().getExitCode());

      Assert.assertTrue("la liste des éléments insérés doit être non vide",
            CollectionUtils.isNotEmpty(saeListCaptureMasseReferenceFile));
      Assert.assertEquals("la liste doit contenir un seul élément", 1,
            saeListCaptureMasseReferenceFile.size());

      EasyMock.verify(provider, documentService);

   }

   private void initReader() throws ConnectionServiceEx {
      provider.openConnexion();
      EasyMock.expectLastCall().once();

      provider.closeConnexion();
      EasyMock.expectLastCall().once();

      EasyMock.replay(provider);
   }

   private void initWriterError() throws ConnectionServiceEx,
         InsertionServiceEx {
      provider.openConnexion();
      EasyMock.expectLastCall().once();

      provider.closeConnexion();
      EasyMock.expectLastCall().once();

      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            documentService).once();
      EasyMock.expect(
            documentService.insertStorageReference(EasyMock
                  .anyObject(VirtualStorageReference.class))).andThrow(
            new InsertionServiceEx()).once();

      EasyMock.replay(provider, documentService);

   }

   private void initSuccess() throws ConnectionServiceEx, InsertionServiceEx {

      StorageReferenceFile ref = new StorageReferenceFile();
      ref.setUuid(UUID.randomUUID());

      provider.openConnexion();
      EasyMock.expectLastCall().once();

      provider.closeConnexion();
      EasyMock.expectLastCall().once();

      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            documentService).once();
      EasyMock.expect(
            documentService.insertStorageReference(EasyMock
                  .anyObject(VirtualStorageReference.class))).andReturn(ref);

      EasyMock.replay(provider, documentService);

   }

   private void initConnectionError() throws ConnectionServiceEx {
      provider.openConnexion();
      EasyMock.expectLastCall().andThrow(new ConnectionServiceEx()).once();

      provider.closeConnexion();
      EasyMock.expectLastCall().once();

      EasyMock.replay(provider);
   }

   private void initCloseConnectionError() throws ConnectionServiceEx,
         InsertionServiceEx {

      StorageReferenceFile ref = new StorageReferenceFile();
      ref.setUuid(UUID.randomUUID());

      provider.openConnexion();
      EasyMock.expectLastCall().once();

      provider.closeConnexion();
      EasyMock.expectLastCall().andThrow(new RuntimeException()).once();

      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            documentService).once();
      EasyMock.expect(
            documentService.insertStorageReference(EasyMock
                  .anyObject(VirtualStorageReference.class))).andReturn(ref);

      EasyMock.replay(provider, documentService);

   }
}
