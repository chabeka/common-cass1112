package fr.urssaf.image.sae.services.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitaire.
 * 
 * 
 */
public final class FormatUtils {

   public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
   public static final Locale DEFAULT_LOCAL = Locale.FRENCH;

   /**
    * Formatter la liste des code erreurs.
    * 
    * @param listCodeErrors
    *           liste des codes erreur
    * @return Une Chaîne de code erreur.
    */
   public static String formattingDisplayList(List<String> listCodeErrors) {

      StringBuilder builder = new StringBuilder();

      if (CollectionUtils.isNotEmpty(listCodeErrors)) {

         List<String> listCodeErrorsTri = new ArrayList<String>(
               new HashSet<String>(listCodeErrors));

         Collections.sort(listCodeErrorsTri);

         int index = 0;
         for (String value : listCodeErrorsTri) {
            builder.append(value);
            if (index < listCodeErrorsTri.size() - 1) {
               builder.append(", ");
            }
            index++;
         }

      }

      return builder.toString();

   }

   /** Cette classe n'est pas concue pour être instanciée. */
   private FormatUtils() {
      assert false;
   }

   /**
    * Convertit une date en chaîne
    * 
    * @param date
    *           : La date à convertir
    * @return chaîne à partir d'une date
    */
   public static String dateToString(final Date date) {
      String newDate = StringUtils.EMPTY;
      if (date != null) {
         final SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN,
               DEFAULT_LOCAL);
         newDate = formatter.format(date);
      }
      return newDate;
   }
}
