package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.Optional;
import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;

/**
 * DAO de la colonne famille <code>JobRequest</code>
 */

public interface IJobRequestDaoCql extends IGenericDAO<JobRequestCql, UUID> {

   public Optional<JobRequestCql> getJobRequestIdByJobKey(final byte[] jobKey);
}
