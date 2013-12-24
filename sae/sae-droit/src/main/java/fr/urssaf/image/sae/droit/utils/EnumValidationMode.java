package fr.urssaf.image.sae.droit.utils;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.droit.dao.model.FormatProfil;

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
         boolean retour = false;
         if (!StringUtils.isBlank(str)
               && (StringUtils.equalsIgnoreCase(str, MONITOR.toString()) || StringUtils
                     .equalsIgnoreCase(str, STRICT.toString()))) {
            retour = true;
         }
         return retour;
      } catch (Exception e) {
         return false;
      }
   }

}
