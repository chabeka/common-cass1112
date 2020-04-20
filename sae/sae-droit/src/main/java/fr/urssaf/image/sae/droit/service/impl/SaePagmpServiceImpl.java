/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmpSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PrmdSupportFacade;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmpNotFoundException;
import fr.urssaf.image.sae.droit.service.SaePagmpService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaePagmpService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * (Thrift et Cql)
 */
@Component
public class SaePagmpServiceImpl implements SaePagmpService {



  private static final String CHECK = "checkPagmpInexistant";

  private static final String TRC_CREATE = "createPagmp()";
  private static final String TRC_MODIFIER = "modifierPagmp()";

  private static final String PAGMP = "Le PAGMp ";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SaePagmpServiceImpl.class);

  private static final String PREFIXE_PAGMP = "/DroitPagmp/";

  private final CuratorFramework curatorClient;

  private final PagmpSupportFacade pagmpSupportFacade;

  private final PrmdSupportFacade prmdSupportFacade;



  /**
   * constructeur
   * 
   * @param pagmp
   *          {@link PagmpSupportFacade}
   * @param prmd
   *          {@link PrmdSupportFacade}
   * @param curator
   *          {@link CuratorFramework}
   */   
  @Autowired
  public SaePagmpServiceImpl(final PagmpSupportFacade pagmpSupportFacade, final PrmdSupportFacade prmdSupportFacade,
                             final CuratorFramework curator) {
    this.pagmpSupportFacade = pagmpSupportFacade;
    this.prmdSupportFacade = prmdSupportFacade;
    curatorClient = curator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void createPagmp(final Pagmp pagmp) {

    LOGGER.debug("{} - Début de la création du pagmp {}", TRC_CREATE, pagmp.getCode());

    final String resourceName = PREFIXE_PAGMP + pagmp.getCode();

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
                                                            resourceName);
    try {
      ZookeeperUtils.acquire(mutex, resourceName);

      LOGGER.debug("{} - Vérification que le pagmp {} n'existe pas", TRC_CREATE, pagmp.getCode());
      checkPagmpInexistant(pagmp);
      LOGGER.debug("{} - Vérification que le prmd rattaché au pagmp {} existe", TRC_CREATE, pagmp.getCode());
      checkPrmdExiste(pagmp);
      pagmpSupportFacade.create(pagmp);
      checkLock(mutex, pagmp);

      LOGGER.debug("{} - Fin de la création du pagmp {}", TRC_CREATE, pagmp.getCode());

    } finally {
      mutex.release();
    }
  }


  /**
   * {@inheritDoc}
   * @throws PagmpNotFoundException 
   */
  @Override
  public final void modifierPagmp(final Pagmp pagmp) throws PagmpNotFoundException {

    LOGGER.debug("{} - Début de la modification du pagmp {}", TRC_MODIFIER, pagmp.getCode());
    final String resourceName = PREFIXE_PAGMP + pagmp.getCode();

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
                                                            resourceName);
    try {
      ZookeeperUtils.acquire(mutex, resourceName);

      LOGGER.debug("{} - Vérification que le pagmp {} existe bien", TRC_MODIFIER, pagmp.getCode());
      checkPagmpExistant(pagmp);
      LOGGER.debug("{} - Vérification que le prmd rattaché au pagmp {} existe", TRC_MODIFIER, pagmp.getCode());
      checkPrmdExiste(pagmp);

      pagmpSupportFacade.create(pagmp);

      checkLock(mutex, pagmp);

      LOGGER.debug("{} - Fin de la modification du pagmp {}", TRC_MODIFIER, pagmp.getCode());

    } finally {
      mutex.release();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPagmpExiste(final Pagmp pagmp) {
    final Pagmp storedPagmp = findPagmpByCode(pagmp.getCode());

    return storedPagmp!=null;
  }

  /**
   * Vérifie que le pagmp n'existe pas. S'il existe, une exception est levée
   * 
   * @param pagmp
   *           le pagmp à créer
   */
  private void checkPagmpInexistant(final Pagmp pagmp) {


    Pagmp storedPagmp = null;
    storedPagmp = findPagmpByCode(pagmp.getCode());
    if (storedPagmp != null) {
      LOGGER
      .warn(
            "{} - Le PAGMp {} existe déjà dans la famille de colonne DroitPagmp",
            CHECK, pagmp.getCode());
      throw new PagmpReferenceException(PAGMP + pagmp.getCode()
      + " existe déjà dans la famille de colonne DroitPagmp");
    }

  }




  /**
   * Vérifie que le pagmp existe déjà. S'il n'existe pas, une exception est levée
   * 
   * @param pagmp
   *           le pagmp à modifier
   * @throws PagmpNotFoundException 
   */
  private void checkPagmpExistant(final Pagmp pagmp) throws PagmpNotFoundException {

    final Pagmp storedPagmp = findPagmpByCode(pagmp.getCode());
    if (storedPagmp == null) {
      LOGGER
      .warn(
            "{} - Le PAGMp {} n'existe pas dans la famille de colonne DroitPagmp",
            CHECK, pagmp.getCode());
      throw new PagmpNotFoundException(PAGMP + pagmp.getCode()
      + " n'existe pas dans la famille de colonne DroitPagmp");
    }

  }

  /**
   * Vérifie que le Prmd existe. Si ce n'est pas le cas, exception
   * {@link PrmdReferenceException} levée
   * 
   * @param pagmp
   *           le pagmp
   */
  private void checkPrmdExiste(final Pagmp pagmp) {
    // final Prmd prmd = prmdSupport.find(pagmp.getPrmd());

    Prmd prmdTemp = null;
    prmdTemp = findPrmdByCode(pagmp.getPrmd());

    if (prmdTemp == null) {
      throw new PrmdReferenceException("Le PRMD " + pagmp.getPrmd()
      + " n'a pas été trouvé "
      + "dans la famille de colonnes DroitPrmd");
    }

  }



  private void checkLock(final ZookeeperMutex mutex, final Pagmp pagmp) {

    if (!ZookeeperUtils.isLock(mutex)) {

      final Pagmp storedPagmp = pagmpSupportFacade.find(pagmp.getCode());

      if (storedPagmp == null) {
        throw new PagmpReferenceException(PAGMP + pagmp.getCode()
        + " n'a pas été créé");
      }

      if (!storedPagmp.equals(pagmp)) {
        throw new DroitRuntimeException(PAGMP + pagmp.getCode()
        + " a déjà été créé");
      }
    }
  }

  /**
   * Recherche pagm par code
   * 
   * @param code
   * @param storedPagmp
   * @return Pagmp
   */
  private Pagmp findPagmpByCode(final String code) {

    return pagmpSupportFacade.find(code);
  }

  /**
   * Recherche prmd par code
   * 
   * @param prmd
   * @return Prmd
   */
  private Prmd findPrmdByCode(final String prmd) {

    return prmdSupportFacade.find(prmd);
  }
}
