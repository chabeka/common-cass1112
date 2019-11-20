/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;

/**
 * Interface DAO de {@link JobInstanceToJobExecutionCql}<br>
 * Les parametres:<br>
 * <b> JobInstancesByNameCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> Long</b>
 * le type d'Identifiant de l'objet
 */
public interface IJobInstanceToJobExecutionDaoCql extends IGenericIndexDAO<JobInstanceToJobExecutionCql, Long> {

  /**
   * Methode permettant de retourner la derniere instance de job instance par identifiant de job
   * 
   * @param id
   *          Identifiant de job
   * @return la derniere instance de job instance par identifiant de job
   */
  JobInstanceToJobExecutionCql getLastJobInstanceToJobExecution(Long id);

}
