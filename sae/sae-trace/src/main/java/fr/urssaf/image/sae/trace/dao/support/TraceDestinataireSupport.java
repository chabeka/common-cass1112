/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;

/**
 * Support de la classe DAO {@link TraceDestinataireDao}
 * 
 */
@Component
public class TraceDestinataireSupport {

   @Autowired
   private TraceDestinataireDao dao;

   /**
    * Création d'une colonne
    * 
    * @param trace
    *           trace à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(TraceDestinataire trace, long clock) {
      ColumnFamilyTemplate<String, String> tmpl = dao.getDestTmpl();
      ColumnFamilyUpdater<String, String> updater = tmpl.createUpdater(trace
            .getCodeEvt());

      if (MapUtils.isNotEmpty(trace.getDestinataires())) {
         for (String key : trace.getDestinataires().keySet()) {

            insertColumn(updater, key, trace.getDestinataires().get(key), clock);

         }
      }

      tmpl.update(updater);
   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant de la ligne
    * @param clock
    *           horloge de suppression
    */
   public final void delete(String code, long clock) {
      Mutator<String> mutator = dao.createMutator();
      dao.mutatorSuppressionDestinataire(mutator, code, clock);
      mutator.execute();
   }

   /**
    * Recherche et retourne l'enregistrement de la trace destinataire en
    * fonction du code fourni
    * 
    * @param code
    *           code de la trace destinataire
    * @return l'enregistrement de la trace destinataire correspondante
    */
   public final TraceDestinataire find(String code) {
      ColumnFamilyTemplate<String, String> tmpl = dao.getDestTmpl();
      ColumnFamilyResult<String, String> result = tmpl.queryColumns(code);

      TraceDestinataire trace = getTraceDestinataireFromResult(result);

      return trace;
   }

   private void insertColumn(ColumnFamilyUpdater<String, String> updater,
         String key, List<String> list, long clock) {

      if (TraceDestinataireDao.COL_HIST_ARCHIVE.equals(key)) {
         dao.writeColumnHistArchive(updater, list, clock);

      } else if (TraceDestinataireDao.COL_HIST_EVT.equals(key)) {
         dao.writeColumnHistEvt(updater, list, clock);

      } else if (TraceDestinataireDao.COL_REG_EXPLOIT.equals(key)) {
         dao.writeColumnRegExploit(updater, list, clock);

      } else if (TraceDestinataireDao.COL_REG_SECURITE.equals(key)) {
         dao.writeColumnRegSecurite(updater, list, clock);

      } else if (TraceDestinataireDao.COL_REG_TECHNIQUE.equals(key)) {
         dao.writeColumnRegTechnique(updater, list, clock);
      } else {
         throw new TraceRuntimeException(
               "Impossible de créer l'enregistrement demandé. " + "La clé "
                     + key + " n'est pas supportée");
      }
   }

   private TraceDestinataire getTraceDestinataireFromResult(
         ColumnFamilyResult<String, String> result) {

      TraceDestinataire trace = null;

      if (result != null && result.hasNext()) {
         trace = new TraceDestinataire();
         trace.setCodeEvt(result.getKey());

         List<String> colNames = Arrays.asList(
               TraceDestinataireDao.COL_HIST_ARCHIVE,
               TraceDestinataireDao.COL_HIST_EVT,
               TraceDestinataireDao.COL_REG_EXPLOIT,
               TraceDestinataireDao.COL_REG_SECURITE,
               TraceDestinataireDao.COL_REG_TECHNIQUE);

         Map<String, List<String>> destinataires = new HashMap<String, List<String>>();
         List<String> values;
         for (String colName : colNames) {
            values = getListFromResult(colName, result);
            destinataires.put(colName, values);
         }

         trace.setDestinataires(destinataires);
      }

      return trace;

   }

   private List<String> getListFromResult(String colName,
         ColumnFamilyResult<String, String> result) {

      List<String> value = null;
      byte[] bValue = result.getByteArray(colName);

      if (bValue != null) {
         value = ListSerializer.get().fromBytes(bValue);
      }

      return value;
   }
}
