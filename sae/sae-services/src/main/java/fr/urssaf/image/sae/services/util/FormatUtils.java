package fr.urssaf.image.sae.services.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.metadata.utils.Utils;

/**
 * Classe utilitaire.
 * 
 * 
 */
public class FormatUtils {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatUtils.class);
   public static final String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";
   public static final Locale DEFAULT_LOCAL = Locale.FRENCH;

   /**
    * Formatter la liste des code erreurs.
    * 
    * @param list
    * @return Une Chaîne de code erreur.
    */
   public static String formattingDisplayList(List<String> listCodeErrors) {
      StringBuilder builder = new StringBuilder();
      listCodeErrors = new ArrayList<String>(
            new HashSet<String>(listCodeErrors));
      Collections.sort(listCodeErrors);
      int i = 0;
      for (String value : Utils.nullSafeIterable(listCodeErrors)) {
         builder.append(value);
         if (i < listCodeErrors.size() - 1) {
            builder.append(", ");
         }
         i++;
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
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static String dateToString(final Date date) {
      String newDate = StringUtils.EMPTY;
      try {
         if (date != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat(
                  DATE_PATTERN, DEFAULT_LOCAL);
            newDate = formatter.format(date);
         }
      } catch (Exception e) {
         LOGGER.error("Erreur de parsing. Détail : {}", e.getMessage());
      }
      return newDate;
   }
}
