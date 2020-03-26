/**
 *   (AC75095028) 
 */
package fr.urssaf.image.sae.trace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.trace.dao.IGenericTraceTypeDao;

/**
 * Classe de migration Trace
 */
public class MigrationTrace {

  // public String keyspace_tu = "keyspace_tu";

  protected static final Date DATE = new Date();

  @Autowired
  protected IGenericTraceTypeDao genericdao;

  @Autowired
  protected CassandraCQLClientFactory ccfcql;

  // @Qualifier("CassandraClientFactory")
  @Autowired
  protected CassandraClientFactory ccfthrift;

}
