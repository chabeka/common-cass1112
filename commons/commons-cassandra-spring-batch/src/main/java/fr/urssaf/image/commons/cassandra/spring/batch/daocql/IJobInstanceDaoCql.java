/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import org.springframework.batch.admin.service.SearchableJobInstanceDao;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.sae.commons.dao.IGenericDAO;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobInstanceDaoCql extends IGenericDAO<JobInstanceCql, Long>, SearchableJobInstanceDao {

  public void deleteJobInstance(final Long instanceId);
}
