/**
 *
 */
package fr.urssaf.image.sae.testutils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ExecutionContext;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobExecutionDaoThrift;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.thrift.CassandraJobInstanceDaoThrift;

/**
 *
 *
 */
public class TestUtils {

  private static final String INDEX = "index";

  /**
   * @param jobInstance
   * @param index
   * @return
   */
  public static JobExecution createJobExecution(final JobInstance jobInstance, final int index) {
    final JobExecution jobExecution = new JobExecution(jobInstance);
    final Map<String, Object> mapContext = new HashMap<>();
    mapContext.put("contexte1", "test1");
    mapContext.put("contexte2", 2);
    mapContext.put(INDEX, index);
    final ExecutionContext executionContext = new ExecutionContext(mapContext);
    jobExecution.setExecutionContext(executionContext);
    jobExecution.setExitStatus(new ExitStatus("123", "test123"));
    return jobExecution;
  }

  public static JobInstance getOrCreateTestJobInstance(final String jobName, final CassandraJobInstanceDaoThrift jobInstanceDao) {

    final Map<String, JobParameter> mapJobParameters = new HashMap<>();
    mapJobParameters.put("premier_parametre", new JobParameter("test1"));
    mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
    mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
    final JobParameters jobParameters = new JobParameters(mapJobParameters);

    JobInstance jobInstance = jobInstanceDao.getJobInstance(jobName, jobParameters);
    if (jobInstance == null) {
      jobInstance = jobInstanceDao.createJobInstance(jobName, jobParameters);
    }
    return jobInstance;
  }

  public static JobInstance getOrCreateTestJobInstanceCql(final String jobName, final IJobInstanceDaoCql jobInstanceDaocql) {
    final Map<String, JobParameter> mapJobParameters = new HashMap<>();
    mapJobParameters.put("premier_parametre", new JobParameter("test1"));
    mapJobParameters.put("deuxieme_parametre", new JobParameter("test2"));
    mapJobParameters.put("troisieme_parametre", new JobParameter(122L));
    final JobParameters jobParameters = new JobParameters(mapJobParameters);

    JobInstance jobInstance = jobInstanceDaocql.getJobInstance(jobName, jobParameters);
    if (jobInstance == null) {
      jobInstance = jobInstanceDaocql.createJobInstance(jobName, jobParameters);
    }
    return jobInstance;
  }

  public static JobExecution saveJobExecutionThrift(final JobInstance jobInstance, final int index, final CassandraJobExecutionDaoThrift jobExecutionDao) {
    final JobExecution jobExecution = createJobExecution(jobInstance, index);
    if (index != 0 && index % 9 == 0) {
      jobExecution.setEndTime(new Date());
    }
    jobExecutionDao.saveJobExecution(jobExecution);
    return jobExecution;
  }

  public static JobExecution saveJobExecutionCql(final JobInstance jobInstance, final int index, final IJobExecutionDaoCql daocql) {
    final JobExecution jobExecution = createJobExecution(jobInstance, index);
    daocql.saveJobExecution(jobExecution);
    return jobExecution;
  }
}
