/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;

/**
 * Interface DAO de {@link JobStepsCql}
 * 
 * @param <JobStepsCql>
 *          Type de d'objet contenue dans le registre
 * @param <Long>
 *          le type d'Identifiant de l'objet
 */
public interface IJobStepsDaoCql extends IGenericIndexDAO<JobStepsCql, Long> {

}
