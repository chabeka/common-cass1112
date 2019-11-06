/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.commons.utils;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;

/**
 * (AC75095351) Conversion ParametreThrift <-> ParametreCql
 */
public class ParametersUtils {

  public static ParameterCql convertParameterToParameterCql(final ParameterRowType typeParameters, final Parameter parameter) {
    final ParameterCql parameterCql = new ParameterCql();

    parameterCql.setTypeParameters(typeParameters);
    if (parameter.getName() != null) {
      parameterCql.setName(parameter.getName());
    }
    parameterCql.setValue(parameter.getValue());
    return parameterCql;
  }

  public static Parameter convertParameterCqlToParameter(final ParameterCql parameterCql) {
    Parameter parameter = null;

    parameter = new Parameter(parameterCql.getName(), parameterCql.getValue());


    return parameter;
  }


  public static List<Parameter> convertListParametersCqlToListParameters(final List<ParameterCql> listParameterCql) {
    final List<Parameter> listParameter = new ArrayList<>();
    for (final ParameterCql parameterCql : listParameterCql) {
      listParameter.add(convertParameterCqlToParameter(parameterCql));
    }
    return listParameter;
  }


  public static List<ParameterCql> convertListParametersToListParametersCql(final List<Parameter> listParameter, final ParameterRowType parameterRowType) {
    final List<ParameterCql> listParameterCql = new ArrayList<>();
    if (listParameter != null) {
      for (final Parameter parameter : listParameter) {
        listParameterCql.add(convertParameterToParameterCql(parameterRowType, parameter));
      }
    }
    return listParameterCql;
  }
}
