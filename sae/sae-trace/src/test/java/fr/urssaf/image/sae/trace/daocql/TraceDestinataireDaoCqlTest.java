/**
 *
 */
package fr.urssaf.image.sae.trace.daocql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.commons.TraceDestinataireEnum;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TraceDestinataireDaoCqlTest {

  @Autowired
  private TraceDestinataireCqlSupport tracesupport;

  @Autowired
  private CassandraServerBean server;

  private final List<String> list = Arrays.asList("date", "contrat");

  @Autowired
  TimeUUIDEtTimestampSupport timeuuid;

  @After
  public void after() throws Exception {
    // server.resetDataOnly();
    server.resetData();
  }

  @Test
  public void init() {
    try {
      if (server.isCassandraStarted()) {
        server.resetData();
        // server.resetData(true, MODE_API.DATASTAX);
      }
      Assert.assertTrue(true);

    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }
  @Test(expected = TraceRuntimeException.class)
  public void createTestFailureCodeInexistant() {
    createTrace("TEST|CREATE", "colonneNonRepertoriee");
  }

  @Test(expected = TraceRuntimeException.class)
  public void create_new_trace_with_bad_code() {
    final List<TraceDestinataire> allTraces = tracesupport.findAll();
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt("WS_REPRISE_MASSE|KO");

    final Map<String, List<String>> dest = new HashMap<>();
    dest.put("BAD_CODE|KO", list);
    trace.setDestinataires(dest);

    tracesupport.create(trace, new Date().getTime());

  }

  @Test
  public void create_new_trace_and_find_it_with_succes() {

    final List<TraceDestinataire> traces = tracesupport.findAll();
    final String code = "TEST|CREATE";
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt(code);

    final Map<String, List<String>> dest = new HashMap<>();
    dest.put(TraceDestinataireEnum.HIST_ARCHIVE.name(), list);
    trace.setDestinataires(dest);

    tracesupport.create(trace, new Date().getTime());
    final TraceDestinataire traceFromDB = tracesupport.findById(code);

    Assert.assertEquals("le code événement doit etre correct",
                        code,
                        traceFromDB.getCodeEvt());
    Assert.assertNotNull("la liste des destinataires doit etre non nulle",
                         traceFromDB.getDestinataires());
    Assert
    .assertEquals(
                  "il doit y avoir un et un seul élément dans la map des destinataires",
                  1,
                  traceFromDB.getDestinataires().size());
    Assert.assertEquals("la clé présente doit etre valide",
                        TraceDestinataireEnum.HIST_ARCHIVE.name(),
                        traceFromDB
                        .getDestinataires().keySet().toArray(new String[0])[0]);

    final List<String> values = traceFromDB.getDestinataires().get(
                                                                   TraceDestinataireEnum.HIST_ARCHIVE.name());
    Assert.assertEquals(
                        "le nombre d'éléments de la liste doit etre correcte", 2, values
                        .size());
    Assert.assertTrue("tous les éléments présents doivent etre corrects",
                      list.containsAll(values));
  }

  @Test
  public void create_delete_with_success() {
    final Date date = DateRegUtils.getDateWithoutTime();
    final UUID id = timeuuid.buildUUIDFromDate(date);
    final String code = "TEST|DELETE";
    final String colName = TraceDestinataireEnum.HIST_ARCHIVE.name();

    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt(code);

    final Map<String, List<String>> dest = new HashMap<>();
    dest.put(TraceDestinataireEnum.HIST_ARCHIVE.name(), list);
    trace.setDestinataires(dest);
    tracesupport.create(trace, new Date().getTime());

    tracesupport.delete(code, new Date().getTime());

    final TraceDestinataire TraceDest = tracesupport.findById(code);
    Assert.assertNull("L'objet doit etre null", TraceDest);

  }

  private void createTrace(final String code, final String colName) {
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt(code);

    final Map<String, List<String>> dest = new HashMap<>();
    dest.put(colName, list);
    trace.setDestinataires(dest);

    tracesupport.create(trace, new Date().getTime());
  }

  @Test
  public void findAll() {
    final TraceDestinataire trace = new TraceDestinataire();
    trace.setCodeEvt("TEST|CREATE");
    final Map<String, List<String>> map = new HashMap<>();
    final List<String> listString = new ArrayList<>();
    listString.add("TEST LIST");
    map.put("HIST_ARCHIVE", listString);
    trace.setDestinataires(map);
    tracesupport.create(trace, new Date().getTime());
    final List<TraceDestinataire> traces = tracesupport.findAll();
    Assert.assertNotNull(traces);
  }

}
