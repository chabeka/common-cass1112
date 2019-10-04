/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.dao.support.facade.PrmdSupportFacade;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.service.SaePrmdService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaePrmdService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * (Thrift et Cql)
 */
@Component
public class SaePrmdServiceFacadeImpl implements SaePrmdService {


  private static final String CHECK = "checkPrmdInexistant";

  private static final String PRMD = "Le PRMD ";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SaePrmdServiceFacadeImpl.class);

  private static final String TRC_CREATE = "createPrmd()";
  private static final String TRC_EXISTS = "prmdExists()";

  private static final String TRC_FIND = "getPrmd()";

  private static final String PREFIXE_PRMD = "/DroitPrmd/";


  private final CuratorFramework curatorClient;

  private final PrmdSupportFacade prmdSupportFacade;

  /**
   * constructeur
   * 
   * @param prmd
   *           {@link PrmdSupport}
   * @param curator
   *           {@link CuratorFramework}
   */
  @Autowired
  public SaePrmdServiceFacadeImpl(final PrmdSupportFacade prmdSupportFacade,
                                  final CuratorFramework curator) {
    this.prmdSupportFacade = prmdSupportFacade;
    curatorClient = curator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void createPrmd(final Prmd prmd) {

    final String resourceName = PREFIXE_PRMD + prmd.getCode();

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
                                                            resourceName);
    try {
      LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
      ZookeeperUtils.acquire(mutex, resourceName);

      LOGGER.debug("{} - Vérification PRMD inexistant", TRC_CREATE);
      checkPrmdInexistant(prmd);

      LOGGER.debug("{} - Création PRMD", TRC_CREATE);
      createOrModifyPrmd(prmd);

      checkLock(mutex, prmd);

    } finally {
      mutex.release();
    }
  }

  @Override
  public final void modifyPrmd(final Prmd prmd) {
    final String resourceName = PREFIXE_PRMD + prmd.getCode();

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
                                                            resourceName);
    try {
      LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
      ZookeeperUtils.acquire(mutex, resourceName);

      LOGGER.debug("{} - Vérification PRMD existant", TRC_CREATE);
      checkPrmdExistant(prmd);

      LOGGER.debug("{} - Création PRMD", TRC_CREATE);
      createOrModifyPrmd(prmd);

      checkLock(mutex, prmd);

    } finally {
      mutex.release();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean prmdExists(final String code) {

    LOGGER.debug("{} - Début de recherche du PRMD", TRC_EXISTS);
    boolean exists = false;
    final Prmd storedPrmd = findPrmdByCode(code);

    if (storedPrmd != null) {
      exists = true;
    }

    LOGGER.debug("{} - Fin de recherche du PRMD", TRC_EXISTS);

    return exists;
  }

  /**
   * Vérifie si le PRMD existe. Si c'est le cas renvoie une exception
   * {@link DroitRuntimeException}
   * 
   * @param prmd
   *           le PRMD a créer
   */
  private void checkPrmdInexistant(final Prmd prmd) {

    if (prmdExists(prmd.getCode())) {

      LOGGER.warn("{} - Le PRMD {} existe déjà dans la "
          + "famille de colonnes DroitPRMD", CHECK, prmd.getCode());
      throw new DroitRuntimeException(PRMD + prmd.getCode()
      + " existe déjà dans la " + "famille de colonnes DroitPRMD");
    }

  }

  /**
   * Vérifie si le PRMD n'existe pas. Si c'est le cas renvoie une exception
   * {@link DroitRuntimeException}
   * 
   * @param prmd
   *           le PRMD a modifier
   */
  private void checkPrmdExistant(final Prmd prmd) {

    if (!prmdExists(prmd.getCode())) {

      LOGGER.warn("{} - Le PRMD à modifier {} n'existe pas dans la "
          + "famille de colonnes DroitPRMD", CHECK, prmd.getCode());
      throw new DroitRuntimeException(PRMD + prmd.getCode()
      + " à modifier n'existe pas dans la "
      + "famille de colonnes DroitPRMD");
    }

  }

  private void checkLock(final ZookeeperMutex mutex, final Prmd prmd) {

    if (!ZookeeperUtils.isLock(mutex)) {

      final Prmd storedPrmd = findPrmdByCode(prmd.getCode());

      if (storedPrmd == null) {
        throw new PrmdReferenceException(PRMD + prmd.getCode()
        + " n'a pas été créé");
      }

      if (!storedPrmd.equals(prmd)) {
        throw new DroitRuntimeException(PRMD + prmd.getCode()
        + " a déjà été créé");
      }

    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Prmd getPrmd(final String code) {

    LOGGER.debug("{} - Début de la récupération du PRMD", TRC_FIND);
    final Prmd storedPrmd = findPrmdByCode(code);
    LOGGER.debug("{} - Fin de la récupération du PRMD", TRC_FIND);

    return storedPrmd;
  }

  /**
   * Recherche prmd par code suivant ModeGestionAPI
   * 
   * @param prmd
   * @return Prmd
   */
  private Prmd findPrmdByCode(final String prmd) {

    return prmdSupportFacade.find(prmd);
  }

  /**
   * Création prmd par code suivant ModeGestionAPI
   * 
   * @param prmd
   * @return
   */
  private void createOrModifyPrmd(final Prmd prmd) {
    prmdSupportFacade.create(prmd);
  }

}
