package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;

/**
 * DAO de la colonne famille <code>JobHistory</code>
 */
@Repository
public class JobHistoryDaoCqlImpl extends GenericDAOImpl<JobHistoryCql, UUID> implements IJobHistoryDaoCql {

  private static final String JOBHISTORY_CFNAME = "JobHistory";

  private static final int MAX_JOB_ATTIBUTS = 100;

  private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours

}
