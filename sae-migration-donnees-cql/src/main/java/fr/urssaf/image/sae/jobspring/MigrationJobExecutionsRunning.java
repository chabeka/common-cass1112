/**
 *
 */
package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsRunningCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionsRunningDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Classe permettant de faire la migration de données de la table {@link JobExecutionsRunning}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobExecutionsRunning extends MigrationJob implements IMigration {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationJobinstanceToJobExecution.class);

  @Autowired
  IJobExecutionsRunningDaoCql jobExeToJobR;

  @Override
  public void migrationFromThriftToCql() {

    LOG.info(" MigrationJobExecutionsRunning - migrationFromThriftToCql - DEBUT ");

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBEXECUTIONS_RUNNING_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    int nb = 0;
    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (!Constante.ALL_JOBS_KEY.equals(key)) {
        final Long idJobEx = row.getLong("column1");
        final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
        final JobExecutionsRunningCql jobExes = new JobExecutionsRunningCql();
        jobExes.setJobExecutionId(idJobEx);
        jobExes.setJobName(key);
        jobExes.setValue(value);

        jobExeToJobR.saveWithMapper(jobExes);
        nb++;
      }

    }
    LOG.info(" MigrationJobExecutionsRunning - migrationFromThriftToCql - FIN   ");
    LOG.info(" MigrationJobExecutionsRunning - migrationFromThriftToCql - Total:{}   ", nb);

  }

  @Override
  public void migrationFromCqlTothrift() {

    LOG.info(" MigrationJobExecutionsRunning - migrationFromCqlTothrift - DEBUT ");

    final Serializer<String> sSlz = StringSerializer.get();
    final Serializer<byte[]> bSlz = BytesArraySerializer.get();
    final Serializer<Long> lSlz = LongSerializer.get();
    final byte[] empty = new byte[0];

    final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

    final Iterator<JobExecutionsRunningCql> it = jobExeToJobR.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      final JobExecutionsRunningCql jobExToJR = it.next();
      final String jobName = jobExToJR.getJobName();
      final Long jobExecutionId = jobExToJR.getJobExecutionId();

      mutator.addInsertion(sSlz.toBytes(jobName),
                           Constante.JOBEXECUTIONS_RUNNING_CFNAME,
                           HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
      mutator.addInsertion(sSlz.toBytes(Constante.ALL_JOBS_KEY),
                           Constante.JOBEXECUTIONS_RUNNING_CFNAME,
                           HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
      mutator.execute();
      nb++;
    }

    LOG.info(" MigrationJobExecutionsRunning - migrationFromCqlTothrift - FIN   ");
    LOG.info(" MigrationJobExecutionsRunning - migrationFromCqlTothrift - Total:   ", nb);

  }

  //############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobExecutionsRunning() {

    // liste venant de la base thrift après transformation
    final List<JobExecutionsRunningCql> listJobThrift = getListJobExeRunThrift();

    // liste venant de la base cql
    final List<JobExecutionsRunningCql> listJobCql = new ArrayList<>();
    final Iterator<JobExecutionsRunningCql> it = jobExeToJobR.findAllWithMapper();
    while (it.hasNext()) {
      final JobExecutionsRunningCql jobExToJR = it.next();        
      listJobCql.add(jobExToJR);	    	
    }

    // comparaison de deux listes
    final boolean isListEq = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isListEq) {
      LOG.info("MIGRATION_JobExecutionsRunning -- Les listes JobExecutionsRunning sont identiques , nb=" + listJobThrift.size());
    } else {
      LOG.warn("MIGRATION_JobExecutionsRunning -- ATTENTION: Les listes JobExecutionsRunning sont différentes ");
    }

    return isListEq;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * @return
   */
  public List<JobExecutionsRunningCql> getListJobExeRunThrift(){

    final List<JobExecutionsRunningCql> listJobThrift = new ArrayList<>();

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBEXECUTIONS_RUNNING_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (!Constante.ALL_JOBS_KEY.equals(key)) {
        final Long idJobEx = row.getLong("column1");
        final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
        final JobExecutionsRunningCql jobExes = new JobExecutionsRunningCql();
        jobExes.setJobExecutionId(idJobEx);
        jobExes.setJobName(key);
        jobExes.setValue(value);

        listJobThrift.add(jobExes);
      }
    }
    return listJobThrift;
  }
}
