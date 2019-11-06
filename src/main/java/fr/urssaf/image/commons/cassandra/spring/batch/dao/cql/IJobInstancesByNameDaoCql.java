/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobInstancesByNameDaoCql extends IGenericIndexDAO<JobInstancesByNameCql, String> {

  public void deleteById(String id);
}
