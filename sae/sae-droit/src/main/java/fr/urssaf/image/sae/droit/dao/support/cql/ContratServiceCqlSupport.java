/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.dao.support.cql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.droit.dao.ContratServiceDao;
import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;

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
    Assert.notNull(contratService.getCodeClient(), "le code client ne peut etre null");
        contratservicedaocql.saveWithMapper(contratService);
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
