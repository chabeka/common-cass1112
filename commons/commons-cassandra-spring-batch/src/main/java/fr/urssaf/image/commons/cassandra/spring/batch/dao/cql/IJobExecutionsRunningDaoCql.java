/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsRunningCql;

/**
 * Interface DAO de {@link JobExecutionsRunningCql} <br>
 * Les Parametres:<br>
 * <b>JobExecutionsRunningCql</b> Type de d'objet contenue dans le registre<br>
 * <b>String</b> le type d'Identifiant de l'objet
 */
public interface IJobExecutionsRunningDaoCql extends IGenericIndexDAO<JobExecutionsRunningCql, String> {

}
