/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.droit.cache.CacheConfig;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceDatasSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.ContratServiceReferenceException;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmReferenceException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaeDroitService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaeDroitServiceImpl implements SaeDroitService {

   private static final String CHECK_CONTRAT = "checkContratServiceInexistant";
   private static final String CHECK_PAGM = "checkPagmInexistant";
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

   private final LoadingCache<String, ServiceContract> contratsCache;
   private final LoadingCache<String, List<Pagm>> pagmsCache;
   private final LoadingCache<String, Pagma> pagmasCache;
   private final LoadingCache<String, Pagmp> pagmpsCache;
   private final LoadingCache<String, Prmd> prmdsCache;
   private final LoadingCache<String, ActionUnitaire> actionsCache;

   private final ContratServiceSupport contratSupport;
   private final PagmSupport pagmSupport;

   private final CuratorFramework curatorClient;

   private final JobClockSupport clockSupport;
   
   private final ContratServiceDatasSupport completeContratSupport;

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
    * @param curatorClient
    *           connexion à Zookeeper
    * @param clockSupport
    *           support pour la gestion de l'horloge CASSANDRA
    */
   @Autowired
   public SaeDroitServiceImpl(final ContratServiceSupport contratSupport,
         final PagmSupport pagmSupport, final PagmaSupport pagmaSupport,
         final PagmpSupport pagmpSupport,
         final ActionUnitaireSupport actionSupport, final ContratServiceDatasSupport completeCsSupport,
         final PrmdSupport prmdSupport, final CuratorFramework curatorClient,
         final JobClockSupport clockSupport, CacheConfig cacheConfig) {

      contratsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, ServiceContract>() {

               @Override
               public ServiceContract load(String identifiant) {
                  return contratSupport.find(identifiant);
               }

            });

      pagmsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, List<Pagm>>() {

               @Override
               public List<Pagm> load(String identifiant) {
                  return pagmSupport.find(identifiant);
               }

            });

      pagmasCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, Pagma>() {

               @Override
               public Pagma load(String identifiant) {
                  return pagmaSupport.find(identifiant);
               }

            });

      pagmpsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, Pagmp>() {

               @Override
               public Pagmp load(String identifiant) {
                  return pagmpSupport.find(identifiant);
               }

            });

      actionsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, ActionUnitaire>() {

               @Override
               public ActionUnitaire load(String identifiant) {
                  return actionSupport.find(identifiant);
               }

            });

      prmdsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, Prmd>() {

               @Override
               public Prmd load(String identifiant) {
                  return prmdSupport.find(identifiant);
               }

            });
      this.completeContratSupport =completeCsSupport;
      this.contratSupport = contratSupport;
      this.pagmSupport = pagmSupport;
      this.curatorClient = curatorClient;
      this.clockSupport = clockSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeDroits loadSaeDroits(String idClient, List<String> pagms)
         throws ContratServiceNotFoundException, PagmNotFoundException {

      LOGGER.debug("{} - Vérification de l'existence du contrat de service {}",
            TRC_LOAD, idClient);

      try {
         contratsCache.getUnchecked(idClient);
      } catch (InvalidCacheLoadException e) {
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
      } catch (InvalidCacheLoadException e) {
         // initialisation de la liste à vide, afin d'avoir une liste de
         // référence
         listPagm = new ArrayList<Pagm>();
      }

      SaeDroits saeDroits = new SaeDroits();

      LOGGER
            .debug(
                  "{} - Pour chaque pagm, on vérifie que les pagma et pagmp associés existent",
                  TRC_LOAD);
      for (String codePagm : pagms) {
         Pagm pagm = checkPagmExists(codePagm, listPagm, idClient);

         Pagma pagma;
         try {
            pagma = pagmasCache.getUnchecked(pagm.getPagma());
         } catch (InvalidCacheLoadException e) {
            throw new PagmaReferenceException(
                  "Le PAGMa "
                        + pagm.getPagma()
                        + " n'a pas été trouvé dans la famille de colonne DroitPagma",
                  e);
         }

         Prmd prmd = getPrmd(pagm.getPagmp());

         for (String codeAction : pagma.getActionUnitaires()) {
            try {
               actionsCache.getUnchecked(codeAction);
            } catch (InvalidCacheLoadException e) {
               throw new ActionUnitaireReferenceException("L'action unitaire "
                     + codeAction + " n'a pas été trouvée "
                     + "dans la famille de colonne DroitActionUnitaire", e);
            }

            gererPrmd(saeDroits, codeAction, prmd, pagm);

         }

      }

      return saeDroits;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws LockTimeoutException
    */
   @Override
   public final void createContratService(ServiceContract serviceContract,
         List<Pagm> pagms) {

      LOGGER.debug("{} - Debut de la création du contrat de service",
            TRC_CREATE);

      String lockName = PREFIXE_CONTRAT + serviceContract.getCodeClient();

      ZookeeperMutex mutex = ZookeeperUtils
            .createMutex(curatorClient, lockName);

      try {

         ZookeeperUtils.acquire(mutex, lockName);

         LOGGER
               .debug(
                     "{} - Vérification que le contrat de service {} n'est pas préexistant",
                     TRC_CREATE, serviceContract.getCodeClient());
         checkContratServiceInexistant(serviceContract);
         LOGGER.debug("{} - Vérification que le Pagm est inexistant",
               TRC_CREATE);
         checkPagmInexistant(serviceContract);
         LOGGER.debug("{} - vérification que les pagmas et pagmps existent",
               TRC_CREATE);
         checkPagmsExist(pagms);

         contratSupport.create(serviceContract, clockSupport.currentCLock());
         for (Pagm currentPagm : pagms) {
            pagmSupport.create(serviceContract.getCodeClient(), currentPagm,
                  clockSupport.currentCLock());
         }

         checkLock(mutex, serviceContract, pagms);

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
   public final boolean contratServiceExists(String idClient) {
      boolean exists;

      try {
         contratsCache.getUnchecked(idClient);
         exists = true;

      } catch (InvalidCacheLoadException e) {
         exists = false;
      }

      return exists;

   }

   /**
    * @param mutex
    */
   private void checkLock(ZookeeperMutex mutex, ServiceContract contrat,
         List<Pagm> pagms) {
      if (!ZookeeperUtils.isLock(mutex)) {

         String codeContrat = contrat.getCodeClient();

         ServiceContract storedContract;
         try {
            storedContract = contratsCache.getUnchecked(codeContrat);

         } catch (InvalidCacheLoadException e) {
            throw new ContratServiceReferenceException(MESSAGE_CONTRAT
                  + codeContrat + "n'a pas été créé", e);
         }

         List<Pagm> pagmList;
         try {
            pagmList = pagmsCache.getUnchecked(codeContrat);
         } catch (InvalidCacheLoadException e) {
            throw new PagmReferenceException("les pagm du contrat de service "
                  + codeContrat + "n'ont pas été créé", e);
         }

         if (!storedContract.equals(contrat)) {
            throw new DroitRuntimeException(MESSAGE_CONTRAT + codeContrat
                  + " a déjà été créé");
         }

         if (pagmList.size() != pagms.size() || !pagmList.containsAll(pagms)) {
            throw new DroitRuntimeException(
                  "Les pagms rattachés au contrat de service " + codeContrat
                        + " ont déjà été créés");
         }

      }

   }

   private Pagm checkPagmExists(String codePagm, List<Pagm> listPagm,
         String idClient) throws PagmNotFoundException {

      Pagm pagm = null;
      boolean found = false;
      int index = 0;
      while (index < listPagm.size() && !found) {
         if (listPagm.get(index).getCode().equals(codePagm)) {
            found = true;
            pagm = listPagm.get(index);
         }
         index++;
      }

      if (!found) {
         throw new PagmNotFoundException("Aucun PAGM '" + codePagm
               + "' n'a été trouvé pour le contrat de service " + idClient);
      }

      return pagm;

   }

   private Prmd getPrmd(String codePagmp) {

      Pagmp pagmp;
      try {
         pagmp = pagmpsCache.getUnchecked(codePagmp);
      } catch (InvalidCacheLoadException e) {
         throw new PagmpReferenceException("Le PAGMp " + codePagmp
               + " n'a pas été trouvé dans la famille de colonne DroitPagma", e);
      }

      Prmd prmd;
      try {
         prmd = prmdsCache.getUnchecked(pagmp.getPrmd());
      } catch (InvalidCacheLoadException e) {
         throw new PrmdReferenceException("Le PRMD " + pagmp.getPrmd()
               + " n'a pas été trouvé dans la famille de colonne DroitPagma", e);
      }

      return prmd;
   }

   private void gererPrmd(SaeDroits saeDroits, String actionUnitaire,
         Prmd prmd, Pagm pagm) {

      if (saeDroits.get(actionUnitaire) == null) {
         saeDroits.put(actionUnitaire, new ArrayList<SaePrmd>());
      }

      SaePrmd saePrmd = new SaePrmd();
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
   private void checkContratServiceInexistant(ServiceContract serviceContract) {

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
    * vérifie si le contrat de service est pré existant pour le Pagm. Si c'est
    * le cas, levée d'une {@link RuntimeException}
    * 
    * @param serviceContract
    *           le contrat de service
    */
   private void checkPagmInexistant(ServiceContract serviceContract) {
      try {
         pagmsCache.getUnchecked(serviceContract.getCodeClient());
         LOGGER.warn("{} - Le Pagm du contrat de service {} existe déjà dans "
               + "la famille de colonne DroitPagm", CHECK_PAGM, serviceContract
               .getCodeClient());
         throw new DroitRuntimeException(MESSAGE_CONTRAT
               + serviceContract.getCodeClient()
               + " existe déjà dans la famille de colonne DroitPagm");
      } catch (InvalidCacheLoadException e) {
         LOGGER.debug("{} - aucune référence au pagm du contrat de service "
               + " trouvée dans la famille de colonne DroitPagm."
               + " On continue le traitement", CHECK_PAGM, serviceContract
               .getCodeClient());
      }

   }

   /**
    * vérifie si tous les PAGMa et PAGMp d'une {@link RuntimeException}
    * 
    * @param serviceContract
    *           le contrat de service
    */
   private void checkPagmsExist(List<Pagm> pagms) {

      for (Pagm pagm : pagms) {

         try {
            pagmasCache.getUnchecked(pagm.getPagma());

         } catch (InvalidCacheLoadException e) {
            throw new PagmaReferenceException("Le pagma " + pagm.getPagma()
                  + " n'a pas été trouvé "
                  + "dans la famille de colonne DroitPagma", e);
         }

         try {
            pagmpsCache.getUnchecked(pagm.getPagmp());

         } catch (InvalidCacheLoadException e) {
            throw new PagmpReferenceException("Le pagmp " + pagm.getPagmp()
                  + " n'a pas été trouvé "
                  + "dans la famille de colonne DroitPagmp", e);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ServiceContract getServiceContract(String idClient) {
      try {
         return contratsCache.getUnchecked(idClient);
      } catch (InvalidCacheLoadException e) {
         throw new ContratServiceReferenceException(MESSAGE_CONTRAT + idClient
               + " n'existe pas", e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addPagmContratService(String idContratService, Pagm pagm) {
      try {
         contratsCache.getUnchecked(idContratService);
      } catch (InvalidCacheLoadException e) {
         throw new ContratServiceReferenceException(MESSAGE_CONTRAT
               + idContratService + " n'existe pas", e);
      }

      try {
         List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
         if (pagms.contains(pagm)) {
            throw new PagmReferenceException(MESSAGE_PAGM + pagm.getCode()
                  + " existe déjà");
         } else {
            LOGGER
                  .debug(
                        "le pagm {} est inexistant, on peut l'ajouter au contrat de service",
                        pagm.getCode());
         }

      } catch (InvalidCacheLoadException e) {
         LOGGER
               .debug(
                     "impossible de trouver des pagms associés au contrat de service {}, on peut ajouter le pagm {}",
                     idContratService, pagm.getCode());
      }

      String lockName = PREFIXE_CONTRAT + idContratService;

      ZookeeperMutex mutex = ZookeeperUtils
            .createMutex(curatorClient, lockName);

      try {

         ZookeeperUtils.acquire(mutex, lockName);
         pagmSupport
               .create(idContratService, pagm, clockSupport.currentCLock());

      } finally {
         mutex.release();
      }

      // Recharge immédiatement le cache des PAGM du CS, pour intégrer
      // le nouveau PAGM que l'on vient juste de créer.
      // Attention : cette mise à jour de cache valable que pour le serveur
      // en cours.
      pagmsCache.invalidate(idContratService);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Pagm> getListePagm(String idContratService) {

      List<Pagm> pagms;

      try {
         pagms = pagmsCache.getUnchecked(idContratService);
      } catch (InvalidCacheLoadException e) {
         pagms = new ArrayList<Pagm>();
      }

      return pagms;

   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public List<ServiceContract> findAllContractService(int maxResult) {

      return contratSupport.findAll(maxResult);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public ServiceContractDatas getFullContratService(String idClient) {

      return completeContratSupport.getCs(idClient);
   }
   

}
