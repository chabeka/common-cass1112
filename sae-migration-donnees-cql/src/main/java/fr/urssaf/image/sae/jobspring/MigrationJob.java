/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.jobspring;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobExecutionDAO;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobSpringDAO;

/**
 * TODO (AC75095028) Description du type
 */
public class MigrationJob {

  protected static final Date DATE = new Date();

  @Autowired
  protected IGenericJobSpringDAO genericdao;

  @Autowired
  protected IGenericJobExecutionDAO genericJobExdao;

  @Autowired
  protected CassandraCQLClientFactory ccfcql;

  @Qualifier("CassandraClientFactory")
  protected CassandraClientFactory ccfthrift;

}
