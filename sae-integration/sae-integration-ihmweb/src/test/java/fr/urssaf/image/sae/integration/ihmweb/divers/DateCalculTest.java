package fr.urssaf.image.sae.integration.ihmweb.divers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Des calculs de dates
 */
public class DateCalculTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(DateCalculTest.class);

   
   @Ignore
   @Test
   public void calculeDateFinConservation() {

      int anneeDebutConservation = 2011;
      int moisDebutConservation = 10;
      int jourDebutConservation = 13;

      int dureeConservation = 1825;

      Calendar calendar = Calendar.getInstance();

      calendar.set(Calendar.YEAR, anneeDebutConservation);
      calendar.set(Calendar.MONTH, moisDebutConservation - 1); // les numéros de
                                                               // mois
                                                               // commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, jourDebutConservation);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      Date dateDebutConservation = calendar.getTime();

      Date dateFinConservation = DateUtils.addDays(dateDebutConservation,
            dureeConservation);

      String pattern = "yyyy-MM-dd";
      DateFormat dateFormat = new SimpleDateFormat(pattern);

      String dateFormatee = dateFormat.format(dateFinConservation);

      LOG.debug(dateFormatee);

   }

   
   @Ignore
   @Test
   public void calculeDateFinConservation2() {

      Date dateDuJour = new Date();

      int dureeConservation = 1825;

      Date dateFinConservation = DateUtils.addDays(dateDuJour,
            dureeConservation);

      String pattern = "yyyy-MM-dd";
      DateFormat dateFormat = new SimpleDateFormat(pattern);

      String dateFormatee = dateFormat.format(dateFinConservation);

      LOG.debug(dateFormatee);

   }

}
