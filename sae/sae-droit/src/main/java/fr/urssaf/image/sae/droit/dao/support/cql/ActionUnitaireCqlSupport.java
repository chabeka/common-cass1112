/**
 *AC75095351
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.droit.dao.cql.IActionUnitaireDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

/**
 * Support de la classe DAO {@link IActionUnitaireDaoCql}
 */
@Service
public class ActionUnitaireCqlSupport {

  @Autowired
  IActionUnitaireDaoCql actionunitairedaocql;

  public ActionUnitaireCqlSupport(final IActionUnitaireDaoCql actionunitairedaocql) {
    this.actionunitairedaocql = actionunitairedaocql;
  }
  /**
   * Création d'une action unitaire
   *
   * @param action
   *          unitaire
   *          action unitaire à créer
   */
  public void create(final ActionUnitaire actionUnitaire) {
    saveOrUpdate(actionUnitaire);
  }

  /**
   * Méthode de suppression d'une Action Unitaire
   *
   * @param code
   *          identifiant de la action unitaire
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    actionunitairedaocql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de Action Unitaire en
   * fonction du code fourni
   *
   * @param code
   *          code de l'action unitaire
   * @return l'enregistrement de l'action unitaire correspondante
   */
  public final ActionUnitaire find(final String code) {

    return findById(code);

  }

  /**
   * * Recherche et retourne l'enregistrement de Action Unitaire en
   * fonction du code fourni en utilisant le mapper
   * 
   * @param code
   * @return l'enregistrement de l'action unitaire correspondante
   */
  public ActionUnitaire findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return actionunitairedaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * Sauvegarde d'une action unitaire
   * 
   * @param actionUnitaire
   */
  private void saveOrUpdate(final ActionUnitaire actionUnitaire) {
    Assert.notNull(actionUnitaire, "l'objet actionUnitaire ne peut être null");
    Assert.notNull(actionUnitaire.getCode(), "le code ne peut être null");
    actionunitairedaocql.saveWithMapper(actionUnitaire);
  }

  /**
   * Retourne la liste de toutes les actions unitaires
   */
  public List<ActionUnitaire> findAll() {
    final Iterator<ActionUnitaire> it = actionunitairedaocql.findAllWithMapper();
    final List<ActionUnitaire> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * Retourne la liste de toutes les actions unitaires
   */
  public List<ActionUnitaire> findAll(final int max) {
    final int i = 0;
    final Iterator<ActionUnitaire> it = actionunitairedaocql.findAllWithMapper();
    final List<ActionUnitaire> list = new ArrayList<>();
    while (it.hasNext() && i < max) {
      list.add(it.next());
    }
    return list;
  }
}
