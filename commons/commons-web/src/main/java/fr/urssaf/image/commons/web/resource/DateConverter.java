package fr.urssaf.image.commons.web.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * Instanciation de {@link Converter} pour {@link Date}<br>
 * 
 * 
 */
public class DateConverter implements Converter<String, Date> {

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
   public DateConverter(String pattern) {
      this.pattern = pattern;
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
    * 
    * @param source
    *           date sous forme d'une chaine de caractères
    * @return source converti en date
    * @throws ConversionFailedException
    */
   @Override
   public final Date convert(String source) {

      Date date = null;
      if (StringUtils.hasText(source)) {
         try {
            date = getFormat().parse(source);
         } catch (ParseException e) {
            throw new ConversionFailedException(TypeDescriptor
                  .valueOf(String.class), TypeDescriptor.valueOf(Date.class),
                  source, e);
         }
      }
      return date;
   }

   private SimpleDateFormat getFormat() {
      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale
            .getDefault());
      dateFormat.setLenient(false);

      return dateFormat;
   }
}
