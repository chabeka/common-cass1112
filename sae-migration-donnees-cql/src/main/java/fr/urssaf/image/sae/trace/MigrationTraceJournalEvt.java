/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.sae.trace.commons.TraceFieldsName;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDocDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;
import fr.urssaf.image.sae.trace.model.GenericTraceType;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationTraceJournalEvt extends MigrationTrace {

  @Autowired
  TraceJournalEvtIndexDao indexthrift;

  @Autowired
  ITraceJournalEvtIndexDocCqlDao indexDocDaocql;

  @Autowired
  TraceJournalEvtIndexDocDao indexDocDaothrift;

  @Autowired
  private TraceJournalEvtSupport supportJThrift;

  @Autowired
  private TraceJournalEvtCqlSupport supportcql;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTraceJournalEvt.class);

  /**
   * Utilisation de cql uniquement
   * Migration de la CF thrift vers la CF cql en utilsant un mapping manuel. L'extration des données est faite
   * à partir du type {@link GenericTraceType} qui permet de wrapper les colonnes
   */
  public int migrationFromThriftToCql() {

    LOGGER.debug(" migrationFromThriftToCql start");

    final Iterator<GenericTraceType> listT = genericdao.findAllByCFName("TraceJournalEvt", keyspace_tu);

    UUID lastKey = null;

    String login = null;
    Date timestamp = null;
    String codeEvt = null;
    String contrat = null;
    String contexte = null;
    List<String> pagms = null;
    Map<String, String> infos = new HashMap<>();
    TraceJournalEvtCql tracejevt;
    int nb = 0;

    List<TraceJournalEvtCql> listToSave = new ArrayList<>();
    while (listT.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) listT.next();
      final UUID key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (lastKey == null) {
        lastKey = key;
      }
      // compare avec la derniere clé qui a été extraite
      // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
      // alors on enrgistre celui qui vient d'être traité
      if (key != null && !key.equals(lastKey)) {

        tracejevt = new TraceJournalEvtCql(lastKey, timestamp);
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

        if (listToSave.size() == 10000) {
          nb = nb + listToSave.size();
          supportcql.saveAllTraces(listToSave);
          listToSave = new ArrayList<>();
        }

      }

      // extraction du nom de la colonne
      final String columnName = row.getString("column1");

      // extraction de la value en fonction du nom de la colonne
      if (TraceFieldsName.COL_TIMESTAMP.getName().equals(columnName)) {

        timestamp = DateSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_CODE_EVT.getName().equals(columnName)) {

        codeEvt = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_CONTRAT_SERVICE.getName().equals(columnName)) {

        contrat = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_LOGIN.getName().equals(columnName)) {

        login = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_PAGMS.getName().equals(columnName)) {

        pagms = ListSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_INFOS.getName().equals(columnName)) {
        final Map<String, Object> map = MapSerializer.get().fromByteBuffer(row.getBytes("value"));
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
          final String infosKey = entry.getKey();
          final String value = entry.getValue() != null ? entry.getValue().toString() : "";
          infos.put(infosKey, value);
        }
      } else if (TraceFieldsName.COL_CONTEXTE.getName().equals(columnName)) {

        contexte = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      }

    }
    if (listToSave.size() > 0) {
      // Ajouter le dernier cas traité
      tracejevt = new TraceJournalEvtCql(lastKey, timestamp);
      tracejevt.setCodeEvt(codeEvt);
      tracejevt.setContexte(contexte);
      tracejevt.setContratService(contrat);
      tracejevt.setInfos(infos);
      tracejevt.setLogin(login);
      tracejevt.setPagms(pagms);
      listToSave.add(tracejevt);

      nb = nb + listToSave.size();
      supportcql.saveAllTraces(listToSave);
      listToSave = new ArrayList<>();
    }
    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationFromThriftToCql end");
    return nb;
  }

  /**
   * Migration des données de la CF cql vers thrift
   */
  public int migrationFromCqlToThrift() {

    LOGGER.debug(" migrationFromCqlToThrift start");

    final Iterator<TraceJournalEvtCql> tracej = supportcql.findAll();
    // final List<TraceJournalEvtCql> nb_Rows = Lists.newArrayList(tracej);
    int nb = 0;
    while (tracej.hasNext()) {
      final TraceJournalEvtCql nextJ = tracej.next();
      final TraceJournalEvt traceTrhift = UtilsTraceMapper.createTraceJournalEvtFromCqlToThrift(nextJ);
      final Date date = nextJ.getTimestamp();
      final Long times = date != null ? date.getTime() : 0;
      supportJThrift.create(traceTrhift, times);
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationFromCqlToThrift end");

    return nb;
  }

  /**
   * Migration de la CF INDEX du journal de cql vers thrift
   */
  public int migrationIndexFromCqlToThrift() {

    LOGGER.debug(" migrationIndexFromCqlToThrift start");
    int nb = 0;
    final Iterator<TraceJournalEvtIndexCql> it = supportcql.findAllIndex();
    while (it.hasNext()) {
      final TraceJournalEvtIndex index = createTraceIndexFromCqlToThrift(it.next());

      final String journee = DateRegUtils.getJournee(index.getTimestamp());
      final ColumnFamilyUpdater<String, UUID> indexUpdater = indexthrift.createUpdater(journee);
      indexthrift.writeColumn(indexUpdater,
                              index.getIdentifiant(),
                              index,
                              index.getTimestamp().getTime());
      indexthrift.update(indexUpdater);

      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationIndexFromCqlToThrift end");

    return nb;
  }

  /**
   * Migration de la CF index du journal de thritf vers la CF cql
   */
  public int migrationIndexFromThriftToCql() {

    LOGGER.debug(" migrationIndexFromThriftToCql start");

    int nb = 0;
    final List<Date> dates = DateRegUtils.getListFromDates(DateUtils.addYears(DATE, -18), DateUtils.addYears(DATE, 1));
    for (final Date d : dates) {

      final List<TraceJournalEvtIndex> list = supportJThrift.findByDate(d);
      List<TraceJournalEvtIndexCql> listTemp = new ArrayList<>();
      for (final TraceJournalEvtIndex next : list) {
        final TraceJournalEvtIndexCql trace = createTraceIndexFromThriftToCql(next);
        listTemp.add(trace);
        if (listTemp.size() == 10000) {
          nb = nb + listTemp.size();
          supportcql.saveAllIndex(listTemp);
          listTemp = new ArrayList<>();
          System.out.println(" Temp i : " + nb);
        }
      }
      if (!listTemp.isEmpty()) {
        nb = nb + listTemp.size();
        supportcql.saveAllIndex(listTemp);
        listTemp = new ArrayList<>();
      }
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationIndexFromThriftToCql end");

    return nb;
  }

  /**
   * Migration de la CF INDEX DOC du journal de thrift vers cql
   *
   * @throws Exception
   */
  public int migrationIndexDocFromThriftToCql() throws Exception {

    LOGGER.debug(" migrationIndexDocFromThriftToCql start");

    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(indexDocDaothrift.getKeyspace(),
                                stringSerializer,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily("TraceJournalEvtIndexDoc");
    final int blockSize = 1000;
    String startKey = "";
    int totalKey = 1;
    int count;
    int nbRows = 0;
    do {
      rangeSlicesQuery.setRange("", "", false, 1);
      rangeSlicesQuery.setKeys(startKey, "");
      rangeSlicesQuery.setRowCount(blockSize);
      rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
          .execute();

      final OrderedRows<String, String, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();
      // On enlève 1, car sinon à chaque itération, la startKey serait
      // comptée deux fois.
      totalKey += count - 1;
      nbRows = totalKey;
      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<String, String, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      for (final me.prettyprint.hector.api.beans.Row<String, String, byte[]> row : orderedRows) {

        final List<TraceJournalEvtIndexDoc> list = supportJThrift.findByIdDoc(java.util.UUID.fromString(row.getKey()));
        for (final TraceJournalEvtIndexDoc tr : list) {
          indexDocDaocql.save(UtilsTraceMapper.createTraceIndexDocFromCqlToThrift(tr, row.getKey()));
          nbRows++;
        }
      }

    } while (count == blockSize);

    LOGGER.debug(" Nb total de cle dans la CF: " + totalKey);
    LOGGER.debug(" Nb total d'entrées dans la CF : " + nbRows);
    LOGGER.debug(" migrationIndexFromThriftToCql end");

    return totalKey;

  }

  /**
   * Migration de la CF INDEX DOC du journal de cql vers thrift
   */
  public int migrationIndexDocFromCqlToThrift() {

    LOGGER.debug(" migrationIndexDoc_From_Cql_To_Thrift start");

    int nb = 0;
    final Iterator<TraceJournalEvtIndexDocCql> it = indexDocDaocql.findAll();
    while (it.hasNext()) {
      final TraceJournalEvtIndexDocCql indexCql = it.next();
      final TraceJournalEvtIndexDoc index = UtilsTraceMapper.createTraceIndexDocFromCqlToThrift(indexCql);
      final String idDoc = index.getIdentifiant().toString();
      final ColumnFamilyUpdater<String, UUID> updater = indexDocDaothrift.createUpdater(idDoc);
      indexDocDaothrift.writeColumn(updater, index.getIdentifiant(), index, indexCql.getTimestamp().getTime());
      indexDocDaothrift.update(updater);
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationIndexDoc_From_Cql_To_Thrift end");

    return nb;
  }

  /**
   * Créer un {@link TraceJournalEvtIndexCql} à partir d'un {@link TraceJournalEvtIndex}
   *
   * @param index
   *          l'index {@link TraceJournalEvtIndexCql}
   * @return l'index {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndexCql createTraceIndexFromThriftToCql(final TraceJournalEvtIndex index) {
    final TraceJournalEvtIndexCql tr = new TraceJournalEvtIndexCql();
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    tr.setIdentifiantIndex(journee);
    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());

    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());

    return tr;
  }

  /**
   * Créer un index {@link TraceJournalEvtIndex} à partir d'une trace {@link TraceJournalEvtIndexCql}
   *
   * @param index
   *          {@link TraceJournalEvtIndexCql}
   * @return un {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndex createTraceIndexFromCqlToThrift(final TraceJournalEvtIndexCql index) {
    final TraceJournalEvtIndex tr = new TraceJournalEvtIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());

    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());
    return tr;
  }

}
