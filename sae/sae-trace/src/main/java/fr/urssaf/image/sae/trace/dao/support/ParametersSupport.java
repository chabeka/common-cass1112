/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.ParametersDao;
import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;

/**
 * Support de la classe DAO {@link ParametersDao}
 * 
 */
@Component
public class ParametersSupport {

   @Autowired
   private ParametersDao dao;

   /**
    * Création ou modification du paramètre donné
    * 
    * @param parameter
    *           paramètre à créer ou modifier
    * @param clock
    *           horloge de la création
    */
   public final void create(Parameter parameter, long clock) {
      ColumnFamilyTemplate<String, String> tmpl = dao.getParamsTmpl();
      ColumnFamilyUpdater<String, String> updater = tmpl
            .createUpdater(ParametersDao.KEY_ROW_PURGE);

      dao.writeColumnParameter(updater, parameter, clock);

      tmpl.update(updater);
   }

   /**
    * Recherche et retourne le paramètre avec l'identifiant donné
    * 
    * @param identifiant
    *           identifiant de la trace d'exploitation
    * @return la trace d'exploitation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'a pas été trouvé
    */
   public final Parameter find(ParameterType identifiant)
         throws ParameterNotFoundException {
      ColumnFamilyTemplate<String, String> tmpl = dao.getParamsTmpl();
      ColumnFamilyResult<String, String> result = tmpl
            .queryColumns(ParametersDao.KEY_ROW_PURGE);

      Parameter param = getParameterFromResult(result, identifiant);

      if (param == null) {
         throw new ParameterNotFoundException("le paramètre "
               + identifiant.toString() + " n'existe pas");
      }

      return param;
   }

   private Parameter getParameterFromResult(
         ColumnFamilyResult<String, String> result, ParameterType identifiant) {

      Parameter param = null;

      if (result != null && result.hasResults()) {
         byte[] byteArray = result.getByteArray(identifiant.toString());

         if (byteArray != null) {
            Object value = ObjectSerializer.get().fromBytes(byteArray);
            param = new Parameter(identifiant, value);
         }
      }

      return param;
   }

}
