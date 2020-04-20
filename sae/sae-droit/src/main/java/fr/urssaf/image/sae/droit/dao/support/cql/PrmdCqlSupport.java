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

import fr.urssaf.image.sae.droit.dao.cql.IPrmdDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Prmd;

/**
 * Support de la classe DAO {@link IPrmdDaoCql}
 */
@Service
public class PrmdCqlSupport {

  @Autowired
  IPrmdDaoCql prmddaocql;

  public PrmdCqlSupport(final IPrmdDaoCql prmddaocql) {
    this.prmddaocql = prmddaocql;
  }
  /**
   * Création d'une prmd
   *
   * @param prmd
   *          prmd à créer
   */
  public void create(final Prmd prmd) {
    saveOrUpdate(prmd);
  }

  /**
   * Méthode de suppression d'une Prmd
   *
   * @param code
   *          identifiant de la prmd
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    prmddaocql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de Prmd en
   * fonction du code fourni
   *
   * @param code
   *          code du prmd
   * @return l'enregistrement du prmd correspondante
   */

  public final Prmd find(final String code) {
    return findById(code);
  }

  /**
   * {@inheritDoc}
   */
  public Prmd findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return prmddaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * Enregistre le prmd
   * 
   * @param prmd
   */
  private void saveOrUpdate(final Prmd prmd) {
    Assert.notNull(prmd, "l'objet prmd ne peut etre null");
    Assert.notNull(prmd.getCode(), "le code ne peut etre null");


        prmddaocql.saveWithMapper(prmd);

  }

  /**
   * Retourne la liste de tous les Prmd
   * 
   * @return liste des Prmd
   */
  public List<Prmd> findAll() {
    final Iterator<Prmd> it = prmddaocql.findAllWithMapper();
    final List<Prmd> list = new ArrayList<>();
    while (it.hasNext()) {

      list.add(it.next());

    }
    return list;
  }

  /**
   * Retourne la liste de tous les Prmd
   * 
   * @return liste des Prmd
   */
  public List<Prmd> findAll(final int max) {
    int i = 0;
    final Iterator<Prmd> it = prmddaocql.findAllWithMapper();
    final List<Prmd> list = new ArrayList<>();
    while (it.hasNext() && i < max) {
      list.add(it.next());
      i++;
    }
    return list;
  }
}
