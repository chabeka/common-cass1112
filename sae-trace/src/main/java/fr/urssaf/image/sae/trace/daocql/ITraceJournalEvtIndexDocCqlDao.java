package fr.urssaf.image.sae.trace.daocql;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;

/**
 * Interface DAO de {@link TraceJournalEvtIndexDocCql}
 * 
 * @param <TraceJournalEvtIndexDocCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceJournalEvtIndexDocCqlDao extends IGenericDAO<TraceJournalEvtIndexDocCql, UUID> {

}
