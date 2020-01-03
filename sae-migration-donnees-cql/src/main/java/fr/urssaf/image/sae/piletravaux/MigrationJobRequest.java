/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupport;
import fr.urssaf.image.sae.pile.travaux.utils.JobRequestMapper;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.utils.CompareUtils;
import fr.urssaf.image.sae.utils.RepriseFileUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
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

  private static final String JOBREQUEST_TXT = "JobRequest.txt";

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

  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationJobRequest - migrationFromThriftToCql - start ");


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
      file = RepriseFileUtils.getKeysFile(RepriseFileUtils.getKeyFileDir(), JOBREQUEST_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String strKey = RepriseFileUtils.getLastLine(file);
      if (strKey != null && !strKey.isEmpty()) {
        startKey = UUID.fromString(strKey);
      }

      // Parametrage de la requete hector

      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(ccf.getKeyspace(),
                                  UUIDSerializer.get(),
                                  StringSerializer.get(),
                                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(JobRequestDao.JOBREQUEST_CFNAME);
      final int blockSize = 1000;
      int count;

      // Map contenant key = (numero d'iteration = mapKey)
      // value=(liste des UUID des objets de l'iteration)
      final Map<Integer, List<UUID>> map = new HashMap<>();

      // Numero d'itération
      int mapKey = 0;

      do {

        // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
        // toutes les clé jusqu'à la dernière
        // on fixe un nombre maximal de ligne à traiter à chaque itération
        // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

        rangeSlicesQuery.setRange(null, null, false, blockSize);
        rangeSlicesQuery.setKeys(startKey, null);
        rangeSlicesQuery.setRowCount(blockSize);
        final QueryResult<OrderedRows<UUID, String, byte[]>> result = rangeSlicesQuery.execute();

        final OrderedRows<UUID, String, byte[]> orderedRows = result.get();
        count = orderedRows.getCount();

        // Parcours des rows pour déterminer la dernière clé de l'ensemble
        final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = orderedRows.peekLast();
        if (lastRow != null) {
          startKey = lastRow.getKey();
        }

        // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
        // son utilisation
        final QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<>();
        final ColumnFamilyResultWrapper<UUID, String> resultConverter = converter
            .getColumnFamilyResultWrapper(result,
                                          UUIDSerializer.get(),
                                          StringSerializer.get(),
                                          bytesSerializer);
        // On itère sur le résultat
        final HectorIterator<UUID, String> resultIterator = new HectorIterator<>(resultConverter);

        // Liste des ids de l'iteration n-1 (null si au debut)
        final List<UUID> lastlistUUID = map.get(mapKey - 1);

        // Liste des ids de l'iteration courante
        final List<UUID> currentlistUUID = new ArrayList<>();

        for (final ColumnFamilyResult<UUID, String> row : resultIterator) {
          final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(row);
          // On peut obtenir un jobRequest null dans le cas d'un jobRequest effacé
          if (jobRequest != null) {
            final JobRequestCql jobcql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(jobRequest);

            currentlistUUID.add(jobcql.getIdJob());
            // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
            if (lastlistUUID == null || !lastlistUUID.contains(jobcql.getIdJob())) {
              cqldao.saveWithMapper(jobcql);
              totalRow++;
            }

            // ecriture dans le fichier
            bWriter.append(jobcql.getIdJob().toString());
            bWriter.newLine();
          }
        }

        // remettre à jour la map
        map.put(mapKey, currentlistUUID);
        map.remove(mapKey -1);
        mapKey++;

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
    cqldao.count();
    while (it.hasNext()) {
      final JobRequestCql jobRequest = it.next();
      listRToCql.add(jobRequest);
    }

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
    final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(ccf.getKeyspace(),
                                UUIDSerializer.get(),
                                StringSerializer.get(),
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(JobRequestDao.JOBREQUEST_CFNAME);
    final int blockSize = 1000;
    UUID startKey = null;
    int count;

    // Map contenant key = (numero d'iteration)
    // value=(liste des UUID des objets de l'iteration)
    final Map<Integer, List<UUID>> map = new HashMap<>();

    // Numero d'itération
    int mapKey = 0;

    do {

      // on fixe la clé de depart et la clé de fin. Dans notre cas il n'y a pas de clé de fin car on veut parcourir
      // toutes les clé jusqu'à la dernière
      // on fixe un nombre maximal de ligne à traiter à chaque itération
      // si le nombre de resultat < blockSize on sort de la boucle ==> indique la fin des colonnes

      rangeSlicesQuery.setRange(null, null, false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      // rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<UUID, String, byte[]>> result = rangeSlicesQuery.execute();

      final OrderedRows<UUID, String, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();

      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = orderedRows.peekLast();
      if (lastRow != null) {
        startKey = lastRow.getKey();
      }

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      final QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<UUID, String> resultConverter = converter
          .getColumnFamilyResultWrapper(result,
                                        UUIDSerializer.get(),
                                        StringSerializer.get(),
                                        bytesSerializer);
      // On itère sur le résultat
      final HectorIterator<UUID, String> resultIterator = new HectorIterator<>(resultConverter);

      // Liste des ids de l'iteration n-1 (null si au debut)
      final List<UUID> lastlistUUID = map.get(mapKey - 1);

      // Liste des ids de l'iteration courante
      final List<UUID> currentlistUUID = new ArrayList<>();

      for (final ColumnFamilyResult<UUID, String> row : resultIterator) {
        final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(row);
        // On peut obtenir un jobRequest null dans le cas d'un jobRequest effacé
        if (jobRequest != null) {
          final JobRequestCql jobcql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(jobRequest);

          currentlistUUID.add(jobcql.getIdJob());
          // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
          if (lastlistUUID == null || !lastlistUUID.contains(jobcql.getIdJob())) {
            listJobThrift.add(jobcql);
          }
        }
      }

      // remettre à jour la map
      map.put(mapKey, currentlistUUID);
      map.remove(mapKey - 1);
      mapKey++;
    } while (count == blockSize);

    return listJobThrift;
  }

}
