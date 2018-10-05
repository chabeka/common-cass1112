/**
 *
 */
package fr.urssaf.image.sae.trace.dao.mapper;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;

/**
 * @author AC75007648
 */
public class TraceJournalEvtMapper {

  public TraceJournalEvt mapTraceJournalEvtCql(final TraceJournalEvtCql traceJournalEvtCql) {
    final TraceJournalEvt traceJournalEvt = new TraceJournalEvt();
    traceJournalEvt.setCodeEvt(traceJournalEvtCql.getCodeEvt());
    traceJournalEvt.setContexte(traceJournalEvtCql.getContexte());
    return null;
  }

}
