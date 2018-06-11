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
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndexCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndexCql;

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

  public static TraceRegSecurite createTraceRegSecuriteThriftFromCqlTrace(final TraceRegSecuriteCql traceCql) {
    final TraceRegSecurite tr = new TraceRegSecurite(traceCql.getIdentifiant(), traceCql.getTimestamp());
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

  public static TraceRegSecuriteCql createTraceRegSecuCqlFromCqlThrift(final TraceRegSecurite traceThrift) {
    final TraceRegSecuriteCql tr = new TraceRegSecuriteCql(traceThrift.getIdentifiant(), traceThrift.getTimestamp());
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

  public static TraceRegTechnique createTraceRegTechniqueThriftFromCqlTrace(final TraceRegTechniqueCql traceCql) {
    final TraceRegTechnique tr = new TraceRegTechnique(traceCql.getIdentifiant(), traceCql.getTimestamp());
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
    tr.setStacktrace(traceCql.getStacktrace());
    return tr;
  }

  public static TraceRegTechniqueCql createTraceRegTechniqueCqlFromCqlThrift(final TraceRegTechnique traceThrift) {
    final TraceRegTechniqueCql tr = new TraceRegTechniqueCql(traceThrift.getIdentifiant(), traceThrift.getTimestamp());
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
    tr.setStacktrace(traceThrift.getStacktrace());
    return tr;
  }

  public static TraceRegExploitation createTraceRegExploitationThriftFromCqlTrace(final TraceRegExploitationCql traceCql) {
    final TraceRegExploitation tr = new TraceRegExploitation(traceCql.getIdentifiant(), traceCql.getTimestamp());
    tr.setCodeEvt(traceCql.getCodeEvt());
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

  public static TraceRegExploitationCql createTraceRegTechniqueCqlFromCqlThrift(final TraceRegExploitation traceThrift) {
    final TraceRegExploitationCql tr = new TraceRegExploitationCql(traceThrift.getIdentifiant(), traceThrift.getTimestamp());
    tr.setCodeEvt(traceThrift.getCodeEvt());
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
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }

  public static TraceRegSecuriteIndexCql createTraceRegSecuIndexFromThriftToCql(final TraceRegSecuriteIndex index) {
    final TraceRegSecuriteIndexCql tr = new TraceRegSecuriteIndexCql();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContrat(index.getContrat());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }

  public static TraceRegSecuriteIndex createTraceRegSecuIndexFromCqlToThrift(final TraceRegSecuriteIndexCql index) {
    final TraceRegSecuriteIndex tr = new TraceRegSecuriteIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContrat(index.getContrat());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }

  public static TraceRegTechniqueIndexCql createTraceRegTechniqueIndexFromThriftToCql(final TraceRegTechniqueIndex index) {
    final TraceRegTechniqueIndexCql tr = new TraceRegTechniqueIndexCql();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContrat(index.getContrat());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }

  public static TraceRegTechniqueIndex createTraceRegTechniqueIndexFromCqlToThrift(final TraceRegTechniqueIndexCql index) {
    final TraceRegTechniqueIndex tr = new TraceRegTechniqueIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContexte(index.getContexte());
    tr.setContrat(index.getContrat());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }

  public static TraceRegExploitationIndexCql createTraceRegExploitationIndexFromThriftToCql(final TraceRegExploitationIndex index) {
    final TraceRegExploitationIndexCql tr = new TraceRegExploitationIndexCql();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContrat(index.getContrat());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setAction(index.getAction());
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }

  public static TraceRegExploitationIndex createTraceRegExploitationIndexFromCqlToThrift(final TraceRegExploitationIndexCql index) {
    final TraceRegExploitationIndex tr = new TraceRegExploitationIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContrat(index.getContrat());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setAction(index.getAction());
    tr.setTimestamp(index.getTimestamp());
    return tr;
  }
}
