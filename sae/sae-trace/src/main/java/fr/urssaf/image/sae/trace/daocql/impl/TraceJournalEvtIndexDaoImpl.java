/**
 *
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;

/**
 * @author AC75007648
 */
@Repository
public class TraceJournalEvtIndexDaoImpl extends GenericDAOImpl<TraceJournalEvt, UUID> implements ITraceJournalEvtIndexCqlDao {

}
