/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.SaeListVirtualReferenceFile;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class ControleFichiersReferenceStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private SaeListVirtualReferenceFile saeListVirtualReferenceFile;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {

      saeListVirtualReferenceFile.clear();

      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test
   public void testSommaireVirtuelFichierInexistant() throws IOException,
         InsertionServiceEx, ParserConfigurationException, SAXException {

      // création de l'arbo
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = null;

      try {
         fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         ExecutionContext context = new ExecutionContext();
         context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
               .toString());
         context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
               .getUrlEcde().toString()));
         JobParameters jobParameters = new JobParameters(map);

         JobExecution execution = launcher.launchStep(
               "controleFichiersReference", jobParameters, context);

         Assert.assertEquals(
               "le step doit etre en erreur car le fichier n'existe pas",
               ExitStatus.FAILED.getExitCode(), execution.getExitStatus()
                     .getExitCode());

      } finally {

         if (fos != null) {
            try {
               fos.close();
            } catch (Exception e) {
               // nothing to do
            }
         }
      }
   }

   @Test
   public void testSommaireVirtuelFichierVide() throws IOException,
         InsertionServiceEx, ParserConfigurationException, SAXException {

      // création de l'arbo
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      File documents = new File(ecdeTestSommaire.getRepEcde(), "documents");
      documents.mkdir();
      File document = new File(documents, "attestation1.pdf");
      ClassPathResource resDocument = new ClassPathResource("docVide.pdf");

      FileOutputStream fos = null;
      FileOutputStream fosDoc = null;

      try {
         fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         fosDoc = new FileOutputStream(document);
         IOUtils.copy(resDocument.getInputStream(), fosDoc);

         ExecutionContext context = new ExecutionContext();
         context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
               .toString());
         context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
               .getUrlEcde().toString()));
         JobParameters jobParameters = new JobParameters(map);

         JobExecution execution = launcher.launchStep(
               "controleFichiersReference", jobParameters, context);

         Assert.assertEquals(
               "le step doit etre en erreur car le fichier est vide",
               ExitStatus.FAILED.getExitCode(), execution.getExitStatus()
                     .getExitCode());

      } finally {

         if (fos != null) {
            try {
               fos.close();
            } catch (Exception e) {
               // nothing to do
            }
         }
      }
   }

   @Test
   public void testReaderReturnError() throws IOException {

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
      context
            .put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde().toString());

      JobExecution execution = launcher.launchStep("controleFichiersReference",
            jobParameters, context);

      context = execution.getExecutionContext();

      Assert.assertNotNull("Une exception doit etre presente dans le context",
            context.get(Constantes.DOC_EXCEPTION));

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

   }

   @Test
   public void testProcessorReturnError() throws IOException {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      ExecutionContext contextParam = new ExecutionContext();
      contextParam.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
            .toString());
      contextParam.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
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
      JobExecution execution = launcher.launchStep("controleFichiersReference",
            jobParameters, contextParam);
      ExecutionContext context = execution.getExecutionContext();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

   }

   @Test
   public void testSommaireVirtuelSucces() throws IOException,
         InsertionServiceEx, ParserConfigurationException, SAXException {

      // création de l'arbo
      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_virtuel_success.xml");
      File documents = new File(ecdeTestSommaire.getRepEcde(), "documents");
      documents.mkdir();
      File document = new File(documents, "attestation1.pdf");
      ClassPathResource resDocument = new ClassPathResource("doc1.PDF");

      FileOutputStream fos = null;
      FileOutputStream fosDoc = null;

      try {
         fos = new FileOutputStream(sommaire);
         IOUtils.copy(resSommaire.getInputStream(), fos);

         fosDoc = new FileOutputStream(document);
         IOUtils.copy(resDocument.getInputStream(), fosDoc);

         ExecutionContext context = new ExecutionContext();
         context.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
               .toString());
         context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

         Map<String, JobParameter> map = new HashMap<String, JobParameter>();
         map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
               .getUrlEcde().toString()));
         JobParameters jobParameters = new JobParameters(map);

         JobExecution execution = launcher.launchStep(
               "controleFichiersReference", jobParameters, context);

         Assert.assertEquals(
               "le step doit etre en erreur car le fichier est vide",
               ExitStatus.COMPLETED.getExitCode(), execution.getExitStatus()
                     .getExitCode());

         Assert
               .assertEquals(
                     "Un élément doit être présent dans la liste des éléments contrôlés",
                     1, saeListVirtualReferenceFile.size());

      } finally {

         if (fos != null) {
            try {
               fos.close();
            } catch (Exception e) {
               // nothing to do
            }
         }
      }
   }
}
