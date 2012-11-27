/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.List;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.TraceDestinataireDao;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
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
}
