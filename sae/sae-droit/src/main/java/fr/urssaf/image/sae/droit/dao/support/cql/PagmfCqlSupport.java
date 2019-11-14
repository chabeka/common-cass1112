/**
 *AC75095351
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.droit.dao.cql.IPagmfDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

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

    final boolean isValidCode = true;
    final String errorKey = "";

    if (isValidCode) {

      // recuperation de l'objet ayant le meme code dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<Pagmf> pagmfOpt = pagmfdaocql.findWithMapperById(pagmf.getCodePagmf());
      if (pagmfOpt.isPresent()) {
        final Pagmf pagmFromBD = pagmfOpt.get();

        pagmfdaocql.saveWithMapper(pagmFromBD);
      } else {
        pagmfdaocql.saveWithMapper(pagmf);
      }
    } else {
      throw new DroitRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

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
