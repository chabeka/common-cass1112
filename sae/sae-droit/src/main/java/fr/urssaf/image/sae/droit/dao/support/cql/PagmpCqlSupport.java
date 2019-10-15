/**
 *
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.droit.dao.cql.IPagmpDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

/**
 * Support de la classe DAO {@link IPagmpDaoCql}
 */
@Service
public class PagmpCqlSupport {


  @Autowired
  IPagmpDaoCql pagmpdaocql;

  public PagmpCqlSupport(final IPagmpDaoCql pagmpdaocql) {
    this.pagmpdaocql = pagmpdaocql;
  }
  /**
   * Création d'une pagmp
   *
   * @param pagmp
   *          pagmp à créer
   */
  public void create(final Pagmp pagmp) {
    saveOrUpdate(pagmp);
  }

  /**
   * Méthode de suppression d'une Pagmp
   *
   * @param code
   *          identifiant de la pagmp
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    pagmpdaocql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de Pagmp en
   * fonction du code fourni
   *
   * @param code
   *          code du pagmp
   * @return l'enregistrement du pagmp correspondante
   */

  public final Pagmp find(final String code) {
    return findById(code);
  }

  /**
   * {@inheritDoc}
   */
  public Pagmp findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return pagmpdaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * @param pagmp
   */
  private void saveOrUpdate(final Pagmp pagmp) {
    Assert.notNull(pagmp, "l'objet pagmp ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "";


    if (isValidCode) {

      // recuperation de l'objet ayant le meme code dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<Pagmp> pagmpOpt = pagmpdaocql.findWithMapperById(pagmp.getCode());
      if (pagmpOpt.isPresent()) {
        final Pagmp pagmFromBD = pagmpOpt.get();

        pagmpdaocql.saveWithMapper(pagmFromBD);
      } else {
        pagmpdaocql.saveWithMapper(pagmp);
      }
    } else {
      throw new DroitRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

  }

  /**
   * Retourne la liste de tous les Pagmp
   * 
   * @return liste Pagmp
   */
  public List<Pagmp> findAll() {
    final Iterator<Pagmp> it = pagmpdaocql.findAllWithMapper();
    final List<Pagmp> list = new ArrayList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * Retourne la liste de tous les Pagmp
   * 
   * @return liste Pagmp
   */
  public List<Pagmp> findAll(final int max) {
    int i = 0;
    final Iterator<Pagmp> it = pagmpdaocql.findAllWithMapper();
    final List<Pagmp> list = new ArrayList<>();
    while (it.hasNext() && i < max) {
      list.add(it.next());
      i++;
    }
    return list;
  }
}
