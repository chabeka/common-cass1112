package fr.urssaf.image.commons.util.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;


/**
 * Tests unitaires de la classe {@link DateCompareUtil}
 */
@SuppressWarnings("PMD")
public class DateCompareUtilTest {
   
   
   private static Date debutOctobre() {
      // 01/10/2010 11:08:45:364
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR,2010);
      calendar.set(Calendar.MONTH,9); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH,01);
      calendar.set(Calendar.HOUR_OF_DAY, 11);
      calendar.set(Calendar.MINUTE, 8);
      calendar.set(Calendar.SECOND, 45);
      calendar.set(Calendar.MILLISECOND, 364);
      Date laDate = calendar.getTime();
      return laDate;
   }
   
   
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
   
   
   private static Date aujourdhuiApresMinuit() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, 11);
      calendar.set(Calendar.MINUTE, 8);
      calendar.set(Calendar.SECOND, 45);
      calendar.set(Calendar.MILLISECOND, 364);
      Date laDate = calendar.getTime();
      return laDate;
   }
   
   
   private static Date hier() {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DATE, -1);
      Date laDate = calendar.getTime();
      return laDate;
   }
   
   
   /**
    * Test du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(DateCompareUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateCompareUtil#sup(Date, Date)}
    */
   @Test
   public void sup() {
   
      Boolean result;
      Date date1;
      Date date2;
      
      date1 = null;
      date2 = null;
      result = DateCompareUtil.sup(date1, date2);
      assertFalse("La comparaison de dates a échoué",result);
      
      date1 = debutOctobre();
      date2 = null;
      result = DateCompareUtil.sup(date1, date2);
      assertTrue("La comparaison de dates a échoué",result);
      
      date1 = null;
      date2 = debutOctobre();
      result = DateCompareUtil.sup(date1, date2);
      assertFalse("La comparaison de dates a échoué",result);
      
      date1 = debutOctobre();
      date2 = finOctobre();
      result = DateCompareUtil.sup(date1, date2);
      assertFalse("La comparaison de dates a échoué",result);
      
      date1 = finOctobre();
      date2 = debutOctobre();
      result = DateCompareUtil.sup(date1, date2);
      assertTrue("La comparaison de dates a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateCompareUtil#inf(Date, Date)}
    */
   @Test
   public void inf() {
   
      Boolean result;
      Date date1;
      Date date2;
      
      date1 = null;
      date2 = null;
      result = DateCompareUtil.inf(date1, date2);
      assertFalse("La comparaison de dates a échoué",result);
      
      date1 = debutOctobre();
      date2 = null;
      result = DateCompareUtil.inf(date1, date2);
      assertFalse("La comparaison de dates a échoué",result);
      
      date1 = null;
      date2 = debutOctobre();
      result = DateCompareUtil.inf(date1, date2);
      assertTrue("La comparaison de dates a échoué",result);
      
      date1 = debutOctobre();
      date2 = finOctobre();
      result = DateCompareUtil.inf(date1, date2);
      assertTrue("La comparaison de dates a échoué",result);
      
      date1 = finOctobre();
      date2 = debutOctobre();
      result = DateCompareUtil.inf(date1, date2);
      assertFalse("La comparaison de dates a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateCompareUtil#futur(Date)}
    */
   @Test
   public void futur() {
      
      Boolean result;
      Date date;
      
      date = null;
      result = DateCompareUtil.futur(date);
      assertFalse("La comparaison de dates a échoué",result);
      
      date = aujourdhuiApresMinuit();
      result = DateCompareUtil.futur(date);
      assertTrue("La comparaison de dates a échoué",result);
      
      date = hier();
      result = DateCompareUtil.futur(date);
      assertFalse("La comparaison de dates a échoué",result);
   
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DateCompareUtil#past(Date)}
    */
   @Test
   public void past() {
      
      Boolean result;
      Date date;
      
      date = null;
      result = DateCompareUtil.past(date);
      assertTrue("La comparaison de dates a échoué",result);
      
      date = aujourdhuiApresMinuit();
      result = DateCompareUtil.past(date);
      assertFalse("La comparaison de dates a échoué",result);
      
      date = hier();
      result = DateCompareUtil.past(date);
      assertTrue("La comparaison de dates a échoué",result);
   
   }

}
