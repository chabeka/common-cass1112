package fr.urssaf.image.commons.spring.batch.operator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.apache.commons.lang.SystemUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fr.urssaf.image.commons.spring.batch.support.stax.XSDValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-operator.xml",
      "/applicationContext-item.xml", "/applicationContext-jobs.xml",
      "/applicationContext-service-test.xml",
      "/applicationContext-datasource-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@Ignore("doit installer les tables dans POSTGRES")
public class Job1OperatorServiceTest {

   @Autowired
   private Job1OperatorService operatorService;

   private static final File TMP_FILE;

   static {

      TMP_FILE = SystemUtils.getJavaIoTmpDir();

   }

   @Test
   public void validate_success() throws ParserConfigurationException,
         SAXException, IOException {

      int count_items = 10000000;

      File xmlPath = new File(TMP_FILE, "batch-exemple/bibliotheque_"
            + count_items + ".xml");

      File xsdPath = new File("src/main/resources/schemas/bibliotheque.xsd");

      try {
         XSDValidator.validXMLFileWithSAX(xmlPath, xsdPath);

      } catch (SAXParseException e) {

         Assert.fail("colonne:" + e.getColumnNumber() + " ligne:"
               + e.getLineNumber() + " " + e.getMessage());
      }

   }

   @Test
   public void start() {

      int count_items = 100000;

      File xmlPath = new File(TMP_FILE, "batch-exemple/bibliotheque_"
            + count_items + ".xml");
      File filePath = new File(TMP_FILE, "batch-exemple/bibliotheque_"
            + count_items + ".txt");

      Long executionId = operatorService.start(xmlPath, filePath);

      Assert.assertNotNull(
            "l'identifiant de contexte d'exécution doit être renseigné",
            executionId);
   }

   private long executionId;

   @Test
   public void stop() {

      Assert.assertTrue("renseigner préalablement la variable 'executionId'",
            executionId > 0);

      boolean isStopped = operatorService.stop(executionId);

      Assert.assertTrue("Le job n°" + executionId + " doit être arrêté",
            isStopped);
   }

   @Test
   public void restart() {

      Assert.assertTrue("renseigner préalablement la variable 'executionId'",
            executionId > 0);

      Long newExecutionId = operatorService.restart(executionId);

      Assert.assertNotNull("Le job n°" + executionId + " doit être redemarré",
            newExecutionId);

   }

}
