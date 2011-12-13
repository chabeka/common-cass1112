package fr.urssaf.image.sae.storage.dfce.services.support.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.services.support.InterruptionTraitementSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/appliContext-sae-storage-dfce-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class InterruptionTraitementSupportValidationTest {

   @Autowired
   private InterruptionTraitementSupport support;

   private static final String FAIL_MESSAGE = "Une exception de type llegalArgumentException doit être levée";

   private static final String EXCEPTION_MESSAGE = "Le message de l'exception est inattendu";

   private static final String ARG_START = "12:37:54";

   private static final int ARG_DELAY = 120;

   private static final int ARG_TENTATIVES = 2;

   @Test
   public void interruption_success() {

      InterruptionTraitementSupport support = new InterruptionTraitementSupport() {

         @Override
         public void interruption(String start, int delay, int tentatives) {
            // implémentation vide
         }

      };

      support.interruption("02:24:18", 120, 2);

   }

   @Test
   public void interruption_failure_start_empty() {

      assertInterruptionStart(" ");
      assertInterruptionStart("");
      assertInterruptionStart(null);

   }

   private void assertInterruptionStart(String start) {

      try {

         support.interruption(start, ARG_DELAY, ARG_TENTATIVES);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'startTime' doit être renseigné.", e.getMessage());
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

      try {

         support.interruption(time, ARG_DELAY, ARG_TENTATIVES);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE, "L'argument 'startTime'='"
               + time + "' doit être au format HH:mm:ss.", e.getMessage());
      }
   }

   @Test
   public void interruption_failure_delay_negatif() {

      assertInterruptionDelay(0);
      assertInterruptionDelay(-2);
   }

   private void assertInterruptionDelay(int delay) {

      try {

         support.interruption(ARG_START, delay, ARG_TENTATIVES);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'delay' doit être au moins égal à 1.", e
                     .getMessage());
      }
   }

   @Test
   public void interruption_failure_tentatives_negatif() {

      assertInterruptionTentatives(0);
      assertInterruptionTentatives(-2);
   }

   private void assertInterruptionTentatives(int tentatives) {

      try {

         support.interruption(ARG_START, ARG_DELAY, tentatives);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'tentatives' doit être au moins égal à 1.", e
                     .getMessage());
      }
   }
}
