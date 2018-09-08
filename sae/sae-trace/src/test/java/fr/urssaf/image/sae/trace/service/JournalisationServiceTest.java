/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.model.JournalisationType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class JournalisationServiceTest {

   private static final String ARG_0 = "{0}";
   private static final String MESSAGE_OK = "le message d'erreur doit etre correct";
   private static final String ILLEGAL_EXPECTED = "Une exception IllegalArgumentException est attendue";
   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   @Autowired
   private JournalisationService service;

   @Test
   public void testExportTypeObligatoire() {

      try {
         service.exporterTraces(null, null, null);
         Assert.fail("IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testExportRepertoireObligatoire() {

      try {
         service.exporterTraces(JournalisationType.JOURNALISATION_EVT, null,
               null);
         Assert.fail("IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "repertoire"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testExportDateObligatoire() {

      try {
         service.exporterTraces(JournalisationType.JOURNALISATION_EVT, "dd",
               null);
         Assert.fail("IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testExportRepertoireExisteObligatoire() {

      try {
         service.exporterTraces(JournalisationType.JOURNALISATION_EVT, "dd",
               new Date());
         Assert.fail("IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, "le répertoire n'existe pas",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testExportCheminPasRepertoireObligatoire() throws IOException {

      File file = File.createTempFile("ecrit", ".tmp");

      try {
         service.exporterTraces(JournalisationType.JOURNALISATION_EVT, file.getAbsolutePath(),
               new Date());
         Assert.fail("IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK,
               "le chemin spécifié n'est pas un répertoire", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      
      } finally {
         FileUtils.deleteQuietly(file);
      }

   }
   
   @Test
   public void testRecupererTypeObligatoire() {

      try {
         service.recupererDates(null);
         Assert.fail("IllegalArgumentException attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }
}