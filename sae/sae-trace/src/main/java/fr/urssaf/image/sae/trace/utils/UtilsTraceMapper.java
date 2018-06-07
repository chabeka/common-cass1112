/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.utils;

import java.util.HashMap;
import java.util.Map;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;

/**
 * TODO (AC75095028) Description du type
 */
public class UtilsTraceMapper {

  /**
   * Transforme un {@link TraceJournalEvtCql} en {@link TraceJournalEvt}
   * 
   * @param traceCql
   *          la trace CQL
   * @return Trace Thrift
   */
  public static TraceJournalEvt createTraceThriftFromCqlTrace(final TraceJournalEvtCql traceCql) {
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
   * Transforme un {@link TraceJournalEvt} en {@link TraceJournalEvtCql}
   *
   * @param traceThrift
   *          la Trace Thrift
   * @return Trace Cql
   */
  public static TraceJournalEvtCql createTraceCqlFromCqlThrift(final TraceJournalEvt traceThrift) {
    final TraceJournalEvtCql tr = new TraceJournalEvtCql(traceThrift.getIdentifiant(), traceThrift.getTimestamp());
    tr.setCodeEvt(traceThrift.getCodeEvt());
    tr.setContexte(traceThrift.getContexte());
    tr.setContratService(traceThrift.getContratService());
    tr.setLogin(traceThrift.getLogin());
    tr.setPagms(traceThrift.getPagms());
    final Map<String, String> infos = new HashMap<>();
    for (final Map.Entry<String, Object> entry : traceThrift.getInfos().entrySet()) {
      infos.put(entry.getKey(), entry.getValue().toString());
    }
    tr.setInfos(infos);

    return tr;
  }

  // LES INDEX

  /**
   * Transforme un {@link TraceJournalEvtIndex} en {@link TraceJournalEvtIndexCql}
   *
   * @param index
   *          index Thrift
   * @return index cql
   */
  public static TraceJournalEvtIndexCql createJournalIndexFromThriftToCql(final TraceJournalEvtIndex index) {
    final TraceJournalEvtIndexCql tr = new TraceJournalEvtIndexCql();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());

    return tr;
  }

  /**
   * Transforme un {@link TraceJournalEvtIndexCql} en {@link TraceJournalEvtIndex}
   *
   * @param index
   *          index cql
   * @return index Thrift
   */
  public static TraceJournalEvtIndex createTraceJournalIndexFromCqlToThrift(final TraceJournalEvtIndexCql index) {
    final TraceJournalEvtIndex tr = new TraceJournalEvtIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    return tr;
  }
}
