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

import fr.urssaf.image.sae.droit.dao.cql.IPagmaDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagma;

/**
 * Support de la classe DAO {@link IPagmaDaoCql}
 */
@Service
public class PagmaCqlSupport {


  @Autowired
  IPagmaDaoCql pagmadaocql;

  public PagmaCqlSupport(final IPagmaDaoCql pagmadaocql) {
    this.pagmadaocql = pagmadaocql;
  }
  /**
   * Création d'une pagma
   *
   * @param pagma
   *          pagma à créer
   */
  public void create(final Pagma pagma) {
    saveOrUpdate(pagma);
  }

  /**
   * Méthode de suppression d'une Pagma
   *
   * @param code
   *          identifiant de la pagma
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    pagmadaocql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de Pagma en
   * fonction du code fourni
   *
   * @param code
   *          code du pagma
   * @return l'enregistrement du pagma correspondante
   */

  public final Pagma find(final String code) {
    return findById(code);

  }

  /**
   * {@inheritDoc}
   */
  public Pagma findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return pagmadaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * @param pagma
   */
  private void saveOrUpdate(final Pagma pagma) {
    Assert.notNull(pagma, "l'objet pagma ne peut etre null");
    Assert.notNull(pagma.getCode(), "le code ne peut etre null");

        pagmadaocql.saveWithMapper(pagma);


  }

  /**
   * Retourne la liste de tous les Pagma
   * 
   * @return liste des Pagma
   */
  public List<Pagma> findAll() {
    final Iterator<Pagma> it = pagmadaocql.findAllWithMapper();
    final List<Pagma> list = new ArrayList<>();
    while (it.hasNext()) {

      list.add(it.next());

    }
    return list;
  }
}
