/**
 *   (AC75095028) 
 */
package fr.urssaf.image.sae.trace;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.CompareTraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegExploitationCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegExploitationIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.utils.RepriseFileUtils;
import fr.urssaf.image.sae.utils.RowUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Classe de migration de TraceRegTechnique
 */
@Component
public class MigrationTraceRegTechnique extends MigrationTrace {

  private static final String TRACE_REG_TECHNIQUE_TXT = "TraceRegTechnique.txt";

  private static final String TRACE_REG_TECHNIQUE_INDEX_TXT = "TraceRegTechniqueIndex.txt";

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTraceRegTechnique.class);

  @Autowired
  TraceRegTechniqueIndexDao thriftdao;

  @Autowired
  TraceRegTechniqueCqlSupport supportcql;

  @Autowired
  TraceRegTechniqueSupport supportThrift;

  @Autowired
  private CompareTraceRegTechnique compRegTec;

  /**
   * Migration de la table thrift vers cql
   * 
   * @return
   * @throws Exception
   */
  public int migrationFromThriftToCql() throws Exception {

    LOGGER.info(" MigrationTraceRegTechnique-migrationFromThriftToCql Start");

    // Clé de depart de l'itération
    UUID startKey = null;
    int totalCount = 0;

    // Gestion du ficher d'enregistrement des clés en cas de reprise
    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    UUID startkeyError = null;

    try {
      //LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -1", totalCount);
      file = RepriseFileUtils.getKeysFile(RepriseFileUtils.getKeyFileDir(), TRACE_REG_TECHNIQUE_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String strKey = RepriseFileUtils.getLastLine(file);
      if (strKey != null && !strKey.isEmpty()) {
        startKey = UUID.fromString(strKey);
        startkeyError = startKey;
      }
      // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -1", totalCount);
      // Parametrage de la requete hector

      final StringSerializer stringSerializer = StringSerializer.get();
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final UUIDSerializer uSl = UUIDSerializer.get();
      // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -2", totalCount);
      final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(compRegTec.getKeySpace(),
                                  uSl,
                                  stringSerializer,
                                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(compRegTec.getTraceClasseName());
      // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -3", totalCount);
      final int blockSize = RowUtils.BLOCK_SIZE_TRACE_REG_TECHNIQUE;
      int count;

      // Map contenant key = (numero d'iteration) value=(liste des cles (UUID) des objets de l'iteration)
      final Map<Integer, List<UUID>> lastIteartionMap = new HashMap<>();

      // Numero d'itération
      int iterationNB = 0;

      // Pour chaque tranche de blockSize, on recherche l'objet cql
      do {
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -4", totalCount);
        rangeSlicesQuery.setRange("", "", false, blockSize);
        rangeSlicesQuery.setKeys(startKey, null);
        rangeSlicesQuery.setRowCount(blockSize);
        final QueryResult<OrderedRows<UUID, String, byte[]>> result = rangeSlicesQuery
            .execute();
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -5", totalCount);
        final OrderedRows<UUID, String, byte[]> orderedRows = result.get();
        count = orderedRows.getCount();
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -6", totalCount);
        // Parcours des rows pour déterminer la dernière clé de l'ensemble
        final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = orderedRows.peekLast();
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -7", totalCount);
        if (lastRow == null) {
          LOGGER.error("MigrationTraceRegTechnique-migrationFromThriftToCql-La clé de depart (startKey) dans la requete hector n'a pas été trouvé dans la table thrift");
          break;
        }
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -8", totalCount);
        startKey = lastRow.getKey();

        // Liste des ids de l'iteration n-1 (null si au debut)
        final List<UUID> lastlistUUID = lastIteartionMap.get(iterationNB - 1);
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -9", totalCount);
        // Liste des ids de l'iteration courante
        final List<UUID> currentlistUUID = new ArrayList<>();

        for (final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> row : orderedRows) {
          if (RowUtils.rowUsbHasColumns(row)) {
            // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -10", totalCount);
            // on recupère la trace thrifh
            final TraceRegTechnique trThrift = compRegTec.getTraceFromResult(row);
            // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -11", totalCount);
            // On le transforme en cql
            final TraceRegTechniqueCql trThToCql = compRegTec.createTraceFromObjectThrift(trThrift);
            final UUID key = row.getKey();

            currentlistUUID.add(key);
            // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
            if (lastlistUUID == null || !lastlistUUID.contains(key)) {

              /*
               * LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-Key:{} Contexte:{} Infos:{} StackTrace:{}",
               * key,
               * trThToCql.getContexte() != null ? trThToCql.getContexte().length() : 0,
               * trThToCql.getInfos() != null ? trThToCql.getInfos().size() : 0,
               * trThToCql.getStacktrace() != null ? trThToCql.getStacktrace().length() : 0);
               */


              if ((trThToCql.getContexte() == null || trThToCql.getContexte().length() < RowUtils.MAX_SIZE_COLUMN)
                  && (trThToCql.getInfos() == null || trThToCql.getInfos().size() < RowUtils.MAX_SIZE_COLUMN)
                  && (trThToCql.getStacktrace() == null || trThToCql.getStacktrace().length() < RowUtils.MAX_SIZE_COLUMN)) {
                // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -14", totalCount);
                supportcql.save(trThToCql);
                // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -15", totalCount);
                totalCount++;
                if (totalCount % 1000 == 0) {
                  LOGGER.info(" Nb rows : {}", totalCount);
                }
                // ecriture dans le fichier
                bWriter.append(key.toString());
                bWriter.newLine();
              } else {
                // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -15", totalCount);
                if (trThToCql.getContexte() != null) {
                  LOGGER.warn("Contexte trop gros/Id: {} Contexte:{} Infos:{} StackTrace:{}",
                              trThToCql.getIdentifiant(),
                              trThToCql.getContexte() != null ? trThToCql.getContexte().length() : 0,
                                  trThToCql.getInfos() != null ? trThToCql.getInfos().size() : 0,
                                      trThToCql.getInfos() != null ? trThToCql.getStacktrace().length() : 0);
                }
              }
            }
          }
        }
        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql-nb={} -16", totalCount);
        // remettre à jour la map
        lastIteartionMap.put(iterationNB, currentlistUUID);
        lastIteartionMap.remove(iterationNB - 1);
        iterationNB++;

        // LOGGER.info("MigrationTraceRegTechnique-migrationFromThriftToCql- nb={}-17", totalCount);
      } while (count == blockSize);
    }
    catch (final IOException e) {
      LOGGER.error("Error with startKey={}", startkeyError);
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
    LOGGER.info(" MigrationTraceRegTechnique migrationFromThriftToCql end");
    LOGGER.info(" MigrationTraceRegTechnique migrationFromThriftToCql-Total : " + totalCount);

    return totalCount;
  }

  /**
   * @return
   */
  public int migrationFromCqlToThrift() {

    LOGGER.info(" migrationFromCqlToThrift ---------- FIN");

    final Iterator<TraceRegTechniqueCql> tracej = supportcql.findAll();
    int nbRow = 0;
    while (tracej.hasNext()) {
      final TraceRegTechniqueCql cql = tracej.next();
      final TraceRegTechnique traceTrhift = createTraceThriftFromCqlTrace(cql);
      final Date date = cql.getTimestamp();
      final Long times = date != null ? date.getTime() : 0;
      supportThrift.create(traceTrhift, times);
      nbRow++;
    }

    LOGGER.info(" migrationFromThriftToCql Total nbRow: ---------- " + nbRow);
    LOGGER.info(" migrationFromCqlToThrift ---------- FIN");

    return nbRow;
  }

  // INDEX DE LA TRACE

  /**
   * Migration de la CF index du journal de thritf vers la CF cql
   */
  public void migrationIndexFromThriftToCql() {

    LOGGER.info(" migrationIndexFromThriftToCql ---------- DEBUT");
    int nbRow = 0;

    Date starDate = DateUtils.addYears(DATE, -5);

    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {
      file = RepriseFileUtils.getKeysFile(RepriseFileUtils.getKeyFileDir(), TRACE_REG_TECHNIQUE_INDEX_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String lastLine = RepriseFileUtils.getLastLine(file);
      if (lastLine != null && !lastLine.isEmpty()) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        starDate = format.parse(lastLine);
      }

      final List<Date> dates = DateRegUtils.getListFromDates(starDate, DateUtils.addYears(DATE, 0));
      for (final Date d : dates) {
        final List<TraceRegTechniqueIndex> list = supportThrift.findByDate(d);
        if (list != null && !list.isEmpty()) {
          for (final TraceRegTechniqueIndex nextReg : list) {
            final TraceRegTechniqueIndexCql trace = createTraceIndexFromThriftToCql(nextReg);
            supportcql.getIndexDao().saveWithMapper(trace);
            nbRow++;
            if (nbRow % 1000 == 0) {
              LOGGER.info(" Nb rows : {}", nbRow);
            }
          }
          // ecriture dans le fichier
          bWriter.append(DateRegUtils.getJournee(d));
          bWriter.newLine();
        }
      }
    }
    catch (final IOException | ParseException e) {
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

    LOGGER.info(" migrationIndexFromThriftToCql Total nbRow: ---------- " + nbRow);
    LOGGER.info(" migrationIndexFromThriftToCql ---------- FIN");

  }

  /**
   * Migration de la CF INDEX de TraceRegExploitation de cql vers thrift
   */
  public void migrationIndexFromCqlToThrift() {

    LOGGER.info(" migrationIndexFromThriftToCql ---------- FIN");

    int nbRow = 0;
    final Iterator<TraceRegTechniqueIndexCql> it = supportcql.findAllIndex();
    while (it.hasNext()) {
      final TraceRegTechniqueIndex index = createTraceIndexFromCqlToThrift(it.next());

      final String journee = DateRegUtils.getJournee(index.getTimestamp());

      final ColumnFamilyUpdater<String, UUID> indexUpdater = thriftdao.createUpdater(journee);
      thriftdao.writeColumn(indexUpdater,
                            index.getIdentifiant(),
                            index,
                            index.getTimestamp().getTime());
      thriftdao.update(indexUpdater);

      nbRow++;
    }

    LOGGER.info(" migrationIndexFromThriftToCql Total nbRow: ---------- " + nbRow);
    LOGGER.info(" migrationIndexFromThriftToCql ---------- FIN");
  }

  // TESt DONNEES
  /**
   * Comparer les Traces cql et Thrift
   * 
   * @throws Exception
   */
  public boolean traceComparator() throws Exception {
    final boolean isBaseOk = compRegTec.traceComparator();
    return isBaseOk;
  }

  /**
   * Comparer les Traces cql et Thrift
   * 
   * @throws Exception
   */
  public boolean indexComparator() throws Exception {
    final boolean isBaseOk = compRegTec.indexComparator();
    return isBaseOk;

  }

  // Methodes utilitaires

  /**
   * Créer un index {@link TraceJournalEvtIndex} à partir d'une trace {@link TraceJournalEvtIndexCql}
   *
   * @param index
   *          {@link TraceJournalEvtIndexCql}
   * @return un {@link TraceJournalEvtIndex}
   */
  public TraceRegTechniqueIndex createTraceIndexFromCqlToThrift(final TraceRegTechniqueIndexCql index) {
    final TraceRegTechniqueIndex tr = new TraceRegTechniqueIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());

    tr.setContexte(index.getContexte());
    tr.setContrat(index.getContrat());
    return tr;
  }

  /**
   * Créer un {@link TraceRegExploitationIndexCql} à partir d'un {@link TraceRegExploitationIndex}
   *
   * @param index
   *          l'index {@link TraceRegExploitationIndexCql}
   * @return l'index {@link TraceRegExploitationIndex}
   */
  public TraceRegTechniqueIndexCql createTraceIndexFromThriftToCql(final TraceRegTechniqueIndex index) {
    final TraceRegTechniqueIndexCql tr = new TraceRegTechniqueIndexCql();

    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    tr.setIdentifiantIndex(journee);
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setContrat(index.getContrat());
    tr.setContexte(index.getContexte());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());

    return tr;
  }

  /**
   * Créér une {@link TraceRegExploitation} à partir d'une trace {@link TraceRegExploitationCql}
   *
   * @param traceCql
   *          la {@link TraceRegExploitationCql}
   * @return la trace {@link TraceRegExploitation}
   */
  public TraceRegTechnique createTraceThriftFromCqlTrace(final TraceRegTechniqueCql traceCql) {
    final TraceRegTechnique tr = new TraceRegTechnique(traceCql.getIdentifiant(), traceCql.getTimestamp());
    tr.setCodeEvt(traceCql.getCodeEvt());
    tr.setContexte(traceCql.getContexte());
    tr.setContratService(traceCql.getContratService());
    tr.setLogin(traceCql.getLogin());
    tr.setPagms(traceCql.getPagms());
    final Map<String, Object> infos = new HashMap<>();
    for (final Map.Entry<String, String> entry : traceCql.getInfos().entrySet()) {
      infos.put(entry.getKey(), entry.getValue());
    }
    tr.setInfos(infos);

    return tr;
  }

}
