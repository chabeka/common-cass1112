/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.service;

import fr.urssaf.image.sae.trace.dao.model.Trace;
import fr.urssaf.image.sae.trace.dao.model.TraceIndex;
import fr.urssaf.image.sae.trace.service.JournalEvtServiceCql;

/**
 * TODO (AC75095028) Description du type
 */
public interface ITraceJournalEvtCqlService<T extends Trace, I extends TraceIndex> extends JournalEvtServiceCql {
}
