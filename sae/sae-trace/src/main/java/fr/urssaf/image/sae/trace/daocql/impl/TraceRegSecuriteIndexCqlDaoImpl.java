/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceRegSecuriteIndexCqlDao} de la famille de colonnes {@link TraceRegSecuriteIndexCql}
 * 
 * @param <TraceRegSecuriteIndexCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceRegSecuriteIndexCqlDaoImpl extends GenericDAOImpl<TraceRegSecuriteIndexCql, String> implements ITraceRegSecuriteIndexCqlDao {

}
