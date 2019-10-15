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

import fr.urssaf.image.sae.droit.dao.cql.IPagmaDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

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

    final boolean isValidCode = true;
    final String errorKey = "";


    if (isValidCode) {

      // recuperation de l'objet ayant le meme code dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<Pagma> pagmaOpt = pagmadaocql.findWithMapperById(pagma.getCode());
      if (pagmaOpt.isPresent()) {
        final Pagma pagmFromBD = pagmaOpt.get();

        pagmadaocql.saveWithMapper(pagmFromBD);
      } else {
        pagmadaocql.saveWithMapper(pagma);
      }
    } else {
      throw new DroitRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

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
