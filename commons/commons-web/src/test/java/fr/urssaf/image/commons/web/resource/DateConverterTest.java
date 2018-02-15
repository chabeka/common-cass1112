package fr.urssaf.image.commons.web.resource;

import static org.junit.Assert.assertEquals;

import org.springframework.core.convert.ConversionFailedException;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD")
public class DateConverterTest {

   private static final String DATE_FORMAT = "dd/MM/yyyy";

   private DateConverter dateConverter;

   @Before
   public void before() {

      dateConverter = new DateConverter(DATE_FORMAT);
   }

   @Test
   public void converterSuccess() {

      String dateField = "12/10/1999";

      assertEquals(dateField, DateFormatUtils.format(dateConverter
            .convert(dateField), DATE_FORMAT));
   }
   
   @Test(expected = ConversionFailedException.class)
   public void converterFailure_string() {

      dateConverter.convert("aaa");
   }

   @Test(expected = ConversionFailedException.class)
   public void converterFailure_format() {

      dateConverter.convert("12-12-1999");
   }
   
   @Test(expected = ConversionFailedException.class)
   public void converterFailure_notexist() {

      dateConverter.convert("29/02/1999");
   }
}
