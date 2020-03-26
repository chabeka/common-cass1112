package fr.urssaf.image.sae.jobspring;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobExecutionDAO;
import fr.urssaf.image.sae.jobspring.dao.IGenericJobSpringDAO;

/**
 * (AC75095028) Classe générique de migration pour les Jobs
 */
public class MigrationJob {

  protected static final Date DATE = new Date();

  @Autowired
  protected IGenericJobSpringDAO genericdao;

  @Autowired
  protected IGenericJobExecutionDAO genericJobExdao;

  @Autowired
  protected CassandraCQLClientFactory ccfcql;

  // @Qualifier("CassandraClientFactory")
  @Autowired
  protected CassandraClientFactory ccfthrift;

}
