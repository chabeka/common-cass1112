/**
 * 
 */
package fr.urssaf.image.sae.commons.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.dao.cql.IParametersDaoCql;
import fr.urssaf.image.sae.commons.exception.ParameterRuntimeException;
import fr.urssaf.image.sae.commons.utils.ParametersUtils;


/**
 * Classe permettant de réaliser des opérations sur les paramètres
 * 
 */
@Component
public class ParametersCqlSupport {

  @Autowired
  IParametersDaoCql parametersDaoCql;


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
  public final void create(final ParameterCql parametersCql) {

    saveOrUpdate(parametersCql);
  }

  /**
   * Méthode de suppression d'une Pagm
   *
   * @param code
   *          identifiant de la pagmCql
   */
  public void delete(final ParameterRowType parametersCql) {
    Assert.notNull(parametersCql, "le code ne peut etre null");
    parametersDaoCql.deleteById(parametersCql.getValue());

  }


  /**
   * {@inheritDoc}
   *//*
   * public List<ParameterThrift> findByTypeRowParameterThrift(final ParameterRowType typeParameter) {
   * Assert.notNull(typeParameter, "le typeParameter ne peut etre null");
   * final Iterator<ParameterCql> iterator = parametersDaoCql.IterableFindById(typeParameter.getValue());
   * final List<ParameterCql> pagmsCql = new ArrayList<>();
   * iterator.forEachRemaining(pagmsCql::add);
   * final List<ParameterThrift> parameters = ParametersUtils.convertListParametersCqlToListParametersThrift(pagmsCql);
   * return parameters;
   * }
   */

  /**
   * {@inheritDoc}
   */
  public List<Parameter> findByTypeRowParameter(final ParameterRowType typeParameter) {
    Assert.notNull(typeParameter, "le typeParameter ne peut etre null");
    final Iterator<ParameterCql> iterator = parametersDaoCql.IterableFindById(typeParameter.getValue());
    final List<ParameterCql> pagmsCql = new ArrayList<>();
    iterator.forEachRemaining(pagmsCql::add);

    final List<Parameter> parameters = ParametersUtils.convertListParametersCqlToListParameters(pagmsCql);
    return parameters;

  }

  /**
   * {@inheritDoc}
   */
  public Parameter find(final ParameterType typeParameter, final ParameterRowType typeRowParameter) {

    Assert.notNull(typeParameter, "le typeParameter ne peut etre null");
    Assert.notNull(typeRowParameter, "le typeRowParameter ne peut etre null");
    final Iterator<ParameterCql> iterator = parametersDaoCql.IterableFindById(typeRowParameter.getValue());
    Parameter parameter = null;
    while (iterator.hasNext()) {
      final ParameterCql parameterCql = iterator.next();
      if (parameterCql != null && parameterCql.getName().equals(typeParameter)) {
        parameter = new Parameter(typeParameter, parameterCql.getValue());
        break;
      }
    }
    // Attention null exception à gérer
    return parameter;

  }

  /**
   * Enregistre le parametersCql
   * 
   * @param parametersCql
   */
  private void saveOrUpdate(final ParameterCql parametersCql) {
    Assert.notNull(parametersCql, "l'objet parameters ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "";

    if (isValidCode) {

      // recuperation de l'objet ayant le meme code dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau attention en cql il faut la même rowkey et le même type
      final Optional<ParameterCql> parametersOpt = parametersDaoCql.findWithMapperById(parametersCql.getTypeParameters().getValue());
      if (parametersOpt.isPresent() && parametersOpt.get().getName().equals(parametersCql.getName())) {
        final ParameterCql parametersFromBD = parametersOpt.get();
        parametersDaoCql.saveWithMapper(parametersFromBD);
      } else {
        parametersDaoCql.saveWithMapper(parametersCql);
      }
    } else {
      throw new ParameterRuntimeException(
                                          "Impossible de créer l'enregistrement demandé. " + "La clé "
                                              + errorKey + " n'est pas supportée");
    }

  }

  /**
   * Retoune la liste de tous les Parameters
   * 
   * @return liste des ParameterCql
   */
  public List<ParameterCql> findAll() {
    final Iterator<ParameterCql> it = parametersDaoCql.findAllWithMapper();
    final List<ParameterCql> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

}
