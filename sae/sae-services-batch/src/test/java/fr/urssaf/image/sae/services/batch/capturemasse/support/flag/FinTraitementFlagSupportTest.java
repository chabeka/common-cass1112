/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.flag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.flag.FinTraitementFlagSupport;
import fr.urssaf.image.sae.utils.LogUtils;
import fr.urssaf.image.sae.utils.SaeLogAppender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class FinTraitementFlagSupportTest {

   @Autowired
   private FinTraitementFlagSupport support;

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

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeObligatoire() {

      support.writeFinTraitementFlag(null);

   }

   @Test
   public void testFinFlag() {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      support.writeFinTraitementFlag(ecdeDirectory);

      File file = new File(ecdeDirectory, "fin_traitement.flag");

      Assert.assertTrue("le fichier fin_traitement.flag doit exister", file
            .exists());
   }

   @Test
   public void testLogs() throws IOException {

      Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

      SaeLogAppender logAppender = new SaeLogAppender(Level.WARN,
            "fr.urssaf.image.sae");
      logger.addAppender(logAppender);

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      FileUtils.deleteDirectory(ecdeDirectory);

      try {
         support.writeFinTraitementFlag(ecdeDirectory);

         Assert.fail("une erreur CaptureMasseRuntimeException est attendue");

      } catch (CaptureMasseRuntimeException exception) {
         List<ILoggingEvent> logsWarn = LogUtils.getLogsByLevel(logAppender
               .getLoggingEvents(), Level.WARN);
         Assert.assertEquals("le nombre de logs en WARN doit etre correct", 2,
               logsWarn.size());

      } catch (Exception exception) {
         Assert.fail("une erreur CaptureMasseRuntimeException est attendue");
      }

   }

}
