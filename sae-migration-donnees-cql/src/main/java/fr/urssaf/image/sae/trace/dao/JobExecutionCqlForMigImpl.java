package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.trace.model.JobExecutionCqlForMig;

@Service
public class JobExecutionCqlForMigImpl extends GenericDAOImpl<JobExecutionCqlForMig, UUID> implements IJobExecutionCqlForMigDao{

  /**
   * @param ccf
   */
  @Autowired
  public JobExecutionCqlForMigImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

}
