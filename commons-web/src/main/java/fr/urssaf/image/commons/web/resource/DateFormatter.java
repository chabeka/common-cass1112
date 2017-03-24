package fr.urssaf.image.commons.web.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * Instanciation de {@link Formatter} pour {@link Date}<br>
 * 
 * 
 */
public class DateFormatter implements Formatter<Date> {

   private final String pattern;

   /**
    * Initialisation du format de la date compatible avec
    * {@link SimpleDateFormat}<br>
    * <br>
    * ex: dd/MM/yyyy
    * 
    * @param pattern
    *           format de la date
    */
   public DateFormatter(String pattern) {
      this.pattern = pattern;
   }

   /**
    * Convertie une date en chaine de caractère
    * 
    * {@inheritDoc}
    */
  
   public final String print(Date date, Locale locale) {

      return getFormat(locale).format(date);
   }

   /**
    * Convertie une chaine de caractère en date<br>
    * <br>
    * exemples:
    * <ul>
    * <li>12/12/1999 : ok</li>
    * <li>29/02/1999 : ko car n'existe pas</li>
    * <li>12-12-1999 : ko car pas au bon format</li>
    * <li>aaa : ko car n'est absolument pas une date</li>
    * </ul>
    * <br>
    * 
    * {@inheritDoc}
    */
   @Override
   public final Date parse(String source, Locale locale) throws ParseException {

      return getFormat(locale).parse(source);

   }

   private SimpleDateFormat getFormat(Locale locale) {
      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
      dateFormat.setLenient(false);

      return dateFormat;
   }
}
