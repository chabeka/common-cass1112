/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.utils.XmlValidationUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class ResultatsVirtualFileFailureTaskletTest {

   @Autowired
   private ApplicationContext applicationContext;

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools tools;

   private EcdeTestSommaire testSommaire;

   @Before
   public void init() {
      testSommaire = tools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         tools.cleanEcdeTestSommaire(testSommaire);
      } catch (IOException e) {
         // rien à faire
      }
   }

   @Test
   public void testLancementStep() throws IOException {

      File sommaire = new File(testSommaire.getRepEcde(), "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire_virtuel.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      ExecutionContext context = new ExecutionContext();
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());

      ConcurrentLinkedQueue<String> codes = new ConcurrentLinkedQueue<String>();
      codes.add(Constantes.ERR_BUL002);
      ConcurrentLinkedQueue<Integer> index = new ConcurrentLinkedQueue<Integer>();
      index.add(3);
      ConcurrentLinkedQueue<Integer> refIndex = new ConcurrentLinkedQueue<Integer>();
      ConcurrentLinkedQueue<Exception> exceptions = new ConcurrentLinkedQueue<Exception>();
      exceptions.add(new Exception("la valeur x est erronée"));

      context.put(Constantes.DOC_EXCEPTION, exceptions);
      context.put(Constantes.INDEX_EXCEPTION, index);
      context.put(Constantes.INDEX_REF_EXCEPTION, refIndex);
      context.put(Constantes.CODE_EXCEPTION, codes);

      context.put(Constantes.DOC_COUNT, 3);
      context.put(Constantes.SOMMAIRE, testSommaire.getUrlEcde().toString());

      launcher.launchStep("finErreurVirtuel", context);

      File resultats = new File(testSommaire.getRepEcde(), "resultats.xml");

      Assert.assertTrue("le fichier resultats.xml doit exister", resultats
            .exists());
      Assert.assertTrue("le fichier resultats.xml doit etre non vide",
            resultats.length() > 0);

      Resource sommaireXSD = applicationContext
            .getResource("xsd_som_res/resultats.xsd");
      URL xsdSchema = sommaireXSD.getURL();

      try {
         XmlValidationUtils.parse(resultats, xsdSchema);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      } catch (SAXException e) {
         e.printStackTrace();
         Assert.fail("le fichier resultats.xml doit etre valide");
      }
   }
}
