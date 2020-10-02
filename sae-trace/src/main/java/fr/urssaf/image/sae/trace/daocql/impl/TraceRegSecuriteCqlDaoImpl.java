/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceRegSecuriteCqlDao} de la famille de colonnes {@link TraceRegSecuriteCql}
 * 
 * @param <TraceRegSecuriteCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceRegSecuriteCqlDaoImpl extends GenericDAOImpl<TraceRegSecuriteCql, UUID> implements ITraceRegSecuriteCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceRegSecuriteCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
