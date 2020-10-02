package fr.urssaf.image.sae.trace.daocql;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;

/**
 * Interface DAO de {@link TraceRegTechniqueCql}
 * 
 * @param <TraceRegTechniqueCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceRegTechniqueCqlDao extends IGenericDAO<TraceRegTechniqueCql, UUID> {

}

