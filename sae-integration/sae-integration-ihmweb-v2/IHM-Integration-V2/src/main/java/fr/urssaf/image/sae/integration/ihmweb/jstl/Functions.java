package fr.urssaf.image.sae.integration.ihmweb.jstl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.utils.BooleanUtils;


/**
 * Fonctions JSTL
 */
public final class Functions {

   
   private Functions() {
      
   }
   

   /**
    * Convertit une valeur de type boolean en chaîne de caractères "Oui" ou "Non"
    * 
    * @param value le boolean
    * @return la chaîne de caractères "Oui" ou "Non"
    */
   public static String booleanToOuiNon(boolean value) {
      return BooleanUtils.booleanToOuiNon(value);
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
         DateFormat dateFormat = new SimpleDateFormat(pattern);
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
