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
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegTechniqueIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Support de la classe DAO {@link TraceRegTechniqueDao}
 * 
 */
@Component
public class TraceRegTechniqueSupport {

   private final TraceRegTechniqueDao dao;

   private final TraceRegTechniqueIndexDao indexDao;

   private final TimeUUIDEtTimestampSupport timeUUIDSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceRegTechniqueSupport.class);

   private final SimpleDateFormat dateFormat = new SimpleDateFormat(
         "yyyy-MM-dd HH'h'mm ss's' SSS'ms'", Locale.FRENCH);

   /**
    * Constructeur
    * 
    * @param dao
    *           Service DAO de la famille de colonnes "TraceRegTechnique"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceRegTechniqueIndex"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceRegTechniqueSupport(TraceRegTechniqueDao dao,
         TraceRegTechniqueIndexDao indexDao,
         TimeUUIDEtTimestampSupport timeUUIDSupport) {
      super();
      this.dao = dao;
      this.indexDao = indexDao;
      this.timeUUIDSupport = timeUUIDSupport;
   }

   /**
    * création d'une trace dans le registre technique
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceRegTechnique trace, long clock) {

      // Trace applicative
      String prefix = "create()";
      LOGGER.debug("{} - Début", prefix);

      // création de la trace
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getTechTmpl();
      ColumnFamilyUpdater<UUID, String> updater = tmpl.createUpdater(trace
            .getIdentifiant());

      dao.writeColumnCodeEvt(updater, trace.getCodeEvt(), clock);
      dao.writeColumnContexte(updater, trace.getContexte(), clock);
      dao.writeColumnTimestamp(updater, trace.getTimestamp(), clock);

      if (StringUtils.isNotBlank(trace.getContrat())) {
         dao.writeColumnContratService(updater, trace.getContrat(), clock);
      }

      if (CollectionUtils.isNotEmpty(trace.getPagms())) {
         dao.writeColumnPagms(updater, trace.getPagms(), clock);
      }

      if (StringUtils.isNotBlank(trace.getLogin())) {
         dao.writeColumnLogin(updater, trace.getLogin(), clock);
      }

      if (StringUtils.isNotBlank(trace.getStacktrace())) {
         dao.writeColumnStackTrace(updater, trace.getStacktrace(), clock);
      }

      if (MapUtils.isNotEmpty(trace.getInfos())) {
         dao.writeColumnInfos(updater, trace.getInfos(), clock);
      }

      tmpl.update(updater);

      // création de l'index
      TraceRegTechniqueIndex index = new TraceRegTechniqueIndex(trace);
      String journee = DateRegUtils.getJournee(index.getTimestamp());
      ColumnFamilyUpdater<String, UUID> indexUpdater = indexDao
            .createUpdater(journee);
      indexDao.writeColumn(indexUpdater, index.getIdentifiant(), index, clock);
      indexDao.update(indexUpdater);

      // Trace applicative
      LOGGER
            .debug(
                  "{} - Trace ajoutée dans le registre de surveillance technique : Id={}. Timestamp={}",
                  new Object[] { prefix, trace.getIdentifiant(),
                        dateFormat.format(trace.getTimestamp()) });
      LOGGER.debug("{} - Fin", prefix);

   }

   /**
    * Suppression de toutes les traces et index
    * 
    * @param date
    *           date pour laquelle supprimer toutes les traces
    * @param clock
    *           horloge de la suppression
    * @return le nombre de traces purgées
    */
   public final long delete(Date date, long clock) {

      long nbTracesPurgees = 0;

      SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery;
      sliceQuery = indexDao.createSliceQuery();
      String journee = DateRegUtils.getJournee(date);
      sliceQuery.setKey(journee);

      TraceRegTechniqueIndexIterator iterator = new TraceRegTechniqueIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {

         // Suppression des traces de la CF TraceRegTechnique
         nbTracesPurgees = deleteRecords(iterator, clock);

         // suppression de l'index
         Mutator<String> indexMutator = indexDao.createMutator();
         indexDao.mutatorSuppressionTraceRegTechniqueIndex(indexMutator,
               journee, clock);
         indexMutator.execute();

      }

      return nbTracesPurgees;

   }

   /**
    * Recherche et retourne la trace du registre technique
    * 
    * @param identifiant
    *           l'identifiant de la trace
    * @return la trace technique
    */
   public final TraceRegTechnique find(UUID identifiant) {
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getTechTmpl();
      ColumnFamilyResult<UUID, String> result = tmpl.queryColumns(identifiant);

      return getTraceRegTechniqueFromResult(result);
   }

   /**
    * Recherche et retourne la liste des traces techniques à une date donnée
    * 
    * @param date
    *           date à laquelle rechercher les traces
    * @return la liste des traces techniques
    */
   public final List<TraceRegTechniqueIndex> findByDate(Date date) {
      SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateRegUtils.getJournee(date));

      List<TraceRegTechniqueIndex> list = null;
      TraceRegTechniqueIndexIterator iterator = new TraceRegTechniqueIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {
         list = new ArrayList<TraceRegTechniqueIndex>();
      }

      while (iterator.hasNext()) {
         list.add(iterator.next());
      }

      return list;
   }

   /**
    * recherche et retourne la liste des traces de technique pour un intervalle
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
    * @return la liste des traces techniques
    */
   public final List<TraceRegTechniqueIndex> findByDates(Date startDate,
         Date endDate, int maxCount, boolean reversed) {

      // Trace applicative
      String prefix = "findByDates()";
      LOGGER.debug("{} - Début", prefix);
      LOGGER.debug("{} - Date de début : {}", prefix, dateFormat
            .format(startDate));
      LOGGER.debug("{} - Date de fin : {}", prefix, dateFormat.format(endDate));
      LOGGER.debug("{} - Ordre décroissant : {}", prefix, reversed);

      List<TraceRegTechniqueIndex> list = null;

      SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateRegUtils.getJournee(startDate));

      UUID startUuid = timeUUIDSupport.buildUUIDFromDate(startDate);
      UUID endUuid = timeUUIDSupport.buildUUIDFromDateBorneSup(endDate);

      TraceRegTechniqueIndexIterator iterator = new TraceRegTechniqueIndexIterator(
            sliceQuery, startUuid, endUuid, reversed);

      try {
         if (iterator.hasNext()) {
            list = new ArrayList<TraceRegTechniqueIndex>();
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

   private TraceRegTechnique getTraceRegTechniqueFromResult(
         ColumnFamilyResult<UUID, String> result) {
      TraceRegTechnique exploit = null;

      if (result != null && result.hasResults()) {

         UUID idTrace = result.getKey();
         Date timestamp = result.getDate(TraceRegTechniqueDao.COL_TIMESTAMP);

         exploit = new TraceRegTechnique(idTrace, timestamp);

         exploit.setContexte(result
               .getString(TraceRegTechniqueDao.COL_CONTEXTE));
         exploit
               .setCodeEvt(result.getString(TraceRegTechniqueDao.COL_CODE_EVT));
         exploit.setContrat(result
               .getString(TraceRegTechniqueDao.COL_CONTRAT_SERVICE));
         exploit.setLogin(result.getString(TraceRegTechniqueDao.COL_LOGIN));
         exploit.setStacktrace(result
               .getString(TraceRegTechniqueDao.COL_STACKTRACE));

         byte[] bValue = result.getByteArray(TraceRegTechniqueDao.COL_INFOS);
         if (bValue != null) {
            exploit.setInfos(MapSerializer.get().fromBytes(bValue));
         }

         bValue = result.getByteArray(TraceRegTechniqueDao.COL_PAGMS);
         if (bValue != null) {
            exploit.setPagms(ListSerializer.get().fromBytes(bValue));
         }

      }

      return exploit;
   }

   private long deleteRecords(TraceRegTechniqueIndexIterator iterator,
         long clock) {

      long result = 0;

      // suppression de toutes les traces
      Mutator<UUID> mutator = dao.createMutator();
      while (iterator.hasNext()) {
         dao.mutatorSuppressionTraceRegTechnique(mutator, iterator.next()
               .getIdentifiant(), clock);
         result++;
      }
      mutator.execute();

      return result;

   }
}
