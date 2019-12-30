/**
 * 
 */
package fr.urssaf.image.sae.commons.support.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.support.ParametersSupport;
import fr.urssaf.image.sae.commons.support.cql.ParametersCqlSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.commons.utils.ParametersUtils;

/**
 * Classe permettant de réaliser des opérations sur les paramètres
 * 
 */
@Component
public class ParametersSupportFacade {

  private final String cfName = Constantes.CF_PARAMETERS;

  private final ParametersSupport parametersSupport;

  private final ParametersCqlSupport parametersCqlSupport;

  private final JobClockSupport clockSupport;
  /**
   * Constructeur
   * 
   * @param parametersDao
   *           objet permettant de réaliser les opérations sur CASSANDRA
   */
  @Autowired
  public ParametersSupportFacade(final ParametersSupport parametersSupport,
                                 final ParametersCqlSupport parametersCqlSupport,
                                 final JobClockSupport clockSupport) {
    this.parametersSupport = parametersSupport;
    this.parametersCqlSupport = parametersCqlSupport;
    this.clockSupport = clockSupport;
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
  public final void create(final Parameter parameter, final ParameterRowType rowKey) {
    final ParameterCql parameterCql = ParametersUtils.convertParameterToParameterCql(rowKey, parameter);

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      parametersSupport.create(parameter, rowKey, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      parametersCqlSupport.create(parameterCql, clockSupport.currentCLock());
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      parametersSupport.create(parameter, rowKey, clockSupport.currentCLock());
      parametersCqlSupport.create(parameterCql, clockSupport.currentCLock());

      break;

    default:
      throw new ModeGestionAPIUnkownException("ParametersSupportFacade/Create/Mode API inconnu");
    }
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
  public final Parameter find(final ParameterType parameterName,
                              final ParameterRowType rowKey) throws ParameterNotFoundException {


    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return parametersSupport.find(parameterName, rowKey);

    case MODE_API.DATASTAX:

      return parametersCqlSupport.find(parameterName, rowKey);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return parametersSupport.find(parameterName, rowKey);

    case MODE_API.DUAL_MODE_READ_CQL:
      return parametersCqlSupport.find(parameterName, rowKey);

    default:
      throw new ModeGestionAPIUnkownException("ParametersSupportFacade/find/Mode API inconnu");
    }
  }

}
