/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch;

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
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlValidationUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
public class ResultatsFileFailureTaskletTest {

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
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);
      IOUtils.copy(resSommaire.getInputStream(), fos);

      ExecutionContext context = new ExecutionContext();
      context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
      context.put(Constantes.SOMMAIRE, testSommaire.getUrlEcde().toString());

      ConcurrentLinkedQueue<String> codes = new ConcurrentLinkedQueue<String>();
      codes.add(Constantes.ERR_BUL002);
      ConcurrentLinkedQueue<Integer> index = new ConcurrentLinkedQueue<Integer>();
      index.add(3);
      ConcurrentLinkedQueue<String> messageExceptionList = new ConcurrentLinkedQueue<String>();
      messageExceptionList.add(new Exception("la valeur x est erronée").toString());
      ConcurrentLinkedQueue<Integer> refIndex = new ConcurrentLinkedQueue<Integer>();

      context.put(Constantes.DOC_EXCEPTION, messageExceptionList);
      context.put(Constantes.INDEX_EXCEPTION, index);
      context.put(Constantes.INDEX_REF_EXCEPTION, refIndex);
      context.put(Constantes.CODE_EXCEPTION, codes);

      context.put(Constantes.DOC_COUNT, 21);

      launcher.launchStep("finErreurCapture", context);

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
