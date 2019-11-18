/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;

/**
 * Interface DAO de {@link JobInstancesByNameCql}
 * 
 * @param <JobInstancesByNameCql>
 *          Type de d'objet contenue dans le registre
 * @param <String>
 *          le type d'Identifiant de l'objet
 */
public interface IJobInstancesByNameDaoCql extends IGenericIndexDAO<JobInstancesByNameCql, String> {

	/**
	 * Supprimer le {@link JobInstancesByNameCql} en fonction de l'id 
	 * @param id
	 */
  public void deleteById(String id);
}
