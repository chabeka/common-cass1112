package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceRegTechniqueIndexCqlDao} de la famille de colonnes {@link TraceRegTechniqueIndexCql}
 * 
 * @param <TraceRegTechniqueIndexCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceRegTechniqueIndexCqlDaoImpl extends GenericDAOImpl<TraceRegTechniqueIndexCql, String> implements ITraceRegTechniqueIndexCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceRegTechniqueIndexCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
