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

import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteDao;
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegSecuriteIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Support de la classe DAO {@link TraceRegSecuriteDao}
 * 
 */
@Component
public class TraceRegSecuriteSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceRegSecuriteSupport.class);

   private final SimpleDateFormat dateFormat = new SimpleDateFormat(
         "yyyy-MM-dd HH'h'mm ss's' SSS'ms'", Locale.FRENCH);

   private final TraceRegSecuriteDao dao;

   private final TraceRegSecuriteIndexDao indexDao;

   private final TimeUUIDEtTimestampSupport timeUUIDSupport;

   /**
    * @param dao
    *           Service DAO de la famille de colonnes "TraceRegSecurite"
    * @param indexDao
    *           Service DAO de la famille de colonnes "TraceRegSecuriteIndex"
    * @param timeUUIDSupport
    *           Utilitaires pour créer des TimeUUID
    */
   @Autowired
   public TraceRegSecuriteSupport(TraceRegSecuriteDao dao,
         TraceRegSecuriteIndexDao indexDao,
         TimeUUIDEtTimestampSupport timeUUIDSupport) {
      super();
      this.dao = dao;
      this.indexDao = indexDao;
      this.timeUUIDSupport = timeUUIDSupport;
   }

   /**
    * Création d'une trace dans le registre de sécurité
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceRegSecurite trace, long clock) {

      // Trace applicative
      String prefix = "create()";
      LOGGER.debug("{} - Début", prefix);

      // création de la trace
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getCfTmpl();
      ColumnFamilyUpdater<UUID, String> updater = tmpl.createUpdater(trace
            .getIdentifiant());

      dao.writeColumnCodeEvt(updater, trace.getCodeEvt(), clock);
      dao.writeColumnContexte(updater, trace.getContexte(), clock);
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
      TraceRegSecuriteIndex index = new TraceRegSecuriteIndex(trace);
      String journee = DateRegUtils.getJournee(index.getTimestamp());
      ColumnFamilyUpdater<String, UUID> indexUpdater = indexDao
            .createUpdater(journee);
      indexDao.writeColumn(indexUpdater, index.getIdentifiant(), index, clock);
      indexDao.update(indexUpdater);

      // Trace applicative
      LOGGER
            .debug(
                  "{} - Trace ajoutée dans le registre de sécurité : Id={}. Timestamp={}",
                  new Object[] { prefix, trace.getIdentifiant(),
                        dateFormat.format(trace.getTimestamp()) });
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

      SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery;
      sliceQuery = indexDao.createSliceQuery();
      String journee = DateRegUtils.getJournee(date);
      sliceQuery.setKey(journee);

      TraceRegSecuriteIndexIterator iterator = new TraceRegSecuriteIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {

         // Suppression des traces de la CF TraceRegSecurite
         nbTracesPurgees = deleteRecords(iterator, clock);

         // suppression de l'index
         Mutator<String> indexMutator = indexDao.createMutator();
         indexDao.mutatorSuppressionTraceRegSecuriteIndex(indexMutator,
               journee, clock);
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
   public final TraceRegSecurite find(UUID identifiant) {
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getCfTmpl();
      ColumnFamilyResult<UUID, String> result = tmpl.queryColumns(identifiant);

      return getTraceRegSecuriteFromResult(result);
   }

   /**
    * Recherche et retourne toutes les traces de sécurité écrites à une date
    * donnée
    * 
    * @param date
    *           la date à laquelle rechercher les traces
    * @return la liste des traces de sécurité
    */
   public final List<TraceRegSecuriteIndex> findByDate(Date date) {
      SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery = indexDao
            .createSliceQuery();

      sliceQuery.setKey(DateRegUtils.getJournee(date));

      List<TraceRegSecuriteIndex> list = null;
      TraceRegSecuriteIndexIterator iterator = new TraceRegSecuriteIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {
         list = new ArrayList<TraceRegSecuriteIndex>();
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
   public final List<TraceRegSecuriteIndex> findByDates(Date startDate,
         Date endDate, int maxCount, boolean reversed) {

      // Trace applicative
      String prefix = "findByDates()";
      LOGGER.debug("{} - Début", prefix);
      LOGGER.debug("{} - Date de début : {}", prefix, dateFormat
            .format(startDate));
      LOGGER.debug("{} - Date de fin : {}", prefix, dateFormat.format(endDate));
      LOGGER.debug("{} - Ordre décroissant : {}", prefix, reversed);

      List<TraceRegSecuriteIndex> list = null;

      SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateRegUtils.getJournee(startDate));

      UUID startUuid = timeUUIDSupport.buildUUIDFromDate(startDate);
      UUID endUuid = timeUUIDSupport.buildUUIDFromDateBorneSup(endDate);

      TraceRegSecuriteIndexIterator iterator = new TraceRegSecuriteIndexIterator(
            sliceQuery, startUuid, endUuid, reversed);

      try {
         if (iterator.hasNext()) {
            list = new ArrayList<TraceRegSecuriteIndex>();
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

   private TraceRegSecurite getTraceRegSecuriteFromResult(
         ColumnFamilyResult<UUID, String> result) {

      TraceRegSecurite securite = null;

      if (result != null && result.hasResults()) {

         UUID idTrace = result.getKey();
         Date timestamp = result.getDate(TraceRegSecuriteDao.COL_TIMESTAMP);

         securite = new TraceRegSecurite(idTrace, timestamp);

         securite
               .setCodeEvt(result.getString(TraceRegSecuriteDao.COL_CODE_EVT));
         securite.setContexte(result
               .getString(TraceRegSecuriteDao.COL_CONTEXTE));
         securite.setContratService(result
               .getString(TraceRegSecuriteDao.COL_CONTRAT_SERVICE));
         securite.setLogin(result.getString(TraceRegSecuriteDao.COL_LOGIN));

         byte[] bValue = result.getByteArray(TraceRegSecuriteDao.COL_INFOS);
         if (bValue != null) {
            securite.setInfos(MapSerializer.get().fromBytes(bValue));
         }

         bValue = result.getByteArray(TraceRegSecuriteDao.COL_PAGMS);
         if (bValue != null) {
            securite.setPagms(ListSerializer.get().fromBytes(bValue));
         }
      }

      return securite;
   }

   private long deleteRecords(TraceRegSecuriteIndexIterator iterator, long clock) {

      long result = 0;

      // suppression de toutes les traces
      Mutator<UUID> mutator = dao.createMutator();
      while (iterator.hasNext()) {
         dao.mutatorSuppressionLigne(mutator, iterator.next().getIdentifiant(),
               clock);
         result++;
      }
      mutator.execute();

      return result;

   }
}
