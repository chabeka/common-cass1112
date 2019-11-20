package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;

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
