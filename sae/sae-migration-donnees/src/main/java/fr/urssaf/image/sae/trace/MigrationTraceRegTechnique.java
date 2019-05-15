/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

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
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.support.TraceRegTechniqueSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceRegTechniqueCqlSupport;
import fr.urssaf.image.sae.trace.model.GenericTraceType;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationTraceRegTechnique extends MigrationTrace {

   @Autowired
   TraceRegTechniqueIndexDao thriftdao;

   @Autowired
   TraceRegTechniqueCqlSupport supportcql;

   @Autowired
   TraceRegTechniqueSupport supportThrift;

   /**
    * Utilisation de cql uniquement
    * Migration de CF thrift vers la CF cql en utilsant un mapping manuel. L'extration des données est faite
    * à partir du type {@link GenericTraceType} qui permet de wrapper les colonnes
    */
   public int migrationFromThriftToCql() {

      final Iterator<GenericTraceType> listT = genericdao.findAllByCFName("TraceRegTechnique", keyspace_tu);

      UUID lastKey = null;
      
      Date timestamp = null;
      String codeEvt = null;
      String contrat = null;
      String login = null;
      List<String> pagms = null;
      Map<String, String> infos = new HashMap<>();
      String context = null;
      String stacktrace = null;
      int nb = 0;
      TraceRegTechniqueCql traceregTech;

      List<TraceRegTechniqueCql> listToSave = new ArrayList<>();
      while (listT.hasNext()) {

         // Extraction de la clé

         final Row row = (Row) listT.next();
         final UUID key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
         if(lastKey == null) {
        	 lastKey = key;
         }
         // compare avec la derniere clé qui a été extraite
         // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
         // alors on enrgistre celui qui vient d'être traité
         if (key != null && !key.equals(lastKey)) {

            traceregTech = new TraceRegTechniqueCql(lastKey, timestamp);
            traceregTech.setCodeEvt(codeEvt);
            traceregTech.setContratService(contrat);
            traceregTech.setInfos(infos);
            traceregTech.setLogin(login);
            traceregTech.setPagms(pagms);
            traceregTech.setContexte(context);
            traceregTech.setStacktrace(stacktrace);
            listToSave.add(traceregTech);
            lastKey = key;
            // réinitialisation
            timestamp = null;
            codeEvt = null;
            contrat = null;
            login = null;
            pagms = null;
            infos = new HashMap<>();
            context = null;

            if (listToSave.size() == 10000) {
               nb = nb + listToSave.size();

               supportcql.saveAllTraces(listToSave);
               listToSave = new ArrayList<>();
               System.out.println(" Temp i : " + nb);
            }

         }

         // extraction du nom de la colonne
         final String columnName = row.getString("column1");

         // extraction de la value en fonction du nom de la colonne
         if (TraceFieldsName.COL_TIMESTAMP.getName().equals(columnName)) {

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

            context = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
         } else if (TraceFieldsName.COL_STACKTRACE.getName().equals(columnName)) {
            stacktrace = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
         }

      }
      if (listToSave.size() > 0) {
         // Ajouter le dernier cas traité
         traceregTech = new TraceRegTechniqueCql(lastKey, timestamp);
         traceregTech.setCodeEvt(codeEvt);
         traceregTech.setContratService(contrat);
         traceregTech.setInfos(infos);
         traceregTech.setLogin(login);
         traceregTech.setPagms(pagms);
         traceregTech.setContexte(context);
         traceregTech.setStacktrace(stacktrace);
         listToSave.add(traceregTech);

         nb = nb + listToSave.size();
         supportcql.saveAllTraces(listToSave);
         listToSave = new ArrayList<>();
      }
      // System.out.println(" Total : " + nb);
      return nb;
   }

   /**
    * @return
    */
   public int migrationFromCqlToThrift() {

      final Iterator<TraceRegTechniqueCql> tracej = supportcql.findAll();
      int nb = 0;
      while (tracej.hasNext()) {
         final TraceRegTechniqueCql cql = tracej.next();
         final TraceRegTechnique traceTrhift = createTraceThriftFromCqlTrace(cql);
         final Date date = cql.getTimestamp();
         final Long times = date != null ? date.getTime() : 0;
         supportThrift.create(traceTrhift, times);
         nb++;
      }
      System.out.println(" Total : " + nb);
      return nb;
   }

   // INDEX DE LA TRACE

   /**
    * Migration de la CF index du journal de thritf vers la CF cql
    */
   public void migrationIndexFromThriftToCql() {

      int i = 0;
      final List<Date> dates = DateRegUtils.getListFromDates(DateUtils.addYears(DATE, -18), DateUtils.addYears(DATE, 1));
      for (final Date d : dates) {
         final Iterator<TraceRegTechniqueIndex> it = supportThrift.findByDateIterator(d);
         List<TraceRegTechniqueIndexCql> listTemp = new ArrayList<>();
         while (it.hasNext()) {
            final TraceRegTechniqueIndex nextReg = it.next();
            final TraceRegTechniqueIndexCql trace = createTraceIndexFromThriftToCql(nextReg);
            listTemp.add(trace);
            if (listTemp.size() == 10000) {
               i = i + listTemp.size();
               supportcql.saveAllIndex(listTemp);
               listTemp = new ArrayList<>();
            }
         }
         if (!listTemp.isEmpty()) {
            i = i + listTemp.size();
            supportcql.saveAllIndex(listTemp);
            listTemp = new ArrayList<>();

         }
      }
      System.out.println(" Total : " + i);
   }

   /**
    * Migration de la CF INDEX de TraceRegExploitation de cql vers thrift
    */
   public void migrationIndexFromCqlToThrift() {

      int i = 0;
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

         i++;
      }
      System.out.println(" Total : " + i);
   }

   // Methodes utilitaires

   /**
    * Créer un index {@link TraceJournalEvtIndex} à partir d'une trace {@link TraceJournalEvtIndexCql}
    *
    * @param index
    *           {@link TraceJournalEvtIndexCql}
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
    *           l'index {@link TraceRegExploitationIndexCql}
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
    *           la {@link TraceRegExploitationCql}
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
