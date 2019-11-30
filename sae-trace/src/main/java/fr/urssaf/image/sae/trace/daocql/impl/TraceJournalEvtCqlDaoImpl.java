/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceJournalEvtCqlDao} de la famille de colonnes {@link TraceJournalEvtCql}
 * 
 * @param <TraceJournalEvtCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceJournalEvtCqlDaoImpl extends GenericDAOImpl<TraceJournalEvtCql, UUID> implements ITraceJournalEvtCqlDao {

}
