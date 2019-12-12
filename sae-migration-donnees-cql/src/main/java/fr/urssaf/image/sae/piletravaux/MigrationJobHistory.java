/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobQueueServiceImpl;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Migration de CF JobHistory
 */
@Component
public class MigrationJobHistory {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobHistory.class);

  @Autowired
  IGenericJobTypeDao genericdao;

  @Autowired
  JobHistoryDao daoThrift;

  @Autowired
  JobQueueServiceImpl serviceThrift;

  @Autowired
  IJobHistoryDaoCql cqldao;


  // @Qualifier("CassandraClientFactory")
  @Autowired
  private CassandraClientFactory ccf;

  // String keyspace = "SAE";

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public int migrationFromThriftToCql() {

    LOGGER.info(" MigrationJobHistory - migrationFromThriftToCql - start ");

    final Iterator<GenericJobType> it = genericdao.findAllByCFName("JobHistory", ccf.getKeyspace().getKeyspaceName());

    UUID lastKey = null;

    Map<UUID, String> trace = new HashMap<>();
    JobHistoryCql jobH;
    int nb = 0;
    UUID key = null;

    while (it.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) it.next();
      key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (lastKey == null) {
        lastKey = key;
      }
      // compare avec la derniere clé qui a été extraite
      // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
      // alors on enrgistre celui qui vient d'être traité
      if (key != null && !key.equals(lastKey)) {

        jobH = new JobHistoryCql();
        jobH.setIdjob(lastKey);
        jobH.setTrace(trace);

        // enregistrement
        cqldao.saveWithMapper(jobH);

        // réinitialisation
        lastKey = key;
        trace = new HashMap<>();
        nb++;
      }

      // extraction de la colonne
      final UUID columnName = row.getUUID("column1");

      // extraction de la value
      final String message = StringSerializer.get().fromByteBuffer(row.getBytes("value"));

      trace.put(columnName, message);

    }
    if (key != null) {

      jobH = new JobHistoryCql();
      jobH.setIdjob(key);
      jobH.setTrace(trace);

      // enregistrement
      cqldao.saveWithMapper(jobH);

      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobHistory - migrationFromThriftToCql - end");

    return nb;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationJobHistory - migrationFromCqlToThrift start ");

    final Iterator<JobHistoryCql> it = cqldao.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      // final Row row = (Row) it.next();
      final JobHistoryCql jobcql = it.next();
      final UUID idIob = jobcql.getIdjob();

      for (final Entry<UUID, String> entry : jobcql.getTrace().entrySet()) {
        final GenericJobType job = new GenericJobType();
        job.setKey(UUIDSerializer.get().toByteBuffer(idIob));
        job.setColumn1(entry.getKey());
        job.setValue(StringSerializer.get().toByteBuffer(entry.getValue()));
        serviceThrift.addHistory(idIob, entry.getKey(), entry.getValue());
      }
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobHistory - migrationFromCqlToThrift end");
  }

  // ############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobHistoryCql() {

    // liste d'objet cql venant de la base thrift après transformation
    final List<JobHistoryCql> listJobThrift = getListJobHistoryThrift();

    // liste venant de la base cql

    final List<JobHistoryCql> listRToCql = new ArrayList<>();
    final Iterator<JobHistoryCql> it = cqldao.findAllWithMapper();
    while (it.hasNext()) {
      final JobHistoryCql jobHistory = it.next();
      listRToCql.add(jobHistory);
    }

    // comparaison de deux listes
    final boolean isEqBase = CompareUtils.compareListsGeneric(listRToCql, listJobThrift);
    if (isEqBase) {
      LOGGER.info("MIGRATION_JobInstanceByName -- Les listes metadata sont identiques");
    } else {
      LOGGER.warn("MIGRATION_JobInstanceByName -- ATTENTION: Les listes metadata sont différentes ");
    }

    return isEqBase;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * 
   * @return
   */
  public List<JobHistoryCql> getListJobHistoryThrift() {

    final List<JobHistoryCql> listJobThrift = new ArrayList<>();

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final UUIDSerializer uuidSerializer = UUIDSerializer.get();

    final RangeSlicesQuery<UUID, UUID, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccf.getKeyspace(),
                                uuidSerializer,
                                uuidSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JobHistoryDao.JOBHISTORY_CFNAME);
    final int blockSize = 1000;
    UUID startKey = null;
    UUID currentKey = null;
    UUID lastKey = null;

    Map<UUID, String> mapTrace = new HashMap<>();
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
      final QueryResult<OrderedRows<UUID, UUID, byte[]>> result = rangeSlicesQuery.execute();

      final OrderedRows<UUID, UUID, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<UUID, UUID, byte[]> lastRow = orderedRows.peekLast();
      if (lastRow != null) {
        startKey = lastRow.getKey();
      }

      for (final me.prettyprint.hector.api.beans.Row<UUID, UUID, byte[]> row : orderedRows) {

        UUID column = null;
        String infoTrace = null;

        currentKey = row.getKey();

        if (lastKey == null) {
          lastKey = currentKey;
        }

        if (currentKey != null && !currentKey.equals(lastKey)) {
          final JobHistoryCql jobH = new JobHistoryCql();
          jobH.setIdjob(lastKey);
          jobH.setTrace(mapTrace);
          lastKey = currentKey;
          listJobThrift.add(jobH);

          // reinitialisation
          mapTrace = new HashMap<>();
        }

        final List<HColumn<UUID, byte[]>> tHl = row.getColumnSlice().getColumns();
        for (final HColumn<UUID, byte[]> col : tHl) {
          column = col.getName();
          infoTrace = StringSerializer.get().fromBytes(col.getValue());
        }
        mapTrace.put(column, infoTrace);

      }

    } while (count == blockSize);

    // enregistrer le dernier cas
    if (currentKey != null) {
      final JobHistoryCql jobH = new JobHistoryCql();
      jobH.setIdjob(lastKey);
      jobH.setTrace(mapTrace);
      listJobThrift.add(jobH);
    }

    return listJobThrift;
  }

}
