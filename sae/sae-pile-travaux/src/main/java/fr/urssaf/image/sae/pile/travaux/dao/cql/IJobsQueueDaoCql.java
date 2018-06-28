package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;

/**
 * DAO de la colonne famille <code>JobsQueue</code>
 */

public interface IJobsQueueDaoCql extends IGenericDAO<JobQueueCql, UUID> {

  public void deleteByIdAndIndexColumn(final UUID id, final String colSituation, long clock);

}
