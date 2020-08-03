/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.droit.cache.CacheConfig;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.facade.ActionUnitaireSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.ContratServiceSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.FormatControlProfilSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmaSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmfSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PagmpSupportFacade;
import fr.urssaf.image.sae.droit.dao.support.facade.PrmdSupportFacade;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.ContratServiceReferenceException;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmReferenceException;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePagmf;
import fr.urssaf.image.sae.droit.model.SaePagmp;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

//Attention à la gestion de pagm dont l'entité est différente en cql

/**
 * Classe d'implémentation du service {@link SaeDroitService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * (Thrift et Cql)
 */
@Component
public class SaeDroitServiceImpl implements SaeDroitService {

  private static final String CHECK_CONTRAT = "checkContratServiceInexistant";
  private static final String TRC_LOAD = "loadSaeDroits()";
  private static final String TRC_CREATE = "createContratService()";

  private static final String MESSAGE_CONTRAT = "Le contrat de service ";
  private static final String MESSAGE_PAGM = "Le pagm ";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SaeDroitServiceImpl.class);

  /**
   * préfixe utilisé pour les locks zookeeper
   */
  private static final String PREFIXE_CONTRAT = "/DroitContratService/";

  private final String cfName = Constantes.CF_DROIT_CONTRAT_SERVICE;
  private final LoadingCache<String, ServiceContract> contratsCache;
  private final LoadingCache<String, List<Pagm>> pagmsCache;
  private final LoadingCache<String, Pagma> pagmasCache;
  private final LoadingCache<String, Pagmp> pagmpsCache;
  private final LoadingCache<String, Pagmf> pagmfsCache;
  private final LoadingCache<String, FormatControlProfil> formatControlProfilsCache;
  private final LoadingCache<String, Prmd> prmdsCache;
  private final LoadingCache<String, ActionUnitaire> actionsCache;

  private final ContratServiceSupportFacade contratSupport;

  private final PagmSupportFacade pagmSupport;

  private final PagmaSupportFacade pagmaSupport;

  private final PagmpSupportFacade pagmpSupport;

  private final PagmfSupportFacade pagmfSupport;

  private final CuratorFramework curatorClient;



  private final Keyspace keyspace;

  private static final int MAX_CONTRATS_SERVICES = 200;

  /**
   * Constructeur
   * 
   * @param contratSupport
   *           support pour les contratsCache de service
   * @param pagmSupport
   *           support pour les pagm
   * @param pagmaSupport
   *           support pour les pagma
   * @param pagmpSupport
   *           support pour les pagmp
   * @param actionSupport
   *           support pour les actions unitaires
   * @param prmdSupport
   *           support pour les prmd
   * @param pagmfSupport
   *           support pour les pagmf
   * @param formControlProfilSupport
   *           support pour les profils de contrôle
   * @param curatorClient
   *           connexion à Zookeeper
   * @param clockSupport
   *           support pour la gestion de l'horloge CASSANDRA
   * @param cacheConfig
   *           Configuration des différents caches
   * @param keyspace
   *           keyspace utilisé
   */
  @Autowired
  public SaeDroitServiceImpl(final ContratServiceSupportFacade contratSupport,
                             final PagmSupportFacade pagmSupport, final PagmaSupportFacade pagmaSupport,
                             final PagmpSupportFacade pagmpSupport, final PagmfSupportFacade pagmfSupport,
                             final FormatControlProfilSupportFacade formControlProfilSupport,
                             final ActionUnitaireSupportFacade actionSupport,
                             final PrmdSupportFacade prmdSupport,
                             final CuratorFramework curatorClient,
                             final CacheConfig cacheConfig,
                             final Keyspace keyspace) {

    contratsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                               cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                             new CacheLoader<String, ServiceContract>() {

                                                                                                                               @Override
                                                                                                                               public ServiceContract load(final String identifiant) {
                                                                                                                                 return contratSupport.find(identifiant);
                                                                                                                               }

                                                                                                                             });

    pagmsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                          new CacheLoader<String, List<Pagm>>() {

                                                                                                                            @Override
                                                                                                                            public List<Pagm> load(final String identifiant) {
                                                                                                                              return pagmSupport.find(identifiant);
                                                                                                                            }

                                                                                                                          });

    pagmasCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                             cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                           new CacheLoader<String, Pagma>() {

                                                                                                                             @Override
                                                                                                                             public Pagma load(final String identifiant) {
                                                                                                                               return pagmaSupport.find(identifiant);
                                                                                                                             }

                                                                                                                           });

    pagmpsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                             cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                           new CacheLoader<String, Pagmp>() {

                                                                                                                             @Override
                                                                                                                             public Pagmp load(final String identifiant) {
                                                                                                                               return pagmpSupport.find(identifiant);
                                                                                                                             }

                                                                                                                           });

    pagmfsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                             cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                           new CacheLoader<String, Pagmf>() {

                                                                                                                             @Override
                                                                                                                             public Pagmf load(final String identifiant) {
                                                                                                                               return pagmfSupport.find(identifiant);
                                                                                                                             }

                                                                                                                           });

    formatControlProfilsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                                           cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                                         new CacheLoader<String, FormatControlProfil>() {

                                                                                                                                           @Override
                                                                                                                                           public FormatControlProfil load(final String identifiant) {
                                                                                                                                             return formControlProfilSupport.find(identifiant);
                                                                                                                                           }

                                                                                                                                         });

    actionsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                              cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                            new CacheLoader<String, ActionUnitaire>() {

                                                                                                                              @Override
                                                                                                                              public ActionUnitaire load(final String identifiant) {
                                                                                                                                return actionSupport.find(identifiant);
                                                                                                                              }

                                                                                                                            });

    prmdsCache = CacheBuilder.newBuilder().expireAfterWrite(
                                                            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
                                                                                                                          new CacheLoader<String, Prmd>() {

                                                                                                                            @Override
                                                                                                                            public Prmd load(final String identifiant) {
                                                                                                                              return prmdSupport.find(identifiant);
                                                                                                                            }

                                                                                                                          });

    this.contratSupport = contratSupport;
    this.pagmSupport = pagmSupport;
    this.pagmaSupport = pagmaSupport;
    this.pagmpSupport = pagmpSupport;
    this.pagmfSupport = pagmfSupport;

    this.curatorClient = curatorClient;
    this.keyspace = keyspace;

    if (cacheConfig.isInitCacheOnStartupDroits()) {
      // initialisation de tous les caches
      populateAllCache(actionSupport, prmdSupport, formControlProfilSupport);
    }
  }

  private void populateAllCache(final ActionUnitaireSupportFacade actionSupport,
                                final PrmdSupportFacade prmdSupport,
                                final FormatControlProfilSupportFacade formControlProfilSupport) {
    // initialisation du cache des contrats de services
    final List<ServiceContract> allCs = contratSupport.findAll(MAX_CONTRATS_SERVICES);

    for (final ServiceContract cs : allCs) {
      contratsCache.put(cs.getCodeClient(), cs);

      // force la recuperation du cache des pagms
      final List<Pagm> pagms = pagmSupport.find(cs.getCodeClient());

      if (pagms != null) {
        pagmsCache.put(cs.getCodeClient(), pagms);
        for (final Pagm pagm : pagms) {
          initCache(actionSupport, prmdSupport, formControlProfilSupport, pagm);
        }
      }
    }
  }

  /**
   * @param actionSupport
   * @param prmdSupport
   * @param formControlProfilSupport
   * @param pagm
   */
  private void initCache(final ActionUnitaireSupportFacade actionSupport, final PrmdSupportFacade prmdSupport,
                         final FormatControlProfilSupportFacade formControlProfilSupport, final Pagm pagm) {
    final Pagma pagma = pagmaSupport.find(pagm.getPagma());
    if (pagma != null) {
      pagmasCache.put(pagm.getPagma(), pagma);
    }
    final Pagmp pagmp = pagmpSupport.find(pagm.getPagmp());
    if (pagmp != null) {
      pagmpsCache.put(pagm.getPagmp(), pagmp);
    }
    Pagmf pagmf = null;
    if (StringUtils.isNotEmpty(pagm.getPagmf())) {
      pagmf = pagmfSupport.find(pagm.getPagmf());
      if (pagmf != null) {
        pagmfsCache.put(pagm.getPagmf(), pagmf);
      }
    }

    // initialisation du cache des actions
    if (pagma.getActionUnitaires() != null) {
      for (final String action : pagma.getActionUnitaires()) {
        final ActionUnitaire actionUnitaire = actionSupport.find(action);
        if (actionUnitaire != null) {
          actionsCache.put(action, actionUnitaire);
        } 
      }
    }

    // initialisation du cache des perimetres de donnees
    if (pagmp.getPrmd() != null) {
      final Prmd prmd = prmdSupport.find(pagmp.getPrmd());
      if (prmd != null) {
        prmdsCache.put(pagmp.getPrmd(), prmd);
      } 
    }

    if (pagmf != null) {
      // initialisation du cache des profil de controle de format
      final FormatControlProfil fcp = formControlProfilSupport.find(pagmf.getCodeFormatControlProfil());
      if (fcp != null) {
        formatControlProfilsCache.put(pagmf.getCodeFormatControlProfil(), fcp);
      }
    }
  }


  @Override
  /**
   * {@inheritDoc}
   */
  public final SaeDroitsEtFormat loadSaeDroits(final String idClient,
                                               final List<String> pagms) throws ContratServiceNotFoundException,
  FormatControlProfilNotFoundException, PagmNotFoundException {

    final SaeDroitsEtFormat saeDroitEtFormat = new SaeDroitsEtFormat();

    LOGGER.debug("{} - Vérification de l'existence du contrat de service {}",
                 TRC_LOAD, idClient);

    try {
      contratsCache.getUnchecked(idClient);
    } catch (final InvalidCacheLoadException e) {
      throw new ContratServiceNotFoundException(
                                                "Aucun contrat de service n'a été trouvé "
                                                    + " pour l'identifiant " + idClient, e);
    }

    List<Pagm> listPagm;
    LOGGER
    .debug(
           "{} - Vérification ques des pagms sont associés au contrat de service {}",
           TRC_LOAD, idClient);
    try {
      listPagm = pagmsCache.getUnchecked(idClient);
    } catch (final InvalidCacheLoadException e) {
      // initialisation de la liste à vide, afin d'avoir une liste de
      // référence
      listPagm = new ArrayList<>();
    }

    final SaeDroits saeDroits = new SaeDroits();

    LOGGER
    .debug(
           "{} - Pour chaque pagm, on vérifie que les pagma et pagmp associés existent",
           TRC_LOAD);

    final List<Pagm> ExistingPagms = getExistingPagm(pagms, listPagm, idClient);

    for (final Pagm pagm : ExistingPagms) {

      Pagma pagma;
      try {
        pagma = pagmasCache.getUnchecked(pagm.getPagma());
      } catch (final InvalidCacheLoadException e) {
        throw new PagmaReferenceException(
                                          "Le PAGMa "
                                              + pagm.getPagma()
                                              + " n'a pas été trouvé dans la famille de colonne DroitPagma",
                                              e);
      }

      final Prmd prmd = getPrmd(pagm.getPagmp());

      for (final String codeAction : pagma.getActionUnitaires()) {
        try {
          actionsCache.getUnchecked(codeAction);
        } catch (final InvalidCacheLoadException e) {
          throw new ActionUnitaireReferenceException("L'action unitaire "
              + codeAction + " n'a pas été trouvée "
              + "dans la famille de colonne DroitActionUnitaire", e);
        }

        gererPrmd(saeDroits, codeAction, prmd, pagm);
        // ------------------------
        saeDroitEtFormat.setSaeDroits(saeDroits);
      }

      // récupération des Pagmf à partir du PAGM donné.
      // Remarque un PAGMF peut ne pas exister dans ce cas on continue le
      // traitement et on ne remonte pas d'exception.
      final Pagmf pagmf = getPagmf(pagm.getPagmf());
      if (pagmf != null) {
        // Récupération du formatControlProfil
        final FormatControlProfil formatControlProfil = getFormatControlProfil(pagmf
                                                                               .getCodeFormatControlProfil());
        // NB : Si le formatControlProfil n'existe pas une exception
        // FormatControlProfilNotFoundException est levée.

        // ajout au viContenu. La clé à ajouter est le code du Pagm et le
        // profile est ajouté à la liste de profile de contrôle
        // Set<FormatControlProfil> setFormat = getListFormatControlProfil(
        // viContenu, pagm.getCode());
        // setFormat.add(formatControlProfil);
        // viContenu.getControlProfilMap().put(pagm.getCode(), setFormat);

        // ajout du formatControlProfil
        final List<FormatControlProfil> listFormatControlProfil = saeDroitEtFormat
            .getListFormatControlProfil();
        listFormatControlProfil.add(formatControlProfil);
        saeDroitEtFormat
        .setListFormatControlProfil(listFormatControlProfil);
      }

    }

    return saeDroitEtFormat;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws LockTimeoutException
   */
  @Override
  public final void createContratService(final ServiceContract serviceContract,
                                         final List<SaePagm> listeSaePagms) {

    LOGGER.debug("{} - Debut de la création du contrat de service",
                 TRC_CREATE);

    final String lockName = PREFIXE_CONTRAT + serviceContract.getCodeClient();

    final ZookeeperMutex mutex = ZookeeperUtils
        .createMutex(curatorClient, lockName);

    try {

      ZookeeperUtils.acquire(mutex, lockName);

      LOGGER
      .debug(
             "{} - Vérification que le contrat de service {} n'est pas préexistant",
             TRC_CREATE, serviceContract.getCodeClient());
      checkContratServiceInexistant(serviceContract);

      contratSupport.create(serviceContract);

      try {
        for (final SaePagm saePagm : listeSaePagms) {
          ajouterPagmContratService(serviceContract.getCodeClient(),
                                    saePagm);
        }
      } catch (final PagmReferenceException e) {
        // Suppression du CS si les PAGM n'ont pas été créés
        contratSupport.delete(serviceContract.getCodeClient());
        throw new DroitRuntimeException(e);
      } catch (final DroitRuntimeException e) {
        // Suppression du CS si les PAGM n'ont pas été créés
        contratSupport.delete(serviceContract.getCodeClient());
        throw new DroitRuntimeException(e);
      }

      checkLock(mutex, serviceContract, listeSaePagms);

      LOGGER.debug("{} - Fin de la création du contrat de service",
                   TRC_CREATE);
    } finally {
      mutex.release();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean contratServiceExists(final String idClient) {
    boolean exists;

    try {
      contratsCache.getUnchecked(idClient);
      exists = true;

    } catch (final InvalidCacheLoadException e) {
      exists = false;
    }

    return exists;

  }

  /**
   * @param mutex
   */
  private void checkLock(final ZookeeperMutex mutex, final ServiceContract contrat,
                         final List<SaePagm> listeSaePagm) {
    if (!ZookeeperUtils.isLock(mutex)) {

      final String codeContrat = contrat.getCodeClient();

      ServiceContract storedContract;
      try {
        storedContract = contratsCache.getUnchecked(codeContrat);

      } catch (final InvalidCacheLoadException e) {
        throw new ContratServiceReferenceException(MESSAGE_CONTRAT
                                                   + codeContrat + "n'a pas été créé", e);
      }

      List<Pagm> pagmList;
      try {
        pagmList = pagmsCache.getUnchecked(codeContrat);
      } catch (final InvalidCacheLoadException e) {
        throw new PagmReferenceException("les pagm du contrat de service "
            + codeContrat + "n'ont pas été créé", e);
      }

      if (!storedContract.equals(contrat)) {
        throw new DroitRuntimeException(MESSAGE_CONTRAT + codeContrat
                                        + " a déjà été créé");
      }

      if (pagmList.size() != listeSaePagm.size()
          || !pagmList.containsAll(listeSaePagm)) {
        throw new DroitRuntimeException(
                                        "Les pagms rattachés au contrat de service " + codeContrat
                                        + " ont déjà été créés");
      }
    }
  }


  /**
   * @param mutex
   */
  private void checkLock(final ZookeeperMutex mutex, final ServiceContract contrat) {
    if (!ZookeeperUtils.isLock(mutex)) {

      final String codeContrat = contrat.getCodeClient();

      ServiceContract storedContract;
      try {
        storedContract = contratsCache.getUnchecked(codeContrat);

      } catch (final InvalidCacheLoadException e) {
        throw new ContratServiceReferenceException(MESSAGE_CONTRAT
                                                   + codeContrat + "n'a pas été créé", e);
      }

      if (!storedContract.equals(contrat)) {
        throw new DroitRuntimeException(MESSAGE_CONTRAT + codeContrat
                                        + " a déjà été créé");
      }
    }
  }

  //   private Pagm checkPagmExists(String codePagm, List<Pagm> listPagm,
  //         String idClient) throws PagmNotFoundException {
  //
  //      Pagm pagm = null;
  //      boolean found = false;
  //      int index = 0;
  //      while (index < listPagm.size() && !found) {
  //         if (listPagm.get(index).getCode().equals(codePagm)) {
  //            found = true;
  //            pagm = listPagm.get(index);
  //         }
  //         index++;
  //      }
  //
  //      if (!found) {
  //         throw new PagmNotFoundException("Aucun PAGM '" + codePagm
  //               + "' n'a été trouvé pour le contrat de service " + idClient);
  //      }
  //
  //      return pagm;
  //
  //   }

  /**
   * Retourne la liste des Pagm existants à partir des codes passés en paramètre
   * @param pagms
   * @param listPagm
   * @param idClient
   * @return list de Pagm
   * @throws PagmNotFoundException si aucun Pagm trouvé
   */
  private List<Pagm> getExistingPagm(final List<String> pagms, final List<Pagm> listPagm,
                                     final String idClient) throws PagmNotFoundException {

    final List<Pagm> ExistingPagms = new ArrayList<>();
    for (final Pagm pagm : listPagm) {
      for (final String codePagm : pagms) {
        if(pagm.getCode().equals(codePagm)){
          ExistingPagms.add(pagm);
        }
      }
    }
    // Si aucun PAGM trouvé
    if (ExistingPagms.size() == 0) {
      throw new PagmNotFoundException("Aucun PAGM de la liste n'a été trouvé pour le contrat de service " + idClient);
    }

    return ExistingPagms;
  }

  private Prmd getPrmd(final String codePagmp) {

    Pagmp pagmp;
    try {
      pagmp = pagmpsCache.getUnchecked(codePagmp);
    } catch (final InvalidCacheLoadException e) {
      throw new PagmpReferenceException("Le PAGMp " + codePagmp
                                        + " n'a pas été trouvé dans la famille de colonne DroitPagma", e);
    }

    Prmd prmd;
    try {
      prmd = prmdsCache.getUnchecked(pagmp.getPrmd());
    } catch (final InvalidCacheLoadException e) {
      throw new PrmdReferenceException("Le PRMD " + pagmp.getPrmd()
      + " n'a pas été trouvé dans la famille de colonne DroitPrmd", e);
    }

    return prmd;
  }

  private void gererPrmd(final SaeDroits saeDroits, final String actionUnitaire,
                         final Prmd prmd, final Pagm pagm) {

    if (saeDroits.get(actionUnitaire) == null) {
      saeDroits.put(actionUnitaire, new ArrayList<SaePrmd>());
    }

    final SaePrmd saePrmd = new SaePrmd();
    saePrmd.setPrmd(prmd);
    saePrmd.setValues(pagm.getParametres());

    saeDroits.get(actionUnitaire).add(saePrmd);

  }

  /**
   * vérifie si le contrat de service est pré existant. Si c'est le cas, levée
   * d'une {@link RuntimeException}
   * 
   * @param serviceContract
   *           le contrat de service
   */
  private void checkContratServiceInexistant(final ServiceContract serviceContract) {

    if (contratServiceExists(serviceContract.getCodeClient())) {
      LOGGER.warn("{} - Le contrat de service {} existe déjà dans "
          + "la famille de colonne DroitContratService", CHECK_CONTRAT,
          serviceContract.getCodeClient());
      throw new DroitRuntimeException(MESSAGE_CONTRAT
                                      + serviceContract.getCodeClient()
                                      + " existe déjà dans la famille de colonne DroitContratService");
    } else {
      LOGGER.debug("{} - aucune référence au contrat de service {} "
          + " trouvée dans la famille de colonne DroitContratService."
          + " On continue le traitement", CHECK_CONTRAT, serviceContract
          .getCodeClient());
    }

  }

  /**
   * vérifie si le contrat de service est pré existant. Si ce n'est pas le cas, levée
   * d'une {@link RuntimeException}
   * 
   * @param serviceContract
   *           le contrat de service
   */
  private void checkContratServiceExistant(final ServiceContract serviceContract) {

    if (!contratServiceExists(serviceContract.getCodeClient())) {
      LOGGER.warn("{} - Le contrat de service {} n'existe pas "
          + "la famille de colonne DroitContratService", CHECK_CONTRAT,
          serviceContract.getCodeClient());
      throw new DroitRuntimeException(MESSAGE_CONTRAT
                                      + serviceContract.getCodeClient()
                                      + " n'existe pas dans la famille de colonne DroitContratService");
    } else {
      LOGGER.debug("{} - une référence au contrat de service {} "
          + " trouvée dans la famille de colonne DroitContratService."
          + " On continue le traitement", CHECK_CONTRAT, serviceContract
          .getCodeClient());
    }

  }


  /**
   * {@inheritDoc}
   */
  @Override
  public final ServiceContract getServiceContract(final String idClient) {
    try {
      return contratsCache.getUnchecked(idClient);
    } catch (final InvalidCacheLoadException e) {
      throw new ContratServiceReferenceException(MESSAGE_CONTRAT + idClient
                                                 + " n'existe pas", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<ServiceContract> findAllContractService(final int maxResult) {

    return contratSupport.findAll(maxResult);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<String> findAllCodeClientCs(final int maxResult) {
    final List<ServiceContract> listeSC = contratSupport.findAll(maxResult);
    final List<String> listeCodesClient = new ArrayList<>();
    for (final ServiceContract serviceContract : listeSC) {
      listeCodesClient.add(serviceContract.getCodeClient());
    }
    Collections.sort(listeCodesClient);
    return listeCodesClient;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<SaeContratService> findAllSaeContractService(final int maxResult) {

    final List<ServiceContract> listeCs = contratSupport.findAll(maxResult);
    final List<SaeContratService> listeSaeCs = new ArrayList<>();
    SaeContratService saeCs;
    for (final ServiceContract serviceContract : listeCs) {
      saeCs = getFullContratService(serviceContract.getCodeClient());
      listeSaeCs.add(saeCs);
    }
    return listeSaeCs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final SaeContratService getFullContratService(final String idClient) {

    final ServiceContract contrat = getServiceContract(idClient);

    final List<SaePagm> listeSaePagm = getListeSaePagm(idClient);

    final List<SaePrmd> listeSaePrmd = findPrmd(listeSaePagm);

    final List<FormatControlProfil> listeFormatControlProfil = getListeFcp(listeSaePagm);

    final SaeContratService saeContrat = new SaeContratService();
    saeContrat.setCodeClient(contrat.getCodeClient());
    saeContrat.setDescription(contrat.getDescription());
    saeContrat.setIdCertifClient(contrat.getIdCertifClient());
    saeContrat.setIdPki(contrat.getIdPki());
    saeContrat.setLibelle(contrat.getLibelle());
    saeContrat.setListCertifsClient(contrat.getListCertifsClient());
    saeContrat.setListPki(contrat.getListPki());
    saeContrat.setViDuree(contrat.getViDuree());
    saeContrat.setVerifNommage(contrat.isVerifNommage());
    saeContrat.setSaePagms(listeSaePagm);
    saeContrat.setSaePrmds(listeSaePrmd);
    saeContrat.setFormatControlProfils(listeFormatControlProfil);

    return saeContrat;
  }

  private List<FormatControlProfil> getListeFcp(final List<SaePagm> saePagms) {
    final List<String> listeCodes = new ArrayList<>();

    for (final SaePagm saePagm : saePagms) {
      if (saePagm.getPagmf() != null) {
        final String codeFcp = saePagm.getPagmf().getFormatProfile();
        if (!listeCodes.contains(codeFcp)) {
          listeCodes.add(codeFcp);
        }
      }

    }

    final List<FormatControlProfil> listeFcp = new ArrayList<>(
        listeCodes.size());
    FormatControlProfil fcp;
    for (final String code : listeCodes) {
      try {
        fcp = formatControlProfilsCache.getUnchecked(code);
      } catch (final InvalidCacheLoadException e) {
        throw new PrmdReferenceException(
                                         "Le profil de contrôle de format "
                                             + code
                                             + " n'a pas été trouvé dans la famille de colonne DroitFormatControlProfil",
                                             e);
      }
      listeFcp.add(fcp);
    }

    return listeFcp;
  }

  private List<SaePrmd> findPrmd(final List<SaePagm> saePagms) {
    final List<String> listeCodes = new ArrayList<>();

    for (final SaePagm saePagm : saePagms) {
      final String codePrmd = saePagm.getPagmp().getPrmd();
      if (!listeCodes.contains(codePrmd)) {
        listeCodes.add(codePrmd);
      }
    }

    final List<SaePrmd> saePrmds = new ArrayList<>(listeCodes.size());
    Prmd prmd;
    for (final String code : listeCodes) {
      try {
        prmd = prmdsCache.getUnchecked(code);
      } catch (final InvalidCacheLoadException e) {
        throw new PrmdReferenceException("Le PRMD " + code
                                         + " n'a pas été trouvé dans la famille de colonne DroitPrmd",
                                         e);
      }
      final SaePrmd saePrmd = new SaePrmd();
      saePrmd.setPrmd(prmd);
      saePrmds.add(saePrmd);
    }

    return saePrmds;
  }

  /**
   * 
   * @return Mutator </code>
   */
  private Mutator<String> createMutator() {

    final Mutator<String> mutator = HFactory.createMutator(keyspace,
                                                           StringSerializer.get());

    return mutator;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<SaePagm> getListeSaePagm(final String idContratService) {

    final List<SaePagm> listeSaePagm = new ArrayList<>();
    List<Pagm> pagms;
    try {
      pagms = pagmsCache.getUnchecked(idContratService);
    } catch (final InvalidCacheLoadException e) {
      pagms = new ArrayList<>();
    }

    for (final Pagm pagm : pagms) {

      final Pagma pagma = pagmasCache.getUnchecked(pagm.getPagma());
      final SaePagma saePagma = new SaePagma();
      saePagma.setCode(pagma.getCode());
      saePagma.setActionUnitaires(pagma.getActionUnitaires());

      final Pagmp pagmp = pagmpsCache.getUnchecked(pagm.getPagmp());
      final SaePagmp saePagmp = new SaePagmp();
      saePagmp.setCode(pagmp.getCode());
      saePagmp.setDescription(pagmp.getDescription());
      saePagmp.setPrmd(pagmp.getPrmd());

      final SaePagm saePagm = new SaePagm();
      saePagm.setCode(pagm.getCode());
      saePagm.setDescription(pagm.getDescription());
      saePagm.setParametres(pagm.getParametres());
      saePagm.setPagma(saePagma);
      saePagm.setPagmp(saePagmp);
      saePagm.setCompressionPdfActive(pagm.getCompressionPdfActive());
      saePagm.setSeuilCompressionPdf(pagm.getSeuilCompressionPdf());

      if (pagm.getPagmf() != null) {
        final Pagmf pagmf = getPagmf(pagm.getPagmf());
        if (pagmf != null) {
          final SaePagmf saePagmf = new SaePagmf();
          saePagmf.setCodePagmf(pagmf.getCodePagmf());
          saePagmf.setDescription(pagmf.getDescription());
          saePagmf.setFormatProfile(pagmf.getCodeFormatControlProfil());
          saePagm.setPagmf(saePagmf);
        }
      }

      listeSaePagm.add(saePagm);
    }

    return listeSaePagm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void ajouterPagmContratService(final String idContratService,
                                              final SaePagm saePagm) {

    // Vérification de l'existence du contrat de service
    testExistenceCS(idContratService);

    // Vérification de la non existence du PAGM à créer
    try {
      final List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
      boolean pagmExist = false;
      for (final Pagm pagm : pagms) {
        if (pagm.getCode().equals(saePagm.getCode())) {
          pagmExist = true;
          break;
        }
      }

      if (pagmExist) {
        throw new PagmReferenceException(MESSAGE_PAGM + saePagm.getCode()
        + " existe déjà");
      } else {
        LOGGER
        .debug(
               "le pagm {} est inexistant, on peut l'ajouter au contrat de service",
               saePagm.getCode());
      }

    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "impossible de trouver des pagms associés au contrat de service {}, on peut ajouter le pagm {}",
             idContratService, saePagm.getCode());
    }

    // Vérification que les actions unitaires souhaitées existent bien en bdd,
    // sinon on ne peut pas créer le PAGM
    final SaePagma saePagma = saePagm.getPagma();
    final List<String> listeActionsUnitaires = saePagma.getActionUnitaires();
    String actionInexistante = "";
    for (final String action : listeActionsUnitaires) {
      try {
        actionsCache.getUnchecked(action);
      } catch (final InvalidCacheLoadException e) {
        actionInexistante = action;
        break;
      }
    }

    if (!StringUtils.isEmpty(actionInexistante)) {
      throw new PagmReferenceException(MESSAGE_PAGM + saePagm.getCode()
      + " ne peut pas être créé : l'action unitaire "
      + actionInexistante + " n'existe pas !");
    } else {
      LOGGER
      .debug(
             "Toutes les actions unitaire du PAGM {} existent, on peut l'ajouter au contrat de service",
             saePagm.getCode());
    }

    // Création du Mutator
    final Mutator<String> mutator = createMutator();

    // Préparation de la création du PAGM
    creerPagm(idContratService, saePagm, mutator);

    // Execution de la création
    mutator.execute();

    // Recharge immédiatement les caches, pour intégrer
    // le nouveau PAGM que l'on vient juste de créer.
    // Attention : cette mise à jour de cache valable que pour le serveur
    // en cours.
    pagmsCache.invalidate(idContratService);
    pagmasCache.invalidate(saePagm.getPagma().getCode());
    pagmpsCache.invalidate(saePagm.getPagmp().getCode());
    if (saePagm.getPagmf() != null
        && !StringUtils.isBlank(saePagm.getPagmf().getCodePagmf())) {
      pagmfsCache.invalidate(saePagm.getPagmf().getCodePagmf());
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void supprimerPagmContratService(final String idContratService,
                                                final String codePagm) {

    // Vérification de l'existence du contrat de service
    testExistenceCS(idContratService);

    // Vérification de l'existence du PAGM à supprimer
    try {
      final List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
      Pagm pagmASupprimer = null;
      boolean pagmExist = false;
      for (final Pagm pagm : pagms) {
        if (pagm.getCode().equals(codePagm)) {
          pagmExist = true;
          pagmASupprimer = pagm;
        }
      }

      if (pagmExist) {
        LOGGER.debug("le pagm {} existe, suppression possible", codePagm);

        // Création du Mutator
        final Mutator<String> mutator = createMutator();

        // Préparation de la suppression du PAGM
        supprimerPagm(idContratService, pagmASupprimer, mutator);

        // Execution de la suppression
        mutator.execute();

        // Recharge immédiatement le cache des PAGM du CS, pour intégrer
        // le nouveau PAGM que l'on vient juste de créer.
        // Attention : cette mise à jour de cache valable que pour le
        // serveur en cours.
        pagmsCache.invalidate(idContratService);
        pagmasCache.invalidate(pagmASupprimer.getPagma());
        pagmpsCache.invalidate(pagmASupprimer.getPagmp());
        if (pagmASupprimer.getPagmf() != null) {
          pagmfsCache.invalidate(pagmASupprimer.getPagmf());
        }

      } else {
        LOGGER.debug(
                     "le pagm {} n'existe pas, aucune suppression à effectuer",
                     codePagm);
      }

    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "impossible de trouver des pagms associés au contrat de service {}, aucune suppression à effectuer",
             idContratService);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void modifierPagmContratService(final String idContratService,
                                               final SaePagm saePagm) {

    // Vérification de l'existence du contrat de service
    testExistenceCS(idContratService);

    // Vérification de l'existence du PAGM à modifier
    try {
      final List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
      Pagm pagmASupprimer = null;
      boolean pagmExist = false;
      for (final Pagm pagm : pagms) {
        if (pagm.getCode().equals(saePagm.getCode())) {
          pagmExist = true;
          pagmASupprimer = pagm;
        }
      }

      if (pagmExist) {
        LOGGER.debug("le pagm {} existe, modification possible", saePagm
                     .getCode());

        // Création du Mutator
        final Mutator<String> mutator = createMutator();

        // Préparation de la suppression du PAGM
        supprimerPagm(idContratService, pagmASupprimer, mutator);
        pagmsCache.invalidate(idContratService);
        pagmasCache.invalidate(saePagm.getPagma().getCode());
        pagmpsCache.invalidate(saePagm.getPagmp().getCode());
        if (saePagm.getPagmf() != null
            && !StringUtils.isBlank(saePagm.getPagmf().getCodePagmf())) {
          pagmfsCache.invalidate(saePagm.getPagmf().getCodePagmf());
        }

        // Préparation de l'ajout du PAGM
        creerPagm(idContratService, saePagm, mutator);

        // Execution de la suppression
        mutator.execute();

        // Recharge immédiatement le cache des PAGM du CS, pour intégrer
        // le nouveau PAGM que l'on vient juste de créer.
        // Attention : cette mise à jour de cache valable que pour le
        // serveur en cours.
        pagmsCache.invalidate(idContratService);

      } else {
        LOGGER.debug(
                     "le pagm {} n'existe pas, aucune modification à effectuer",
                     saePagm.getCode());
      }

    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "impossible de trouver des pagms associés au contrat de service {}, aucune modification à effectuer",
             idContratService);
    }

  }

  /**
   * Préparation de la création d'un PAGM avec Mutator
   * 
   * @param idContratService
   *           L'identifiant du contrat de service
   * @param saePagm
   *           Le PAGM à créer
   * @param mutator
   *           Mutator
   */
  private void creerPagm(final String idContratService, final SaePagm saePagm,
                         final Mutator<String> mutator) {

    // Ajout du PAGMa
    final SaePagma saePagma = saePagm.getPagma();
    final Pagma pagma = new Pagma();
    pagma.setActionUnitaires(saePagma.getActionUnitaires());
    pagma.setCode(saePagma.getCode());

    pagmaSupport.create(pagma, mutator);

    // Ajout du PAGMp
    final SaePagmp saePagmp = saePagm.getPagmp();
    final Pagmp pagmp = new Pagmp();
    pagmp.setCode(saePagmp.getCode());
    pagmp.setDescription(saePagmp.getDescription());
    pagmp.setPrmd(saePagmp.getPrmd());
    pagmpSupport.create(pagmp, mutator);

    // Ajout du PAGMf
    final SaePagmf saePagmf = saePagm.getPagmf();
    if (saePagmf != null) {
      final Pagmf pagmf = new Pagmf();
      pagmf.setCodePagmf(saePagmf.getCodePagmf());
      pagmf.setDescription(saePagmf.getDescription());
      pagmf.setCodeFormatControlProfil(saePagmf.getFormatProfile());

      pagmfSupport.create(pagmf);
    }

    // Ajout du PAGM
    final Pagm pagm = new Pagm();
    pagm.setCode(saePagm.getCode());
    pagm.setDescription(saePagm.getDescription());
    pagm.setPagma(saePagm.getPagma().getCode());
    pagm.setPagmp(saePagm.getPagmp().getCode());
    if (saePagm.getPagmf() != null) {
      pagm.setPagmf(saePagm.getPagmf().getCodePagmf());
    }
    pagm.setParametres(saePagm.getParametres());
    if (saePagm.getCompressionPdfActive() != null) {
      pagm.setCompressionPdfActive(saePagm.getCompressionPdfActive());
    }
    if (saePagm.getSeuilCompressionPdf() != null) {
      pagm.setSeuilCompressionPdf(saePagm.getSeuilCompressionPdf());
    }
    pagmSupport.create(idContratService, pagm, mutator);

  }

  /**
   * Préparation de la suppression d'un PAGM avec Mutator
   * 
   * @param idContratService
   *           L'identifiant du contrat de service
   * @param saePagm
   *           Le PAGM à créer
   * @param mutator
   *           Mutator
   */
  private void supprimerPagm(final String idContratService, final Pagm pagmASupprimer,
                             final Mutator<String> mutator) {

    try {
      final Pagma pagma = pagmasCache.getUnchecked(pagmASupprimer.getPagma());
      // Suppression du PAGMa
      pagmaSupport.delete(pagma.getCode(),
                          mutator);
    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "Pas de PAGMa pour le pagm {}, aucune suppression de PAGMa à effectuer",
             pagmASupprimer.getCode());
    }
    try {
      final Pagmp pagmp = pagmpsCache.getUnchecked(pagmASupprimer.getPagmp());
      // Ajout du PAGMp
      pagmpSupport.delete(pagmp.getCode(), mutator);
    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "Pas de PAGMp pour le pagm {}, aucune suppression de PAGMp à effectuer",
             pagmASupprimer.getCode());
    }
    try {
      if (pagmASupprimer.getPagmf() != null) {
        final Pagmf pagmf = pagmfsCache.getUnchecked(pagmASupprimer.getPagmf());
        // Ajout du PAGMf
        pagmfSupport.delete(pagmf.getCodePagmf(), mutator);
      }
    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "Pas de PAGMf pour le pagm {}, aucune suppression de PAGMf à effectuer",
             pagmASupprimer.getCode());
    }

    pagmSupport.delete(idContratService, pagmASupprimer.getCode(),
                       mutator);

  }

  /**
   * Teste l'existence du CS
   * 
   * @param idContratService
   *           Identifiant du CS
   */
  private void testExistenceCS(final String idContratService) {
    // Vérification de l'existence du contrat de service
    try {
      contratsCache.getUnchecked(idContratService);
    } catch (final InvalidCacheLoadException e) {
      throw new ContratServiceReferenceException(MESSAGE_CONTRAT
                                                 + idContratService + " n'existe pas", e);
    }
  }

  /**
   * Récupération du Pagmf
   * 
   * @param codePagmf
   *           code
   * @return PAGMF
   */
  private Pagmf getPagmf(final String codePagmf) {

    Pagmf pagmf = null;
    try {
      if (!StringUtils.isBlank(codePagmf)) {
        pagmf = pagmfsCache.getUnchecked(codePagmf);
      }

    } catch (final InvalidCacheLoadException e) {
      LOGGER
      .debug(
             "Le PAGMf "
                 + codePagmf
                 + " n'a pas été trouvé dans la famille de colonne DroitPagmf",
                 e);
    }
    return pagmf;
  }

  /**
   * Récupération du FormatControlProfil à partir de son code
   * 
   * @param codeFormatControlProfil
   *           code
   * @return FormatControlProfil
   * @throws FormatControlProfilNotFoundException
   *            : formatControlProfilInexistant
   */
  private FormatControlProfil getFormatControlProfil(
                                                     final String codeFormatControlProfil)
                                                         throws FormatControlProfilNotFoundException {

    FormatControlProfil formatControlProfil;
    try {
      formatControlProfil = formatControlProfilsCache
          .getUnchecked(codeFormatControlProfil);
    } catch (final InvalidCacheLoadException e) {
      throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
                                                     .loadMessage("erreur.format.control.profil.not.found",
                                                                  codeFormatControlProfil), e);
    }
    return formatControlProfil;
  }

  /**
   * Recupère l'ensemble des formatsControlProfil à partir d'une cle de la Map
   * contenu dans le ViContenuExtrait.
   * 
   * 
   * @param viConten
   *           VIContenuExtrait
   * @param cle
   *           cle dans la Map
   */
  // private Set<FormatControlProfil> getListFormatControlProfil(
  // VIContenuExtrait viConten, String cle) {
  //
  // Iterator<Entry<String, Set<FormatControlProfil>>> iterator = viConten
  // .getControlProfilMap().entrySet().iterator();
  // Set<FormatControlProfil> setFormat = null;
  // while (iterator.hasNext()) {
  // Map.Entry<String, Set<FormatControlProfil>> entry = (Map.Entry<String,
  // Set<FormatControlProfil>>) iterator
  // .next();
  // String key = (String) entry.getKey();
  // if (StringUtils.equalsIgnoreCase(cle, key)) {
  // setFormat = (Set<FormatControlProfil>) entry.getValue();
  // return setFormat;
  // }
  // }
  // return setFormat;
  // }


  /**
   * {@inheritDoc}
   * 
   * @throws LockTimeoutException
   */
  @Override
  public final void modifierContratService(final ServiceContract serviceContract) {

    LOGGER.debug("{} - Debut de la modification du contrat de service",
                 TRC_CREATE);

    final String lockName = PREFIXE_CONTRAT + serviceContract.getCodeClient();

    final ZookeeperMutex mutex = ZookeeperUtils
        .createMutex(curatorClient, lockName);

    try {

      ZookeeperUtils.acquire(mutex, lockName);

      LOGGER
      .debug(
             "{} - Vérification que le contrat de service existe déjà",
             TRC_CREATE, serviceContract.getCodeClient());
      checkContratServiceExistant(serviceContract);

      contratSupport.create(serviceContract);

      checkLock(mutex, serviceContract);

      LOGGER.debug("{} - Fin de la création du contrat de service",
                   TRC_CREATE);
    } finally {
      mutex.release();
    }

  }

  /**
   * @return the contratsCache
   */
  @Override
  public void refrechContratsCache(final String cle) {
    contratsCache.refresh(cle);
  }

}
