package fr.urssaf.image.sae.webdemo.service.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateQueryException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.web.resource.DateConverter;
import fr.urssaf.image.sae.webdemo.resource.Dir;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@SuppressWarnings("PMD")
public class LogDAOTest {

   private static final int FIRST_RESULT = 10;
   private static final int MAX_RESULTS = 100;

   @Autowired
   private LogDAO service;

   private final static String[] COLONNES = new String[] { "idseq",
         "horodatage", "occurences", "probleme", "action", "infos" };

   private static Date date_1;

   private static Date date_2;

   @BeforeClass
   public static void beforeClass() {

      DateConverter dateFactory = new DateConverter("dd/MM/yyyy");

      date_1 = dateFactory.convert("01/12/2010");
      date_2 = dateFactory.convert("15/12/2010");
   }

   @Test
   public void findSuccess() {

      for (String sort : COLONNES) {

         assertFind(sort, Dir.DESC);
         assertFind(sort, Dir.ASC);
      }

      assertFind(null, null);

   }

   @Test
   public void findSuccess_nosort() {

      assertFind(null, null);

   }

   private void assertFind(String sort, Dir dir) {

      assertFind(24, 34, sort, dir, date_1, date_2);
      assertFind(41, 51, sort, dir, date_1, null);
      assertFind(100, 983, sort, dir, null, date_2);
   }

   private void assertFind(int results, int total, String sort, Dir dir,
         Date start, Date end) {

      assertEquals("la colonne '" + sort + "' n'est pas triable", results,
            service.find(FIRST_RESULT, MAX_RESULTS, sort, dir, start, end)
                  .size());

      assertEquals(total, service.count(start, end));
   }

   @Test(expected = HibernateQueryException.class)
   public void findFailure() {

      service.find(FIRST_RESULT, MAX_RESULTS, "inconnu", Dir.DESC, date_1,
            date_2);

   }
}
