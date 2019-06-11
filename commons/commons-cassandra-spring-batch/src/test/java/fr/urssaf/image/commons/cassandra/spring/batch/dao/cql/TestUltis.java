/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Truncate;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsRunningCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;

/**
 * TODO (AC75095028) Description du type
 */
public class TestUltis {

  /**
   * vider toutes les tables en rapport avec spring batch
   * 
   * @param session
   * @return
   */
  public static BatchStatement truncateBase(final Session session) {
    final BatchStatement statement = new BatchStatement();
    // jobinstance
    final Truncate trun = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobInstanceCql.class));
    session.execute(trun);

    // jobstep
    final Truncate trun1 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobStepCql.class));
    session.execute(trun1);
    // jobsteps
    final Truncate trun2 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobStepsCql.class));
    session.execute(trun2);
    // jobexecution
    final Truncate trun3 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobExecutionCql.class));
    session.execute(trun3);

    // jobexecutions
    final Truncate trun4 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobExecutionsCql.class));
    session.execute(trun4);

    // jobexecutionsrunning
    final Truncate trun5 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobExecutionsRunningCql.class));
    session.execute(trun5);

    // jobinstancetojobexecution
    final Truncate trun6 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobInstanceToJobExecutionCql.class));
    session.execute(trun6);

    // jobexecutiontojobstep
    final Truncate trun7 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(JobExecutionToJobStepCql.class));
    session.execute(trun7);

    // sequences
    final Truncate trun8 = QueryBuilder.truncate(ColumnUtil.getColumnFamily(SequencesCql.class));
    session.execute(trun8);

    return statement;
  }
}
