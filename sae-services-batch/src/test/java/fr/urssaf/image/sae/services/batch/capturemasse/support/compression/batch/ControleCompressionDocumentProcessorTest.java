package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml", 
      "/applicationContext-sae-services-capturemasse-test-mock-droits.xml" })
public class ControleCompressionDocumentProcessorTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(ControleCompressionDocumentProcessorTest.class);
   
   @Autowired
   private ControleCompressionDocumentProcessor processor;
   
   @Autowired
   private SaeDroitService saeDroitService;
   
   @Autowired
   private EcdeTestTools ecdeTestTools;
   
   private EcdeTestSommaire ecdeTestSommaire;

   private StepExecution stepExecution;
   
   @Before
   public void init() {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");
      
      viExtrait.setPagms(new ArrayList<String>());
      viExtrait.getPagms().add("PAGM_1");
      viExtrait.getPagms().add("PAGM_2");

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
      
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      JobExecution jobExecution = new JobExecution(Long.valueOf(1));
      stepExecution = new StepExecution("compressionDocuments", jobExecution);
   }
   
   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);
      
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }
   
   private void copieDocument(File ecdeDirectory, String nomFichier) {
      // copie le fichier nomFichier
      // dans le repertoire de l'ecde
      File repertoireDocuments = new File(ecdeDirectory, "documents");
      if (!repertoireDocuments.exists()) {
         repertoireDocuments.mkdirs();
      }
      File fileDoc = new File(repertoireDocuments, nomFichier);
      ClassPathResource resDoc = new ClassPathResource("doc/" + nomFichier);
      FileOutputStream fos = null;
      try {
         fos = new FileOutputStream(fileDoc);
         IOUtils.copy(resDoc.getInputStream(), fos);
         resDoc.getInputStream().close();
      } catch (FileNotFoundException e) {
         LOG.error(e.getMessage());
      } catch (IOException e) {
         LOG.error(e.getMessage());
      } finally {
         if (fos != null) {
            try {
               fos.close();
            } catch (IOException ex) {
               // ignore ... any significant errors should already have been
               // reported via an IOException from the final flush.
            }
         }
      }
   }
   
   @Test
   public void testCompressionActive() throws Exception {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      pagms.add(saePagm);
      
      copieDocument(ecdeDirectory, "doc-5mo.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("doc-5mo.pdf");
      
      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());
      
      processor.beforeStep(stepExecution);
      
      Assert.assertNull("La liste des documents à compresser aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsToCompress"));
      
      processor.process(document);
      
      Assert.assertNotNull("La liste des documents à compresser n'aurait pas du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsToCompress"));
      List<String> docsToCompress = ((List<String>) stepExecution.getJobExecution().getExecutionContext().get("documentsToCompress"));
      Assert.assertFalse("La liste des documents à compresser n'aurait pas du être vide", docsToCompress.isEmpty());
      Assert.assertEquals("La liste des documents à compresser aurait du comporter un seul document", 1, docsToCompress.size());
      
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void testCompressionInactive() throws Exception {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Linux");
      
      List<SaePagm> pagms = new ArrayList<SaePagm>();
      EasyMock.expect(saeDroitService.getListeSaePagm(EasyMock.anyObject(String.class))).andReturn(pagms);
      EasyMock.replay(saeDroitService);
      
      SaePagm saePagm = new SaePagm();
      saePagm.setCode("PAGM_1");
      saePagm.setPagma(new SaePagma());
      saePagm.getPagma().setActionUnitaires(new ArrayList<String>());
      saePagm.getPagma().getActionUnitaires().add("archivage_masse");
      saePagm.setCompressionPdfActive(Boolean.TRUE);
      pagms.add(saePagm);
      
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("attestation_consultation.pdf");
      
      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());
      
      processor.beforeStep(stepExecution);
      
      Assert.assertNull("La liste des documents à compresser aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsToCompress"));
      
      processor.process(document);
      
      Assert.assertNull("La liste des documents à compresser aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsToCompress"));
      
      EasyMock.reset(saeDroitService);
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
}
