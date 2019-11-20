/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.List;

import org.springframework.batch.admin.service.SearchableJobInstanceDao;
import org.springframework.batch.core.JobInstance;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;

/**
 * Interface DAO de {@link JobInstanceCql}<br>
 * Les parametres:<br>
 * <b> JobInstanceCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> Long</b>
 * le type d'Identifiant de l'objet<br>
 */
public interface IJobInstanceDaoCql extends IGenericDAO<JobInstanceCql, Long>, SearchableJobInstanceDao {

  /**
   * Suppression d'une instance de Jod en fonction de son id
   * @param instanceId
   * 			L'id de l'instance
   */
  public void deleteJobInstance(final Long instanceId);

  /**
   * Reservation d'un Job sur serveur donné
   * @param instanceId
   * 			L'id de l'instance
   * @param	serverName
   * 			le nom du serveur
   */
  public void reserveJob(long instanceId, String serverName);

  /**
   * Retourne le nom du serveur qui reserve le Job d'identifiant donné
   * @param instanceId
   * 			L'id de l'instance
   * @return
   * 			le nom du serveur
   */
  public String getReservingServer(long instanceId);

  /**
   * Retourne la liste des Job non reservé
   */
  public List<JobInstance> getUnreservedJobInstances();
}
