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

import fr.urssaf.image.sae.commons.CompareTraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteIndexDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitation;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegExploitationCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegExploitationIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegSecuriteCqlSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.utils.RepriseFileUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * (AC75095028) Classe de migration de TraceRegSecurite
 */
@Component
public class MigrationTraceRegSecurite extends MigrationTrace {

  private static final String TRACE_REG_SECURITE_TXT = "TraceRegSecurite.txt";

  private static final String TRACE_REG_SECURITE_INDEX_TXT = "TraceRegSecuriteIndex.txt";

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTraceRegSecurite.class);

  @Autowired
  TraceRegSecuriteIndexDao thriftdao;

  @Autowired
  TraceRegSecuriteCqlSupport supportcql;

  @Autowired
  TraceRegSecuriteSupport supportThrift;

  @Autowired
  private CompareTraceRegSecurite compRegSecu;


  /**
   * Migration de la table thrift vers cql
   * 
   * @return
   * @throws Exception
   */
  public int migrationFromThriftToCql() throws Exception {

    LOGGER.debug(" MigrationTraceRegSecurite migrationFromThriftToCql Start");

    // Clé de depart de l'itération
    UUID startKey = null;
    int totalCount = 0;

    // Gestion du ficher d'enregistrement des clés en cas de reprise
    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {

      file = RepriseFileUtils.getKeysFile(RepriseFileUtils.getKeyFileDir(), TRACE_REG_SECURITE_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String strKey = RepriseFileUtils.getLastLine(file);
      if (strKey != null && !strKey.isEmpty()) {
        startKey = UUID.fromString(strKey);
      }

      // Parametrage de la requete hector

      final StringSerializer stringSerializer = StringSerializer.get();
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      final UUIDSerializer uSl = UUIDSerializer.get();

      final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(compRegSecu.getKeySpace(),
                                  uSl,
                                  stringSerializer,
                                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(compRegSecu.getTraceClasseName());
      final int blockSize = 10;
      int count;

      // Map contenant key = (numero d'iteration) value=(liste des cles (UUID) des objets de l'iteration)
      final Map<Integer, List<UUID>> lastIteartionMap = new HashMap<>();

      // Numero d'itération
      int iterationNB = 0;

      // Pour chaque tranche de blockSize, on recherche l'objet cql
      do {
        rangeSlicesQuery.setRange("", "", false, blockSize);
        rangeSlicesQuery.setKeys(startKey, null);
        rangeSlicesQuery.setRowCount(blockSize);
        final QueryResult<OrderedRows<UUID, String, byte[]>> result = rangeSlicesQuery
            .execute();

        final OrderedRows<UUID, String, byte[]> orderedRows = result.get();
        count = orderedRows.getCount();
        // Parcours des rows pour déterminer la dernière clé de l'ensemble
        final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = orderedRows.peekLast();

        if (lastRow == null) {
          LOGGER.error("La clé de depart (startKey) dans la requete hector n'a pas été trouvé dans la table thrift");
          break;
        }
        startKey = lastRow.getKey();

        // Liste des ids de l'iteration n-1 (null si au debut)
        final List<UUID> lastlistUUID = lastIteartionMap.get(iterationNB - 1);

        // Liste des ids de l'iteration courante
        final List<UUID> currentlistUUID = new ArrayList<>();

        for (final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> row : orderedRows) {

          // on recupère la trace thrifh
          final TraceRegSecurite trThrift = compRegSecu.getTraceFromResult(row);
          // On le transforme en cql
          final TraceRegSecuriteCql trThToCql = compRegSecu.createTraceFromObjectThrift(trThrift);

          final UUID key = row.getKey();

          currentlistUUID.add(key);
          // enregistrement ==> la condition empeche d'enregistrer la lastKey deux fois
          if (lastlistUUID == null || !lastlistUUID.contains(key)) {
            supportcql.save(trThToCql, new Date().getTime());
            totalCount++;
          }

          // ecriture dans le fichier
          bWriter.append(key.toString());
          bWriter.newLine();

        }

        // remettre à jour la map
        lastIteartionMap.put(iterationNB, currentlistUUID);
        lastIteartionMap.remove(iterationNB - 1);
        iterationNB++;

      } while (count == blockSize);

    }
    catch (final IOException e) {
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

    LOGGER.debug(" Totale : " + totalCount);
    LOGGER.debug(" MigrationTraceRegSecurite migrationFromThriftToCql end");

    return totalCount;
  }

  /**
   * @return
   */
  public int migrationFromCqlToThrift() {

    LOGGER.info(" MigrationTraceRegSecurite migrationFromCqlToThrift ---------- DEBUT");

    final Iterator<TraceRegSecuriteCql> tracej = supportcql.findAll();

    int nbRow = 0;
    while (tracej.hasNext()) {
      final TraceRegSecuriteCql next = tracej.next();
      final TraceRegSecurite traceTrhift = createTraceThriftFromCqlTrace(next);
      final Date date = next.getTimestamp();
      final Long times = date != null ? date.getTime() : 0;
      supportThrift.create(traceTrhift, times);
      nbRow++;
    }

    LOGGER.info(" MigrationTraceRegSecurite migrationFromCqlToThrift Total nbRow: ---------- " + nbRow);
    LOGGER.info(" MigrationTraceRegSecurite migrationFromCqlToThrift ---------- FIN");

    return nbRow;
  }

  // INDEX DE LA TRACE

  /**
   * Migration de la CF index du journal de thritf vers la CF cql
   * 
   * @return
   */
  public int migrationIndexFromThriftToCql() {

    LOGGER.info(" MigrationTraceRegSecurite migrationIndexFromThriftToCql ---------- DEBUT");

    int nbRow = 0;
    Date starDate = DateUtils.addYears(DATE, -5);

    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {
      file = RepriseFileUtils.getKeysFile(RepriseFileUtils.getKeyFileDir(), TRACE_REG_SECURITE_INDEX_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String lastLine = RepriseFileUtils.getLastLine(file);
      if (lastLine != null && !lastLine.isEmpty()) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        starDate = format.parse(lastLine);
      }

      final List<Date> dates = DateRegUtils.getListFromDates(starDate, DateUtils.addYears(DATE, 1));
      for (final Date d : dates) {

        final List<TraceRegSecuriteIndex> list = supportThrift.findByDate(d);

        if (list != null && !list.isEmpty()) {
          for (final TraceRegSecuriteIndex next : list) {

            final TraceRegSecuriteIndexCql trace = createTraceIndexFromThriftToCql(next);
            supportcql.getIndexDao().saveWithMapper(trace);
            nbRow++;
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

    LOGGER.info(" MigrationTraceRegSecurite migrationIndexFromThriftToCql Total nbRow: ---------- " + nbRow);
    LOGGER.info(" MigrationTraceRegSecurite migrationIndexFromThriftToCql ---------- FIN");
    return nbRow;

  }

  /**
   * Migration de la CF INDEX de TraceRegExploitation de cql vers thrift
   */
  public void migrationIndexFromCqlToThrift() {
    LOGGER.info(" MigrationTraceRegSecurite migrationIndexFromCqlToThrift ---------- FIN");
    int nbRow = 0;
    final Iterator<TraceRegSecuriteIndexCql> it = supportcql.findAllIndex();
    while (it.hasNext()) {
      final TraceRegSecuriteIndexCql next = it.next();
      final TraceRegSecuriteIndex index = createTraceIndexFromCqlToThrift(next);

      final String journee = DateRegUtils.getJournee(index.getTimestamp());

      final ColumnFamilyUpdater<String, UUID> indexUpdater = thriftdao.createUpdater(journee);
      thriftdao.writeColumn(indexUpdater,
                            index.getIdentifiant(),
                            index,
                            index.getTimestamp().getTime());
      thriftdao.update(indexUpdater);

      nbRow++;
    }

    LOGGER.info(" MigrationTraceRegSecurite migrationIndexFromCqlToThrift Total nbRow: ---------- " + nbRow);
    LOGGER.info(" MigrationTraceRegSecurite migrationIndexFromCqlToThrift ---------- FIN");
  }

  // TEST DES DONNEES

  /**
   * Comparer les Traces cql et Thrift
   * @throws Exception
   */
  public boolean traceComparator() throws Exception {
    final boolean isBaseOk = compRegSecu.traceComparator();
    return isBaseOk;
  }

  /**
   * Comparer les Traces cql et Thrift
   * @throws Exception
   */
  public boolean indexComparator() throws Exception {
    final boolean isBaseOk = compRegSecu.indexComparator();
    return isBaseOk;

  }
  // Methodes utilitaires

  /**
   * Créer un index {@link TraceJournalEvtIndex} à partir d'une trace {@link TraceJournalEvtIndexCql}
   *
   * @param index
   *           {@link TraceJournalEvtIndexCql}
   * @return un {@link TraceJournalEvtIndex}
   */
  public TraceRegSecuriteIndex createTraceIndexFromCqlToThrift(final TraceRegSecuriteIndexCql index) {
    final TraceRegSecuriteIndex tr = new TraceRegSecuriteIndex();
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
   *           l'index {@link TraceRegExploitationIndexCql}
   * @return l'index {@link TraceRegExploitationIndex}
   */
  public TraceRegSecuriteIndexCql createTraceIndexFromThriftToCql(final TraceRegSecuriteIndex index) {
    final TraceRegSecuriteIndexCql tr = new TraceRegSecuriteIndexCql();

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
   *           la {@link TraceRegExploitationCql}
   * @return la trace {@link TraceRegExploitation}
   */
  public TraceRegSecurite createTraceThriftFromCqlTrace(final TraceRegSecuriteCql traceCql) {
    final TraceRegSecurite tr = new TraceRegSecurite(traceCql.getIdentifiant(), traceCql.getTimestamp());
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
