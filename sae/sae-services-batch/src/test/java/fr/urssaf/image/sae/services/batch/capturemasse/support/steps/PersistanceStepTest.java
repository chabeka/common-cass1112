/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
      "/applicationContext-sae-services-capturemasse-test-mock-storagedocument.xml",
      "/applicationContext-sae-services-capturemasse-test.xml" })
public class PersistanceStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageDocumentService;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }

      EasyMock.reset(storageDocumentService);
   }

   @Test
   @Ignore
   public void testPersistance() throws IOException, InsertionServiceEx {

      StorageDocument storageDocument = new StorageDocument();
      storageDocument.setUuid(UUID.randomUUID());

      // Mock du service de stockage
      EasyMock.expect(
            storageDocumentService.insertStorageDocument(EasyMock
                  .anyObject(StorageDocument.class)))
            .andReturn(storageDocument);

      storageDocumentService.setStorageDocumentServiceParameter(EasyMock
            .anyObject(ServiceProvider.class));

      EasyMock.expectLastCall();

      EasyMock.replay(storageDocumentService);

      // création de l'arbo
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = null;
      FileOutputStream fileOs = null;
      File repertoireEcdeDocuments = new File(ecdeTestSommaire.getRepEcde(),
            "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");

      try {
         fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         fileOs = new FileOutputStream(fileAttestation1);
         IOUtils.copy(resAttestation1.getInputStream(), fos);

         String uuid = UUID.randomUUID().toString();

         // lancement du job
         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
               .getUrlEcde().toString()));
         map.put(Constantes.ID_TRAITEMENT, new JobParameter(uuid));
         JobParameters jobParameters = new JobParameters(map);

         ExecutionContext context = new ExecutionContext();
         context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
               .toString());
         context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
         context.put(Constantes.ID_TRAITEMENT, uuid);
         context.put(Constantes.CODE_EXCEPTION,
               new ConcurrentLinkedQueue<String>());
         context.put(Constantes.INDEX_EXCEPTION,
               new ConcurrentLinkedQueue<Integer>());
         context.put(Constantes.DOC_EXCEPTION,
               new ConcurrentLinkedQueue<Exception>());
         context.put(Constantes.INDEX_REF_EXCEPTION,
               new ConcurrentLinkedQueue<Integer>());

         JobExecution execution = launcher.launchStep("persistanceDocuments",
               jobParameters, context);

         Assert.assertEquals("le step doit etre completed",
               ExitStatus.COMPLETED, execution.getExitStatus());

      } finally {
         if (fileOs != null) {
            try {
               fileOs.close();
            } catch (Exception e) {
               // nothing to do
            }
         }

         if (fos != null) {
            try {
               fos.close();
            } catch (Exception e) {
               // nothing to do
            }
         }
      }
   }

   // @Test
   // public void testPersistance() throws IOException, InsertionServiceEx {
   //
   // StorageDocument storageDocument = new StorageDocument();
   // storageDocument.setUuid(UUID.randomUUID());
   //
   // // Mock du service de stockage
   // EasyMock.expect(
   // storageDocumentService.insertStorageDocument(EasyMock
   // .anyObject(StorageDocument.class)))
   // .andReturn(storageDocument);
   //
   // storageDocumentService.setStorageDocumentServiceParameter(EasyMock
   // .anyObject(ServiceProvider.class));
   //
   // EasyMock.expectLastCall();
   //
   // EasyMock.replay(storageDocumentService);
   //
   // // création de l'arbo
   // File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
   // ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
   // FileOutputStream fos = null;
   // FileOutputStream fileOs = null;
   // File repertoireEcdeDocuments = new File(ecdeTestSommaire.getRepEcde(),
   // "documents");
   // ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
   // File fileAttestation1 = new File(repertoireEcdeDocuments, "doc1.PDF");
   //
   // try {
   // fos = new FileOutputStream(sommaire);
   // IOUtils.copy(resSommaire.getInputStream(), fos);
   //
   // fileOs = new FileOutputStream(fileAttestation1);
   // IOUtils.copy(resAttestation1.getInputStream(), fos);
   //
   // String uuid = UUID.randomUUID().toString();
   //
   // // lancement du job
   // Map<String, JobParameter> map = new HashMap<String, JobParameter>();
   // map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
   // .getUrlEcde().toString()));
   // map.put(Constantes.ID_TRAITEMENT, new JobParameter(uuid));
   // JobParameters jobParameters = new JobParameters(map);
   //
   // ExecutionContext context = new ExecutionContext();
   // context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
   // .toString());
   // context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
   // context.put(Constantes.ID_TRAITEMENT, uuid);
   //
   // JobExecution execution = launcher.launchStep("persistanceDocuments",
   // jobParameters, context);
   //
   // Assert.assertEquals("le step doit etre completed",
   // ExitStatus.COMPLETED, execution.getExitStatus());
   //
   // } finally {
   // if (fileOs != null) {
   // try {
   // fileOs.close();
   // } catch (Exception e) {
   // // nothing to do
   // }
   // }
   //
   // if (fos != null) {
   // try {
   // fos.close();
   // } catch (Exception e) {
   // // nothing to do
   // }
   // }
   // }
   // }

}
