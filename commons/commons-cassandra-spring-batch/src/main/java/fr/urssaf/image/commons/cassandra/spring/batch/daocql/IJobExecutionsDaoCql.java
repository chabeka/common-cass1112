/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql;

import java.util.Optional;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.sae.commons.dao.IGenericIndexDAO;

/**
 * TODO (AC75095028) Description du type
 */
public interface IJobExecutionsDaoCql extends IGenericIndexDAO<JobExecutionsCql, String> {

   /**
    * Recherche par colonne indexée
    *
    * @param id
    *           la colonne indexée
    * @return
    */
   public Optional<JobExecutionsCql> findByJobExecutionId(final Long id);
}
