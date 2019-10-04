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

  /*
   * public static ParameterCql convertParameterToParameterCql(final ParameterRowType typeParameters, final ParameterThrift parameter) {
   * final ParameterCql parameterCql = new ParameterCql();
   * parameterCql.setTypeParameters(typeParameters);
   * if (parameter.getName() != null) {
   * parameterCql.setName(parameter.getName());
   * }
   * parameterCql.setValue(parameter.getValue());
   * return parameterCql;
   * }
   */

  public static ParameterCql convertParameterToParameterCql(final ParameterRowType typeParameters, final Parameter parameter) {
    final ParameterCql parameterCql = new ParameterCql();

    parameterCql.setTypeParameters(typeParameters);
    if (parameter.getName() != null) {
      parameterCql.setName(parameter.getName());
    }
    parameterCql.setValue(parameter.getValue());
    return parameterCql;
  }

  /*
   * public static ParameterThrift convertParameterCqlToParameterThrift(final ParameterCql parameterCql) {
   * final ParameterThrift parameter;
   * parameter = new ParameterThrift(parameterCql.getName(), parameterCql.getValue(), parameterCql.getTypeParameters());
   * return parameter;
   * }
   */

  /*
   * public static ParameterCql convertParameterThriftToParameterCql(final ParameterThrift parameterThrift) {
   * final ParameterCql parameterCql = new ParameterCql();
   * parameterCql.setTypeParameters(parameterThrift.getParameterRowType());
   * parameterCql.setName(parameterThrift.getName());
   * parameterCql.setValue(parameterThrift.getValue());
   * return parameterCql;
   * }
   */

  /*
   * public static ParameterThrift convertParameterToParameterThrift(final Parameter parameter, final ParameterRowType parameterRowType) {
   * final ParameterThrift parameterThrift;
   * parameterThrift = new ParameterThrift(parameter.getName(), parameter.getValue(), parameterRowType);
   * return parameterThrift;
   * }
   */

  public static Parameter convertParameterCqlToParameter(final ParameterCql parameterCql) {
    Parameter parameter = null;

    parameter = new Parameter(parameterCql.getName(), parameterCql.getValue());


    return parameter;
  }

  /*
   * public static List<ParameterThrift> convertListParametersCqlToListParametersThrift(final List<ParameterCql> listParameterCql) {
   * final List<ParameterThrift> listParameter = new ArrayList<>();
   * for (final ParameterCql parameterCql : listParameterCql) {
   * listParameter.add(convertParameterCqlToParameterThrift(parameterCql));
   * }
   * return listParameter;
   * }
   */

  /*
   * public static List<ParameterThrift> convertListParametersToListParametersThrift(final List<Parameter> listParameter,
   * final ParameterRowType parameterRowType) {
   * final List<ParameterThrift> listParameterThrift = new ArrayList<>();
   * for (final Parameter parameter : listParameter) {
   * listParameterThrift.add(convertParameterToParameterThrift(parameter, parameterRowType));
   * }
   * return listParameterThrift;
   * }
   */

  public static List<Parameter> convertListParametersCqlToListParameters(final List<ParameterCql> listParameterCql) {
    final List<Parameter> listParameter = new ArrayList<>();
    for (final ParameterCql parameterCql : listParameterCql) {
      listParameter.add(convertParameterCqlToParameter(parameterCql));
    }
    return listParameter;
  }

  /*
   * public static List<ParameterCql> convertListParametersThriftToListParametersCql(final List<ParameterThrift> listParameterThrift) {
   * final List<ParameterCql> listParameterCql = new ArrayList<>();
   * if (listParameterThrift != null) {
   * for (final ParameterThrift parameterThrift : listParameterThrift) {
   * listParameterCql.add(convertParameterThriftToParameterCql(parameterThrift));
   * }
   * }
   * return listParameterCql;
   * }
   */

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
