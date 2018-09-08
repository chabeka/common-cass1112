/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class JournalEvtServiceTest {

   private static final String REPERTOIRE_EXISTS = "le répertoire doit exister";

   private static final String REPERTOIRE_FICHIER = "le paramètre n'est pas un répertoire";

   private static final String DATE_DEB_INF_DATE_FIN = "la date de début doit être inférieure à la date de fin";

   private static final String ARG_0 = "{0}";

   private static final String MESSAGE_OK = "le message d'erreur doit etre correct";

   private static final String ILLEGAL_EXPECTED = "Une exception IllegalArgumentException est attendue";

   @Autowired
   private JournalEvtService service;

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   @Test
   public void testLectureIdentifiantObligatoire() {

      try {
         service.lecture(null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "identifiant"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testLectureDateDebutObligatoire() {

      try {
         service.lecture(null, null, 0, true);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date de début"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testLectureDateFinObligatoire() {

      try {
         service.lecture(new Date(), null, 0, true);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date de fin"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testLectureDateDebutInfDateFin() {

      try {
         service
               .lecture(DateUtils.addHours(new Date(), 2), new Date(), 0, true);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, DATE_DEB_INF_DATE_FIN, exception
               .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testLectureDateDebutEqDateFin() {

      try {
         Date date = new Date();
         service.lecture(date, date, 0, true);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, DATE_DEB_INF_DATE_FIN, exception
               .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testLectureLimiteObligatoire() {

      try {
         service
               .lecture(new Date(), DateUtils.addHours(new Date(), 2), 0, true);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "limite"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testPurgeDateDebutObligatoire() {

      try {
         service.purge(null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date de purge"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }
   
   @Test
   public void testHasRecordsDebutObligatoire() {

      try {
         service.hasRecords(null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }

   }

   @Test
   public void testExportDateObligatoire() {
      try {
         service.export(null, null, null, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date d'export"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }
   }

   @Test
   public void testExportRepertoireObligatoire() {
      try {
         service.export(new Date(), null, null, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "répertoire"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }
   }
   
   @Test
   public void testExportIdObligatoire() {
      try {
         service.export(new Date(), "fichier", null, null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "identifiant du journal précédent"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }
   }
   
   @Test
   public void testExportHashObligatoire() {
      try {
         service.export(new Date(), "fichier", "c", null);
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "hash du journal précédent"), exception.getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }
   }
   
   
   

   @Test
   public void testExportRepertoireExisteObligatoire() {
      try {
         service.export(new Date(), "fichierInexistant", "c", "d");
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, REPERTOIRE_EXISTS, exception
               .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);
      }
   }

   @Test
   public void testExportRepertoireIsRepertoireObligatoire() throws IOException {

      File file = File.createTempFile("repertoire", ".tmp");

      try {
         FileUtils.writeStringToFile(file,
               "ceci est un fichier et non un répertoire");
         service.export(new Date(), file.getAbsolutePath(), "c", "d");
         Assert.fail(ILLEGAL_EXPECTED);

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals(MESSAGE_OK, REPERTOIRE_FICHIER, exception
               .getMessage());

      } catch (Exception exception) {
         Assert.fail(ILLEGAL_EXPECTED);

      } finally {
         FileUtils.deleteQuietly(file);
      }
   }

}
