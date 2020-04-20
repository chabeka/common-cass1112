/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;

/**
 * Interface DAO de {@link TraceJournalEvtCql}
 * 
 * @param <TraceJournalEvtCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceJournalEvtCqlDao extends IGenericDAO<TraceJournalEvtCql, UUID> {

}
