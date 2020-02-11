package fr.urssaf.image.sae.commons;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDocDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexDocSerializer;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import fr.urssaf.image.sae.utils.RowUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

@Repository
public class CompareIndexDoc {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompareIndexDoc.class);

  @Autowired
  TraceJournalEvtIndexDocDao dao;

  /**
   * Créer un index {@link TraceJournalEvtIndexDoc} à partir d'une trace {@link TraceJournalEvtIndexDocCql}
   *
   * @param index
   *          {@link TraceJournalEvtIndexCql}
   * @return un {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndexDocCql createIndexDocFromObjectThrift(final TraceJournalEvtIndexDoc indexThrift, final String key) {
    return UtilsTraceMapper.createTraceIndexDocFromThriftToCql(indexThrift, key);
  }

  /**
   * Chercher un objet {@link IC} dans la table thrift avec 
   * Requetage hector par tranche sur la table {@link I}
   * @param trCql
   * @return si l'objet {@link IC} a été trouvé dans la base thrift
   * @throws Exception
   */

  public boolean checkIndexDocCql(final TraceJournalEvtIndexDocCql indexCql, final Map<String, Map<UUID, TraceJournalEvtIndexDocCql>> map) throws Exception {


    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final UUIDSerializer uSl = UUIDSerializer.get();

    final RangeSlicesQuery<String, UUID, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(dao.getKeyspace(),
                                stringSerializer,
                                uSl,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(TraceJournalEvtIndexDoc.class.getSimpleName());
    final int blockSize = RowUtils.BLOCK_SIZE_TRACE_JOURNAL_EVT;
    String startKey = StringUtils.EMPTY;
    int count;
    boolean isEqObj = false;
    Map<UUID, TraceJournalEvtIndexDocCql> mapRow = null;
    Integer mapKey = 0;
    // Pour chaque tranche de blockSize, on recherche l'objet cql
    do {
      rangeSlicesQuery.setRange(null, null, false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      QueryResult<OrderedRows<String, UUID, byte[]>> result = null;
      try {
        result = rangeSlicesQuery.execute();
      } catch (final Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }

      final OrderedRows<String, UUID, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<String, UUID, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter extraction
      final QueryResultConverter<String, UUID, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<String, UUID> result0 = converter
          .getColumnFamilyResultWrapper(result, stringSerializer, uSl, bytesSerializer);

      // map cache
      mapRow = new HashMap<>();

      // On itère sur le résultat
      final HectorIterator<String, UUID> resultIterator = new HectorIterator<>(result0); 
      for (final ColumnFamilyResult<String, UUID> row : resultIterator) {
        final String key = row.getKey();
        final Iterator<UUID> it = row.getColumnNames().iterator();

        while (it.hasNext()) {
          final UUID uuid = it.next();
          final HColumn<UUID, ByteBuffer> tHl = row.getColumn(uuid);
          final TraceJournalEvtIndexDoc indexThrift = TraceJournalEvtIndexDocSerializer.get().fromByteBuffer(tHl.getValue()); 
          final TraceJournalEvtIndexDocCql indexThToCql = createIndexDocFromObjectThrift(indexThrift, key);
          if(indexThToCql.equals(indexCql)) {
            isEqObj = true;
          } else {
            // construire une map qui nous servira de cache
            mapRow.put(indexThToCql.getIdentifiantIndex(), indexThToCql);
          }
        } 
      }

      // on ajoute que si la plage n'a pas encore été ajouté
      if (!map.containsKey(mapKey.toString())) {
        map.put(mapKey.toString(), mapRow);
      }
      mapKey++;
      // on sort de la boucle si l'element est trouvé dans la tranche courante
      if (isEqObj) {
        break;
      }
    } while (count == blockSize);

    if(!isEqObj) {
      LOGGER.info("l' objet d'identifiant " +indexCql.getIdentifiant() + " n'a pas été trouvé dans la table cql" );
    }
    return isEqObj;
  }
}
