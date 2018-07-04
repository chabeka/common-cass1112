package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.Optional;
import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;

/**
 * DAO de la colonne famille <code>JobRequest</code>
 */

public interface IJobRequestDaoCql extends IGenericDAO<JobRequest, UUID> {

  public Optional<JobRequest> getJobRequestIdByJobKey(final byte[] jobKey);
}
