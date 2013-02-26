/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class StatusServiceTest {

   private static final String ARG_0 = "{0}";

   private static final String MESSAGE_OK = "le message d'erreur doit etre correct";

   private static final String ILLEGAL_EXPECTED = "Une exception IllegalArgumentException est attendue";

   @Autowired
   private StatusService service;

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   @Test
   public void testIsJournalisationObligatoire() {

      try {
         service.isJournalisationRunning(null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testIsPurgeObligatoire() {

      try {
         service.isPurgeRunning(null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de purge"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testUpdateJournalisationTypeObligatoire() {

      try {
         service.updateJournalisationStatus(null, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testUpdateJournalisationValeurObligatoire() {

      try {
         service.updateJournalisationStatus(
               JournalisationType.JOURNALISATION_EVT, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "valeur"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testUpdatePurgeTypeObligatoire() {

      try {
         service.updatePurgeStatus(null, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de purge"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testUpdatePurgeValeurObligatoire() {

      try {
         service.updatePurgeStatus(PurgeType.PURGE_EVT, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "valeur"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

}
