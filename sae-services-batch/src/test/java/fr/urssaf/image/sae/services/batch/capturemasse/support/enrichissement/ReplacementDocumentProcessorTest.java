package fr.urssaf.image.sae.services.batch.capturemasse.support.enrichissement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
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

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch.ReplacementDocumentProcessor;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class ReplacementDocumentProcessorTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(ReplacementDocumentProcessorTest.class);
   
   @Autowired
   private ReplacementDocumentProcessor processor;
   
   @Autowired
   private EcdeTestTools ecdeTestTools;
   
   private EcdeTestSommaire ecdeTestSommaire;

   private StepExecution stepExecution;
   
   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
      
      JobExecution jobExecution = new JobExecution(Long.valueOf(1));
      stepExecution = new StepExecution("persistanceDocuments", jobExecution);
   }
   
   @After
   public void end() {
      
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
   public void testReplacement_compressedDoc() throws Exception {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      
      StorageDocument document = new StorageDocument();
      document.setFilePath("attestation_consultation.pdf");
      document.setMetadatas(new ArrayList<StorageMetadata>());
      StorageMetadata metadata = new StorageMetadata(StorageTechnicalMetadatas.HASH.getShortCode());
      document.getMetadatas().add(metadata);
      
      String cheminDocOnEcde = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + document.getFilePath();
      
      Map<String, CompressedDocument> documentsCompressed = new HashMap<String, CompressedDocument>();
      CompressedDocument compressedDoc = new CompressedDocument();
      compressedDoc.setFileName("attestation_consultation-compressed.pdf");
      compressedDoc.setFilePath(cheminDocOnEcde);
      compressedDoc.setHash("e96e16a9d15ceb481880b7808ab10aaeb5cb4e49");
      documentsCompressed.put(cheminDocOnEcde, compressedDoc);
      
      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());
      stepExecution.getJobExecution().getExecutionContext().put("documentsCompressed", documentsCompressed);
      
      processor.beforeStep(stepExecution);
      
      Assert.assertNull("Le nom du fichier doit être vide", document.getFileName());
      Assert.assertNull("Le hash du fichier doit être vide", metadata.getValue());
      //Assert.assertNull("Le contenu du fichier doit être vide", document.getContent());
      
      // cas 1 : on compresse un document
      StorageDocument outDoc = processor.process(document);
      
      StorageMetadata outHash = null;
      for (StorageMetadata meta : outDoc.getMetadatas()) {
         if (StorageTechnicalMetadatas.HASH.getShortCode().equals(meta.getShortCode())) {
            outHash = meta;
            break;
         }
      }
      
      Assert.assertEquals("Le nom du fichier n'est pas celui attendu", compressedDoc.getFileName(), outDoc.getFileName());
      if (outHash != null) {
         Assert.assertEquals("Le hash du fichier n'est pas celui attendu", compressedDoc.getHash(), outHash.getValue());
      }
      Assert.assertNotNull("Le contenu du fichier ne doit pas être vide", document.getContent());
   }
   
   @Test
   public void testReplacement_noDoc() throws Exception {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();
      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      copieDocument(ecdeDirectory, "attestation_consultation.pdf");
      
      StorageDocument document = new StorageDocument();
      document.setFilePath("attestation_consultation.pdf");
      document.setMetadatas(new ArrayList<StorageMetadata>());
      StorageMetadata metadata = new StorageMetadata(StorageTechnicalMetadatas.HASH.getShortCode());
      document.getMetadatas().add(metadata);
      
      stepExecution.getJobExecution().getExecutionContext().put(Constantes.SOMMAIRE_FILE, sommaire.getPath());
      
      processor.beforeStep(stepExecution);
      
      Assert.assertNull("Le nom du fichier doit être vide", document.getFileName());
      Assert.assertNull("Le hash du fichier doit être vide", metadata.getValue());
      
      // cas 1 : on ne compresse pas de document
      StorageDocument outDoc = processor.process(document);
      
      StorageMetadata outHash = null;
      for (StorageMetadata meta : outDoc.getMetadatas()) {
         if (StorageTechnicalMetadatas.HASH.getShortCode().equals(meta.getShortCode())) {
            outHash = meta;
            break;
         }
      }
      
      Assert.assertNull("Le nom du fichier doit être vide", outDoc.getFileName());
      if (outHash != null) {
         Assert.assertNull("Le hash du fichier doit être vide", outHash.getValue());
      }
      
      Map<String, CompressedDocument> documentsCompressed = new HashMap<String, CompressedDocument>();
      stepExecution.getJobExecution().getExecutionContext().put("documentsCompressed", documentsCompressed);
      
      // cas 2 : on ne compresse pas de document
      outDoc = processor.process(document);
      
      outHash = null;
      for (StorageMetadata meta : outDoc.getMetadatas()) {
         if (StorageTechnicalMetadatas.HASH.getShortCode().equals(meta.getShortCode())) {
            outHash = meta;
            break;
         }
      }
      
      Assert.assertNull("Le nom du fichier doit être vide", outDoc.getFileName());
      if (outHash != null) {
         Assert.assertNull("Le hash du fichier doit être vide", outHash.getValue());
      }
   }
}
