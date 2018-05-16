/**
 *
 */
package fr.urssaf.image.sae.trace.daocql;

import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;

/**
 * @author AC75007648
 */
public interface ITraceJournalEvtIndexCqlDao extends IGenericDAO<TraceJournalEvt, UUID> {

}
