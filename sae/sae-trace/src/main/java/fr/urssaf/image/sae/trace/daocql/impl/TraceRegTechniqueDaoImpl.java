package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceRegTechniqueCqlDao} de la famille de colonnes {@link TraceRegTechniqueCql}
 * 
 * @param <TraceRegTechniqueCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceRegTechniqueDaoImpl extends GenericDAOImpl<TraceRegTechniqueCql, UUID> implements ITraceRegTechniqueCqlDao {

}
