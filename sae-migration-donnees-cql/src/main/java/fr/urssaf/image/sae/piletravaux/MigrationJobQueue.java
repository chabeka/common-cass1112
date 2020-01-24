package fr.urssaf.image.sae.piletravaux;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobsQueueDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobQueueCql;
import fr.urssaf.image.sae.pile.travaux.service.thrift.impl.JobQueueServiceThriftImpl;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * (AC75095028) Migration CF JobQueue
 */
@Component
public class MigrationJobQueue implements IMigration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobQueue.class);

  @Autowired
  IGenericJobTypeDao genericdao;

  @Autowired
  JobQueueServiceThriftImpl serviceThrift;

  @Autowired
  IJobsQueueDaoCql cqldao;


  // @Qualifier("CassandraClientFactory")
  @Autowired
  private CassandraClientFactory ccf;

  // String keyspace = "SAE";

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationJobQueue - migrationFromThriftToCql - start ");

    final Iterator<GenericJobType> it = genericdao.findAllByCFName("JobsQueue", ccf.getKeyspace().getKeyspaceName());

    JobQueueCql jobCql;
    int nb = 0;

    while (it.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      final UUID colName = row.getUUID("column1");
      final JobQueue jobq = JobQueueSerializer.get().fromByteBuffer(row.getBytes("value"));

      jobCql = new JobQueueCql();
      jobCql.setIdJob(colName);
      jobCql.setJobParameters(jobq.getJobParameters());
      jobCql.setKey(key);
      jobCql.setType(jobq.getType());

      // enregistrement
      cqldao.saveWithMapper(jobCql);

      nb++;
    }


    LOGGER.info(" MigrationJobQueue - migrationFromThriftToCql - end");
    LOGGER.info("  MigrationJobQueue - migrationFromThriftToCql  Total : " + nb);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromCqlTothrift() {
    LOGGER.info(" MigrationJobQueue - migrationFromCqlToThrift start ");

    final Iterator<JobQueueCql> it = cqldao.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      // final Row row = (Row) it.next();
      final JobQueueCql jobcql = it.next();

      final JobToCreate jobToCreate = new JobToCreate();
      jobToCreate.setIdJob(jobcql.getIdJob());
      jobToCreate.setType(jobcql.getType());
      jobToCreate.setJobParameters(jobcql.getJobParameters());
      serviceThrift.addJobsQueue(jobToCreate);

      nb++;
    }


    LOGGER.info(" MigrationJobQueue - migrationFromCqlToThrift end");
    LOGGER.info(" MigrationJobQueue - migrationFromCqlToThrift Total : " + nb);

  }

  // ############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobQueueCql() {

    // liste d'objet cql venant de la base thrift après transformation
    final List<JobQueueCql> listJobThrift = getListJobHistoryThrift();

    // liste venant de la base cql

    final List<JobQueueCql> listRToCql = new ArrayList<>();
    final Iterator<JobQueueCql> it = cqldao.findAllWithMapper();
    while (it.hasNext()) {
      final JobQueueCql jobQueueCql = it.next();
      listRToCql.add(jobQueueCql);
    }

    // comparaison de deux listes
    final boolean isEqBase = CompareUtils.compareListsGeneric(listRToCql, listJobThrift);
    if (isEqBase) {
      LOGGER.info("MIGRATION_JobQueueCql -- Les listes JobQueue sont identiques");
    } else {
      LOGGER.warn("MIGRATION_JobQueueCql -- ATTENTION: Les listes JobQueue sont différentes ");
    }

    return isEqBase;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * 
   * @return
   */
  public List<JobQueueCql> getListJobHistoryThrift() {

    final List<JobQueueCql> listJobThrift = new ArrayList<>();

    final Iterator<GenericJobType> it = genericdao.findAllByCFName("JobsQueue", ccf.getKeyspace().getKeyspaceName());

    JobQueueCql jobCql;
    while (it.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      final UUID colName = row.getUUID("column1");
      final JobQueue jobq = JobQueueSerializer.get().fromByteBuffer(row.getBytes("value"));

      jobCql = new JobQueueCql();
      jobCql.setIdJob(colName);
      jobCql.setJobParameters(jobq.getJobParameters());
      jobCql.setKey(key);
      jobCql.setType(jobq.getType());

      // enregistrement
      listJobThrift.add(jobCql);

    }

    return listJobThrift;
  }
}
