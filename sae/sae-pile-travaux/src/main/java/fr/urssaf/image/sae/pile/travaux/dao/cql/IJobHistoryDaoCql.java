package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.UUID;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.model.JobHistoryCql;

/**
 * DAO de la colonne famille <code>JobHistory</code>
 */

public interface IJobHistoryDaoCql extends IGenericDAO<JobHistoryCql, UUID> {

}