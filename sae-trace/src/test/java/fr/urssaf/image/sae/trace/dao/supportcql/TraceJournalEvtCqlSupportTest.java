/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.supportcql;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
public class TraceJournalEvtCqlSupportTest {

  private static final String VALUE = "valeur";

  private static final String KEY = "clé";

  private static final Date DATE = new Date();

  private static final String LOGIN = "LE LOGIN";

  private static final String CONTRAT = "contrat de service";

  private static final List<String> PAGMS = Arrays.asList("PAGM1", "PAGM2");

  private static final String CODE_EVT = "code événement et quoi";

  private static final String CONTEXT = "contexte";

  private static final Map<String, String> INFOS;

  private static final String DATE_FORMAT = "yyyyMMdd";

  static {
    INFOS = new HashMap<>();
    INFOS.put(KEY, VALUE);
  }

  @Autowired
  private TraceJournalEvtCqlSupport cqlsupport;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;

  @After
  public void after() throws Exception {
    server.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testCreateFindSuccess() {

    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DATE);
    final TraceJournalEvtCql trace = createTrace(uuid);
    cqlsupport.create(trace);

    final Optional<TraceJournalEvtCql> securiteOp = cqlsupport.find(uuid);
    Assert.assertTrue("L'objet est non null", securiteOp.isPresent());
    checkBean(securiteOp.get(), uuid);

  }

  @Test
  public void testDelete() {

    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(new Date());
    final TraceJournalEvtCql trace = createTrace(uuid);
    cqlsupport.create(trace);

    final long nbTracesPurgees = cqlsupport.delete(new Date());

    final Optional<TraceJournalEvtCql> securiteOpt = cqlsupport.find(uuid);
    Assert.assertFalse("aucune trace ne doit etre touvée", securiteOpt.isPresent());

    Assert.assertEquals("Le nombre de traces purgées est incorrect", 1L, nbTracesPurgees);

  }

  @Test
  public void testCreateFindByPlageSuccess() {
    final UUID uuid = timeUUIDSupport.buildUUIDFromDate(new Date());
    final TraceJournalEvtCql trace = createTrace(uuid);
    cqlsupport.create(trace);

    final Optional<TraceJournalEvtCql> exploitationOpt = cqlsupport.find(uuid);
    Assert.assertTrue("L'objet doit etre non null", exploitationOpt.isPresent());
    final TraceJournalEvtCql exploitation = exploitationOpt.get();
    checkBean(exploitation, uuid);

  }

  private TraceJournalEvtCql createTrace(final UUID uuid) {
    final TraceJournalEvtCql trace = new TraceJournalEvtCql(uuid, DATE);
    trace.setContexte(CONTEXT);
    trace.setCodeEvt(CODE_EVT);
    trace.setContratService(CONTRAT);
    trace.setPagms(PAGMS);
    trace.setLogin(LOGIN);
    trace.setInfos(INFOS);
    return trace;

  }

  public void createTrace(final TraceJournalEvtCql trace, final long clock) {

    if (MapUtils.isNotEmpty(trace.getInfos())) {
      for (final String key : trace.getInfos().keySet()) {
        final Map<String, String> infosCql = new HashMap<>();
        infosCql.put(key, trace.getInfos().get(key).toString());
        trace.setInfos(infosCql);
      }
    }
  }

  public Date getDateFormatted(final Date date) {
    Date datej = new Date();
    final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    final String journee = DateRegUtils.getJournee(date);

    try {
      datej = dateFormat.parse(journee);
    } catch (final ParseException e) {
      e.printStackTrace();
    }
    return datej;
  }

  private void checkBean(final TraceJournalEvtCql securite, final UUID uuid) {
    Assert.assertNotNull("l'objet doit etre trouvé", securite);
    Assert.assertEquals("le contexte doit etre correcte", CONTEXT, securite.getContexte());
    Assert.assertEquals("le code evenement doit etre correcte", CODE_EVT, securite.getCodeEvt());
    Assert.assertEquals("le contrat doit etre correcte", CONTRAT, securite.getContratService());
    checkPagms(securite.getPagms());
    Assert.assertEquals("l'identifiant doit etre correcte", uuid, securite.getIdentifiant());
    Assert.assertEquals("le login doit etre correcte", LOGIN, securite.getLogin());
    Assert.assertEquals("la date doit etre correcte", DATE, securite.getTimestamp());

    Assert.assertEquals("les infos supplémentaire doivent contenir un élément", 1, securite.getInfos().size());

    Assert.assertTrue("les infos supplémentaire doivent une clé correcte", securite.getInfos().keySet().contains(KEY));

    Assert.assertEquals("les infos supplémentaire doivent contenir une valeur correcte élément", VALUE, securite.getInfos().get(KEY));

  }

  private void checkPagms(final List<String> pagms) {
    Assert.assertNotNull("La liste des PAGM ne doit pas être nulle", pagms);
    Assert.assertEquals("La liste des PAGM doit contenir 2 éléments", 2, pagms.size());
    Assert.assertTrue("La liste des PAGM doit contenir le PAGM \"PAGM1\"", pagms.contains("PAGM1"));
    Assert.assertTrue("La liste des PAGM doit contenir le PAGM \"PAGM2\"", pagms.contains("PAGM2"));
  }
}
