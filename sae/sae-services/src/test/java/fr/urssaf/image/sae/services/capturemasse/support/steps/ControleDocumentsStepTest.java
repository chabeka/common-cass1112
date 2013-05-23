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

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-test.xml" })
public class ControleDocumentsStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

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
   }

   @Test
   public void testReaderReturnError() throws UnexpectedInputException,
         ParseException, Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_format_failure.xml");
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

      JobExecution execution = launcher.launchStep("controleDocuments",
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
   public void testProcessorReturnError() throws UnexpectedInputException,
         ParseException, Exception {

      File sommaire = new File(ecdeTestSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire/sommaire_failure_HashIncorrect.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);

      File repDocuments = new File(ecdeTestSommaire.getRepEcde(), "documents");
      File pdf = new File(repDocuments, "doc1.PDF");

      ClassPathResource resPdf = new ClassPathResource("doc1.PDF");
      FileOutputStream fosPdf = new FileOutputStream(pdf);

      IOUtils.copy(resPdf.getInputStream(), fosPdf);

      ExecutionContext contextParam = new ExecutionContext();
      contextParam.put(Constantes.SOMMAIRE, ecdeTestSommaire.getUrlEcde()
            .toString());
      contextParam.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      contextParam.put(Constantes.CODE_EXCEPTION,
            new ConcurrentLinkedQueue<String>());
      contextParam.put(Constantes.INDEX_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      contextParam.put(Constantes.INDEX_REF_EXCEPTION,
            new ConcurrentLinkedQueue<Integer>());
      contextParam.put(Constantes.DOC_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());

      Map<String, JobParameter> map = new HashMap<String, JobParameter>();
      map.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      JobParameters jobParameters = new JobParameters(map);
      JobExecution execution = launcher.launchStep("controleDocuments",
            jobParameters, contextParam);
      ExecutionContext context = execution.getExecutionContext();

      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Assert.assertEquals("la liste des exceptions doit contenir un élément",
            1, (exceptions.size()));

   }

}
