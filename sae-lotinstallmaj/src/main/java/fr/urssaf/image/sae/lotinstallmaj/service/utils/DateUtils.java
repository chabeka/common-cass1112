package fr.urssaf.image.sae.lotinstallmaj.service.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe de génération de date par défaut
 */
public class DateUtils {


   public static Date getTracabiliteDerniereDateTraitee() {

      // On démarre la traçabilité SAE au 01/02/2013
      // La dernière date des traitements est positionné au 31/01/2013
      // Car les traitements font un J+1 à partir des valeurs de paramètres

      final Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 2013);
      calendar.set(Calendar.MONTH, 0); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, 31);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      return calendar.getTime();
   }

   public static Date getRndDate() {

      final Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 1970);
      calendar.set(Calendar.MONTH, 0); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      return calendar.getTime();
   }

   public static Date getCorbeilleDerniereDateTraitee() {
      // La dernière date des traitements est positionné au 01/06/2016
      final Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 2016);
      calendar.set(Calendar.MONTH, 5); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      return calendar.getTime();
   }

}
