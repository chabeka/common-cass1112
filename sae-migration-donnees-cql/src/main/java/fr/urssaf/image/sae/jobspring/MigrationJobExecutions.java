package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionsDaoCql;
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
 * Classe permettant de faire la migration de données de la table {@link JobExecutionsRunnings}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobExecutions extends MigrationJob implements IMigration {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationJobExecutions.class);

  protected static final String ALL_JOBS_KEY = "_all";

  @Autowired
  IJobExecutionsDaoCql jobExesdao;

  protected static final String JOBEXECUTIONS_CFNAME = "JobExecutions";

  @Override
  public void migrationFromThriftToCql() {


    LOG.info("MigrationJobExecutions - migrationFromThriftToCql - DEBUT");

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(JOBEXECUTIONS_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    int nb = 0;
    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      final Long id = row.getLong("column1");
      final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      final JobExecutionsCql jobExes = new JobExecutionsCql();
      jobExes.setJobExecutionId(id);
      jobExes.setJobName(key);
      jobExes.setValue(value);

      // La clé de type String correspond soit au nom du job, soit au mot clé « _all »
      // qui permet de parcourir les exécutions tout job confondu
      // Ce systeme avec le mot clé « _all » ne sert pas dans le cql
      // Donc on enregistre pas les données se trouvant dans la clé « _all »
      if (!ALL_JOBS_KEY.equals(key)) {
        jobExesdao.saveWithMapper(jobExes);
      }
      nb++;
    }

    LOG.info("MigrationJobExecutions - migrationFromThriftToCql - FIN");
    LOG.info("MigrationJobExecutions - migrationFromThriftToCql - Total:{} ", nb);

  }

  @Override
  public void migrationFromCqlTothrift() {
    LOG.info("MigrationJobExecutions - migrationFromCqlTothrift - DEBUT");

    final Iterator<JobExecutionsCql> it = jobExesdao.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {

      final JobExecutionsCql job = it.next();
      final Serializer<String> sSlz = StringSerializer.get();
      final Serializer<Long> lSlz = LongSerializer.get();
      final Serializer<byte[]> bSlz = BytesArraySerializer.get();
      final Long jobExecutionId = job.getJobExecutionId();

      final byte[] empty = new byte[0];
      final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

      // La clé de type String correspond soit au nom du job, soit au mot clé « _all » qui permet de parcourir les exécutions tout job confondu
      mutator.addInsertion(sSlz.toBytes(job.getJobName()), JOBEXECUTIONS_CFNAME, HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
      mutator.addInsertion(sSlz.toBytes(ALL_JOBS_KEY), JOBEXECUTIONS_CFNAME, HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));

      mutator.execute();
      nb++;
    }

    LOG.info("MigrationJobExecutions - migrationFromCqlTothrift - FIN");
    LOG.info("MigrationJobExecutions - migrationFromThriftToCql - Total:{} ", nb);

  }

  // ############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobExecutions() {

    // liste venant de la base thrift après transformation
    final List<JobExecutionsCql> listJobThrift = getListJobExeThrift();

    // liste venant de la base cql
    final List<JobExecutionsCql> listJobCql = new ArrayList<>();
    final Iterator<JobExecutionsCql> it = jobExesdao.findAllWithMapper();
    while (it.hasNext()) {    	
      final JobExecutionsCql job = it.next();	        
      listJobCql.add(job);	    	
    }

    // comparaison de deux liste
    final boolean isListEq = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isListEq) {
      LOG.info("MIGRATION_JobExecutions -- Les listes JobExecutions sont identiques , nb=" + listJobThrift.size());
    } else {
      LOG.warn("MIGRATION_JobExecutions -- ATTENTION: Les listes JobExecutions sont différentes ");
    }

    return isListEq;
  }

  /**
   * Liste des job cql venant de thirft après transformation
   * @return
   */
  public List<JobExecutionsCql> getListJobExeThrift(){

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(JOBEXECUTIONS_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    final List<JobExecutionsCql> listJobThrift = new ArrayList<>();

    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      final Long id = row.getLong("column1");
      final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      final JobExecutionsCql jobExes = new JobExecutionsCql();
      jobExes.setJobExecutionId(id);
      jobExes.setJobName(key);
      jobExes.setValue(value);

      // La clé de type String correspond soit au nom du job, soit au mot clé « _all »
      // qui permet de parcourir les exécutions tout job confondu
      // Ce systeme avec le mot clé « _all » ne sert pas dans le cql
      // Donc on enregistre pas les données se trouvant dans la clé « _all »
      if (!ALL_JOBS_KEY.equals(key)) {
        listJobThrift.add(jobExes);
      }
    }
    return listJobThrift;
  }
}
