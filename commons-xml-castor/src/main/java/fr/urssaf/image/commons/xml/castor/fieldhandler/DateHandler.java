package fr.urssaf.image.commons.xml.castor.fieldhandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

public class DateHandler extends GeneralizedFieldHandler {

   private static final String FORMAT = "dd/MM/yyyy";

   @Override
   /**
    * value est de type Date<br>
    * la méthode retourne la date sous forme de chaines de caractères au format dd/MM/yyyy
    */
   public Object convertUponGet(Object value) {

      String obj = null;

      if (value != null) {
         SimpleDateFormat formatter = new SimpleDateFormat(FORMAT,Locale.getDefault());
         Date date = (Date) value;
         obj = formatter.format(date);
      }

      return obj;
   }

   @Override
   /**
    * value est de type chaine de caractère<br>
    * elle représente une date au format dd/MM/yyyy<br>
    * la méthode retourne un objet de type date
    */
   public Object convertUponSet(Object value) {
      SimpleDateFormat formatter = new SimpleDateFormat(FORMAT,Locale.getDefault());
      Date date = null;
      try {
         date = formatter.parse((String) value);
      } catch (ParseException e) {
         throw new IllegalArgumentException(e);
      }
      return date;
   }

   @SuppressWarnings("unchecked")
   @Override
   public Class getFieldType() {
      return Date.class;
   }

}
