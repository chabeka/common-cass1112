/**
 * 
 */
package fr.urssaf.image.sae.commons.support;

import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.dao.ParametersDao;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;

/**
 * Classe permettant de réaliser des opérations sur les paramètres
 * 
 */
@Component
public class ParametersSupport {

   private final ParametersDao parametersDao;

   /**
    * Constructeur
    * 
    * @param parametersDao
    *           objet permettant de réaliser les opérations sur CASSANDRA
    */
   @Autowired
   public ParametersSupport(ParametersDao parametersDao) {
      this.parametersDao = parametersDao;
   }

   /**
    * Ajout d'une colonne de paramètre
    * 
    * @param parameter
    *           parametre a inserer
    * @param rowKey
    *           nom de la ligne
    * @param clock
    *           horloge de la création
    */
   public final void create(Parameter parameter, ParameterRowType rowKey,
         long clock) {

      ColumnFamilyUpdater<String, String> updater = parametersDao.getCfTmpl()
            .createUpdater(rowKey.toString());
      parametersDao.writeColumnParameter(updater, parameter, clock);

      parametersDao.getCfTmpl().update(updater);
   }

   /**
    * Recherche d'un parametre
    * 
    * @param parameterName
    *           parametre a recuperer
    * @param rowKey
    *           nom de la ligne
    * @throws ParameterNotFoundException
    *            exception levée lorsque le parametre n'est pas trouve
    * @return le parametre
    */
   public final Parameter find(ParameterType parameterName,
         ParameterRowType rowKey) throws ParameterNotFoundException {
      ColumnFamilyTemplate<String, String> tmpl = parametersDao.getCfTmpl();
      ColumnFamilyResult<String, String> result = tmpl.queryColumns(rowKey
            .toString());

      Parameter param = getParameterFromResult(result, parameterName);

      if (param == null) {
         throw new ParameterNotFoundException("le paramètre "
               + parameterName.toString() + " n'existe pas");
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
