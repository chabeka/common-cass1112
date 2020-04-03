package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;

/**
 * DAO de la colonne famille {@link JobQueueCql}
 */

public interface IJobsQueueDaoCql extends IGenericDAO<JobQueueCql, String> {

   public void deleteByIdAndIndexColumn(final UUID id, final String key, long clock);

   public Iterator<JobQueueCql> getUnreservedJobRequest();

   public Iterator<JobQueueCql> getNonTerminatedSimpleJobs(String hostname);

   public Optional<JobQueueCql> findByIdAndIndexColumn(final UUID id, final String key);

   // public List<JobQueueCql> getNonTerminatedJobs(String key);

   public Optional<JobQueueCql> findByIndexedColumn(final UUID idjob);

   /**
    * Retourne un iterateur sur la liste des jobs dans JobQueue faisant l'objet d'un Sémaphore
    * 
    * @return
    */
   Iterator<JobQueueCql> getSemaphoredJobs();
}
