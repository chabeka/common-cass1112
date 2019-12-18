/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegTechniqueIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import junit.framework.Assert;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * TODO (AC75095028) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-migration-test.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MigrationTraceRegTechniqueTest {

  private static final Date DATE = new Date();

  int NB_ROWS = 100;

  @Autowired
  TraceRegTechniqueIndexDao indexDao;

  @Autowired
  ITraceRegTechniqueIndexCqlDao indexDaocql;

  @Autowired
  private TraceRegTechniqueCqlSupport supportCql;

  @Autowired
  private TraceRegTechniqueSupport supportThrift;

  @Autowired
  MigrationTraceRegTechnique mtracej;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;

  @Autowired
  private CassandraServerBean server;

  private static final Map<String, String> INFOSCQL;
  static {
    INFOSCQL = new HashMap<>();
    INFOSCQL.put("KEY", "VALUE");
  }

  private static final Map<String, Object> INFOSTHRIFT;
  static {
    INFOSTHRIFT = new HashMap<>();
    INFOSTHRIFT.put("KEY", "VALUE");
  }

  @After
  public void after() throws Exception {
    server.resetData(false, MODE_API.DATASTAX);
  }

  @Test
  public void migrationFromThriftToCql() {
    populateTableThrift();

    final int nb = mtracej.migrationFromThriftToCql();
    final List<TraceRegTechniqueCql> listCql = Lists.newArrayList(supportCql.findAll());

    Assert.assertEquals(nb, NB_ROWS);
    Assert.assertEquals(NB_ROWS, listCql.size());
    Assert.assertEquals(nb, listCql.size());

    // index
    mtracej.migrationIndexFromThriftToCql();
    final List<TraceRegTechniqueIndexCql> listIndexCql = Lists.newArrayList(indexDaocql.findAll());
    Assert.assertEquals(100, listIndexCql.size());
  }

  @Test
  public void migrationFromCqlTothrift() {
    populateTableCql();

    final List<TraceRegTechniqueCql> listCql = Lists.newArrayList(supportCql.findAll());
    Assert.assertEquals(NB_ROWS, listCql.size());

    final int nb = mtracej.migrationFromCqlToThrift();
    Assert.assertEquals(nb, NB_ROWS);

    // index
    mtracej.migrationIndexFromCqlToThrift();
    final long nb_key = countRow();
    Assert.assertEquals(100, nb_key);
  }

  // CLASSE UTILITAIRE

  public void populateTableCql() {
    for (int i = 0; i < NB_ROWS; i++) {
      final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DateUtils.addMinutes(DATE, i));
      createTraceCql(uuid);
    }

  }

  private void populateTableThrift() {
    for (int i = 0; i < NB_ROWS; i++) {
      final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DateUtils.addMinutes(DATE, i));
      createTraceThrift(uuid);
    }
  }

  private void createTraceCql(final UUID uuid) {
    final TraceRegTechniqueCql trace = new TraceRegTechniqueCql(uuid, DATE);
    trace.setContexte("CONTEXTE + suffixe");
    trace.setCodeEvt("CODE_EVT + suffixe");
    trace.setContratService("CONTRAT + suffixe");
    trace.setLogin("LOGIN + suffixe");
    trace.setInfos(INFOSCQL);
    trace.setPagms(Arrays.asList("PAGM  + suffixe"));

    supportCql.create(trace);
  }

  private void createTraceThrift(final UUID uuid) {
    final TraceRegTechnique trace = new TraceRegTechnique(uuid, DATE);
    trace.setContexte("CONTEXTE + suffixe");
    trace.setCodeEvt("CODE_EVT + suffixe");
    trace.setContratService("CONTRAT + suffixe");
    trace.setLogin("LOGIN + suffixe");
    trace.setInfos(INFOSTHRIFT);
    trace.setPagms(Arrays.asList("PAGM  suffixe"));

    supportThrift.create(trace, new Date().getTime());
  }

  public final int countRow() {

    SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery;
    sliceQuery = indexDao.createSliceQuery();
    final String journee = DateRegUtils.getJournee(DATE);
    sliceQuery.setKey(journee);

    final Iterator<TraceRegTechniqueIndex> iterator = new TraceRegTechniqueIndexIterator(sliceQuery);
    final List<TraceRegTechniqueIndex> list = Lists.newArrayList(iterator);

    return list.size();

  }

  @Test
  public void sliceQueryTest() throws Exception {
    final long debut = System.currentTimeMillis();
    System.out.println("Debut  TEST migration des données TraceRegTechnique");
    System.out.println("Debut: Création des données TraceRegTechnique" + Calendar.getInstance().getTime());
    populateTableThrift();
    System.out.println("Fin: Création des données TraceRegTechnique" + Calendar.getInstance().getTime());
    System.out.println("Debut migration des données TraceRegTechnique" + Calendar.getInstance().getTime());
    mtracej.migrationFromThriftToCql();
    mtracej.migrationIndexFromThriftToCql();
    System.out.println("Fin: Migration TraceRegTechnique " + Calendar.getInstance().getTime());
    System.out.println("Debut comparaison des données TraceRegTechnique" + Calendar.getInstance().getTime());
    try {
      mtracej.traceComparator();
      mtracej.indexComparator();
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
    final long fin = System.currentTimeMillis();
    System.out.println("Fin compararaison des données TraceRegTechnique " + Calendar.getInstance().getTime());
    System.out.println("Duree en s:" + String.valueOf((fin - debut) / 1000));
    System.out.println("Fin TEST migration des données TraceRegTechnique");
  }
}
