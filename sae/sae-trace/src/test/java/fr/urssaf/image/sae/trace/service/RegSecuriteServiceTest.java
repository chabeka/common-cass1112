/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;

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
public class RegSecuriteServiceTest {

   @Autowired
   private RegSecuriteService service;

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   @Test
   public void testLectureIdentifiantObligatoire() {

      try {
         service.lecture(null);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "identifiant"),
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testLectureDateDebutObligatoire() {

      try {
         service.lecture(null, null, 0);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "date de début"),
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testLectureDateFinObligatoire() {

      try {
         service.lecture(new Date(), null, 0);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "date de fin"),
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testLectureDateDebutInfDateFin() {

      try {
         service.lecture(DateUtils.addHours(new Date(), 2), new Date(), 0);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "la date de début doit être inférieure à la date de fin",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testLectureDateDebutEqDateFin() {

      try {
         Date date = new Date();
         service.lecture(date, date, 0);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "la date de début doit être inférieure à la date de fin",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testLectureLimiteObligatoire() {

      try {
         service.lecture(new Date(), DateUtils.addHours(new Date(), 2), 0);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "limite"), exception
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testPurgeDateDebutObligatoire() {

      try {
         service.purge(null, null);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "date de début"),
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testPurgeDateFinObligatoire() {

      try {
         service.purge(new Date(), null);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               StringUtils.replace(MESSAGE_ERREUR, "{0}", "date de fin"),
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testPurgeDateDebutInfDateFin() {

      try {
         service.purge(DateUtils.addHours(new Date(), 2), new Date());
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "la date de début doit être inférieure à la date de fin",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

   @Test
   public void testPurgeDateDebutEqDateFin() {

      try {
         Date date = new Date();
         service.purge(date, date);
         Assert.fail("Une exception IllegalArgumentException est attendue");

      } catch (IllegalArgumentException exception) {
         Assert.assertEquals("le message d'erreur doit etre correct",
               "la date de début doit être inférieure à la date de fin",
               exception.getMessage());

      } catch (Exception exception) {
         Assert.fail("Une exception IllegalArgumentException est attendue");
      }

   }

}
