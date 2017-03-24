package fr.urssaf.image.commons.web.resource;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD")
public class DateFormatterTest {

   private static final String DATE_FORMAT = "dd/MM/yyyy";

   private DateFormatter dateFormatter;

   @Before
   public void before() {

      dateFormatter = new DateFormatter(DATE_FORMAT);
   }

   @Test
   public void parseSuccess() throws ParseException {

      String dateField = "12/10/1999";

      assertEquals(dateField, DateFormatUtils.format(dateFormatter.parse(
            dateField, Locale.getDefault()), DATE_FORMAT));

   }

   @Test(expected = ParseException.class)
   public void parseFailure_format() throws ParseException {

      String dateField = "12-10-1999";

      dateFormatter.parse(dateField, Locale.getDefault());

   }

   @Test(expected = ParseException.class)
   public void parseFailure_notexist() throws ParseException {

      String dateField = "29/02/1999";

      dateFormatter.parse(dateField, Locale.getDefault());

   }

   @Test(expected = ParseException.class)
   public void parseFailure_stringt() throws ParseException {

      String dateField = "aaaa";

      dateFormatter.parse(dateField, Locale.getDefault());

   }

   @Test
   public void print() throws ParseException {

      Date date = new Date();

      assertEquals(DateFormatUtils.format(date, DATE_FORMAT), dateFormatter
            .print(date, Locale.getDefault()));

   }

}
