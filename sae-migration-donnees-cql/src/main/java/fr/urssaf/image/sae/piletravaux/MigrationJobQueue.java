/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux;

import java.util.Iterator;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * TODO (AC75095028) Description du type
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


  @Qualifier("CassandraClientFactory")
  private CassandraClientFactory ccf;

  // String keyspace = "SAE";

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationJobHistory - migrationFromThriftToCql - start ");

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
      cqldao.save(jobCql);

      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobHistory - migrationFromThriftToCql - end");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromCqlTothrift() {
    LOGGER.info(" MigrationJobHistory - migrationFromCqlToThrift start ");

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

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobHistory - migrationFromCqlToThrift end");

  }
}
