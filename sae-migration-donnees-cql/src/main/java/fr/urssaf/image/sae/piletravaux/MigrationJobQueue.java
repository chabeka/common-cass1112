/**
 *  TODO (AC75095028) Description du fichier
 */
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
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
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
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

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

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobQueue - migrationFromThriftToCql - end");

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

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobQueue - migrationFromCqlToThrift end");

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

    for (final JobQueueCql cql : listRToCql) {
      for (final JobQueueCql thr : listJobThrift) {
        final String id1 = thr.getKey() + thr.getIdJob() + "";
        final String id2 = cql.getKey() + cql.getIdJob() + "";
        if (id1.equals(id2)) {
          System.out.println(cql);
          final boolean is = cql.equals(thr);
          if (!is) {
            System.out.println(cql);
            System.out.println(thr);
          }
          else {
            System.out.println(is);
          }
        }
      }
    }
    // comparaison de deux listes
    final boolean isEqBase = CompareUtils.compareListsGeneric(listRToCql, listJobThrift);
    if (isEqBase) {
      LOGGER.info("MIGRATION_JobQueueCql -- Les listes metadata sont identiques");
    } else {
      LOGGER.warn("MIGRATION_JobQueueCql -- ATTENTION: Les listes metadata sont différentes ");
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

    final UUIDSerializer uuidSerializer = UUIDSerializer.get();
    final StringSerializer strSerializer = StringSerializer.get();
    final JobQueueSerializer jobQueueSerializer = JobQueueSerializer.get();

    final RangeSlicesQuery<String, UUID, JobQueue> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccf.getKeyspace(),
                                strSerializer,
                                uuidSerializer,
                                jobQueueSerializer);
    rangeSlicesQuery.setColumnFamily(JobsQueueDao.JOBSQUEUE_CFNAME);
    final int blockSize = 1000;
    String startKey = null;
    String currentKey = null;

    int count;
    do {

      // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
      // toutes les clé jusqu'à la dernière
      // on fixe un nombre maximal de ligne à traiter à chaque itération
      // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

      rangeSlicesQuery.setRange(null, null, false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      // rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<String, UUID, JobQueue>> result = rangeSlicesQuery.execute();

      final OrderedRows<String, UUID, JobQueue> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<String, UUID, JobQueue> lastRow = orderedRows.peekLast();
      if (lastRow != null) {
        startKey = lastRow.getKey();
      }

      for (final me.prettyprint.hector.api.beans.Row<String, UUID, JobQueue> row : orderedRows) {

        currentKey = row.getKey();

        final List<HColumn<UUID, JobQueue>> tHl = row.getColumnSlice().getColumns();
        for (final HColumn<UUID, JobQueue> col : tHl) {
          final JobQueue jobq = col.getValue();
          final JobQueueCql jobH = new JobQueueCql();
          jobH.setKey(currentKey);
          jobH.setIdJob(jobq.getIdJob());
          jobH.setType(jobq.getType());
          jobH.setJobParameters(jobq.getJobParameters());
          listJobThrift.add(jobH);

        }

      }

    } while (count == blockSize);

    return listJobThrift;
  }
}
