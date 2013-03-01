/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.exceptions.HInvalidRequestException;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceJournalEvtDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceJournalEvtIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.TimeUUIDTraceUtils;

/**
 * Support de la classe DAO {@link TraceJournalEvtDao}
 * 
 */
@Component
public class TraceJournalEvtSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceJournalEvtSupport.class);

   private final SimpleDateFormat dateFormat = new SimpleDateFormat(
         "yyyy-MM-dd HH'h'mm ss's' SSS'ms'", Locale.FRENCH);

   @Autowired
   private TraceJournalEvtDao dao;

   @Autowired
   private TraceJournalEvtIndexDao indexDao;

   /**
    * Création d'une trace dans le registre de sécurité
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceJournalEvt trace, long clock) {

      // Trace applicative
      String prefix = "create()";
      LOGGER.debug("{} - Début", prefix);

      // création de la trace
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getJournalEvtTmpl();
      ColumnFamilyUpdater<UUID, String> updater = tmpl.createUpdater(trace
            .getIdentifiant());

      dao.writeColumnCodeEvt(updater, trace.getCodeEvt(), clock);
      dao.writeColumnContext(updater, trace.getContexte(), clock);
      dao.writeColumnTimestamp(updater, trace.getTimestamp(), clock);

      if (StringUtils.isNotBlank(trace.getContratService())) {
         dao.writeColumnContratService(updater, trace.getContratService(),
               clock);
      }

      if (CollectionUtils.isNotEmpty(trace.getPagms())) {
         dao.writeColumnPagms(updater, trace.getPagms(), clock);
      }

      if (StringUtils.isNotBlank(trace.getLogin())) {
         dao.writeColumnLogin(updater, trace.getLogin(), clock);
      }

      if (trace.getInfos() != null) {
         dao.writeColumnInfos(updater, trace.getInfos(), clock);
      }

      tmpl.update(updater);

      // création de l'index
      TraceJournalEvtIndex index = new TraceJournalEvtIndex(trace);
      String journee = DateRegUtils.getJournee(index.getTimestamp());
      ColumnFamilyUpdater<String, UUID> indexUpdater = indexDao
            .createUpdater(journee);
      indexDao.writeColumn(indexUpdater, index.getIdentifiant(), index, clock);
      indexDao.update(indexUpdater);

      // Trace applicative
      LOGGER.debug("{} - Trace ajoutée dans le journal des événements : {}",
            prefix, trace.getIdentifiant());
      LOGGER.debug("{} - Fin", prefix);

   }

   /**
    * Suppression de toutes les traces et index
    * 
    * @param date
    *           date à laquelle supprimer les traces
    * @param clock
    *           horloge de la suppression
    * @return le nombre de traces purgées
    */
   public final long delete(Date date, long clock) {

      long nbTracesPurgees = 0;

      SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery;
      sliceQuery = indexDao.createSliceQuery();
      String journee = DateRegUtils.getJournee(date);
      sliceQuery.setKey(journee);

      TraceJournalEvtIndexIterator iterator = new TraceJournalEvtIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {

         // Suppression des traces de la CF TraceJournalEvt
         nbTracesPurgees = deleteRecords(iterator, clock);

         // suppression de l'index
         Mutator<String> indexMutator = indexDao.createMutator();
         indexDao.mutatorSuppressionTraceJournalEvtIndex(indexMutator, journee,
               clock);
         indexMutator.execute();

      }

      return nbTracesPurgees;

   }

   /**
    * Recherche et retourne la trace de sécurité avec l'id donné
    * 
    * @param identifiant
    *           identifiant de la trace
    * @return la trace de sécurité
    */
   public final TraceJournalEvt find(UUID identifiant) {
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getJournalEvtTmpl();
      ColumnFamilyResult<UUID, String> result = tmpl.queryColumns(identifiant);

      return getTraceJournalEvtFromResult(result);
   }

   /**
    * Recherche et retourne toutes les traces de sécurité écrites à une date
    * donnée
    * 
    * @param date
    *           la date à laquelle rechercher les traces
    * @return la liste des traces de sécurité
    */
   public final List<TraceJournalEvtIndex> findByDate(Date date) {
      SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery = indexDao
            .createSliceQuery();

      sliceQuery.setKey(DateRegUtils.getJournee(date));

      List<TraceJournalEvtIndex> list = null;
      TraceJournalEvtIndexIterator iterator = new TraceJournalEvtIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {
         list = new ArrayList<TraceJournalEvtIndex>();
      }

      while (iterator.hasNext()) {
         list.add(iterator.next());
      }

      return list;
   }

   /**
    * recherche et retourne la liste des traces de sécurité pour un intervalle
    * de dates données
    * 
    * @param startDate
    *           date de début de recherche
    * @param endDate
    *           date de fin de recherche
    * @param maxCount
    *           nombre maximal d'enregistrements à retourner
    * @param reversed
    *           booleen indiquant si l'ordre décroissant doit etre appliqué<br>
    *           <ul>
    *           <li>true : ordre décroissant</li>
    *           <li>false : ordre croissant</li>
    *           </ul>
    * @return la liste des traces de sécurité
    */
   public final List<TraceJournalEvtIndex> findByDates(Date startDate,
         Date endDate, int maxCount, boolean reversed) {

      // Trace applicative
      String prefix = "findByDates()";
      LOGGER.debug("{} - Début", prefix);
      LOGGER.debug("{} - Date de début : {}", prefix, dateFormat
            .format(startDate));
      LOGGER.debug("{} - Date de fin : {}", prefix, dateFormat.format(endDate));
      LOGGER.debug("{} - Ordre décroissant : {}", prefix, reversed);

      List<TraceJournalEvtIndex> list = null;

      SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateRegUtils.getJournee(startDate));

      UUID startUuid = TimeUUIDTraceUtils.buildUUIDFromDate(startDate);
      UUID endUuid = TimeUUIDTraceUtils.buildUUIDFromDateBorneSup(endDate);

      TraceJournalEvtIndexIterator iterator = new TraceJournalEvtIndexIterator(
            sliceQuery, startUuid, endUuid, reversed);

      try {
         if (iterator.hasNext()) {
            list = new ArrayList<TraceJournalEvtIndex>();
         }
      } catch (HInvalidRequestException ex) {
         LOGGER
               .warn(
                     "{} - Echec de la requête Cassandra. Date de début : {}. UUID début : {}. Date de fin : {}. UUID fin : {}.",
                     new Object[] { prefix, dateFormat.format(startDate),
                           startUuid, dateFormat.format(endDate), endUuid });
         throw ex;
      }

      int count = 0;
      while (iterator.hasNext() && count < maxCount) {
         list.add(iterator.next());
         count++;
      }

      // Trace applicative
      LOGGER.debug("{} - Fin", prefix);

      return list;
   }

   private TraceJournalEvt getTraceJournalEvtFromResult(
         ColumnFamilyResult<UUID, String> result) {

      TraceJournalEvt trace = null;

      if (result != null && result.hasResults()) {

         UUID idTrace = result.getKey();
         Date timestamp = result.getDate(TraceJournalEvtDao.COL_TIMESTAMP);

         trace = new TraceJournalEvt(idTrace, timestamp);

         trace.setCodeEvt(result.getString(TraceJournalEvtDao.COL_CODE_EVT));
         trace.setContexte(result.getString(TraceJournalEvtDao.COL_CONTEXT));
         trace.setContratService(result
               .getString(TraceJournalEvtDao.COL_CONTRAT_SERVICE));
         trace.setLogin(result.getString(TraceJournalEvtDao.COL_LOGIN));

         byte[] bValue = result.getByteArray(TraceJournalEvtDao.COL_INFOS);
         if (bValue != null) {
            trace.setInfos(MapSerializer.get().fromBytes(bValue));
         }

         bValue = result.getByteArray(TraceJournalEvtDao.COL_PAGMS);
         if (bValue != null) {
            trace.setPagms(ListSerializer.get().fromBytes(bValue));
         }
      }

      return trace;
   }

   private long deleteRecords(TraceJournalEvtIndexIterator iterator, long clock) {

      long result = 0;

      // suppression de toutes les traces
      Mutator<UUID> mutator = dao.createMutator();
      while (iterator.hasNext()) {
         dao.mutatorSuppressionRegExploitation(mutator, iterator.next()
               .getIdentifiant(), clock);
         result++;
      }
      mutator.execute();

      return result;

   }
}
