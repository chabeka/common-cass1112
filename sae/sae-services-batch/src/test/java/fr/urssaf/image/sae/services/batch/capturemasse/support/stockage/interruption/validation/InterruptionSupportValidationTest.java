package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.validation;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-batch-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class InterruptionSupportValidationTest {

   @Autowired
   private InterruptionTraitementMasseSupport support;

   private static final String FAIL_MESSAGE = "Une exception de type llegalArgumentException doit être levée";

   private static final String EXCEPTION_MESSAGE = "Le message de l'exception est inattendu";

   private static final String ARG_START = "12:37:54";

   private static final int ARG_DELAY = 120;

   private static final int ARG_TENTATIVES = 2;

   private static final DateTime ARG_CURRENT_DATE = new DateTime();

   private static final InterruptionTraitementConfig ARG_INTERRUPTION_CONFIG;

   static {

      ARG_INTERRUPTION_CONFIG = new InterruptionTraitementConfig();
      ARG_INTERRUPTION_CONFIG.setDelay(ARG_DELAY);
      ARG_INTERRUPTION_CONFIG.setStart(ARG_START);
      ARG_INTERRUPTION_CONFIG.setTentatives(ARG_TENTATIVES);
   }

   @Test
   public void interruption_success() throws InterruptionTraitementException {

      InterruptionTraitementMasseSupport support = new InterruptionTraitementMasseSupport() {

         @Override
         public void interruption(DateTime currentDate,
               InterruptionTraitementConfig interruptionConfig) {
            // implémentation vide
         }

         @Override
         public boolean hasInterrupted(DateTime currentDate,
               InterruptionTraitementConfig interruptionConfig) {
            // implémentation vide
            return false;
         }

      };

      support.interruption(ARG_CURRENT_DATE, ARG_INTERRUPTION_CONFIG);

   }

   @Test
   public void interruption_failure_start_empty() {

      assertInterruptionStart(" ");
      assertInterruptionStart("");
      assertInterruptionStart(null);

   }

   private void assertInterruptionStart(String start) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(ARG_DELAY);
      interruptionConfig.setStart(start);
      interruptionConfig.setTentatives(ARG_TENTATIVES);

      try {

         support.interruption(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'startTime' doit être renseigné.", e.getMessage());

      } catch (InterruptionTraitementException e) {

         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void interruption_failure_start_format() {

      assertInterruptionFormat("12:54");
      assertInterruptionFormat("24:18:59");
      assertInterruptionFormat("18:60:59");
      assertInterruptionFormat("12:54:60");
   }

   private void assertInterruptionFormat(String time) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(ARG_DELAY);
      interruptionConfig.setStart(time);
      interruptionConfig.setTentatives(ARG_TENTATIVES);

      try {

         support.interruption(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE, "L'argument 'startTime'='"
               + time + "' doit être au format HH:mm:ss.", e.getMessage());

      } catch (InterruptionTraitementException e) {

         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void interruption_failure_delay_negatif() {

      assertInterruptionDelay(0);
      assertInterruptionDelay(-2);
   }

   private void assertInterruptionDelay(int delay) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(delay);
      interruptionConfig.setStart(ARG_START);
      interruptionConfig.setTentatives(ARG_TENTATIVES);

      try {

         support.interruption(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'delay' doit être au moins égal à 1.", e
                     .getMessage());

      } catch (InterruptionTraitementException e) {

         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void interruption_failure_tentatives_negatif() {

      assertInterruptionTentatives(0);
      assertInterruptionTentatives(-2);

   }

   private void assertInterruptionTentatives(int tentatives) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(ARG_DELAY);
      interruptionConfig.setStart(ARG_START);
      interruptionConfig.setTentatives(tentatives);

      try {

         support.interruption(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'tentatives' doit être au moins égal à 1.", e
                     .getMessage());
      } catch (InterruptionTraitementException e) {

         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void interruption_failure_interruptionConfig_empty() {

      try {

         support.interruption(ARG_CURRENT_DATE, null);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'interruptionConfig' doit être renseigné.", e
                     .getMessage());

      } catch (InterruptionTraitementException e) {

         throw new NestableRuntimeException(e);
      }
   }

   @Test
   public void interruption_failure_dateCurrent_empty() {

      try {

         support.interruption(null, ARG_INTERRUPTION_CONFIG);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'currentDate' doit être renseigné.", e.getMessage());

      } catch (InterruptionTraitementException e) {

         throw new NestableRuntimeException(e);

      }
   }

}