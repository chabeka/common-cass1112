package fr.urssaf.image.sae.trace.daocql;

import java.util.Iterator;

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
  public Iterator<TraceRegTechniqueIndexCql> IterableFindById(final String journee, final boolean ordreInverse);

}
