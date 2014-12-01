package fr.urssaf.image.sae.droit.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Enumeration pour les différents mode de validation. Nécessaire pour le bean
 * {@link FormatProfil}
 * 
 */
public enum EnumValidationMode {

   MONITOR,

   STRICT;

   /**
    * Permets de vérifier si le String donné en paramètre fais partie de l'enum.
    * 
    * @param str
    *           String à vérifier.
    * @return vrai si contenu dans l'Enum.
    */
   public static boolean contains(String str) {

      try {
         EnumValidationMode.valueOf(StringUtils.upperCase(str));
         return true;

      } catch (Exception e) {
         return false;
      }
   }

}
