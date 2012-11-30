/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteDao;
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegSecuriteIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

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

      if (StringUtils.isNotBlank(trace.getLogin())) {
         dao.writeColumnLogin(updater, trace.getLogin(), clock);
      }

      if (trace.getInfos() != null) {
         dao.writeColumnInfos(updater, trace.getInfos(), clock);
      }

      tmpl.update(updater);

      // création de l'index
      TraceRegSecuriteIndex index = new TraceRegSecuriteIndex(trace);
      ColumnFamilyTemplate<Date, UUID> indexTmpl = indexDao.getSecuIndexTmpl();
      ColumnFamilyUpdater<Date, UUID> indexUpdater = indexTmpl
            .createUpdater(DateUtils.truncate(trace.getTimestamp(),
                  Calendar.DATE));
      indexDao.writeColumn(indexUpdater, index.getIdentifiant(), index, clock);
      indexTmpl.update(indexUpdater);
   }

   /**
    * Suppression de toutes les traces et index
    * 
    * @param date
    *           date à laquelle supprimer les traces
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(Date date, long clock) {

      SliceQuery<Date, UUID, TraceRegSecuriteIndex> sliceQuery;
      sliceQuery = indexDao.createSliceQuery();
      Date dateRef = DateUtils.truncate(date, Calendar.DATE);
      sliceQuery.setKey(dateRef);

      TraceRegSecuriteIndexIterator iterator = new TraceRegSecuriteIndexIterator(
            sliceQuery);

      deleteRecords(iterator, clock);

      // suppression de l'index
      Mutator<Date> indexMutator = indexDao.createMutator();
      indexDao.mutatorSuppressionTraceRegSecuriteIndex(indexMutator, dateRef,
            clock);
      indexMutator.execute();

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
      SliceQuery<Date, UUID, TraceRegSecuriteIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateUtils.truncate(date, Calendar.DATE));

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

      SliceQuery<Date, UUID, TraceRegSecuriteIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateUtils.truncate(startDate, Calendar.DATE));

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
      }

      return securite;
   }

   private void deleteRecords(TraceRegSecuriteIndexIterator iterator, long clock) {

      // suppression de toutes les traces
      Mutator<UUID> mutator = dao.createMutator();
      while (iterator.hasNext()) {
         dao.mutatorSuppressionTraceRegSecurite(mutator, iterator.next()
               .getIdentifiant(), clock);
      }
      mutator.execute();
   }
}
