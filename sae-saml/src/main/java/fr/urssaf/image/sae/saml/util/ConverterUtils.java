package fr.urssaf.image.sae.saml.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

/**
 * Classe utilitaire de convertion
 * 
 * 
 */
public final class ConverterUtils {

   private ConverterUtils() {

   }

   /**
    * conversion de {@link DateTime} vers {@link Date}
    * 
    * @param datetime
    *           date à convertir
    * @return date convertie
    */
   public static Date date(DateTime datetime) {

      Date date = null;

      if (datetime != null) {
         date = datetime.toDate();
      }

      return date;
   }

   /**
    * conversion de {@link String} vers {@link UUID}
    * 
    * @param name
    *           uuid sous forme de chaine de caractère
    * @return UUID répresentant cette chaine
    */
   public static UUID uuid(String name) {

      return UUID.fromString(name);
   }

   /**
    * conversion de {@link String} vers {@link URI}<br>
    * Lève dune exception {@link IllegalArgumentException} instanciant
    * l'exception {@link URISyntaxException} si la chaine ne peut être converti
    * en uri
    * 
    * @param str
    *           uri sous forme de charactère
    * @return URI répresentant cette chaine
    */
   public static URI uri(String str) {

      URI uri = null;

      if (str != null) {
         try {
            uri = new URI(str);
         } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
         }
      }

      return uri;
   }

}
