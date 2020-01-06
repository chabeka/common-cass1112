package fr.urssaf.image.sae.piletravaux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobQueueServiceImpl;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;
import fr.urssaf.image.sae.utils.CompareUtils;
import fr.urssaf.image.sae.utils.RepriseFileUtils;
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

  private static final String JOBHISTORY_TXT = "JobHistory.txt";

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

    LOGGER.debug("MigrationJobHistory --  migrationFromThriftToCql start");

    // Clé de depart de l'itération
    UUID startKey = null;

    // nombre total de Row traité
    int totalRow = 0;

    // Gestion du ficher d'enregistrement des clés en cas de reprise
    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {
      file = RepriseFileUtils.getKeysFile(RepriseFileUtils.getKeyFileDir(), JOBHISTORY_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String strKey = RepriseFileUtils.getLastLine(file);
      if (strKey != null && !strKey.isEmpty()) {
        startKey = UUID.fromString(strKey);
      }


      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final UUIDSerializer uuidSerializer = UUIDSerializer.get();

      final RangeSlicesQuery<UUID, UUID, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(ccf.getKeyspace(),
                                  uuidSerializer,
                                  uuidSerializer,
                                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(JobHistoryDao.JOBHISTORY_CFNAME);
      final int blockSize = 1000;
      int count = 0;

      // Map contenant key = (numero d'iteration) value=(liste des cles (UUID) des objets de l'iteration)
      final Map<Integer, List<UUID>> lastIteartionMap = new HashMap<>();

      // Numero d'itération
      int iterationNB = 0;

      // map contenant les differentes etapes du job (de la creation à la fin)
      Map<UUID, String> mapTrace = new HashMap<>();

      // Pour chaque tranche de blockSize, on recherche l'objet cql
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

        // Liste des ids de l'iteration n-1 (null si au debut)
        final List<UUID> lastlistUUID = lastIteartionMap.get(iterationNB - 1);

        // Liste des ids de l'iteration courante
        final List<UUID> currentlistUUID = new ArrayList<>();

        for (final me.prettyprint.hector.api.beans.Row<UUID, UUID, byte[]> row : orderedRows) {

          UUID column = null;
          String infoTrace = null;

          final UUID currentKey = row.getKey();
          currentlistUUID.add(currentKey);

          final List<HColumn<UUID, byte[]>> tHl = row.getColumnSlice().getColumns();
          for (final HColumn<UUID, byte[]> col : tHl) {
            column = col.getName();
            infoTrace = StringSerializer.get().fromBytes(col.getValue());
          }
          mapTrace.put(column, infoTrace);

          final JobHistoryCql jobH = new JobHistoryCql();
          jobH.setIdjob(currentKey);
          jobH.setTrace(mapTrace);

          // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
          if (lastlistUUID == null || !lastlistUUID.contains(currentKey)) {
            cqldao.saveWithMapper(jobH);
            totalRow++;
          }

          mapTrace = new HashMap<>();
          // ecriture dans le fichier
          bWriter.append(currentKey.toString());
          bWriter.newLine();
        }

        // remettre à jour la map
        lastIteartionMap.put(iterationNB, currentlistUUID);
        lastIteartionMap.remove(iterationNB - 1);
        iterationNB++;

      } while (count == blockSize);

    }
    catch (final Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    finally {
      try {
        bWriter.close();
      }
      catch (final Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    LOGGER.debug(" Totale : " + totalRow);
    LOGGER.debug("MigrationJobHistory --  migrationFromThriftToCql end");

    return totalRow;
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
      LOGGER.info("MIGRATION_JobHistoryCql -- Les listes metadata sont identiques");
    } else {
      LOGGER.warn("MIGRATION_JobHistoryCql -- ATTENTION: Les listes metadata sont différentes ");
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
