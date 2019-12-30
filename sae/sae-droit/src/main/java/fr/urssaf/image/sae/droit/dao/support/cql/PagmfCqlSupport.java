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

import fr.urssaf.image.sae.droit.dao.cql.IPagmfDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;

/**
 * Support de la classe DAO {@link IPagmfDaoCql}
 */
@Service
public class PagmfCqlSupport {

  @Autowired
  IPagmfDaoCql pagmfdaocql;

  public PagmfCqlSupport(final IPagmfDaoCql pagmfdaocql) {
    this.pagmfdaocql = pagmfdaocql;
  }
  /**
   * Création d'une pagmf
   *
   * @param pagmf
   *          pagmf à créer
   */
  public void create(final Pagmf pagmf) {
    saveOrUpdate(pagmf);
  }

  /**
   * Méthode de suppression d'une Pagmf
   *
   * @param code
   *          identifiant de la pagmf
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    pagmfdaocql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de Pagmf en
   * fonction du code fourni
   *
   * @param code
   *          code du pagmf
   * @return l'enregistrement du pagmf correspondante
   */

  public final Pagmf find(final String code) {
    return findById(code);
  }

  /**
   * @param code
   *          du pagmf
   * @return
   */
  public Pagmf findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return pagmfdaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * enregistre l pagmf
   * 
   * @param pagmf
   */
  private void saveOrUpdate(final Pagmf pagmf) {
    Assert.notNull(pagmf, "l'objet pagmf ne peut etre null");


    pagmfdaocql.saveWithMapper(pagmf);


  }

  /**
   * Retourne lla liste de tous les pagmf
   * 
   * @return liste des Pagmf
   */
  public List<Pagmf> findAll() {
    final Iterator<Pagmf> it = pagmfdaocql.findAllWithMapper();
    final List<Pagmf> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }
}
