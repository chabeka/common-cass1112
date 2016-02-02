package fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.validation;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class HasInterruptedSupportValidationTest {

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
   public void hasInterrupted_success() {

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

      support.hasInterrupted(ARG_CURRENT_DATE, ARG_INTERRUPTION_CONFIG);

   }

   @Test
   public void hasInterrupted_failure_dateCurrent_empty() {

      try {

         support.hasInterrupted(null, ARG_INTERRUPTION_CONFIG);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'currentDate' doit être renseigné.", e.getMessage());
      }
   }

   @Test
   public void hasInterrupted_failure_interruptionConfig_empty() {

      try {

         support.hasInterrupted(ARG_CURRENT_DATE, null);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'interruptionConfig' doit être renseigné.", e
                     .getMessage());
      }
   }

   private void assertHasInterruptedTentatives(int tentatives) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(ARG_DELAY);
      interruptionConfig.setStart(ARG_START);
      interruptionConfig.setTentatives(tentatives);

      try {

         support.hasInterrupted(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'tentatives' doit être au moins égal à 1.", e
                     .getMessage());
      }
   }

   @Test
   public void hasInterrupted_failure_tentatives_negatif() {

      assertHasInterruptedTentatives(0);
      assertHasInterruptedTentatives(-2);
   }

   @Test
   public void hasInterrupted_failure_start_empty() {

      assertHasInterruptedStart(" ");
      assertHasInterruptedStart("");
      assertHasInterruptedStart(null);

   }

   private void assertHasInterruptedStart(String start) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(ARG_DELAY);
      interruptionConfig.setStart(start);
      interruptionConfig.setTentatives(ARG_TENTATIVES);

      try {

         support.hasInterrupted(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'startTime' doit être renseigné.", e.getMessage());
      }
   }

   @Test
   public void hasInterrupted_failure_start_format() {

      assertHasInterruptedFormat("12:54");
      assertHasInterruptedFormat("24:18:59");
      assertHasInterruptedFormat("18:60:59");
      assertHasInterruptedFormat("12:54:60");
   }

   private void assertHasInterruptedFormat(String time) {

      InterruptionTraitementConfig interruptionConfig = new InterruptionTraitementConfig();
      interruptionConfig.setDelay(ARG_DELAY);
      interruptionConfig.setStart(time);
      interruptionConfig.setTentatives(ARG_TENTATIVES);

      try {

         support.hasInterrupted(ARG_CURRENT_DATE, interruptionConfig);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE, "L'argument 'startTime'='"
               + time + "' doit être au format HH:mm:ss.", e.getMessage());
      }
   }
}
