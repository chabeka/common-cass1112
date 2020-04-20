/**
 * 
 */
package fr.urssaf.image.sae.commons.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.dao.ParametersDao;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

/**
 * Classe permettant de réaliser des opérations sur les paramètres
 */
@Component
public class ParametersSupport {

  private final ParametersDao parametersDao;

  /**
   * Constructeur
   * 
   * @param parametersDao
   *          objet permettant de réaliser les opérations sur CASSANDRA
   */
  @Autowired
  public ParametersSupport(final ParametersDao parametersDao) {
    this.parametersDao = parametersDao;
  }

  /**
   * Ajout d'une colonne de paramètre
   * 
   * @param parameter
   *          parametre a inserer
   * @param rowKey
   *          nom de la ligne
   * @param clock
   *          horloge de la création
   */
  public final void create(final Parameter parameter, final ParameterRowType rowKey,
                           final long clock) {

    final ColumnFamilyUpdater<String, String> updater = parametersDao.getCfTmpl()
        .createUpdater(rowKey.toString());
    parametersDao.writeColumnParameter(updater, parameter, clock);

    parametersDao.getCfTmpl().update(updater);
  }

  /**
   * Recherche d'un parametre
   * 
   * @param parameterName
   *          parametre a recuperer
   * @param rowKey
   *          nom de la ligne
   * @throws ParameterNotFoundException
   *           exception levée lorsque le parametre n'est pas trouve
   * @return le parametre
   */
  public final Parameter find(final ParameterType parameterName,
                              final ParameterRowType rowKey)
                                  throws ParameterNotFoundException {
    final ColumnFamilyTemplate<String, String> tmpl = parametersDao.getCfTmpl();
    final ColumnFamilyResult<String, String> result = tmpl.queryColumns(rowKey
                                                                        .toString());

    final Parameter param = getParameterFromResult(result, parameterName);

    if (param == null) {
      throw new ParameterNotFoundException("le paramètre "
          + parameterName.toString() + " n'existe pas");
    }

    return param;
  }

  private Parameter getParameterFromResult(
                                           final ColumnFamilyResult<String, String> result, final ParameterType identifiant) {

    Parameter param = null;

    if (result != null && result.hasResults()) {
      final byte[] byteArray = result.getByteArray(identifiant.toString());

      if (byteArray != null) {
        final Object value = ObjectSerializer.get().fromBytes(byteArray);
        param = new Parameter(identifiant, value);
      }
    }

    return param;
  }

  public final List<Parameter> findAllByRowType(final ParameterRowType rowKey) {

    final ColumnFamilyResult<String, String> result = parametersDao.getCfTmpl()
        .queryColumns(
                      rowKey.toString());

    final Collection<String> colNames = result.getColumnNames();

    List<Parameter> parameters = null;

    if (CollectionUtils.isNotEmpty(colNames)) {
      parameters = new ArrayList<>(colNames.size());
    }

    for (final String name : colNames) {
      try {
        final byte[] bResult = result.getByteArray(name);

        if (bResult != null) {

          final Object value = ObjectSerializer.get().fromBytes(bResult);
          final ParameterType parameterType = ParameterType.valueOfLabel(name);
          if (parameterType != null) {
            final Parameter parameter = new Parameter(parameterType, value);
            parameters.add(parameter);
          }
        }
      }
      catch (final Exception e) {
        // Cas traitement version rnd
        final long value2 = result.getLong(name);
        final ParameterType parameterType = ParameterType.valueOfLabel(name);
        if (parameterType != null) {
          final Parameter parameter = new Parameter(parameterType, value2);
          parameters.add(parameter);
        }
      }
    }

    return parameters;

  }


}
