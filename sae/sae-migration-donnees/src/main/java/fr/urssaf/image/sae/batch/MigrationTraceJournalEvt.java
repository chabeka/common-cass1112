/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.sae.trace.commons.TraceFieldsName;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.model.GenericType;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationTraceJournalEvt extends MigrationTrace {

  @Autowired
  TraceJournalEvtIndexDao indexthrift;

  @Autowired
  private TraceJournalEvtSupport supportJThrift;

  @Autowired
  private TraceJournalEvtCqlSupport supportcql;

  /**
   * Utilisation de cql uniquement
   * Migration de CF thrift vers la CF cql en utilsant un mapping manuel. L'extration des données est faite
   * à partir du type {@link GenericType} qui permet de wrapper les colonnes
   */
  public int migrationFromThriftToCql() {

    final Iterator<GenericType> listT = genericdao.iterablefindAll("TraceJournalEvt", keyspace);

    UUID lastKey = null;
    if (listT.hasNext()) {
      final Row row = (Row) listT.next();
      lastKey = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
    }
    Date timestamp = null;
    String codeEvt = null;
    String contrat = null;
    String login = null;
    List<String> pagms = null;
    Map<String, String> infos = new HashMap<>();
    String contexte = null;
    TraceJournalEvtCql tracejevt;
    int nb = 0;

    List<TraceJournalEvtCql> listToSave = new ArrayList<>();
    while (listT.hasNext()) {

      // Extraction de la clé

      final Row row = (Row) listT.next();
      final UUID key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));

      // compare avec la derniere clé qui a été extraite
      // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
      // alors on enrgistre celui qui vient d'être traité
      if (key != null && !key.equals(lastKey)) {

        tracejevt = new TraceJournalEvtCql(lastKey, timestamp);
        tracejevt.setCodeEvt(codeEvt);
        tracejevt.setContexte(contexte);
        tracejevt.setContratService(contrat);
        tracejevt.setInfos(infos);
        tracejevt.setLogin(login);
        tracejevt.setPagms(pagms);
        listToSave.add(tracejevt);
        lastKey = key;
        // réinitialisation
        timestamp = null;
        codeEvt = null;
        contrat = null;
        login = null;
        pagms = null;
        infos = new HashMap<>();
        contexte = null;

        if (listToSave.size() == 10000) {
          nb = nb + listToSave.size();

          // supportcql.saveAllTraces(listToSave);
          listToSave = new ArrayList<>();
          System.out.println(" Temp i : " + nb);
        }

      }

      // extraction du nom de la colonne
      final String columnName = row.getString("column1");

      // extraction de la value en fonction du nom de la colonne
      if (TraceFieldsName.COL_TIMESTAMP.equals(columnName)) {

        timestamp = DateSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_CODE_EVT.getName().equals(columnName)) {

        codeEvt = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_CONTRAT_SERVICE.getName().equals(columnName)) {

        contrat = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_LOGIN.getName().equals(columnName)) {

        login = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_PAGMS.getName().equals(columnName)) {

        pagms = ListSerializer.get().fromByteBuffer(row.getBytes("value"));
      } else if (TraceFieldsName.COL_INFOS.getName().equals(columnName)) {
        final Map<String, Object> map = MapSerializer.get().fromByteBuffer(row.getBytes("value"));
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
          final String infosKey = entry.getKey();
          final String value = entry.getValue() != null ? entry.getValue().toString() : "";
          infos.put(infosKey, value);
        }
      } else if (TraceFieldsName.COL_CONTEXTE.getName().equals(columnName)) {

        contexte = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
      }

    }
    if (listToSave.size() > 0) {
      // Ajouter le dernier cas traité
      tracejevt = new TraceJournalEvtCql(lastKey, timestamp);
      tracejevt.setCodeEvt(codeEvt);
      tracejevt.setContexte(contexte);
      tracejevt.setContratService(contrat);
      tracejevt.setInfos(infos);
      tracejevt.setLogin(login);
      tracejevt.setPagms(pagms);
      listToSave.add(tracejevt);

      nb = nb + listToSave.size();
      // supportcql.saveAllTraces(listToSave);
      listToSave = new ArrayList<>();
    }
    System.out.println(" Totale : " + nb);
    return nb;
  }

  /**
   * Migration des données de la CF cql vers thrift
   */
  public int migrationFromCqlToThrift() {
    final Iterator<TraceJournalEvtCql> tracej = supportcql.findAll();
    int nb = 0;
    while (tracej.hasNext()) {
      final TraceJournalEvt traceTrhift = createTraceThriftFromCqlTrace(tracej.next());
      final Date date = tracej.next().getTimestamp();
      final Long times = date != null ? date.getTime() : 0;
      supportJThrift.create(traceTrhift, times);
      nb++;
    }
    return nb;
  }

  /**
   * Migration de la CF INDEX du journal de cql vers thrift
   */
  public int migrationIndexFromCqlToThrift() {

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
    System.out.println(" Totale : " + nb);
    return nb;
  }

  /**
   * Migration de la CF index du journal de thritf vers la CF cql
   */
  public int migrationIndexFromThriftToCql() {

    int nb = 0;
    final List<Date> dates = DateRegUtils.getListFromDates(DateUtils.addYears(DATE, -18), DateUtils.addYears(DATE, 1));
    for (final Date d : dates) {
      final Iterator<TraceJournalEvtIndex> it = supportJThrift.findByDateIterator(d);
      List<TraceJournalEvtIndexCql> listTemp = new ArrayList<>();
      while (it.hasNext()) {
        final TraceJournalEvtIndexCql trace = createTraceIndexFromThriftToCql(it.next());
        listTemp.add(trace);
        if (listTemp.size() == 10000) {
          nb = nb + listTemp.size();
          // supportcql.saveAllIndex(listTemp);
          listTemp = new ArrayList<>();
          System.out.println(" Temp i : " + nb);
        }
      }
      if (!listTemp.isEmpty()) {
        nb = nb + listTemp.size();
        // supportcql.saveAllIndex(listTemp);
        listTemp = new ArrayList<>();
      }
    }
    System.out.println(" Totale : " + nb);
    return nb;
  }

  // classe utilitaire

  /**
   * Créér une {@link TraceJournalEvt} à partir d'une trace {@link TraceJournalEvtCql}
   *
   * @param traceCql
   *          la {@link TraceJournalEvtCql}
   * @return la trace {@link TraceJournalEvt}
   */
  public TraceJournalEvt createTraceThriftFromCqlTrace(final TraceJournalEvtCql traceCql) {
    final TraceJournalEvt tr = new TraceJournalEvt(traceCql.getIdentifiant(), traceCql.getTimestamp());
    tr.setCodeEvt(traceCql.getCodeEvt());
    tr.setContratService(traceCql.getContratService());
    tr.setLogin(traceCql.getLogin());
    tr.setPagms(traceCql.getPagms());
    final Map<String, Object> infos = new HashMap<>();
    for (final Map.Entry<String, String> entry : traceCql.getInfos().entrySet()) {
      infos.put(entry.getKey(), entry.getValue());
    }
    tr.setInfos(infos);
    tr.setContexte(traceCql.getContexte());

    return tr;
  }

  /**
   * Créer un {@link TraceJournalEvtIndexCql} à partir d'un {@link TraceJournalEvtIndex}
   *
   * @param index
   *          l'index {@link TraceJournalEvtIndexCql}
   * @return l'index {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndexCql createTraceIndexFromThriftToCql(final TraceJournalEvtIndex index) {
    final TraceJournalEvtIndexCql tr = new TraceJournalEvtIndexCql();
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    tr.setIdentifiantIndex(journee);
    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());

    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());

    return tr;
  }

  /**
   * Créer un index {@link TraceJournalEvtIndex} à partir d'une trace {@link TraceJournalEvtIndexCql}
   *
   * @param index
   *          {@link TraceJournalEvtIndexCql}
   * @return un {@link TraceJournalEvtIndex}
   */
  public TraceJournalEvtIndex createTraceIndexFromCqlToThrift(final TraceJournalEvtIndexCql index) {
    final TraceJournalEvtIndex tr = new TraceJournalEvtIndex();
    tr.setIdentifiant(index.getIdentifiant());
    tr.setCodeEvt(index.getCodeEvt());
    tr.setLogin(index.getLogin());
    tr.setPagms(index.getPagms());
    tr.setTimestamp(index.getTimestamp());

    tr.setContexte(index.getContexte());
    tr.setContratService(index.getContratService());
    return tr;
  }

}
