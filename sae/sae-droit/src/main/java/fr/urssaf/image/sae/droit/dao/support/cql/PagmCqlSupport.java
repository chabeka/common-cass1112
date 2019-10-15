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

import fr.urssaf.image.sae.droit.dao.cql.IPagmDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.utils.PagmUtils;

/**
 * Support de la classe DAO {@link IPagmDaoCql}
 */
@Service
public class PagmCqlSupport {

  @Autowired
  IPagmDaoCql pagmdaocql;

  public PagmCqlSupport(final IPagmDaoCql pagmdaocql) {
    this.pagmdaocql = pagmdaocql;
  }
  /**
   * Création d'une pagm
   *
   * @param pagmCql
   *          pagmCql à créer
   */
  public void create(final PagmCql pagmCql) {
    saveOrUpdate(pagmCql);
  }

  /**
   * Méthode de suppression d'une Pagm
   *
   * @param code
   *          identifiant de la pagmCql
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    pagmdaocql.deleteById(code);

  }

  /**
   * Méthode de suppression d'une Pagm
   *
   * @param entity
   *          Pagm
   *          identifiant de la pagmCql
   */
  public void delete(final PagmCql pagmCql) {
    Assert.notNull(pagmCql, "le code ne peut etre null");
    pagmdaocql.delete(pagmCql);

  }

  /**
   * Recherche et retourne l'enregistrement de Pagm en
   * fonction du code fourni
   *
   * @param code
   *          code du pagm
   * @return l'enregistrement du pagm correspondante
   */

  public final PagmCql find(final String code) {
    return findById(code);
  }

  /**
   * {@inheritDoc}
   */
  public List<Pagm> findByIdClient(final String idClient) {
    Assert.notNull(idClient, "le idClient ne peut etre null");
    final Iterator<PagmCql> iterator = pagmdaocql.IterableFindById(idClient);
    final List<PagmCql> pagmsCql = new ArrayList<>();
    iterator.forEachRemaining(pagmsCql::add);
    final List<Pagm> pagms = PagmUtils.convertListPagmCqlToListPagm(pagmsCql);
    return pagms;

  }

  /**
   * {@inheritDoc}
   */
  public PagmCql findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return pagmdaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * Enregistre le pagmCql
   * 
   * @param pagm
   */
  private void saveOrUpdate(final PagmCql pagmCql) {
    Assert.notNull(pagmCql, "l'objet pagm ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "";


    if (isValidCode) {

      // recuperation de l'objet ayant le meme code dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<PagmCql> pagmOpt = pagmdaocql.findWithMapperById(pagmCql.getCode());
      if (pagmOpt.isPresent()) {
        final PagmCql pagmFromBD = pagmOpt.get();

        pagmdaocql.saveWithMapper(pagmFromBD);
      } else {
        pagmdaocql.saveWithMapper(pagmCql);
      }
    } else {
      throw new DroitRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

  }

  /**
   * Retoune la liste de tous les Pagm
   * 
   * @return liste des PagmCql
   */
  public List<PagmCql> findAll() {
    final Iterator<PagmCql> it = pagmdaocql.findAllWithMapper();
    final List<PagmCql> list = new ArrayList<>();
    while (it.hasNext()) {

      list.add(it.next());

    }
    return list;
  }
}
