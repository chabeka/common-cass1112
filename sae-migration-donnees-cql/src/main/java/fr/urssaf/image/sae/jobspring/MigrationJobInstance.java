package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
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

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.serializer.JobParametersSerializer;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.utils.CompareUtils;
import fr.urssaf.image.sae.utils.RowUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Classe permettant de faire la migration de données de la table {@link Jobinstance}
 * de thrift vers cql ou de cql vers thrift
 */
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
    LOGGER.info(" MigrationJobInstance-migrationFromThriftToCql start");

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccfthrift.getKeyspace(),
                                bytesSerializer,
                                bytesSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JOB_INSTANCE_CF_NAME);
    final int blockSize = RowUtils.BLOCK_SIZE_JOB_INSTANCE;
    byte[] startKey = new byte[0];
    int count;
    int nbRows = 0;

    // Map contenant key = (numero d'iteration)
    // value=(liste des UUID des objets de l'iteration)
    final Map<Integer, List<Long>> lastIteartionMap = new HashMap<>();

    // Numero d'itération
    int iterationNB = 0;

    do {

      // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
      // toutes les clé jusqu'à la dernière
      // on fixe un nombre maximal de ligne à traiter à chaque itération
      // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

      rangeSlicesQuery.setRange(new byte[0], new byte[0], false, blockSize);
      rangeSlicesQuery.setKeys(startKey, new byte[0]);
      rangeSlicesQuery.setRowCount(blockSize);

      final QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery.execute();

      final OrderedRows<byte[], byte[], byte[]> orderedRows = result.get();

      count = orderedRows.getCount();


      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> lastRow = orderedRows.peekLast();

      if (lastRow != null) {
        startKey = lastRow.getKey();
      }

      // Liste des ids de l'iteration n-1 (null si au debut)
      final List<Long> lastlistUUID = lastIteartionMap.get(iterationNB - 1);

      // Liste des ids de l'iteration courante
      final List<Long> currentlistUUID = new ArrayList<>();

      // On recupère l'instance dans le row
      for (final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> row : orderedRows) {

        if (RowUtils.rowBbbHasColumns(row)) {

          final JobInstance job = getTraceFromResult(row);
          if (job != null) {

            final JobInstanceCql cql = JobTranslateUtils.getJobInstanceCqlToJobInstance(job);

            currentlistUUID.add(job.getId());
            // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
            if (lastlistUUID == null || !lastlistUUID.contains(job.getId())) {

              jobInstancedaoCql.saveWithMapper(cql);

              nbRows++;
              if (nbRows % 1000 == 0) {
                LOGGER.info(" Nb rows : " + nbRows);
              }
            }
          }
        }
      }

      // remettre à jour la map
      lastIteartionMap.put(iterationNB, currentlistUUID);
      lastIteartionMap.remove(iterationNB - 1);
      iterationNB++;

    } while (count == blockSize);

    LOGGER.info(" MigrationJobInstance-migrationFromThriftToCql end");
    LOGGER.info(" MigrationJobInstance-migrationFromThriftToCql-Nb total d'entrées dans la CF : " + nbRows);

  }

  public JobInstance getTraceFromResult(final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> row) {

    String name = null;
    Integer version = null;
    Long instanceId = null;
    String jobName = "";
    JobParameters jobParameters = null;

    if (RowUtils.rowBbbHasColumns(row)) {

      instanceId = LongSerializer.get().fromBytes(row.getKey());

      final List<HColumn<byte[], byte[]>> tHl = row.getColumnSlice().getColumns();

      for(final HColumn<byte[], byte[]> col : tHl) {

        if (col.getName() != null) {
          name = StringSerializer.get().fromBytes(col.getName());
        }

        if (name.equals("parameters")) {
          final JobParametersSerializer serializer = JobParametersSerializer.get();
          jobParameters = serializer.fromBytes(col.getValue());
        }

        if (name.equals("name")) {
          final StringSerializer serializer = StringSerializer.get();
          jobName = serializer.fromBytes(col.getValue());
        }

        if (name.equals("version")) {
          version = IntegerSerializer.get().fromBytes(col.getValue());
        }			  

      }
    }

    final JobInstance instance = new JobInstance(instanceId, jobParameters, jobName);
    instance.setVersion(version);
    return instance;

  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {
    LOGGER.info(" MigrationJobInstance-migrationFromCqlTothrift start");
    jobInstanceTemplate = new ThriftColumnFamilyTemplate<>(
        ccfthrift.getKeyspace(),
        JOB_INSTANCE_CF_NAME,
        LongSerializer.get(),
        StringSerializer.get());

    final Iterator<JobInstanceCql> it = jobInstancedaoCql.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      final JobInstance job = JobTranslateUtils.getJobInstanceToJobInstanceCql(it.next());
      saveJobInstance(job);
      nb++;
    }
    LOGGER.info(" MigrationJobInstance-migrationFromCqlTothrift end");
    LOGGER.info(" MigrationJobInstance-migrationFromCqlTothrift-Nb total d'entrées dans la CF : " + nb);
  }

  // #############################################################################################
  // #### -------------------- Methode utilitaires ------------- ####
  // #############################################################################################


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

  //############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobInstance() {

    // liste venant de la base thrift après transformation
    final List<JobInstanceCql> listJobThrift = getListJobInstanceThrift();

    // liste venant de la base cql
    final List<JobInstanceCql> listJobCql = new ArrayList<>();
    final Iterator<JobInstanceCql> it = jobInstancedaoCql.findAllWithMapper();
    while (it.hasNext()) {
      final JobInstanceCql jobInst = it.next();        
      listJobCql.add(jobInst);	    	
    }

    // comparaison de deux listes
    final Boolean isEqual = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (CompareUtils.compareListsGeneric(listJobCql, listJobThrift)) {
      LOGGER.info("MIGRATION_JobInstance -- Les listes JobInstance sont identiques");
    } else {
      LOGGER.warn("MIGRATION_JobInstance -- ATTENTION: Les listes JobInstance sont différentes ");
    }

    return isEqual;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * @return
   */
  public List<JobInstanceCql> getListJobInstanceThrift(){

    final List<JobInstanceCql> listJobThrift = new ArrayList<>();

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccfthrift.getKeyspace(),
                                bytesSerializer,
                                bytesSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JOB_INSTANCE_CF_NAME);
    final int blockSize = RowUtils.BLOCK_SIZE_JOB_INSTANCE;
    byte[] startKey = new byte[0];
    int count;

    // Map contenant key = (numero d'iteration)
    // value=(liste des UUID des objets de l'iteration)
    final Map<Integer, List<Long>> lastIteartionMap = new HashMap<>();

    // Numero d'itération
    int iterationNB = 0;

    do {

      // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
      // toutes les clé jusqu'à la dernière
      // on fixe un nombre maximal de ligne à traiter à chaque itération
      // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

      rangeSlicesQuery.setRange(new byte[0], new byte[0], false, blockSize);
      rangeSlicesQuery.setKeys(startKey, new byte[0]);
      rangeSlicesQuery.setRowCount(blockSize);
      //rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery.execute();

      final OrderedRows<byte[], byte[], byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> lastRow = orderedRows.peekLast();
      if (lastRow != null) {
        startKey = lastRow.getKey();
      }

      // Liste des ids de l'iteration n-1 (null si au debut)
      final List<Long> lastlistUUID = lastIteartionMap.get(iterationNB - 1);

      // Liste des ids de l'iteration courante
      final List<Long> currentlistUUID = new ArrayList<>();

      // On recupère l'instance dans le row
      for (final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> row : orderedRows) {
        if (RowUtils.rowBbbHasColumns(row)) {
          final JobInstance job = getTraceFromResult(row);
          if (job != null) {
            final JobInstanceCql cql = JobTranslateUtils.getJobInstanceCqlToJobInstance(job);

            currentlistUUID.add(job.getId());
            // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
            if (lastlistUUID == null || !lastlistUUID.contains(job.getId())) {
              listJobThrift.add(cql);
            }
          }
        }
      }

      // remettre à jour la map
      lastIteartionMap.put(iterationNB, currentlistUUID);
      lastIteartionMap.remove(iterationNB - 1);
      iterationNB++;

    } while (count == blockSize);

    return listJobThrift;
  }
}
