package fr.urssaf.image.sae.storage.dfce.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD.MethodNamingConventions")
public class LocalTimeUtilsTest {

   @Test
   public void isValidate_success() {

      // heure à minuit
      assertTrueIsValidate("00:12:54");
      assertTrueIsValidate("0:12:54");
      assertTrueIsValidate("00:00:00");
      assertTrueIsValidate("0:00:00");

      // heure du matin
      assertTrueIsValidate("09:12:54");
      assertTrueIsValidate("2:0:0");
      assertTrueIsValidate("9:48:45");

      // heure du soir
      assertTrueIsValidate("16:24:18");

   }

   private void assertTrueIsValidate(String time) {

      boolean isValidate = LocalTimeUtils.isValidate(time);
      Assert.assertTrue("Le format '" + time + "' doit être au bon format",
            isValidate);
   }

   @Test
   public void isValidate_failure() {

      // chaine de caractères
      assertFalseIsValidate("09:4b:45");

      // heure > 23
      assertFalseIsValidate("24:42:45");

      // minutes > 59
      assertFalseIsValidate("09:60:45");

      // secondes > 59
      assertFalseIsValidate("09:45:60");

      // oubli des secondes
      assertFalseIsValidate("09:45");

   }

   private void assertFalseIsValidate(String time) {

      boolean isValidate = LocalTimeUtils.isValidate(time);
      Assert.assertFalse("Le format '" + time
            + "' ne doit pas être au bon format", isValidate);
   }

   @Test
   public void getDifference() throws ParseException {

      String startTime = "03:00:00";
      int delay = 120;

      // avant le start time
      assertDiffTime(-1, "01-01-1999 02:59:59", startTime, delay);

      // debut du start time
      assertDiffTime(120, "01-01-1999 03:00:00", startTime, delay);
      assertDiffTime(119, "01-01-1999 03:00:01", startTime, delay);

      // à la fin du delai
      assertDiffTime(1, "01-01-1999 03:01:59", startTime, delay);
      assertDiffTime(0, "01-01-1999 03:02:00", startTime, delay);

      // après le delai
      assertDiffTime(-1, "01-01-1999 03:02:01", startTime, delay);

   }

   private void assertDiffTime(int expectedDiffTime, String date,
         String startTime, int delay) throws ParseException {

      Date currentDate = DateUtils.parseDate(date,
            new String[] { "dd-MM-yyyy HH:mm:ss" });

      LocalDateTime currentDateTime = LocalDateTime.fromDateFields(currentDate);
      LocalTime startLocalTime = LocalTimeUtils.parse(startTime);

      long diffTime = LocalTimeUtils.getDifference(currentDateTime,
            startLocalTime, delay);

      Assert.assertEquals("L'heure courante de '" + date + "' doit être après "
            + startTime + " dans la limite de " + delay + " secondes",
            expectedDiffTime, diffTime);

   }

}
