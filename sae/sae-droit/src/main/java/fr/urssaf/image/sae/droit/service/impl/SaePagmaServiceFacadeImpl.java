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
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.support.facade.ActionUnitaireSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmaSupportFacade;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmaNotFoundException;
import fr.urssaf.image.sae.droit.service.SaePagmaService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaePagmaService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * (Thrift et Cql)
 */
@Component
public class SaePagmaServiceFacadeImpl implements SaePagmaService {

  private static final String CHECK_NOT_EXISTS = "checkPagmaNotExists";
  private static final String CHECK_EXISTS = "checkPagmaNotExists";
  private static final String TRC_CREATE = "createPagma";
  private static final String TRC_MODIFIER = "modifierPagma";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SaePagmaServiceFacadeImpl.class);

  private static final String PREFIXE_PAGMA = "/DroitPagma/";

  private final CuratorFramework curatorClient;

  private final PagmaSupportFacade pagmaSupportFacade;

  private final ActionUnitaireSupportFacade actionUnitaireSupportFacade;



  /**
   * constructeur
   * 
   * @param action
   *          {@link ActionUnitaireSupportFacade}
   * @param pagma
   *          {@link PagmaSupportFacade}
   * @param curator
   *          {@link CuratorFramework}
   */   
  @Autowired
  public SaePagmaServiceFacadeImpl(final ActionUnitaireSupportFacade actionUnitaireSupportFacade, final PagmaSupportFacade pagmaSupportFacade,
                                   final CuratorFramework curator) {

    this.pagmaSupportFacade = pagmaSupportFacade;
    this.actionUnitaireSupportFacade = actionUnitaireSupportFacade;
    curatorClient = curator;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public final void createPagma(final Pagma pagma) {

    LOGGER.debug("{} - Début de la création du pagma {}", TRC_CREATE, pagma.getCode());
    final String resourceName = PREFIXE_PAGMA + pagma.getCode();

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
                                                            resourceName);
    try {
      ZookeeperUtils.acquire(mutex, resourceName);

      LOGGER.debug("{} - Vérification que le pagma {} n'existe pas", TRC_CREATE, pagma.getCode());
      checkPagmaNotExists(pagma);
      LOGGER.debug("{} - vérification que les actions unitaires rattachées au pagma {} existent", TRC_CREATE, pagma.getCode());
      checkActionsUnitairesExist(pagma);

      pagmaSupportFacade.create(pagma);

      checkLock(mutex, pagma);

      LOGGER.debug("{} - Fin de la création du pagma {}", TRC_CREATE, pagma.getCode());

    } finally {
      mutex.release();
    }

  }


  /**
   * {@inheritDoc}
   * @throws PagmaNotFoundException 
   */
  @Override
  public final void modifierPagma(final Pagma pagma) throws PagmaNotFoundException {
    LOGGER.debug("{} - Début de la modification du pagma {}", TRC_MODIFIER, pagma.getCode());

    final String resourceName = PREFIXE_PAGMA + pagma.getCode();

    final ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
                                                            resourceName);
    try {
      ZookeeperUtils.acquire(mutex, resourceName);

      LOGGER.debug("{} - Vérification que le pagma {} existe", TRC_MODIFIER, pagma.getCode());
      checkPagmaExists(pagma);
      LOGGER.debug("{} - vérification que les actions unitaires rattachées au pagma {} existent", TRC_MODIFIER, pagma.getCode());
      checkActionsUnitairesExist(pagma);
      pagmaSupportFacade.create(pagma);
      checkLock(mutex, pagma);
      LOGGER.debug("{} - Fin de la création du pagma {}", TRC_MODIFIER, pagma.getCode());

    } finally {
      mutex.release();
    }

  }

  /**
   * {@inheritDoc} 
   */
  @Override
  public final boolean isPagmaExiste(final Pagma pagma) {

    final Pagma pagmaTemp = findPagmaByCode(pagma.getCode());

    return pagmaTemp!=null;
  }

  private void checkLock(final ZookeeperMutex mutex, final Pagma pagma) {
    if (!ZookeeperUtils.isLock(mutex)) {

      final String code = pagma.getCode();
      final Pagma storedPagma = findPagmaByCode(code);

      if (storedPagma == null) {
        throw new PagmaReferenceException("le PAGMa " + code
                                          + "n'a pas été créé");
      }
      if (!storedPagma.equals(pagma)) {
        throw new DroitRuntimeException("le PAGMa " + code
                                        + " a déjà été créé");
      }
    }

  }

  /**
   * Vérifie si les actions unitaires existent en base CASSANDRA. Si ce n'est
   * pas le cas soulève une {@link ActionUnitaireReferenceException}
   * 
   * @param pagma
   */
  private void checkActionsUnitairesExist(final Pagma pagma) {


    for (final String action : pagma.getActionUnitaires()) {
      final ActionUnitaire actionUnitaire = actionUnitaireSupportFacade.find(action);
      ;
      if (actionUnitaire == null) {
        throw new ActionUnitaireReferenceException("L'action unitaire "
            + action + " n'a pas été trouvée dans la "
            + "famille de colonne DroitActionUnitaire");
      }
    }

  }

  /**
   * Vérifie si le PAGMa existe en base CASSANDRA. Si c'est le cas soulève une
   * {@link PagmaReferenceException}
   * 
   * @param pagma
   *           la pagma qui doit être créée
   */
  private void checkPagmaNotExists(final Pagma pagma) {


    final Pagma pagmaTemp = findPagmaByCode(pagma.getCode());
    if (pagmaTemp != null) {
      LOGGER
      .warn(
            "{} - Le PAGMa {} existe déjà dans la famille de colonne DroitPagma",
            CHECK_NOT_EXISTS, pagma.getCode());
      throw new PagmaReferenceException("Le PAGMa " + pagma.getCode()
      + " existe déjà dans la famille de colonne DroitPagma");
    }

  }


  /**
   * Vérifie si PAGMa existe bien en base CASSANDRA. Si ce n'est pas le cas soulève une
   * {@link PagmaReferenceException}
   * 
   * @param pagma
   *           la pagma qui doit être modifié
   * @throws PagmaNotFoundException 
   */
  private void checkPagmaExists(final Pagma pagma) throws PagmaNotFoundException {

    final Pagma pagmaTemp = findPagmaByCode(pagma.getCode());
    if (pagmaTemp == null) {
      LOGGER
      .warn(
            "{} - Le PAGMa {} n'existe pas dans la famille de colonne DroitPagma",
            CHECK_EXISTS, pagma.getCode());
      throw new PagmaNotFoundException("Le PAGMa " + pagma.getCode()
      + " n'existe pas dans la famille de colonne DroitPagma");
    }

  }

  /**
   * Recherche pagm par code
   * 
   * @param code
   * @return Pagma
   */
  private Pagma findPagmaByCode(final String code) {

    return pagmaSupportFacade.find(code);
  }
}
