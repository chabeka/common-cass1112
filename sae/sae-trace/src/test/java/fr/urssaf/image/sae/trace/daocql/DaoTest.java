package fr.urssaf.image.sae.trace.daocql;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import junit.framework.Assert;

/**
 * TODO (AC75007648) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DaoTest {

  @Autowired
  private ITraceDestinataireCqlDao destinatairedao;

  @Autowired
  private TraceDestinataireSupport support;

  @Test
  public void migration_of_trace_destinataire_from_older_to_new_version() {
    final List<TraceDestinataire> traces = support.findAll();
    // Assert.assertTrue(traces.isEmpty());

    // Suppression de toutes les données dans la table sur laquelle les données sur migrées
    destinatairedao.deleteAll();
    List<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();
    Assert.assertTrue(new_traces.isEmpty());
    if (!traces.isEmpty()) {
      destinatairedao.saveAll(traces);
      new_traces = destinatairedao.findAllWithMapper();
      Assert.assertEquals(traces.size(), new_traces.size());
    }
  }

  @Test
  public void migration_of_trace_destinataire_from_new_version_to_older() {
    final List<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();

    for (final TraceDestinataire trace : new_traces) {
      support.create(trace, new Date().getTime());
    }
    final List<TraceDestinataire> traces = support.findAll();
    Assert.assertEquals(traces.size(), new_traces.size());
  }
}
