package fr.urssaf.image.sae.igc.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

/**
 * Classe utilitaire de manipulation des messages paramétrables
 * 
 * 
 */
public final class TextUtils {

   private TextUtils() {

   }

   /**
    * Création d'un message à partir d'un modèle où les ${} sont des valeurs
    * dynamiques<br>
    * les valeurs dynamiques sont de type ${[0-9]+} où le chiffre indique
    * l'ordre des valeurs<br>
    * <br>
    * exemples:<br>
    * <code>"l'utilisateur ${0} est  né le '${1}'"</code> avec
    * <code>"pierre"</code> et <code>"05/02/1988"</code> :
    * <code>"l'utilisateur pierre est  né le '05/02/1988'"</code>
    * 
    * 
    * @param source
    *           message paramétrable
    * @param args
    *           valeurs ordonnées des paramètres
    * @return instance du message paramétré
    */
   public static String getMessage(String source, String... args) {

      Map<String, String> values = new HashMap<String, String>();

      int index = 0;
      for (String value : args) {
         values.put(String.valueOf(index), value);
         index++;
      }

      return StrSubstitutor.replace(source, values);
   }

   public static final String ARG_EMPTY = "Le paramètre [${0}] n'est pas renseigné alors qu'il est obligatoire";


   /**
    * Création d'un message à partir de {@value #ARG_EMPTY}
    * 
    * @param arg nom de l'argument non renseigné
    * @return instance du message paramétré
    */
   public static String getArgEmpty(String arg) {

      return TextUtils.getMessage(ARG_EMPTY, arg);
   }
}
