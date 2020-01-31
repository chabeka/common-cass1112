/**
 *   (AC75095028) 
 */
package fr.urssaf.image.sae.trace;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.DiffM;
import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;
import fr.urssaf.image.sae.utils.RowUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Classe de migration de TraceDestinataire
 */
@Component
public class MigrationTraceDestinataire {

  @Autowired
  private ITraceDestinataireCqlDao destinatairedao;

  @Autowired
  private TraceDestinataireSupport supportTDesti;
  @Autowired
  private  TraceDestinataireDao dao;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTraceDestinataire.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public DiffM migrationFromThriftToCql() {

    LOGGER.info(" MigrationTraceDestinataire - migrationFromThriftToCql- start ");

    final List<TraceDestinataire> traces = supportTDesti.findAll();
    List<TraceDestinataire> tracesCql = new ArrayList<>();
    if (!traces.isEmpty()) {
      tracesCql = destinatairedao.saveAll(traces);
    }
    final DiffM diffM = new DiffM();

    diffM.setResultCompare(traces.size() == tracesCql.size());
    LOGGER.info(" MigrationTraceDestinataire - migrationFromThriftToCql- end ");
    LOGGER.info(" MigrationTraceDestinataire - migrationFromThriftToCql- nbThrift={} ", traces.size());
    LOGGER.info(" MigrationTraceDestinataire - migrationFromThriftToCql- nbCql={} ", tracesCql.size());
    return diffM;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public DiffM migrationFromCqlTothrift() {

    LOGGER.info(" MigrationTraceDestinataire - migrationFromCqlTothrift- start ");

    final Iterator<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();
    final List<TraceDestinataire> tracesCql = new ArrayList<>();
    new_traces.forEachRemaining(tracesCql::add);

    while (new_traces.hasNext()) {

      final TraceDestinataire nextTrace = new_traces.next();
      supportTDesti.create(nextTrace, new Date().getTime());

    }
    final List<TraceDestinataire> traces = supportTDesti.findAll();
    final DiffM diffM = new DiffM();
    diffM.setResultCompare(traces.size() == tracesCql.size());
    LOGGER.info(" MigrationTraceDestinataire - migrationFromCqlTothrift- end ");
    LOGGER.info(" MigrationTraceDestinataire - migrationFromCqlTothrift- nbThrift={} ", traces.size());
    LOGGER.info(" MigrationTraceDestinataire - migrationFromCqlTothrift- nbCql={} ", tracesCql.size());
    return diffM;
  }

  /**
   * Comparer les Objet cql et Thrift
   * @throws Exception
   */
  public boolean compareTraceDestinataireFromCQlandThrift() throws Exception {
    // recuperer un iterateur sur la table cql
    // Parcourir les elements et pour chaque element 
    // recuperer un ensemble de X elements dans la table thrift
    // chercher l'element cql dans les X elements
    // si trouvé, on passe à l'element suivant cql
    // sinon on recupère les X element suivant dans la table thrift puis on fait une nouvelle recherche
    // si on en recupère  moins de X et qu'on ne trouve pas l'element cql alors == > echec de comparaison

    final Iterator<TraceDestinataire> itDestinataire = destinatairedao.findAllWithMapper();
    boolean isEqBase = true;

    while (itDestinataire.hasNext()) {
      final TraceDestinataire tr = itDestinataire.next();	
      // checker si l'objet courant est equivalent à celui de la base thrift
      final boolean isObj = checkTraceDestinataire(tr);
      if(!isObj) {
        LOGGER.info(" Objet cql non identique a celui de la base thrift");
      } else {
        isEqBase = isEqBase && isObj;
      }   
    } 
    if(isEqBase) {
      LOGGER.info(" MigrationTraceDestinataire - OK");
    }
    return isEqBase;
  }

  /**
   * Requete hector par tranche sur la table {@link TraceDestinataire}
   * @param trCql
   * @return
   * @throws Exception
   */
  public boolean  checkTraceDestinataire(final TraceDestinataire trCql) throws Exception {

    LOGGER.debug(" TraceDestinataire start");

    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(dao.getKeyspace(),
                                stringSerializer,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily("TraceDestinataire");
    final int blockSize = 10000;
    String startKey = "";
    int totalKey = 1;
    int count;
    int nbRows = 0;
    boolean isEqObj = false;
    do {
      rangeSlicesQuery.setRange("", "", false, 1);
      rangeSlicesQuery.setKeys(startKey, "");
      rangeSlicesQuery.setRowCount(blockSize);
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
        if (RowUtils.rowSsbHasColumns(row)) {
          final TraceDestinataire trThrift = getTraceFromResult(row);
          if(trCql.equals(trThrift)) {
            isEqObj = true;
            break;
          }
        }
      }

    } while (count == blockSize);

    LOGGER.debug(" Nb total de cle dans la CF: " + totalKey);
    LOGGER.debug(" Nb total d'entrées dans la CF : " + nbRows);
    LOGGER.debug(" migrationIndexFromThriftToCql end");

    return isEqObj;

  }

  /**
   * Extraction de la {@link TraceDestinataire} depuis le resultat de la requete hector
   * @param row
   * @return
   */
  private TraceDestinataire getTraceFromResult(final me.prettyprint.hector.api.beans.Row<String, String, byte[]> row) {

    final TraceDestinataire trace = new TraceDestinataire();

    if (RowUtils.rowSsbHasColumns(row)) {
      final String idTrace = row.getKey();
      trace.setCodeEvt(idTrace);
      final me.prettyprint.hector.api.beans.ColumnSlice<String, byte[]> cl = row.getColumnSlice();
      final List<HColumn<String, byte[]>> cols = row.getColumnSlice().getColumns();
      for( int i =0; i < cols.size(); i++ ){
        final Map<String, List<String>> map = new HashMap<>();
        final String colName = cols.get(i).getName();
        final List<String> listDesti = ListSerializer.get().fromBytes(cols.get(i).getValue());
        map.put(colName, listDesti);
        trace.setDestinataires(map);
      }
    }

    return trace;
  }

  public QueryResult<OrderedRows<byte[], byte[], byte[]>> dumpCF_slice(final String CFName, final byte[] sliceStart, final byte[] sliceEnd,
                                                                       final int keyCount, final int colCount) throws Exception {
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(dao.getKeyspace(), bytesSerializer, bytesSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(CFName);
    rangeSlicesQuery.setRange(sliceStart, sliceEnd, false, colCount);
    rangeSlicesQuery.setRowCount(keyCount);
    final QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery
        .execute();
    return result;
  }
}
