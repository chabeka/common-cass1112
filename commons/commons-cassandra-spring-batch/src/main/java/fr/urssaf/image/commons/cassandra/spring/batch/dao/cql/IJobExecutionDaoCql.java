/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import org.springframework.batch.admin.service.SearchableJobExecutionDao;
import org.springframework.batch.core.JobInstance;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;

/**
 * Interface DAO de {@link JobExecutionCql}
 * 
 * @param JobExecutionCql
 *          Type de d'objet contenue dans le registre
 * @param Long
 *          Le type d'Identifiant de l'objet
 */
public interface IJobExecutionDaoCql extends IGenericDAO<JobExecutionCql, Long>, SearchableJobExecutionDao {
  /**
   * Supprime un jobExecution
   *
   * @param jobExecutionId
   *          id du jobExecution à supprimer
   * @param jobName
   *          nom du job
   * @param stepExecutionDao
   *          DAO permettant de supprimer les steps de l'instance
   */
  public void deleteJobExecution(long jobExecutionId, String jobName,
                                 IJobStepExecutionDaoCql stepExecutionDao);

  /**
   * Supprime tous les jobExecutions relatif à une instance de job donnée
   *
   * @param jobInstance
   *          jobInstance concerné
   * @param stepExecutionDao
   *          DAO permettant de supprimer les steps de l'instance
   */
  public void deleteJobExecutionsOfInstance(JobInstance jobInstance,
                                            IJobStepExecutionDaoCql stepExecutionDao);
}
