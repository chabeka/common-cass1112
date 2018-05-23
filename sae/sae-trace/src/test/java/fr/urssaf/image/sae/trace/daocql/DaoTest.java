package fr.urssaf.image.sae.trace.daocql;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import junit.framework.Assert;

/**
 * TODO (AC75007648) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DaoTest {

  private static final Date DATE = new Date();

  private static final int MAX_LIST_SIZE = 100;

  @Autowired
  private ITraceDestinataireCqlDao destinatairedao;

  @Autowired
  private TraceJournalEvtSupport supportjournal;

  @Autowired
  private TraceDestinataireSupport support;

  @Test
  public void migration_of_trace_destinataire_from_older_to_new_version() {
    final List<TraceDestinataire> traces = support.findAll();
    // Assert.assertTrue(traces.isEmpty());

    // initialisation de la table destinataire
    destinatairedao.deleteAll();
    Iterator<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();
    Assert.assertTrue(!new_traces.hasNext());
    while (new_traces.hasNext()) {
      final TraceDestinataire trace = new_traces.next();
      destinatairedao.saveAll(traces);
      new_traces = destinatairedao.findAllWithMapper();
    }
  }

  @Test
  public void migration_of_trace_destinataire_from_new_version_to_older() {
    final Iterator<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();

    while (new_traces.hasNext()) {
      support.create(new_traces.next(), new Date().getTime());
    }

    final List<TraceDestinataire> traces = support.findAll();
    // Assert.assertEquals(traces.size(), new_traces.size());
  }

  @Test
  public void migration_of_trace_journal_from_new_older_version_to_new_version() {

    final List<TraceJournalEvt> list = supportjournal.findAll();
    System.out.println(list.size());

  }
}
