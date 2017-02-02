package fr.urssaf.image.sae.integration.ihmweb.utils;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;

/**
 * Utilitaires pour le type booleen
 */
public final class BooleanUtils {

   
   private BooleanUtils() {
      
   }
   
   
   /**
    * Convertit une chaîne de caractères "oui" ou "non" en boolean
    * 
    * @param ouiNon chaîne de caractères "oui" ou "non" en boolean
    * @return le boolean correspondant
    */
   public static boolean ouiNonToBoolean(String ouiNon) {
      
      String ouiNonTrim = StringUtils.trim(ouiNon);
      ouiNonTrim = ouiNonTrim.toLowerCase(Locale.FRANCE); 
      
      boolean result;
      
      if ("oui".equals(ouiNonTrim)) {
         result = true;
      } else if ("non".equals(ouiNonTrim)) {
         result = false;
      } else {
         throw new IntegrationRuntimeException("La valeur \"" + ouiNon + "\" n'a pas pu être convertie en boolean Java");
      }
         
      return result;
      
   }
   
   
   /**
    * Convertit une valeur de type boolean en chaîne de caractères "Oui" ou "Non"
    * 
    * @param value le boolean
    * @return la chaîne de caractères "Oui" ou "Non"
    */
   public static String booleanToOuiNon(boolean value) {
      
      String result;
      
      if (value) {
         result = "Oui";
      } else {
         result = "Non";
      }
      
      return result;
      
   }
   
}
