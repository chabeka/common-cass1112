/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import java.util.List;

import org.springframework.batch.admin.service.SearchableJobInstanceDao;
import org.springframework.batch.core.JobInstance;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.sae.commons.dao.IGenericDAO;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobInstanceDaoCql extends IGenericDAO<JobInstanceCql, Long>, SearchableJobInstanceDao {

   public void deleteJobInstance(final Long instanceId);

   public void reserveJob(long instanceId, String serverName);

   public String getReservingServer(long instanceId);

   public List<JobInstance> getUnreservedJobInstances();
}
