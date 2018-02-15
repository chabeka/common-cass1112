package fr.urssaf.image.commons.xml.jaxb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateConverter {

   private static final String PATTERN = "MM/dd/yyyy";
   
   private DateConverter(){
      
   }

   public static Date parseDate(String dateString) {

      try {
         return new SimpleDateFormat(PATTERN, Locale.getDefault())
               .parse(dateString);
      } catch (ParseException e) {
         throw new IllegalArgumentException(e);
      }

   }

   public static String printDate(Date date) {

      return new SimpleDateFormat(PATTERN, Locale.getDefault()).format(date);
   }

}
