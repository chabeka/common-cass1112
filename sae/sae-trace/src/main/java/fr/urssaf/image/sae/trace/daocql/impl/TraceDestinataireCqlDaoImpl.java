/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class TraceDestinataireCqlDaoImpl extends GenericDAOImpl<TraceDestinataire, String> implements ITraceDestinataireCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceDestinataireCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
