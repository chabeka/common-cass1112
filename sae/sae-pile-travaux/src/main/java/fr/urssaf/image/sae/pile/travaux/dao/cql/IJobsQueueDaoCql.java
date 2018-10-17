package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;

/**
 * DAO de la colonne famille <code>JobsQueue</code>
 */

public interface IJobsQueueDaoCql extends IGenericDAO<JobQueueCql, String> {

   public void deleteByIdAndIndexColumn(final UUID id, final String key, long clock);

   public Iterator<JobQueueCql> getUnreservedJobRequest();

   public Iterator<JobQueueCql> getNonTerminatedSimpleJobs(String hostname);

   public Optional<JobQueueCql> findByIdAndIndexColumn(final UUID id, final String key);

   // public List<JobQueueCql> getNonTerminatedJobs(String key);

   public Optional<JobQueueCql> findByIndexedColumn(final UUID idjob);
}
