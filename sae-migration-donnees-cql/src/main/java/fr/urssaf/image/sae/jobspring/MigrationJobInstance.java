package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.JobParametersSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.IMigration;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

@Component
public class MigrationJobInstance extends MigrationJob implements IMigration {

  private static final String JOB_INSTANCE_CF_NAME = "JobInstance";

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobInstance.class);

  @Autowired
  IJobInstanceDaoCql jobInstancedaoCql;

  ColumnFamilyTemplate<Long, String> jobInstanceTemplate;

  // Colonnes de JobInstance
  protected static final String JI_NAME_COLUMN = "name";

  protected static final String JI_PARAMETERS_COLUMN = "parameters";

  protected static final String JI_JOB_KEY_COLUMN = "jobKey";

  protected static final String JI_VERSION = "version";

  protected static final String JI_RESERVED_BY = "reservedBy";

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {
    LOGGER.debug(" migrationFromThriftToCql start");

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccfthrift.getKeyspace(),
                                bytesSerializer,
                                bytesSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JOB_INSTANCE_CF_NAME);
    final int blockSize = 1000;
    byte[] startKey = new byte[0];
    int totalKey = 1;
    int count;
    int nbRows = 0;
    do {

      // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
      // toutes les clé jusqu'à la dernière
      // on fixe un nombre maximal de ligne à traiter à chaque itération
      // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

      rangeSlicesQuery.setRange(new byte[0], new byte[0], false, 1);
      rangeSlicesQuery.setKeys(startKey, new byte[0]);
      rangeSlicesQuery.setRowCount(blockSize);
      rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery.execute();

      final OrderedRows<byte[], byte[], byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // On enlève 1, car sinon à chaque itération, la startKey serait
      // comptée deux fois.

      totalKey += count - 1;
      nbRows = totalKey;

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      // On recupère les ids des JobInstance
      final List<Long> jobIds = new ArrayList<>(blockSize);
      for (final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> row : orderedRows) {
        final long instanceId = LongSerializer.get().fromBytes(row.getKey());
        jobIds.add(instanceId);
        nbRows++;
      }

      // on recupère les JobInstance correspondant au ids
      final List<JobInstance> listJob = getJobInstancesFromIds(jobIds);

      // on sauvegarde les JobInstance dans le nouveau schema
      for (final JobInstance inst : listJob) {
        final JobInstanceCql cql = JobTranslateUtils.getJobInstanceCqlToJobInstance(inst);
        jobInstancedaoCql.save(cql);
      }

    } while (count == blockSize);

    LOGGER.debug(" Nb total de cle dans la CF: " + totalKey);
    LOGGER.debug(" Nb total d'entrées dans la CF : " + nbRows);
    LOGGER.debug(" migrationIndexFromThriftToCql end");

  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    jobInstanceTemplate = new ThriftColumnFamilyTemplate<>(
        ccfthrift.getKeyspace(),
        JOB_INSTANCE_CF_NAME,
        LongSerializer.get(),
        StringSerializer.get());

    final Iterator<JobInstanceCql> it = jobInstancedaoCql.findAllWithMapper();
    while (it.hasNext()) {
      final JobInstance job = JobTranslateUtils.getJobInstanceToJobInstanceCql(it.next());
      saveJobInstance(job);
    }

  }

  // #############################################################################################
  // #### -------------------- Methode utilitaires ------------- ####
  // #############################################################################################

  private List<JobInstance> getJobInstancesFromIds(final Collection<Long> jobIds) {
    // Pour optimiser, on récupère tous les jobs d'un coup dans cassandra
    jobInstanceTemplate = new ThriftColumnFamilyTemplate<>(
        ccfthrift.getKeyspace(),
        JOB_INSTANCE_CF_NAME,
        LongSerializer.get(),
        StringSerializer
        .get());
    final ColumnFamilyResult<Long, String> result = jobInstanceTemplate
        .queryColumns(jobIds);

    final Map<Long, JobInstance> map = new HashMap<>(jobIds.size());
    final HectorIterator<Long, String> resultIterator = new HectorIterator<>(result);
    for (final ColumnFamilyResult<Long, String> row : resultIterator) {
      final long instanceId = row.getKey();
      map.put(instanceId, getJobInstance(instanceId, row));
    }

    //
    final List<JobInstance> jobs = new ArrayList<>(jobIds.size());
    for (final Long jobId : jobIds) {
      if (map.containsKey(jobId)) {
        jobs.add(map.get(jobId));
      }
    }
    return jobs;
  }

  private JobInstance getJobInstance(final Long instanceId,
                                     final ColumnFamilyResult<Long, String> result) {
    if (result == null || !result.hasResults()) {
      return null;
    }
    final String jobName = result.getString(JI_NAME_COLUMN);
    final byte[] serializedParams = result.getByteArray(JI_PARAMETERS_COLUMN);
    final JobParametersSerializer serializer = JobParametersSerializer.get();
    final JobParameters jobParameters = serializer.fromBytes(serializedParams);

    final JobInstance instance = new JobInstance(instanceId, jobParameters, jobName);

    instance.incrementVersion();

    return instance;
  }

  private void saveJobInstance(final JobInstance instance) {

    // On enregistre l'instance dans JobInstance
    final ColumnFamilyUpdater<Long, String> updater = jobInstanceTemplate
        .createUpdater(instance.getId());
    updater.setString(JI_NAME_COLUMN, instance.getJobName());
    final JobParametersSerializer serializer = JobParametersSerializer.get();
    final byte[] bytes = serializer.toBytes(instance.getJobParameters());
    updater.setByteArray(JI_PARAMETERS_COLUMN, bytes);
    final byte[] jobKey = CassandraJobHelper.createJobKey(instance.getJobName(), instance.getJobParameters());
    updater.setByteArray(JI_JOB_KEY_COLUMN, jobKey);
    updater.setInteger(JI_VERSION, instance.getVersion());

    jobInstanceTemplate.update(updater);

  }

}
