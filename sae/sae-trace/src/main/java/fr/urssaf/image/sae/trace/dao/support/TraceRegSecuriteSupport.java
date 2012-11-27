/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteDao;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

/**
 * Support de la classe DAO {@link TraceRegSecuriteDao}
 * 
 */
@Component
public class TraceRegSecuriteSupport {

   @Autowired
   private TraceRegSecuriteDao dao;

   /**
    * Création d'une trace dans le registre de sécurité
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceRegSecurite trace, long clock) {
      ColumnFamilyTemplate<UUID, String> tmpl = dao.getSecuTmpl();
      ColumnFamilyUpdater<UUID, String> updater = tmpl.createUpdater(trace
            .getIdentifiant());

      dao.writeColumnCodeEvt(updater, trace.getCodeEvt(), clock);
      dao.writeColumnContexte(updater, trace.getContexte(), clock);
      dao.writeColumnContratService(updater, trace.getContrat(), clock);
      dao.writeColumnLogin(updater, trace.getLogin(), clock);
      dao.writeColumnTimestamp(updater, trace.getTimestamp(), clock);

      if (trace.getInfos() != null) {
         dao.writeColumnInfos(updater, trace.getInfos(), clock);
      }

      tmpl.update(updater);
   }

   /**
    * Suppression d'une trace dans le registre de sécurité
    * 
    * @param identifiant
    *           identifiant de la trace à supprimer
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(UUID identifiant, long clock) {
      Mutator<UUID> mutator = dao.createMutator();
      dao.mutatorSuppressionTraceRegSecurite(mutator, identifiant, clock);
      mutator.execute();
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

   private TraceRegSecurite getTraceRegSecuriteFromResult(
         ColumnFamilyResult<UUID, String> result) {

      TraceRegSecurite securite = null;

      if (result != null && result.hasNext()) {
         securite = new TraceRegSecurite();

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
}
