package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceJournalEvtIndexDocCqlDao} de la famille de colonnes {@link TraceJournalEvtIndexDocCql}
 * 
 * @param <TraceJournalEvtIndexDocCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceJournalEvtIndexDocDaoCqlImpl extends GenericDAOImpl<TraceJournalEvtIndexDocCql, UUID> implements ITraceJournalEvtIndexDocCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceJournalEvtIndexDocDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
