/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import org.springframework.batch.admin.service.SearchableStepExecutionDao;
import org.springframework.batch.core.JobExecution;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;

/**
 * Interface DAO de {@link JobStepCql}
 * 
 * @param <JobStepCql>
 *          Type de d'objet contenue dans le registre
 * @param <Long>
 *          le type d'Identifiant de l'objet
 */
public interface IJobStepExecutionDaoCql extends IGenericDAO<JobStepCql, Long>, SearchableStepExecutionDao {

  /**
   * Supprime un step de la base de données
   *
   * @param stepExecutionId
   *          : id du step à supprimer
   */
  public void deleteStepExecution(final Long stepExecutionId);

  /**
   * Supprime tous les steps d'un jobExecution donné
   *
   * @param jobExecution
   *          : jobExecution concerné
   */
  public void deleteStepsOfExecution(final JobExecution jobExecution);

}
