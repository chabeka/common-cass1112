package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;

/**
 * DAO de la colonne famille {@link JobHistory}
 */
@Repository
public class JobHistoryDaoCqlImpl extends GenericDAOImpl<JobHistoryCql, UUID> implements IJobHistoryDaoCql {

  /**
   * @param ccf
   */
  @Autowired
  public JobHistoryDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }
}
