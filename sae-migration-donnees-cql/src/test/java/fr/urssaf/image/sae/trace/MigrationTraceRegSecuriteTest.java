/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.util.Arrays;
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
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegSecuriteIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegSecuriteCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;
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
public class MigrationTraceRegSecuriteTest {

  private static final Date DATE = new Date();

  int NB_ROWS = 100;

  @Autowired
  TraceRegSecuriteIndexDao indexDao;

  @Autowired
  ITraceRegSecuriteIndexCqlDao indexDaocql;

  @Autowired
  private TraceRegSecuriteCqlSupport supportCql;

  @Autowired
  private TraceRegSecuriteSupport supportThrift;

  @Autowired
  MigrationTraceRegSecurite mtracej;

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
    Assert.assertEquals(nb, NB_ROWS);

    // index
    final int nb_index = mtracej.migrationIndexFromThriftToCql();
    Assert.assertEquals(100, nb_index);
  }

  @Test
  public void migrationFromCqlTothrift() {
    populateTableCql();

    final List<TraceRegSecuriteCql> listCql = Lists.newArrayList(supportCql.findAll());
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
    final TraceRegSecuriteCql trace = new TraceRegSecuriteCql(uuid, DATE);
    trace.setContexte("CONTEXTE + suffixe");
    trace.setCodeEvt("CODE_EVT + suffixe");
    trace.setContratService("CONTRAT + suffixe");
    trace.setLogin("LOGIN + suffixe");
    trace.setInfos(INFOSCQL);
    trace.setPagms(Arrays.asList("PAGM  + suffixe"));

    supportCql.create(trace);
  }

  private void createTraceThrift(final UUID uuid) {
    final TraceRegSecurite trace = new TraceRegSecurite(uuid, DATE);
    trace.setContexte("CONTEXTE + suffixe");
    trace.setCodeEvt("CODE_EVT + suffixe");
    trace.setContratService("CONTRAT + suffixe");
    trace.setLogin("LOGIN + suffixe");
    trace.setInfos(INFOSTHRIFT);
    trace.setPagms(Arrays.asList("PAGM  suffixe"));

    supportThrift.create(trace, new Date().getTime());
  }

  public final int countRow() {

    SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery;
    sliceQuery = indexDao.createSliceQuery();
    final String journee = DateRegUtils.getJournee(DATE);
    sliceQuery.setKey(journee);

    final Iterator<TraceRegSecuriteIndex> iterator = new TraceRegSecuriteIndexIterator(sliceQuery);
    final List<TraceRegSecuriteIndex> list = Lists.newArrayList(iterator);

    return list.size();

  }
  
  @Test
  public void sliceQueryTest() throws Exception {
	
	populateTableThrift();

	mtracej.migrationFromThriftToCql();
	mtracej.migrationIndexFromThriftToCql();

    try {
    	mtracej.traceComparator();
    	mtracej.indexComparator();
	} catch (Exception e) {
		e.printStackTrace();
	}
    
  }
}
