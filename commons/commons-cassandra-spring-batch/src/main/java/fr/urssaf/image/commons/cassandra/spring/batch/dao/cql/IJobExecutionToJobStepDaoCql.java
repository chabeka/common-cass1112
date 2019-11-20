/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;

/**
 * Interface DAO de {@link JobExecutionToJobStepCql}<br>
 * Les parametres:<br>
 * <b> JobExecutionToJobStepCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> Long</b>
 * le type d'Identifiant de l'objet<br>
 */
public interface IJobExecutionToJobStepDaoCql extends IGenericIndexDAO<JobExecutionToJobStepCql, Long> {

}
