/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
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

import fr.urssaf.image.sae.commons.CompareIndexDoc;
import fr.urssaf.image.sae.commons.CompareTraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDocDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexDocCqlDao;
import fr.urssaf.image.sae.trace.model.GenericTraceType;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationTraceJournalEvt extends MigrationTrace {

  /**
   * TODO (AC75095028) Description du champ
   */
  private static final String TRACE_JOURNAL_EVT_TXT = "TraceJournalEvt.txt";

  private static final String TRACE_JOURNAL_EVT_INDEX_TXT = "TraceJournalEvtIndex.txt";

  private static final String TRACE_JOURNAL_EVT_INDEX_DOC_TXT = "TraceJournalEvtIndexDoc.txt";

  @Autowired
  TraceJournalEvtIndexDao indexthrift;

  @Autowired
  ITraceJournalEvtIndexDocCqlDao indexDocDaocql;

  @Autowired
  TraceJournalEvtIndexDocDao indexDocDaothrift;

  @Autowired
  private TraceJournalEvtSupport supportJThrift;

  @Autowired
  private TraceJournalEvtCqlSupport supportcql;

  @Autowired
  private CompareTraceJournalEvt compJEvt;

  @Autowired
  private CompareIndexDoc compJEvtDoc;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTraceJournalEvt.class);

  /**
   * Utilisation de cql uniquement
   * Migration de la CF thrift vers la CF cql en utilsant un mapping manuel. L'extration des données est faite
   * à partir du type {@link GenericTraceType} qui permet de wrapper les colonnes
   */
  public int migrationFromThriftToCql() throws Exception {

    // Clé de depart de l'itération
    UUID startKey = null;

    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {
      file = getKeysFile(TRACE_JOURNAL_EVT_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String strKey = getLastLine(file);
      if (strKey != null && !strKey.isEmpty()) {
        startKey = UUID.fromString(strKey);
      }
    }
    catch (final IOException e) {
      // final String message = "Impossible d'écrire le fichier " + TRACE_JOURNAL_EVT_TXT;
      // LOGGER.error(message);
      LOGGER.error(e.toString());
    }

    // Parametrage de la requete hector

    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final UUIDSerializer uSl = UUIDSerializer.get();

    final RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(compJEvt.getKeySpace(),
                                uSl,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(compJEvt.getTraceClasseName());
    final int blockSize = 1000;
    int count;
    int nbRow = 1;

    // Pour chaque tranche de 10000, on recherche l'objet cql
    do {
      rangeSlicesQuery.setRange("", "", false, blockSize);
      rangeSlicesQuery.setKeys(startKey, null);
      rangeSlicesQuery.setRowCount(blockSize);
      final QueryResult<OrderedRows<UUID, String, byte[]>> result = rangeSlicesQuery
          .execute();

      final OrderedRows<UUID, String, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();
      nbRow = nbRow - 1;
      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> lastRow = orderedRows.peekLast();

      if (lastRow == null) {
        LOGGER.error("La clé de depart (startKey) dans la requete hector n'a pas été trouvé dans la table thrift");
        break;
      }
      startKey = lastRow.getKey();

      for (final me.prettyprint.hector.api.beans.Row<UUID, String, byte[]> row : orderedRows) {

        // on recupère la trace thrifh
        final TraceJournalEvt trThrift = compJEvt.getTraceFromResult(row);
        // On le transforme en cql
        final TraceJournalEvtCql trThToCql = compJEvt.createTraceFromObjectThrift(trThrift);

        // sauvegarde
        // tant que count == blockSize on ajout tout sauf le dernier
        // Cela empeche d'ajouter la lastRow deux fois
        if (count == blockSize && nbRow < count) {
          supportcql.save(trThToCql);
          nbRow++;
        } else if (count != blockSize) {
          supportcql.save(trThToCql);
          nbRow++;
        }

        final UUID key = row.getKey();

        // ecriture dans le fichier
        bWriter.append(key.toString());
        bWriter.newLine();
      }

    } while (count == blockSize);

    try {
      bWriter.close();
    }
    catch (final Exception e) {
      LOGGER.warn(e.getMessage(), e);
    }

    LOGGER.debug(" Totale : " + nbRow);
    LOGGER.debug(" migrationFromThriftToCql end");

    return nbRow;
  }

  /**
   * Migration des données de la CF cql vers thrift
   */
  public int migrationFromCqlToThrift() {

    LOGGER.debug(" migrationFromCqlToThrift start");

    final Iterator<TraceJournalEvtCql> tracej = supportcql.findAll();
    // final List<TraceJournalEvtCql> nb_Rows = Lists.newArrayList(tracej);
    int nb = 0;
    while (tracej.hasNext()) {
      final TraceJournalEvtCql nextJ = tracej.next();
      final TraceJournalEvt traceTrhift = UtilsTraceMapper.createTraceJournalEvtFromCqlToThrift(nextJ);
      final Date date = nextJ.getTimestamp();
      final Long times = date != null ? date.getTime() : 0;
      supportJThrift.create(traceTrhift, times);
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationFromCqlToThrift end");

    return nb;
  }

  /**
   * Migration de la CF INDEX du journal de cql vers thrift
   */
  public int migrationIndexFromCqlToThrift() {

    LOGGER.debug(" migrationIndexFromCqlToThrift start");
    int nb = 0;
    final Iterator<TraceJournalEvtIndexCql> it = supportcql.findAllIndex();
    while (it.hasNext()) {
      final TraceJournalEvtIndex index = createTraceIndexFromCqlToThrift(it.next());

      final String journee = DateRegUtils.getJournee(index.getTimestamp());
      final ColumnFamilyUpdater<String, UUID> indexUpdater = indexthrift.createUpdater(journee);
      indexthrift.writeColumn(indexUpdater,
                              index.getIdentifiant(),
                              index,
                              index.getTimestamp().getTime());
      indexthrift.update(indexUpdater);

      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationIndexFromCqlToThrift end");

    return nb;
  }



  /**
   * Migration de la CF index du journal de thritf vers la CF cql
   * 
   * @throws IOException
   */
  public int migIndexFromThriftToCql() throws IOException {

    LOGGER.debug(" migrationIndexFromThriftToCql start");


    // Clé de depart de l'itération
    Date starDate = DateUtils.addYears(DATE, -5);

    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {
      file = getKeysFile(TRACE_JOURNAL_EVT_INDEX_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      final String lastLine = getLastLine(file);
      if (lastLine != null && !lastLine.isEmpty()) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        starDate = format.parse(lastLine);
      }
    }
    catch (final IOException | ParseException e) {
      LOGGER.error(e.toString());
    }


    int nb = 0;

    final List<Date> dates = DateRegUtils.getListFromDates(starDate, DateUtils.addYears(DATE, 0));

    for (final Date d : dates) {


      final List<TraceJournalEvtIndex> list = supportJThrift.findByDate(d);
      List<TraceJournalEvtIndexCql> listTemp = new ArrayList<>();
      if(list != null && !list.isEmpty())  {
        for (final TraceJournalEvtIndex next : list) {
          final TraceJournalEvtIndexCql trace = createTraceIndexFromThriftToCql(next, DateRegUtils.getJournee(d));
          listTemp.add(trace);
          if (listTemp.size() == 10000) {
            nb = nb + listTemp.size();
            supportcql.saveAllIndex(listTemp);
            listTemp = new ArrayList<>();

            // ecriture dans le fichier
            bWriter.append(DateRegUtils.getJournee(d));
            bWriter.newLine();

          }
        }
        if (!listTemp.isEmpty()) {
          nb = nb + listTemp.size();
          supportcql.saveAllIndex(listTemp);
          bWriter.append(DateRegUtils.getJournee(d));
          bWriter.newLine();
          listTemp = new ArrayList<>();
        }
      }
    }

    try {
      bWriter.close();
    }
    catch (final Exception e) {
      LOGGER.warn(e.getMessage(), e);
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationIndexFromThriftToCql end");

    return nb;
  }

  /**
   * Migration de la CF INDEX DOC du journal de thrift vers cql
   *
   * @throws Exception
   */
  public int migrationIndexDocFromThriftToCql() throws Exception {

    LOGGER.debug(" migrationIndexDocFromThriftToCql start");

    // Clé de depart de l'itération
    String startKey = "";

    BufferedWriter bWriter = null;
    File file = null;
    FileWriter fWriter;
    //
    try {
      file = getKeysFile(TRACE_JOURNAL_EVT_INDEX_DOC_TXT);
      fWriter = new FileWriter(file, true);
      bWriter = new BufferedWriter(fWriter);

      startKey = getLastLine(file);
    }
    catch (final IOException e) {
      LOGGER.error(e.toString());
    }

    final StringSerializer stringSerializer = StringSerializer.get();
    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(indexDocDaothrift.getKeyspace(),
                                stringSerializer,
                                stringSerializer,
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily("TraceJournalEvtIndexDoc");
    final int blockSize = 1000;
    int totalKey = 1;
    int count;
    int nbRows = 1;
    do {
      rangeSlicesQuery.setRange("", "", false, 1);
      rangeSlicesQuery.setKeys(startKey, "");
      rangeSlicesQuery.setRowCount(blockSize);
      rangeSlicesQuery.setReturnKeysOnly();
      final QueryResult<OrderedRows<String, String, byte[]>> result = rangeSlicesQuery
          .execute();

      final OrderedRows<String, String, byte[]> orderedRows = result.get();
      count = orderedRows.getCount();
      // On enlève 1, car sinon à chaque itération, la startKey serait
      // comptée deux fois.
      totalKey += count - 1;
      nbRows = nbRows - 1;
      // Parcours des rows pour déterminer la dernière clé de l'ensemble
      final me.prettyprint.hector.api.beans.Row<String, String, byte[]> lastRow = orderedRows.peekLast();
      startKey = lastRow.getKey();

      for (final me.prettyprint.hector.api.beans.Row<String, String, byte[]> row : orderedRows) {

        final List<TraceJournalEvtIndexDoc> list = supportJThrift.findByIdDoc(java.util.UUID.fromString(row.getKey()));
        for (final TraceJournalEvtIndexDoc tr : list) {
          final TraceJournalEvtIndexDocCql indxDocCql = UtilsTraceMapper.createTraceIndexDocFromCqlToThrift(tr, row.getKey());

          // sauvegarde
          // tant que count == blockSize on ajout tout sauf le dernier
          // Cela empeche d'ajouter la lastRow deux fois
          if (count == blockSize && nbRows < count) {
            indexDocDaocql.saveWithMapper(indxDocCql);
            nbRows++;
          } else if (count != blockSize) {
            indexDocDaocql.saveWithMapper(indxDocCql);
            nbRows++;
          }

          // ecriture dans le fichier
          bWriter.append(row.getKey());
          bWriter.newLine();

        }
      }

    } while (count == blockSize);

    try {
      bWriter.close();
    }
    catch (final Exception e) {
      LOGGER.warn(e.getMessage(), e);
    }

    LOGGER.debug(" Nb total de cle dans la CF: " + totalKey);
    LOGGER.debug(" Nb total d'entrées dans la CF : " + nbRows);
    LOGGER.debug(" migrationIndexFromThriftToCql end");

    return totalKey;

  }

  /**
   * Migration de la CF INDEX DOC du journal de cql vers thrift
   */
  public int migrationIndexDocFromCqlToThrift() {

    LOGGER.debug(" migrationIndexDoc_From_Cql_To_Thrift start");

    int nb = 0;
    final Iterator<TraceJournalEvtIndexDocCql> it = indexDocDaocql.findAll();
    while (it.hasNext()) {
      final TraceJournalEvtIndexDocCql indexCql = it.next();
      final TraceJournalEvtIndexDoc index = UtilsTraceMapper.createTraceIndexDocFromCqlToThrift(indexCql);
      final String idDoc = index.getIdentifiant().toString();
      final ColumnFamilyUpdater<String, UUID> updater = indexDocDaothrift.createUpdater(idDoc);
      indexDocDaothrift.writeColumn(updater, index.getIdentifiant(), index, indexCql.getTimestamp().getTime());
      indexDocDaothrift.update(updater);
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" migrationIndexDoc_From_Cql_To_Thrift end");

    return nb;
  }

  /**
   * Créer un {@link TraceJournalEvtIndexCql} à partir d'un {@link TraceJournalEvtIndex}
   *
   * @param index
   *          l'index {@link TraceJournalEvtIndexCql}
   * @return l'index {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndexCql createTraceIndexFromThriftToCql(final TraceJournalEvtIndex index, final String key) {
    return UtilsTraceMapper.createJournalIndexFromThriftToCql(index, key);
  }

  /**
   * Créer un index {@link TraceJournalEvtIndex} à partir d'une trace {@link TraceJournalEvtIndexCql}
   *
   * @param index
   *          {@link TraceJournalEvtIndexCql}
   * @return un {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndex createTraceIndexFromCqlToThrift(final TraceJournalEvtIndexCql index) {
    return UtilsTraceMapper.createTraceJournalIndexFromCqlToThrift(index); 
  }

  /**
   * Comparer les Traces cql et Thrift
   * @throws Exception
   */
  public boolean traceComparator() throws Exception {
    final boolean isBaseOk = compJEvt.traceComparator();
    return isBaseOk;
  }

  /**
   * Comparer les Traces cql et Thrift
   * @throws Exception
   */
  public boolean indexComparator() throws Exception {
    final boolean isBaseOk = compJEvt.indexComparator();
    return isBaseOk;

  }
  /**
   * Comparer les TracesIndex cql et Thrift
   * @throws Exception
   */
  public boolean indexDocComparator() throws Exception {

    // recuperer un iterateur sur la table cql
    // Parcourir les elements de l'iterateur et pour chaque element 
    // recuperer un ensemble de X elements dans la table thrift
    // chercher l'element cql dans les X elements
    // si trouvé, on passe à l'element suivant cql
    // sinon on recupère les X element suivant dans la table thrift puis on fait une nouvelle recherche
    // si on en recupère  moins de X et qu'on ne trouve pas l'element cql alors == > echec de comparaison
    //compJEvt.checkIndexCql(null, TraceJournalEvtIndexDoc.class.getSimpleName());

    final Iterator<TraceJournalEvtIndexDocCql> itJournal = indexDocDaocql.findAllWithMapper();
    boolean isEqBase = true;
    boolean isInMap = false;

    final Map<String, Map<UUID, TraceJournalEvtIndexDocCql>> mapRow = new HashMap<>();

    while (itJournal.hasNext()) {
      final TraceJournalEvtIndexDocCql tr = itJournal.next();	

      // Pour chaque trace cql On cherche dans la map
      // La map sera construite au fur et à mesure
      if(!mapRow.isEmpty()) {
        isInMap = false;
        for (final Map.Entry<String, Map<UUID, TraceJournalEvtIndexDocCql>> entry: mapRow.entrySet()) {
          final String key = entry.getKey();			   
          final Map<UUID, TraceJournalEvtIndexDocCql> ssRow = entry.getValue();
          final UUID id = tr.getIdentifiant();
          final TraceJournalEvtIndexDocCql trThToCql = ssRow.get(id);
          if(tr.equals(trThToCql)) {
            isInMap = true;
            ssRow.remove(id);
            if(ssRow.isEmpty()) {
              mapRow.remove(key);
            }
            break;
          }  

        }
      }

      // on verifie que l'objet courant est dans la base cql, si on ne le trouve dans la map
      if(!isInMap) {
        final boolean isObj = compJEvtDoc.checkIndexDocCql(tr, mapRow);
        if(!isObj) {
          LOGGER.info(" Objet cql n'a pas été trouvé dans la base thrift");
        } else {
          isEqBase = isEqBase && isObj;
        }
      }

    } 
    if(isEqBase) {
      LOGGER.info(" MigrationTraceDestinataire - OK");
    }
    return isEqBase;
  }

  // METHODE POUR LE REPRISE

  private File getKeysFile(final String fileName) throws IOException {
    final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    final File dir = new File(classLoader.getResource(migTempFiles).getFile());
    final Path dirPath = dir.toPath();
    final File file = new File(dirPath.toString() + "/" + fileName);

    if (!file.exists()) {
      LOGGER.warn("Création du fichier {}", fileName);
      file.createNewFile();
    }
    return file;
  }

  /**
   * Recuperer la dernière ligne du fichier
   * https://stackoverflow.com/questions/686231/quickly-read-the-last-line-of-a-text-file
   * 
   * @param file
   * @return
   */
  private String getLastLine(final File file) {

    RandomAccessFile fileHandler = null;
    try {
      fileHandler = new RandomAccessFile(file, "r");
      final long fileLength = fileHandler.length() - 1;
      final StringBuilder sb = new StringBuilder();

      for (long filePointer = fileLength; filePointer != -1; filePointer--) {
        fileHandler.seek(filePointer);
        final int readByte = fileHandler.readByte();

        if (readByte == 0xA) {
          if (filePointer == fileLength) {
            continue;
          }
          break;

        } else if (readByte == 0xD) {
          if (filePointer == fileLength - 1) {
            continue;
          }
          break;
        }

        sb.append((char) readByte);
      }

      final String lastLine = sb.reverse().toString();
      return lastLine;
    }
    catch (final IOException e) {
      e.printStackTrace();
      return null;
    }
    finally {
      if (fileHandler != null) {
        try {
          fileHandler.close();
        }
        catch (final IOException e) {
          /* ignore */
        }
      }
    }

  }

}
