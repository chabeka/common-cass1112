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
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.iterator.TraceRegTechniqueIndexIterator;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
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
      dao.writeColumnContratService(updater, trace.getContrat(), clock);
      dao.writeColumnLogin(updater, trace.getLogin(), clock);
      dao.writeColumnStackTrace(updater, trace.getStacktrace(), clock);
      dao.writeColumnTimestamp(updater, trace.getTimestamp(), clock);

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
    * suppression d'une trace du registre technique
    * 
    * @param identifiant
    *           identifiant de la trace
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(UUID identifiant, long clock) {
      // récupération de la date pour la suppression de l'index
      Date date = DateUtils.truncate(find(identifiant).getTimestamp(),
            Calendar.DATE);

      // suppression de la ligne
      Mutator<UUID> mutator = dao.createMutator();
      dao.mutatorSuppressionTraceRegTechnique(mutator, identifiant, clock);
      mutator.execute();

      // suppression de l'index
      Mutator<Date> indexMutator = indexDao.createMutator();
      indexDao.mutatorSuppressionTraceRegTechniqueIndex(indexMutator, date,
            identifiant, clock);
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
   public final List<TraceRegTechnique> findByDate(Date date) {
      SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery = indexDao
            .createSliceQuery();
      sliceQuery.setKey(DateUtils.truncate(date, Calendar.DATE));

      List<TraceRegTechnique> list = null;
      TraceRegTechniqueIndexIterator iterator = new TraceRegTechniqueIndexIterator(
            sliceQuery);

      if (iterator.hasNext()) {
         list = new ArrayList<TraceRegTechnique>();
      }

      TraceRegTechniqueIndex index;
      while (iterator.hasNext()) {
         index = iterator.next();
         list.add(find(index.getIdentifiant()));
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

      }

      return exploit;
   }
}
