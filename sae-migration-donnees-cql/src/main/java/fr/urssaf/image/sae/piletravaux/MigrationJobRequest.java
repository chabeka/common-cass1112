/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.VISerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupport;
import fr.urssaf.image.sae.pile.travaux.utils.JobRequestMapper;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationJobRequest implements IMigration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobRequest.class);


  @Autowired
  IGenericJobTypeDao genericdao;

  @Autowired
  IJobRequestDaoCql cqldao;

  @Autowired
  JobRequestSupport jobRequestSupport;

  @Autowired
  JobRequestDao jobRequestDao;

  // @Qualifier("CassandraClientFactory")
  @Autowired
  private CassandraClientFactory ccf;

  // String keyspace = "SAE";

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationJobRequest - migrationFromThriftToCql - start ");

    // extraction des colonnes dans la table thrift
    final Iterator<GenericJobType> it = genericdao.findAllByCFName("JobRequest", ccf.getKeyspace().getKeyspaceName());

    UUID lastKey = null;
    if (it.hasNext()) {
      final Row row = (Row) it.next();
      lastKey = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
    }

    int nb = 0;
    UUID key = null;

    while (it.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) it.next();
      key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));

      // compare avec la derniere clé qui a été extraite
      // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
      // alors on enrgistre celui qui vient d'être traité
      if (key != null && !key.equals(lastKey)) {

        // <UUID, String> le type de clé et le nom de la colonne
        final ColumnFamilyResult<UUID, String> result = jobRequestSupport.getJobRequestTmpl().queryColumns(lastKey);

        // Conversion en objet JobRequest
        final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(result);

        final JobRequestCql jobcql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(jobRequest);
        // enregistrement
        cqldao.saveWithMapper(jobcql);

        // réinitialisation
        lastKey = key;

        nb++;
      }

      // dernier cas
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport.getJobRequestTmpl().queryColumns(lastKey);
      // Conversion en objet JobRequest
      final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(result);
      final JobRequestCql jobcql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(jobRequest);
      // enregistrement
      cqldao.saveWithMapper(jobcql);

    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobRequest - migrationFromThriftToCql - end");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationJobRequest - migrationFromCqlToThrift start ");

    final Iterator<JobRequestCql> it = cqldao.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      final JobRequest jobcql = JobRequestMapper.mapJobRequestCqlToJobRequestThrift(it.next());
      final UUID idJob = jobcql.getIdJob();

      final JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType(jobcql.getType());
      job.setJobParameters(jobcql.getJobParameters());
      job.setClientHost(jobcql.getClientHost());
      job.setDocCount(jobcql.getDocCount());
      job.setSaeHost(jobcql.getSaeHost());
      job.setCreationDate(jobcql.getCreationDate());
      job.setDocCountTraite(jobcql.getDocCountTraite());
      job.setJobKey(jobcql.getJobKey());
      job.setParameters(job.getParameters());
      job.setVi(jobcql.getVi());

      final Date date = new Date();
      jobRequestSupport.ajouterJobDansJobRequest(job, date.getTime());
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobRequest - migrationFromCqlToThrift end");

  }

  // ############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobRequestCql() {

    // liste d'objet cql venant de la base thrift après transformation
    final List<JobRequestCql> listJobThrift = getListJobRequestThrift();

    // liste venant de la base cql

    final List<JobRequestCql> listRToCql = new ArrayList<>();
    final Iterator<JobRequestCql> it = cqldao.findAllWithMapper();
    while (it.hasNext()) {
      final JobRequestCql jobRequest = it.next();
      listRToCql.add(jobRequest);
    }

    // comparaison de deux listes

    final boolean isEqBase = CompareUtils.compareListsGeneric(listRToCql, listJobThrift);
    if (isEqBase) {
      LOGGER.info("MIGRATION_JobRequestCql -- Les listes metadata sont identiques");
    } else {
      LOGGER.warn("MIGRATION_JobRequestCql -- ATTENTION: Les listes metadata sont différentes ");
    }

    return isEqBase;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * 
   * @return
   */
  public List<JobRequestCql> getListJobRequestThrift() {

    final List<JobRequestCql> listJobThrift = new ArrayList<>();

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<byte[], byte[], byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccf.getKeyspace(),
                                bytesSerializer,
                                bytesSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JobRequestDao.JOBREQUEST_CFNAME);
    final int blockSize = 1000;
    byte[] startKey = new byte[0];
    int count;
    do {

      // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
      // toutes les clé jusqu'à la dernière
      // on fixe un nombre maximal de ligne à traiter à chaque itération
      // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

      rangeSlicesQuery.setRange(new byte[0], new byte[0], false, blockSize);
      rangeSlicesQuery.setKeys(startKey, new byte[0]);
      rangeSlicesQuery.setRowCount(blockSize);
      // rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<byte[], byte[], byte[]>> result = rangeSlicesQuery.execute();

      final OrderedRows<byte[], byte[], byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> lastRow = orderedRows.peekLast();
      if (lastRow != null) {
        startKey = lastRow.getKey();
      }
      int nb = 1;
      for (final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> row : orderedRows) {
        final JobRequest job = getTraceFromResult(row);
        final JobRequestCql cql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(job);
        // tant que count == blockSize on ajout tout sauf le dernier
        // Cela empeche d'ajouter la lastRow deux fois
        if (count == blockSize && nb < count) {
          listJobThrift.add(cql);
        } else if (count != blockSize) {
          listJobThrift.add(cql);
        }
        nb++;
      }

    } while (count == blockSize);

    return listJobThrift;
  }

  /**
   * Recupère un JobRequest à partir de {@link Row}
   * 
   * @param row
   * @return
   */
  public JobRequest getTraceFromResult(final me.prettyprint.hector.api.beans.Row<byte[], byte[], byte[]> row) {

    UUID instanceId = null;
    final JobRequest jobRequest = new JobRequest();

    if (row != null) {

      instanceId = UUIDSerializer.get().fromBytes(row.getKey());
      jobRequest.setIdJob(instanceId);
      final List<HColumn<byte[], byte[]>> tHl = row.getColumnSlice().getColumns();
      for (final HColumn<byte[], byte[]> col : tHl) {
        final String name = StringSerializer.get().fromBytes(col.getName());

        if (JobRequestDao.JR_TYPE_COLUMN.equals(name)) {
          jobRequest.setType(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_PARAMETERS_COLUMN.equals(name)) {
          jobRequest.setParameters(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_STATE_COLUMN.equals(name)) {
          final String state = StringSerializer.get().fromBytes(col.getValue());
          jobRequest.setState(JobState.valueOf(state));
        } else if (JobRequestDao.JR_RESERVED_BY_COLUMN.equals(name)) {
          jobRequest.setReservedBy(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_CREATION_DATE_COLUMN.equals(name)) {
          jobRequest.setCreationDate(NullableDateSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_RESERVATION_DATE_COLUMN.equals(name)) {
          jobRequest.setReservationDate(NullableDateSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_STARTING_DATE_COLUMN.equals(name)) {
          jobRequest.setStartingDate(NullableDateSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_ENDING_DATE_COLUMN.equals(name)) {
          jobRequest.setEndingDate(NullableDateSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_MESSAGE.equals(name)) {
          jobRequest.setMessage(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_SAE_HOST.equals(name)) {
          jobRequest.setSaeHost(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_CLIENT_HOST.equals(name)) {
          jobRequest.setClientHost(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_DOC_COUNT.equals(name)) {
          jobRequest.setDocCount(IntegerSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_DOC_COUNT_TRAITE.equals(name)) {
          jobRequest.setDocCountTraite(IntegerSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_PID.equals(name)) {
          jobRequest.setPid(IntegerSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_TO_CHECK_FLAG.equals(name)) {
          jobRequest.setToCheckFlag(BooleanSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_TO_CHECK_FLAG_RAISON.equals(name)) {
          jobRequest.setToCheckFlagRaison(StringSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_VI.equals(name)) {
          jobRequest.setVi(VISerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_JOB_PARAM_COLUMN.equals(name)) {
          jobRequest.setJobParameters(MapSerializer.get().fromBytes(col.getValue()));
        } else if (JobRequestDao.JR_JOB_KEY_COLUMN.equals(name)) {
          jobRequest.setJobKey(BytesArraySerializer.get().fromBytes(col.getValue()));
        }
      }
    }

    return jobRequest;
  }

}
