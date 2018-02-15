package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml", 
      "/applicationContext-sae-services-capturemasse-test-mock-droits.xml" })
public class CompressionDocumentProcessorTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(CompressionDocumentProcessorTest.class);
   
   @Autowired
   private CompressionDocumentProcessor processor;
   
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
   public void testCompression_oneDoc() throws Exception {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");

      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("attestation_consultation.pdf");
      document.setUMetadatas(new ArrayList<UntypedMetadata>());
      UntypedMetadata metadata = new UntypedMetadata();
      metadata.setLongCode(SAEArchivalMetadatas.TYPE_HASH.getLongCode());
      metadata.setValue("SHA-1");
      document.getUMetadatas().add(metadata);
      
      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Mon-OS-Super-Bien");
      
      List<String> documentsToCompress = new ArrayList<String>();
      String cheminDocOnEcde = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + document.getFilePath();
      documentsToCompress.add(cheminDocOnEcde);
      
      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());
      stepExecution.getJobExecution().getExecutionContext().put("documentsToCompress", documentsToCompress);
      
      processor.beforeStep(stepExecution);
      
      Assert.assertNull("La map des documents compressés aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      
      // cas 1 : on compresse un document
      processor.process(document);
      
      Assert.assertNotNull("La map des documents compressés n'aurait pas du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      Map<String, CompressedDocument> docsCompressed = ((Map<String, CompressedDocument>) stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      Assert.assertFalse("La map des documents compressés n'aurait pas du être vide", docsCompressed.isEmpty());
      Assert.assertEquals("La map des documents compressés aurait du comporter un seul document", 1, docsCompressed.size());
      
      // cas 2 : on retente de compresser le meme document, le document n'est pas compressé
      processor.process(document);
      
      Assert.assertNotNull("La map des documents compressés n'aurait pas du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      docsCompressed = ((Map<String, CompressedDocument>) stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      Assert.assertFalse("La map des documents compressés n'aurait pas du être vide", docsCompressed.isEmpty());
      Assert.assertEquals("La map des documents compressés aurait du comporter un seul document", 1, docsCompressed.size());
      
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
   
   @Test
   public void testCompression_noDoc() throws Exception {
      
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      
      UntypedDocument document = new UntypedDocument();
      document.setFilePath("attestation_consultation.pdf");

      String os = System.getProperty("os.name");
      // change l'os
      System.setProperty("os.name", "Mon-OS-Super-Bien");
      
      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());
      
      processor.beforeStep(stepExecution);
      
      Assert.assertNull("La map des documents compressés aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      
      // cas 1 : on n'a pas de liste de document a compresser
      processor.process(document);
      Assert.assertNull("La map des documents compressés aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      
      List<String> documentsToCompress = new ArrayList<String>();
      stepExecution.getJobExecution().getExecutionContext().put("documentsToCompress", documentsToCompress);
      
      // cas 2 : on n'a une liste vide de document a compresser
      processor.process(document);
      Assert.assertNull("La map des documents compressés aurait du être vide", stepExecution.getJobExecution().getExecutionContext().get("documentsCompressed"));
      
      // remet l'os original
      System.setProperty("os.name", os);
   }
}
