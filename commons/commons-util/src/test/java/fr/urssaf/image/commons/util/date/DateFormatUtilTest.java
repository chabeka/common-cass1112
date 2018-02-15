package fr.urssaf.image.commons.util.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link DateFormatUtil}  
 */
@SuppressWarnings("PMD")
public class DateFormatUtilTest {

   
   private static Date finOctobre() {
      // 31/10/2010 11:08:45:364
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR,2010);
      calendar.set(Calendar.MONTH,9); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH,31);
      calendar.set(Calendar.HOUR_OF_DAY, 11);
      calendar.set(Calendar.MINUTE, 8);
      calendar.set(Calendar.SECOND, 45);
      calendar.set(Calendar.MILLISECOND, 364);
      Date laDate = calendar.getTime();
      return laDate;
   }
   
   
   private static Date debutOctobre() {
      // 01/10/2010 11:08:45:364
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR,2010);
      calendar.set(Calendar.MONTH,9); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH,1);
      calendar.set(Calendar.HOUR_OF_DAY, 11);
      calendar.set(Calendar.MINUTE, 8);
      calendar.set(Calendar.SECOND, 45);
      calendar.set(Calendar.MILLISECOND, 364);
      Date laDate = calendar.getTime();
      return laDate;
   }
   
   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(DateFormatUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test unitaire de la méthode {@link DateFormatUtil#getFormatFR()}
    */
   @Test
   public void getFormatFR() {
      
      SimpleDateFormat leFormatFr = DateFormatUtil.getFormatFR();
      
      Date date = finOctobre();
      
      String actual = leFormatFr.format(date);
      
      String expected = "31/10/2010";
      
      assertEquals("Le formatage de la date en JJ/MM/AAAA est erroné",expected,actual);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateFormatUtil#date(Date, SimpleDateFormat)}
    */
   @Test
   public void date() {
      
      Date date;
      SimpleDateFormat format;
      String result;
      
      date = null;
      format = null;
      result = DateFormatUtil.date(date, format);
      assertNull("Le formatage de la date est erroné",result);
      
      date = null;
      format = new SimpleDateFormat("yyyy");
      result = DateFormatUtil.date(date, format);
      assertNull("Le formatage de la date est erroné",result);
      
      date = finOctobre();
      format = null;
      result = DateFormatUtil.date(date, format);
      assertNull("Le formatage de la date est erroné",result);
      
      date = finOctobre();
      format = new SimpleDateFormat("yyyy");
      result = DateFormatUtil.date(date, format);
      assertEquals("Le formatage de la date est erroné","2010",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateFormatUtil#dateFr(Date)}
    */
   @Test
   public void dateFr() {
      
      Date date ;
      String result;
      
      date = null;
      result = DateFormatUtil.dateFr(date);
      assertNull("Le formatage de la date est erroné",result);
      
      date = finOctobre();
      result = DateFormatUtil.dateFr(date);
      assertEquals("Le formatage de la date est erroné","31/10/2010",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateFormatUtil#todayFr()}
    */
   @Test
   public void todayFr() {
      
      String resultat = DateFormatUtil.todayFr();
      
      // Il est très difficile de vérifier le résultat de cette méthode,
      // qui renvoie la valeur de "maintenant"

      // On vérifie simplement quelques "indicateurs" de résultat
      
      // JJ/MM/AAAA
      
      assertEquals("La date renvoyée n'est pas au bon format",10,resultat.length());
      assertEquals("La date renvoyée n'est pas au bon format","/",resultat.substring(2, 3));
      assertEquals("La date renvoyée n'est pas au bon format","/",resultat.substring(5, 6));
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateFormatUtil#date(java.util.Collection, String)}
    */
   @Test
   public void dateListe() {
      
      List<Date> dates ;
      String pattern;
      List<String> resultat;
      
      dates = null;
      pattern = null;
      resultat = DateFormatUtil.date(dates, pattern);
      assertTrue("Le formatage de la collection de dates est incorrect",resultat.isEmpty());
      
      dates = new ArrayList<Date>();
      pattern = null;
      resultat = DateFormatUtil.date(dates, pattern);
      assertTrue("Le formatage de la collection de dates est incorrect",resultat.isEmpty());
      
      dates = new ArrayList<Date>();
      dates.add(finOctobre());
      dates.add(debutOctobre());
      pattern = "dd/MM";
      resultat = DateFormatUtil.date(dates, pattern);
      assertEquals("Le formatage de la collection de dates est incorrect",dates.size(),resultat.size());
      assertEquals("Le formatage de la collection de dates est incorrect","31/10",resultat.get(0));
      assertEquals("Le formatage de la collection de dates est incorrect","01/10",resultat.get(1));
      
   }
   
}
