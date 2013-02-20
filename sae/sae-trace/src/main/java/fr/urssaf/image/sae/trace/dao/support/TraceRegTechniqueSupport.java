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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegTechniqueIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

/**
 * Support de la classe DAO {@link TraceRegTechniqueDao}
 * 
 */
@Component
public class TraceRegTechniqueSupport {

   @Autowired
   private TraceRegTechniqueDao dao;

   @Autowired
   private TraceRegTechniqueIndexDao indexDao;

   /**
    * création d'une trace dans le registre technique
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceRegTechnique trace, long clock) {
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
      ColumnFamilyTemplate<Date, UUID> indexTmpl = indexDao.getTechIndexTmpl();
      ColumnFamilyUpdater<Date, UUID> indexUpdater = indexTmpl
            .createUpdater(DateUtils.truncate(index.getTimestamp(),
                  Calendar.DATE));
      indexDao.writeColumn(indexUpdater, index.getIdentifiant(), index, clock);
      indexTmpl.update(indexUpdater);
   }

   /**
    * Suppression de toutes les traces et index
    * 
    * @param date
    *           date pour laquelle supprimer toutes les traces
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(Date date, long clock) {

      SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery;
      sliceQuery = indexDao.createSliceQuery();
      Date dateRef = DateUtils.truncate(date, Calendar.DATE);
      sliceQuery.setKey(dateRef);

      TraceRegTechniqueIndexIterator iterator = new TraceRegTechniqueIndexIterator(
            sliceQuery);

      deleteRecords(iterator, clock);

      // suppression de l'index
      Mutator<Date> indexMutator = indexDao.createMutator();
      indexDao.mutatorSuppressionTraceRegTechniqueIndex(indexMutator, dateRef,
            clock);
      indexMutator.execute();

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
      SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateUtils.truncate(date, Calendar.DATE));

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
      List<TraceRegTechniqueIndex> list = null;

      SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateUtils.truncate(startDate, Calendar.DATE));

      UUID startUuid = TimeUUIDUtils.getTimeUUID(startDate.getTime());
      UUID endUuid = TimeUUIDUtils.getTimeUUID(endDate.getTime());

      TraceRegTechniqueIndexIterator iterator = new TraceRegTechniqueIndexIterator(
            sliceQuery, startUuid, endUuid, reversed);

      if (iterator.hasNext()) {
         list = new ArrayList<TraceRegTechniqueIndex>();
      }

      int count = 0;
      while (iterator.hasNext() && count < maxCount) {
         list.add(iterator.next());
         count++;
      }

      return list;
   }

   private TraceRegTechnique getTraceRegTechniqueFromResult(
         ColumnFamilyResult<UUID, String> result) {
      TraceRegTechnique exploit = null;

      if (result != null && result.hasResults()) {
         exploit = new TraceRegTechnique();

         exploit.setIdentifiant(result.getKey());

         exploit.setContexte(result
               .getString(TraceRegTechniqueDao.COL_CONTEXTE));
         exploit
               .setCodeEvt(result.getString(TraceRegTechniqueDao.COL_CODE_EVT));
         exploit.setContrat(result
               .getString(TraceRegTechniqueDao.COL_CONTRAT_SERVICE));
         exploit.setLogin(result.getString(TraceRegTechniqueDao.COL_LOGIN));
         exploit.setStacktrace(result
               .getString(TraceRegTechniqueDao.COL_STACKTRACE));
         exploit.setTimestamp(result
               .getDate(TraceRegTechniqueDao.COL_TIMESTAMP));

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

   private void deleteRecords(TraceRegTechniqueIndexIterator iterator,
         long clock) {

      // suppression de toutes les traces
      Mutator<UUID> mutator = dao.createMutator();
      while (iterator.hasNext()) {
         dao.mutatorSuppressionTraceRegTechnique(mutator, iterator.next()
               .getIdentifiant(), clock);
      }
      mutator.execute();
   }
}
