/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-mock-dfcemanager.xml" })
public class InterruptionTraitementMasseSupportImplTest {

   /**
    * 
    */
   private static final String FORMAT_DATE = "HH:mm:ss";

   @Autowired
   private DFCEServicesManager dfceManager;

   @Autowired
   private InterruptionTraitementMasseSupport support;

   @Autowired
   @Qualifier("interruption_capture_masse")
   private InterruptionTraitementConfig config;

   @Test
   @DirtiesContext
   public void testHasInterruptTrue() {

      Date dateNow = new Date();
      Date dateStart = DateUtils.addMinutes(dateNow, -1);
      SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
      String value = sdf.format(dateStart);

      config.setStart(value);
      config.setDelay(64);

      boolean interrupt = support.hasInterrupted(new DateTime(new Date()
            .getTime()), config);

      Assert.assertTrue("on doit etre en interruption", interrupt);

   }

   @Test
   @DirtiesContext
   public void testHasInterruptFalse() {

      Date dateNow = new Date();
      Date dateStart = DateUtils.addMinutes(dateNow, -1);
      SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
      String value = sdf.format(dateStart);

      config.setStart(value);
      config.setDelay(20);

      boolean interrupt = support.hasInterrupted(new DateTime(new Date()
            .getTime()), config);

      Assert.assertFalse("on ne doit pas etre en interruption", interrupt);
   }

   @Test
   public void testInterruptionError() throws ConnectionServiceEx,
         InterruptionTraitementException {

      dfceManager.getConnection();

      EasyMock.expectLastCall().andThrow(new Error("erreur connexion")).once();

      EasyMock.replay(dfceManager);

      Date dateNow = new Date();
      Date dateStart = DateUtils.addSeconds(dateNow, -60);
      SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
      String value = sdf.format(dateStart);

      config.setStart(value);
      config.setDelay(62);
      config.setTentatives(1);

      try {
         support.interruption(new DateTime(new Date().getTime()), config);
         Assert.fail("exception attendue");

      } catch (Exception e) {
         Assert.assertTrue(
               "on doit avoir une exception InterruptionTraitementException",
               e instanceof InterruptionTraitementException);
         e.printStackTrace();

      } finally {
         EasyMock.reset(dfceManager);
      }
   }
}
