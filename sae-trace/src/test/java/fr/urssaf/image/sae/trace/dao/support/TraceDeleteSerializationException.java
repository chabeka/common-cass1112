/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.SerializationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test2.xml" })
public class TraceDeleteSerializationException {


   @Autowired
   private TraceRegTechniqueSupport supportTraceRegTechnique;
   
   @Autowired
   private TraceJournalEvtSupport supportTraceJournalEvt;
   
   @Autowired
   private CassandraServerBean server;



   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testDeleteExceptionSerialization() {

      GregorianCalendar gc = (GregorianCalendar) GregorianCalendar
            .getInstance();

      gc.set(GregorianCalendar.DATE, 5);
      gc.set(GregorianCalendar.MONTH, 6 - 1);
      gc.set(GregorianCalendar.YEAR, 2013);
      Date date = gc.getTime();
      long nbTracesPurgees = 0;
      try {
         nbTracesPurgees = supportTraceRegTechnique.delete(date, date.getTime());

         Assert.assertEquals("Le nombre de traces purgées doit être 1", 1,
               nbTracesPurgees);

      } catch (Exception exception) {
         Assert.fail("aucune erreur attendue");
      }
      
      try {
         nbTracesPurgees = supportTraceJournalEvt.delete(date, date.getTime());
         Assert.fail("SerializationException attendue");
      } catch (Exception exception) {
         Assert.assertEquals("Exception de type SerializationException",
               SerializationException.class, exception.getClass());
      }

      gc.set(GregorianCalendar.DATE, 17);
      gc.set(GregorianCalendar.MONTH, 6 - 1);
      gc.set(GregorianCalendar.YEAR, 2013);
      Date date2 = gc.getTime();

      try {
         nbTracesPurgees = supportTraceRegTechnique.delete(date2, date2.getTime());
         Assert.fail("SerializationException attendue");
      } catch (Exception exception) {
         Assert.assertEquals("Exception de type SerializationException",
               SerializationException.class, exception.getClass());
      }
      
      try {
         nbTracesPurgees = supportTraceJournalEvt.delete(date2, date2.getTime());

         Assert.assertEquals("Le nombre de traces purgées doit être 1", 1,
               nbTracesPurgees);

      } catch (Exception exception) {
         Assert.fail("aucune erreur attendue");
      }

   }

}
