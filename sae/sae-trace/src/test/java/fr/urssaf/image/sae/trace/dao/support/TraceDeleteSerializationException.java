/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.Calendar;
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
    server.resetDataOnly();
  }

  @Test
  public void testDeleteExceptionSerialization() {

    final GregorianCalendar gc = (GregorianCalendar) Calendar
        .getInstance();

    gc.set(Calendar.DATE, 5);
    gc.set(Calendar.MONTH, 6 - 1);
    gc.set(Calendar.YEAR, 2013);
    final Date date = gc.getTime();
    long nbTracesPurgees = 0;
    try {
      nbTracesPurgees = supportTraceRegTechnique.delete(date, date.getTime(), 500);

      Assert.assertEquals("Le nombre de traces purgées doit être 1", 1,
                          nbTracesPurgees);

    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }

    try {
      nbTracesPurgees = supportTraceJournalEvt.delete(date, date.getTime(), 500);
      Assert.fail("SerializationException attendue");
    } catch (final Exception exception) {
      Assert.assertEquals("Exception de type SerializationException",
                          SerializationException.class, exception.getClass());
    }

    gc.set(Calendar.DATE, 17);
    gc.set(Calendar.MONTH, 6 - 1);
    gc.set(Calendar.YEAR, 2013);
    final Date date2 = gc.getTime();

    try {
      nbTracesPurgees = supportTraceRegTechnique.delete(date2, date2.getTime(), 500);
      Assert.fail("SerializationException attendue");
    } catch (final Exception exception) {
      Assert.assertEquals("Exception de type SerializationException",
                          SerializationException.class, exception.getClass());
    }

    try {
      nbTracesPurgees = supportTraceJournalEvt.delete(date2, date2.getTime(), 500);

      Assert.assertEquals("Le nombre de traces purgées doit être 1", 1,
                          nbTracesPurgees);

    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }

  }

}
