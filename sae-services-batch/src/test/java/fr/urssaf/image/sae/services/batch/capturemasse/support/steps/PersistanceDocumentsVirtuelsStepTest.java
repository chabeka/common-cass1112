/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.bo.VirtualReferenceFile;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseReferenceFile;
import fr.urssaf.image.sae.services.batch.capturemasse.model.SaeListCaptureMasseReferenceFile;
import fr.urssaf.image.sae.services.batch.capturemasse.model.SaeListVirtualReferenceFile;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
      "/applicationContext-sae-services-batch-test.xml",
"/applicationContext-sae-services-capturemasse-test-integration.xml" })
public class PersistanceDocumentsVirtuelsStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private StorageServiceProvider provider;

   @Autowired
   private StorageDocumentService documentService;

   @Autowired
   private SaeListCaptureMasseReferenceFile saeListCaptureMasseReferenceFile;

   @Autowired
   private SaeListVirtualReferenceFile saeListVirtualReferenceFile;

   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

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
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.8");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("ATTESTATION DE MARCHE PUBLIC");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());
   }

   @After
   public void end() throws Exception {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      EasyMock.reset(provider, documentService);

      saeListCaptureMasseReferenceFile.clear();
      saeListVirtualReferenceFile.clear();

      server.resetData();
   }

   @Test
   @DirtiesContext
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
            "persistanceDocumentsVirtuels", jobParameters, contextParam);

      List<String> codeExitStatus = new ArrayList<String>();
      codeExitStatus = Arrays.asList(new ExitStatus("FAILED").getExitCode(), new ExitStatus("FAILED_NO_ROLLBACK").getExitCode());
      
      Assert.assertTrue("le status de sortie doit etre à FAILED FAILED_NO_ROLLBACK", codeExitStatus.contains(execution
            .getExitStatus().getExitCode()));

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> codesExceptionList = (ConcurrentLinkedQueue<String>) execution
      .getExecutionContext().get(Constantes.CODE_EXCEPTION);

      Assert.assertEquals("il doit y avoir une erreur dans la liste", 1,
            codesExceptionList.size());

      EasyMock.verify(provider);

   }

   @Test
   @DirtiesContext
   public void testReaderError() throws IOException, ConnectionServiceEx {
      initConnection();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_format_failure.xml");
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
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep(
            "persistanceDocumentsVirtuels", jobParameters, contextParam);

      Assert.assertEquals("le status de sortie doit etre à FAILED_NO_ROLLBACK",
            ExitStatus.FAILED.getExitCode(), execution.getExitStatus()
            .getExitCode());
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> messageExceptionList = (ConcurrentLinkedQueue<String>) execution
      .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("il doit y avoir une erreur dans la liste", 1,
            messageExceptionList.size());

      EasyMock.verify(provider);

   }

   @Test
   @DirtiesContext
   public void testProcessorError() throws IOException, ConnectionServiceEx {
      initConnection();

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
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep(
            "persistanceDocumentsVirtuels", jobParameters, contextParam);

      Assert.assertEquals("le status de sortie doit etre à FAILED_NO_ROLLBACK",
            ExitStatus.FAILED.getExitCode(), execution.getExitStatus()
            .getExitCode());
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> messageExceptionList = (ConcurrentLinkedQueue<String>) execution
      .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("il doit y avoir une erreur dans la liste", 1,
            messageExceptionList.size());

      EasyMock.verify(provider);

   }

   @Test
   @DirtiesContext
   public void testWriterError() throws IOException, ConnectionServiceEx,
   InsertionServiceEx {
      initStorageFailed();
      initConnection();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      File documents = new File(ecdeTestSommaire.getRepEcde(), "documents");
      documents.mkdir();
      File attestation = new File(documents, "attestation1.pdf");
      ClassPathResource resPdf = new ClassPathResource("doc1.PDF");
      FileOutputStream fosPdf = new FileOutputStream(attestation);

      IOUtils.copy(resPdf.getInputStream(), fosPdf);

      VirtualReferenceFile file = new VirtualReferenceFile();
      file.setFilePath(attestation.getAbsolutePath());
      file.setHash("4bf2ddbd82d5fd38e821e6aae434ac989972a043");
      file.setTypeHash("SHA-1");
      saeListVirtualReferenceFile.add(file);

      CaptureMasseReferenceFile captFile = new CaptureMasseReferenceFile();
      captFile.setFileName("doc1_1_1");
      StorageReferenceFile reference = new StorageReferenceFile();
      reference.setDigest("4bf2ddbd82d5fd38e821e6aae434ac989972a043");
      reference.setDigestAlgorithm("SHA-1");
      reference.setName("doc1");
      reference.setSize(42L);
      reference.setUuid(UUID.randomUUID());
      captFile.setReference(reference);
      saeListCaptureMasseReferenceFile.add(captFile);

      ExecutionContext contextParam = new ExecutionContext();
      contextParam.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
            .toString());
      contextParam.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      contextParam.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      map.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID.randomUUID()
            .toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep(
            "persistanceDocumentsVirtuels", jobParameters, contextParam);

      Assert.assertEquals("le status de sortie doit etre à FAILED",
            ExitStatus.FAILED.getExitCode(), execution.getExitStatus()
            .getExitCode());
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> codeExceptionList = (ConcurrentLinkedQueue<String>) execution
      .getExecutionContext().get(Constantes.CODE_EXCEPTION);

      Assert.assertEquals("il doit y avoir une erreur dans la liste", 1,
            codeExceptionList.size());

   }

   @Test
   @DirtiesContext
   public void testSuccess() throws IOException, ConnectionServiceEx,
   InsertionServiceEx {
      initStorageSuccess();
      initConnection();

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      File documents = new File(ecdeTestSommaire.getRepEcde(), "documents");
      documents.mkdir();
      File attestation = new File(documents, "attestation1.pdf");
      ClassPathResource resPdf = new ClassPathResource("doc1.PDF");
      FileOutputStream fosPdf = new FileOutputStream(attestation);

      IOUtils.copy(resPdf.getInputStream(), fosPdf);

      VirtualReferenceFile file = new VirtualReferenceFile();
      file.setFilePath(attestation.getAbsolutePath());
      file.setHash("4bf2ddbd82d5fd38e821e6aae434ac989972a043");
      file.setTypeHash("SHA-1");
      saeListVirtualReferenceFile.add(file);

      CaptureMasseReferenceFile captFile = new CaptureMasseReferenceFile();
      captFile.setFileName("doc1_1_1");
      StorageReferenceFile reference = new StorageReferenceFile();
      reference.setDigest("4bf2ddbd82d5fd38e821e6aae434ac989972a043");
      reference.setDigestAlgorithm("SHA-1");
      reference.setName("doc1");
      reference.setSize(42L);
      reference.setUuid(UUID.randomUUID());
      captFile.setReference(reference);
      saeListCaptureMasseReferenceFile.add(captFile);

      ExecutionContext contextParam = new ExecutionContext();
      contextParam.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
            .toString());
      contextParam.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      contextParam.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      map.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID.randomUUID()
            .toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep(
            "persistanceDocumentsVirtuels", jobParameters, contextParam);

      Assert.assertEquals("le status de sortie doit etre à COMPLETED",
            ExitStatus.COMPLETED.getExitCode(), execution.getExitStatus()
            .getExitCode());
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> messageExceptionList = (ConcurrentLinkedQueue<String>) execution
      .getExecutionContext().get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("il ne doit pas y avoir d'erreur dans la liste", 0,
            messageExceptionList.size());

      EasyMock.verify(provider, documentService);

   }

   private void initConnectionError() throws ConnectionServiceEx {
      provider.openConnexion();
      EasyMock.expectLastCall().andThrow(new ConnectionServiceEx()).once();

      provider.closeConnexion();
      EasyMock.expectLastCall().once();

      EasyMock.replay(provider);

   }

   private void initConnection() throws ConnectionServiceEx {
      provider.openConnexion();
      EasyMock.expectLastCall().once();

      provider.closeConnexion();
      EasyMock.expectLastCall().once();

      EasyMock.replay(provider);

   }

   private void initStorageFailed() throws InsertionServiceEx {
      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            documentService).once();
      EasyMock.expect(
            documentService.insertVirtualStorageDocument(EasyMock
                  .anyObject(VirtualStorageDocument.class))).andThrow(
                        new InsertionServiceEx());

      EasyMock.replay(documentService);

   }

   private void initStorageSuccess() throws InsertionServiceEx {
      EasyMock.expect(provider.getStorageDocumentService()).andReturn(
            documentService).once();
      EasyMock.expect(
            documentService.insertVirtualStorageDocument(EasyMock
                  .anyObject(VirtualStorageDocument.class))).andReturn(
                        UUID.randomUUID());

      EasyMock.replay(documentService);

   }

}
