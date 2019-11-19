package fr.urssaf.image.sae.trace.daocql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;

/**
 * Interface DAO de {@link TraceRegTechniqueIndexCql}
 * 
 * @param <TraceRegTechniqueIndexCql>
 *          Type de d'objet contenue dans le registre
 * @param <String>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceRegTechniqueIndexCqlDao extends IGenericDAO<TraceRegTechniqueIndexCql, String> {

}
