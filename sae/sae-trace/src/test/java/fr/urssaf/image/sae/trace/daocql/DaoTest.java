package fr.urssaf.image.sae.trace.daocql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.model.GenericType;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import junit.framework.Assert;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

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
  private TraceJournalEvtDao dao;

  @Autowired
  private TraceJournalEvtIndexDao indexDao;

  @Autowired
  private TraceJournalEvtSupport supportJThrift;

  @Autowired
  private TraceJournalEvtCqlSupport supportJCql;

  @Autowired
  private ITraceJournalEvtCqlDao tracejdao;

  @Autowired
  private TraceDestinataireSupport supportTDesti;

  @Autowired
  private ITraceJournalEvtIndexCqlDao indexjdao;

  @Autowired
  private IGenericType genericdao;

  @Test
  public void migration_of_trace_destinataire_from_older_to_new_version() {
    final List<TraceDestinataire> traces = supportTDesti.findAll();
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
      supportTDesti.create(new_traces.next(), new Date().getTime());
    }

    final List<TraceDestinataire> traces = supportTDesti.findAll();
    // Assert.assertEquals(traces.size(), new_traces.size());
  }

  /**
   * Utilisation de cql uniquement
   */
  @Test
  public void migration_of_trace_journal_from_older_to_new_version() {

    @SuppressWarnings("unchecked")
    final Iterator<GenericType> listT = genericdao.iterablefindAll("TraceJournalEvt");

    UUID lastKey = null;
    if (listT.hasNext()) {
      final Row row = (Row) listT.next();
      lastKey = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
    }
    Date timestamp = null;
    String codeEvt = null;
    String contrat = null;
    String login = null;
    List<String> pagms = null;
    Map<String, String> infos = new HashMap<>();
    String contexte = null;
    int i = 0;
    // TraceJournalEvtCql tracejevt = null;

    List<TraceJournalEvtCql> listToSave = new ArrayList<>();
    while (listT.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) listT.next();
      final UUID key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));

      // compare avec la derniere qui a été extraite
      // Si different, cela veut dire qu'on passe sur nu nouvel objet
      // Donc on enrgistre qui vient d'être traité
      if (key != null && !key.equals(lastKey)) {

        final TraceJournalEvtCql tracejevt = new TraceJournalEvtCql(lastKey, timestamp);
        tracejevt.setCodeEvt(codeEvt);
        tracejevt.setContexte(contexte);
        tracejevt.setContratService(contrat);
        tracejevt.setInfos(infos);
        tracejevt.setLogin(login);
        tracejevt.setPagms(pagms);
        listToSave.add(tracejevt);
        lastKey = key;
        // réinitialisation
        timestamp = null;
        codeEvt = null;
        contrat = null;
        login = null;
        pagms = null;
        infos = new HashMap<>();
        contexte = null;
      }

      // extraction du nom de la colonne
      final String columnName = row.getString("column1");

      // extraction de la value en fonction du nom de la colonne
      if (TraceRegTechniqueDao.COL_TIMESTAMP.equals(columnName)) {

        timestamp = DateSerializer.get().fromByteBuffer(row.getBytes("value"));
        // tracejevt = new TraceJournalEvtCql(key, timestamp);
      } else if (TraceRegTechniqueDao.COL_CODE_EVT.equals(columnName)) {

        codeEvt = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceRegTechniqueDao.COL_CONTRAT_SERVICE.equals(columnName)) {

        contrat = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceRegTechniqueDao.COL_LOGIN.equals(columnName)) {

        login = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceRegTechniqueDao.COL_PAGMS.equals(columnName)) {

        pagms = ListSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceRegTechniqueDao.COL_INFOS.equals(columnName)) {
        final Map<String, Object> map = MapSerializer.get().fromByteBuffer(row.getBytes("value"));
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
          final String infosKey = entry.getKey();
          final String value = entry.getValue() != null ? entry.getValue().toString() : "";
          infos.put(infosKey, value);
        }
      } else if (TraceRegTechniqueDao.COL_CONTEXTE.equals(columnName)) {

        contexte = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      }

      if (listToSave.size() == 10000) {
        System.out.println(10000 * ++i);
        tracejdao.saveAll(listToSave);
        listToSave = new ArrayList<>();
      }
    }
    if (listToSave.size() > 0) {
      System.out.println(listToSave.size());
      tracejdao.saveAll(listToSave);
      listToSave = new ArrayList<>();
    }

  }

  @Test
  public void migration_of_trace_journal_from_cql_version_to_thrift() {
    final Iterator<TraceJournalEvtCql> tracej = tracejdao.findAll();
    while (tracej.hasNext()) {
      final TraceJournalEvt traceTrhift = createTratceThriftFromCqlTrace(tracej.next());
      final Date date = tracej.next().getTimestamp();
      final Long times = date != null ? date.getTime() : 0;
      supportJThrift.create(traceTrhift, times);
    }

  }

  public TraceJournalEvt createTratceThriftFromCqlTrace(final TraceJournalEvtCql traceCql) {
    final TraceJournalEvt tr = new TraceJournalEvt(traceCql.getIdentifiant(), traceCql.getTimestamp());
    tr.setCodeEvt(traceCql.getCodeEvt());
    tr.setContexte(traceCql.getContexte());
    tr.setContratService(traceCql.getContratService());
    tr.setLogin(traceCql.getLogin());
    tr.setPagms(traceCql.getPagms());
    final Map<String, Object> infos = new HashMap<>();
    for (final Map.Entry<String, String> entry : traceCql.getInfos().entrySet()) {
      infos.put(entry.getKey(), entry.getValue());
    }
    tr.setInfos(infos);

    return tr;
  }

  /**
   * Transforme un {@link TraceJournalEvtIndex} en {@linkTraceJournalEvtIndexCql}
   *
   * @param index
   * @return
   */
  public TraceJournalEvtIndexCql createTraceIndex_FromThriftToCql(final TraceJournalEvtIndex index) {
    final TraceJournalEvtIndexCql tr = new TraceJournalEvtIndexCql();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());

    return tr;
  }

  public TraceJournalEvtIndex createTraceInde_FromCqlToThrift(final TraceJournalEvtIndexCql index) {
    final TraceJournalEvtIndex tr = new TraceJournalEvtIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    return tr;
  }

  @Test
  public void migration_of_trace_journal_Index_from_thrift_to_cql() {

    int i = 0;
    final List<Date> dates = DateRegUtils.getListFromDates(DateUtils.addYears(DATE, -18), DateUtils.addYears(DATE, 1));
    for (final Date d : dates) {
      final Iterator<TraceJournalEvtIndex> it = supportJThrift.findByDateIterator(d);
      List<TraceJournalEvtIndexCql> listTemp = new ArrayList<>();
      while (it.hasNext()) {
        final TraceJournalEvtIndexCql trace = createTraceIndex_FromThriftToCql(it.next());
        listTemp.add(trace);
        if (listTemp.size() == 10000) {
          indexjdao.saveAll(listTemp);
          listTemp = new ArrayList<>();
        }
        i++;
      }
      if (!listTemp.isEmpty()) {
        indexjdao.saveAll(listTemp);
        listTemp = new ArrayList<>();
      }
    }
    System.out.println(">>>>>>>>>>>>> " + i);

  }

  @Test
  public void migration_of_trace_journal_Index_from_cql_to_thrift() {
    indexjdao.findWithMapperById("eeee");
    int i = 0;
    final Iterator<TraceJournalEvtIndexCql> it = indexjdao.findAll();
    while (it.hasNext()) {
      final TraceJournalEvtIndex index = createTraceInde_FromCqlToThrift(it.next());

      final String journee = DateRegUtils.getJournee(index.getTimestamp());
      final ColumnFamilyUpdater<String, UUID> indexUpdater = indexDao.createUpdater(journee);
      indexDao.writeColumn(indexUpdater,
                           index.getIdentifiant(),
                           index,
                           index.getTimestamp().getTime());
      indexDao.update(indexUpdater);

      i++;
    }

    System.out.println(">>>>>>>>>>>>> " + i);
  }

  @Test
  public void test_migration() {

    UUID startKey = null;
    int i = 1;
    int count = 0;
    int total = 0;

    do {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
                                                                              .createRangeSlicesQuery(dao.getKeyspace(),
                                                                                                      dao.getRowKeySerializer(),
                                                                                                      dao.getColumnKeySerializer(),
                                                                                                      bytesSerializer);
      rangeSlicesQuery.setColumnFamily(dao.getColumnFamilyName());
      rangeSlicesQuery.setRange(
                                StringUtils.EMPTY,
                                StringUtils.EMPTY,
                                false,
                                AbstractDao.DEFAULT_MAX_COLS);
      rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
      rangeSlicesQuery.setKeys(startKey, null);
      QueryResult<OrderedRows<UUID, String, byte[]>> queryResult;
      queryResult = rangeSlicesQuery.execute();

      final OrderedRows<UUID, String, byte[]> orderedRows = queryResult.get();
      count = orderedRows.getCount();
      total += count - 1;
      i++;
      System.out.println("count" + count * i);
      System.out.println(total);
      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      final QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<UUID, String, byte[]>();
      final ColumnFamilyResultWrapper<UUID, String> result = converter
                                                                      .getColumnFamilyResultWrapper(queryResult,
                                                                                                    dao.getRowKeySerializer(),
                                                                                                    dao.getColumnKeySerializer(),
                                                                                                    bytesSerializer);
      final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = queryResult.get().peekLast();
      startKey = lastRow.getKey();
      // On itère sur le résultat
      final HectorIterator<UUID, String> resultIterator = new HectorIterator<UUID, String>(
                                                                                           result);

      final List<TraceJournalEvt> list = new ArrayList<TraceJournalEvt>();
      for (final ColumnFamilyResult<UUID, String> row : resultIterator) {

        list.add(getTraceFromResult(row));

      }
    } while (AbstractDao.DEFAULT_MAX_COLS == count);

  }

  private TraceJournalEvt getTraceFromResult(final ColumnFamilyResult<UUID, String> result) {

    TraceJournalEvt trace = null;

    if (result != null && result.hasResults()) {

      final UUID idTrace = result.getKey();
      final Date timestamp = result.getDate(TraceRegTechniqueDao.COL_TIMESTAMP);

      trace = new TraceJournalEvt(idTrace, timestamp);

      trace.setCodeEvt(result.getString(TraceRegTechniqueDao.COL_CODE_EVT));
      trace.setContratService(result
                                    .getString(TraceRegTechniqueDao.COL_CONTRAT_SERVICE));
      trace.setLogin(result.getString(TraceRegTechniqueDao.COL_LOGIN));

      final byte[] bValue = result.getByteArray(TraceRegTechniqueDao.COL_PAGMS);
      if (bValue != null) {
        trace.setPagms(ListSerializer.get().fromBytes(bValue));
      }

      final byte[] bValue1 = result.getByteArray(TraceRegTechniqueDao.COL_INFOS);
      if (bValue1 != null) {
        trace.setInfos(MapSerializer.get().fromBytes(bValue1));
      }
      trace.setContexte(result.getString(TraceJournalEvtDao.COL_CONTEXT));

    }

    return trace;
  }
}
