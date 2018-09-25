/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.sae.commons.dao.IGenericIndexDAO;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobInstancesByNameDaoCql extends IGenericIndexDAO<JobInstancesByNameCql, String> {

  public void deleteById(String id);
}
