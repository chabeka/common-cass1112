/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import java.util.List;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;

/**
 * Interface DAO de {@link JobStepsCql}<br>
 * Les paramtres:<br>
 * <b> JobStepsCql</b>
 * Type de d'objet contenue dans le registre<br>
 * <b> Long</b>
 * le type d'Identifiant de l'objet
 */
public interface IJobStepDaoCql extends IGenericDAO<JobStepCql, Long> {
	public List<JobStepCql> findJobStepByJobExecutionId(Long jobExecutionId);
}
