package fr.urssaf.image.sae.pile.travaux.dao.cql;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;

/**
 * DAO de la colonne famille {@link JobHistoryCql}
 */

public interface IJobHistoryDaoCql extends IGenericDAO<JobHistoryCql, UUID> {

}
