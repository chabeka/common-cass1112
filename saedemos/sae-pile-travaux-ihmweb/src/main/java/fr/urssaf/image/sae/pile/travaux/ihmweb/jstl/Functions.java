package fr.urssaf.image.sae.pile.travaux.ihmweb.jstl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;


/**
 * Fonctions JSTL
 */
public final class Functions {

   
   private Functions() {
      
   }
   

   /**
    * Convertit une Date/Heure au format JJ/MM/AAAA HH:MM:SS:FFF
    * @param date
    * @return la date formatée
    */
   public static String formateDateTime(Date date) {
      
      if (date==null) {
         return StringUtils.EMPTY;
      } else {
         
         String pattern = "dd/MM/yyyy HH'h'mm sss's' SSS'ms'";
         DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.FRENCH);
         String dateFormatee = dateFormat.format(date);

         return dateFormatee;
         
      }
      
   }
   
   
   public static String ajouteProprietaireDuPC(String hostname) {
      
      if (StringUtils.equalsIgnoreCase(hostname, "CER69-TEC16803")) {
         return hostname + " (PMA)";
      } else if (StringUtils.equalsIgnoreCase(hostname, "CER69-TEC16771")) {
         return hostname + " (BBA)";
      } else if (StringUtils.equalsIgnoreCase(hostname, "CER69-TEC16817")) {
         return hostname + " (FBO)";
      } else {
         return hostname;
      }
      
   }
   
   
   public static String nl2br(String chaine) {
      
      return StringUtils.replace(chaine, "\n", "<br />");
      
   }
   
}
