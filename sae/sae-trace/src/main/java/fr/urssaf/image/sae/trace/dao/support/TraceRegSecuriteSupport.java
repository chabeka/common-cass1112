/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteDao;
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegSecuriteIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Support de la classe DAO {@link TraceRegSecuriteDao}
 * 
 */
@Component
public class TraceRegSecuriteSupport {

   @Autowired
   private TraceRegSecuriteDao dao;

   @Autowired
   private TraceRegSecuriteIndexDao indexDao;

   /**
    * Création d'une trace dans le registre de sécurité
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceRegSecurite trace, long clock) {
      // création de la trace
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getSecuTmpl();
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
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getSecuTmpl();
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
      List<TraceRegSecuriteIndex> list = null;

      SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateRegUtils.getJournee(startDate));

      UUID startUuid = TimeUUIDUtils.getTimeUUID(startDate.getTime());
      UUID endUuid = TimeUUIDUtils.getTimeUUID(endDate.getTime());

      TraceRegSecuriteIndexIterator iterator = new TraceRegSecuriteIndexIterator(
            sliceQuery, startUuid, endUuid, reversed);

      if (iterator.hasNext()) {
         list = new ArrayList<TraceRegSecuriteIndex>();
      }

      int count = 0;
      while (iterator.hasNext() && count < maxCount) {
         list.add(iterator.next());
         count++;
      }

      return list;
   }

   private TraceRegSecurite getTraceRegSecuriteFromResult(
         ColumnFamilyResult<UUID, String> result) {

      TraceRegSecurite securite = null;

      if (result != null && result.hasResults()) {
         securite = new TraceRegSecurite();

         securite.setIdentifiant(result.getKey());
         securite
               .setCodeEvt(result.getString(TraceRegSecuriteDao.COL_CODE_EVT));
         securite.setContexte(result
               .getString(TraceRegSecuriteDao.COL_CONTEXTE));
         securite.setContrat(result
               .getString(TraceRegSecuriteDao.COL_CONTRAT_SERVICE));
         securite.setLogin(result.getString(TraceRegSecuriteDao.COL_LOGIN));
         securite.setTimestamp(result
               .getDate(TraceRegSecuriteDao.COL_TIMESTAMP));

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
         dao.mutatorSuppressionTraceRegSecurite(mutator, iterator.next()
               .getIdentifiant(), clock);
         result++;
      }
      mutator.execute();

      return result;

   }
}
