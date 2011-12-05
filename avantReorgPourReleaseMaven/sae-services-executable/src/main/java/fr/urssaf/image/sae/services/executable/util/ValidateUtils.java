package fr.urssaf.image.sae.services.executable.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitaire de validation en complément de ce qui existe déjà
 * 
 * 
 */
public final class ValidateUtils {

   private ValidateUtils() {

   }

   /**
    * Vérifie qu'un élément dans un tableau {@link String} est bien renseigné.<br>
    * <br>
    * <code>isNotBlank(null,1)= false</code><br>
    * <code>isNotBlank(["toto"],1)= false</code><br>
    * <code>isNotBlank(["toto", null],1)= false</code><br>
    * <code>isNotBlank(["toto", ""],1)= false</code><br>
    * <code>isNotBlank(["toto"," "],1)= false</code><br>
    * <code>isNotBlank(["toto", "titi"],1)= true</code><br>
    * 
    * @param args
    *           tableau des éléments
    * @param index
    *           index de l'élément à vérifier
    * @return vrai si l'élement est bien renseigné, faux sinon
    */
   public static boolean isNotBlank(String[] args, int index) {

      return ArrayUtils.getLength(args) > index
            && StringUtils.isNotBlank(args[index]);
   }
}
