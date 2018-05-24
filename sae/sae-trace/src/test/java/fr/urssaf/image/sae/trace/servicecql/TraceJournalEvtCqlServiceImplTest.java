/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.servicecql;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.daocql.service.ITraceJournalEvtCqlService;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TraceJournalEvtCqlServiceImplTest {
  @Autowired
  ITraceJournalEvtCqlService taceservice;

  @Autowired
  TraceJournalEvtSupport tracesupport;

  private static final int MAX_LIST_SIZE = 100;

  private static final Date DATE = new Date();

  @Test
  public void migration_of_trace_journal_from_older_to_new_version() {
    // tracesupport.getTraceIterator();
    final List<TraceJournalEvt> traces = tracesupport.findAll();
    final TraceJournalEvt trace = tracesupport.find(java.util.UUID.fromString("bbf93e40-dd1f-16e0-bfaa-f8b156a582f1"));

    System.out.println("end");
  }

}
