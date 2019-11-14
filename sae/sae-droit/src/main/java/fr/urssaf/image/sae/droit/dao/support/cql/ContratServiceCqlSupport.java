/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.droit.dao.ContratServiceDao;
import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;

/**
 * Classe de support de la classe {@link ContratServiceDao}
 * 
 */
@Component
public class ContratServiceCqlSupport {

  @Autowired
  IContratServiceDaoCql contratservicedaocql;

  public ContratServiceCqlSupport(final IContratServiceDaoCql contratservicedaocql) {
    this.contratservicedaocql = contratservicedaocql;
  }
  /**
   * Création d'un contrat de service
   *
   * @param contratService
   *          contratService à créer
   */

  public final void create(final ServiceContract contratService) {

    saveOrUpdate(contratService);

  }

  /**
   * Méthode de suppression d'un contrat de service
   *
   * @param code
   *          identifiant de la trace
   */
  public void delete(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    contratservicedaocql.deleteById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de ServiceContract en
   * fonction du code fourni
   *
   * @param code
   *          code de ServiceContract
   * @return l'enregistrement de ServiceContract correspondante
   */
  public final ServiceContract find(final String code) {

    return findById(code);

  }

  /**
   * Recherche et retourne l'enregistrement de ServiceContract en
   * fonction du code fourni avec le mapper
   *
   * @param code
   *          code de ServiceContract
   * @return l'enregistrement de ServiceContract correspondante
   */
  public ServiceContract findById(final String code) {
    Assert.notNull(code, "le code ne peut etre null");
    return contratservicedaocql.findWithMapperById(code).orElse(null);

  }

  /**
   * @param trace
   */
  private void saveOrUpdate(final ServiceContract contratService) {
    Assert.notNull(contratService, "l'objet contratService ne peut etre null");

    final boolean isValidCode = true;
    final String errorKey = "";

    if (isValidCode) {

      // recuperation de l'objet ayant le meme code dans la base cassandra. S'il en existe un, on l'update
      // sinon on en cré un nouveau
      final Optional<ServiceContract> contratServiceOpt = contratservicedaocql.findWithMapperById(contratService.getCodeClient());
      if (contratServiceOpt.isPresent()) {
        final ServiceContract contratServiceFromBD = contratServiceOpt.get();

        contratservicedaocql.saveWithMapper(contratServiceFromBD);
      } else {
        contratservicedaocql.saveWithMapper(contratService);
      }
    } else {
      throw new DroitRuntimeException(
                                      "Impossible de créer l'enregistrement demandé. " + "La clé "
                                          + errorKey + " n'est pas supportée");
    }

  }
  /**
   * {@inheritDoc}
   */
  public List<ServiceContract> findAll() {
    final Iterator<ServiceContract> it = contratservicedaocql.findAllWithMapper();
    final List<ServiceContract> list = new ArrayList<>();
    while (it.hasNext()) {

      list.add(it.next());

    }
    return list;
  }

  /**
   * {@inheritDoc}
   */
  public List<ServiceContract> findAll(final int max) {
    // A CORRIGER
    int i = 0;
    final Iterator<ServiceContract> it = contratservicedaocql.findAllWithMapper();
    final List<ServiceContract> list = new ArrayList<>();
    while (it.hasNext() && i < max) {
      list.add(it.next());
      i++;
    }
    return list;
  }

}
