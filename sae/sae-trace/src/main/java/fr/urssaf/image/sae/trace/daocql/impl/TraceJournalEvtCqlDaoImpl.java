/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class TraceJournalEvtCqlDaoImpl extends GenericDAOImpl<TraceJournalEvtCql, UUID> implements ITraceJournalEvtCqlDao {

}
