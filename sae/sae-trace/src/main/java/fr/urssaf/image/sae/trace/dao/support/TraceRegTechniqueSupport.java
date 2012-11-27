/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueDao;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

/**
 * Support de la classe DAO {@link TraceRegTechniqueDao}
 * 
 */
@Component
public class TraceRegTechniqueSupport {

   @Autowired
   private TraceRegTechniqueDao dao;

   /**
    * création d'une trace dans le registre technique
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceRegTechnique trace, long clock) {
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getTechTmpl();
      ColumnFamilyUpdater<UUID, String> updater = tmpl.createUpdater(trace
            .getIdentifiant());

      dao.writeColumnCodeEvt(updater, trace.getCodeEvt(), clock);
      dao.writeColumnContexte(updater, trace.getContexte(), clock);
      dao.writeColumnContratService(updater, trace.getContrat(), clock);
      dao.writeColumnLogin(updater, trace.getLogin(), clock);
      dao.writeColumnLogin(updater, trace.getStacktrace(), clock);
      dao.writeColumnTimestamp(updater, trace.getTimestamp(), clock);

      if (MapUtils.isNotEmpty(trace.getInfos())) {
         dao.writeColumnInfos(updater, trace.getInfos(), clock);
      }

      tmpl.update(updater);
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
      Mutator<UUID> mutator = dao.createMutator();
      dao.mutatorSuppressionTraceRegTechnique(mutator, identifiant, clock);
      mutator.execute();
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

   private TraceRegTechnique getTraceRegTechniqueFromResult(
         ColumnFamilyResult<UUID, String> result) {
      TraceRegTechnique exploit = null;

      if (result != null && result.hasNext()) {
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
