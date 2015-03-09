/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.lang.StringUtils;
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
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.ContratServiceReferenceException;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmReferenceException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePagmf;
import fr.urssaf.image.sae.droit.model.SaePagmp;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;
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
   private final LoadingCache<String, Pagmf> pagmfsCache;
   private final LoadingCache<String, FormatControlProfil> formatControlProfilsCache;
   private final LoadingCache<String, Prmd> prmdsCache;
   private final LoadingCache<String, ActionUnitaire> actionsCache;

   private final ContratServiceSupport contratSupport;
   private final PagmSupport pagmSupport;
   private final PagmaSupport pagmaSupport;
   private final PagmpSupport pagmpSupport;
   private final PagmfSupport pagmfSupport;

   private final CuratorFramework curatorClient;

   private final JobClockSupport clockSupport;

   private final Keyspace keyspace;

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
   public SaeDroitServiceImpl(final ContratServiceSupport contratSupport,
         final PagmSupport pagmSupport, final PagmaSupport pagmaSupport,
         final PagmpSupport pagmpSupport, final PagmfSupport pagmfSupport,
         final FormatControlProfilSupport formControlProfilSupport,
         final ActionUnitaireSupport actionSupport,
         final PrmdSupport prmdSupport, final CuratorFramework curatorClient,
         final JobClockSupport clockSupport, CacheConfig cacheConfig,
         Keyspace keyspace) {

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

      pagmfsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, Pagmf>() {

               @Override
               public Pagmf load(String identifiant) {
                  return pagmfSupport.find(identifiant);
               }

            });

      formatControlProfilsCache = CacheBuilder.newBuilder().expireAfterWrite(
            cacheConfig.getDroitsCacheDuration(), TimeUnit.MINUTES).build(
            new CacheLoader<String, FormatControlProfil>() {

               @Override
               public FormatControlProfil load(String identifiant) {
                  return formControlProfilSupport.find(identifiant);
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

      this.contratSupport = contratSupport;
      this.pagmSupport = pagmSupport;
      this.pagmaSupport = pagmaSupport;
      this.pagmpSupport = pagmpSupport;
      this.pagmfSupport = pagmfSupport;
      this.curatorClient = curatorClient;
      this.clockSupport = clockSupport;
      this.keyspace = keyspace;
   }

   @Override
   public final SaeDroitsEtFormat loadSaeDroits(String idClient,
         List<String> pagms) throws ContratServiceNotFoundException,
         FormatControlProfilNotFoundException, PagmNotFoundException {

      SaeDroitsEtFormat saeDroitEtFormat = new SaeDroitsEtFormat();

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
            // ------------------------
            saeDroitEtFormat.setSaeDroits(saeDroits);
         }

         // récupération des Pagmf à partir du PAGM donné.
         // Remarque un PAGMF peut ne pas exister dans ce cas on continue le
         // traitement et on ne remonte pas d'exception.
         Pagmf pagmf = getPagmf(pagm.getPagmf());
         if (pagmf != null) {
            // Récupération du formatControlProfil
            FormatControlProfil formatControlProfil = getFormatControlProfil(pagmf
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
            List<FormatControlProfil> listFormatControlProfil = saeDroitEtFormat
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
   public final void createContratService(ServiceContract serviceContract,
         List<SaePagm> listeSaePagms) {

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

         contratSupport.create(serviceContract, clockSupport.currentCLock());

         try {
            for (SaePagm saePagm : listeSaePagms) {
               ajouterPagmContratService(serviceContract.getCodeClient(),
                     saePagm);
            }
         } catch (PagmReferenceException e) {
            // Suppression du CS si les PAGM n'ont pas été créés
            contratSupport.delete(serviceContract.getCodeClient(), clockSupport
                  .currentCLock());
            throw new DroitRuntimeException(e);
         } catch (DroitRuntimeException e) {
            // Suppression du CS si les PAGM n'ont pas été créés
            contratSupport.delete(serviceContract.getCodeClient(), clockSupport
                  .currentCLock());
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
         List<SaePagm> listeSaePagm) {
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

         if (pagmList.size() != listeSaePagm.size()
               || !pagmList.containsAll(listeSaePagm)) {
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
               + " n'a pas été trouvé dans la famille de colonne DroitPrmd", e);
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
    * {@inheritDoc}
    */
   @Override
   public final ServiceContract getServiceContract(String idClient) {
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
   public final List<ServiceContract> findAllContractService(int maxResult) {

      return contratSupport.findAll(maxResult);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<String> findAllCodeClientCs(int maxResult) {
      List<ServiceContract> listeSC = contratSupport.findAll(maxResult);
      List<String> listeCodesClient = new ArrayList<String>();
      for (ServiceContract serviceContract : listeSC) {
         listeCodesClient.add(serviceContract.getCodeClient());
      }
      Collections.sort(listeCodesClient);
      return listeCodesClient;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SaeContratService> findAllSaeContractService(int maxResult) {

      List<ServiceContract> listeCs = contratSupport.findAll(maxResult);
      List<SaeContratService> listeSaeCs = new ArrayList<SaeContratService>();
      SaeContratService saeCs;
      for (ServiceContract serviceContract : listeCs) {
         saeCs = getFullContratService(serviceContract.getCodeClient());
         listeSaeCs.add(saeCs);
      }
      return listeSaeCs;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeContratService getFullContratService(String idClient) {

      ServiceContract contrat = getServiceContract(idClient);

      List<SaePagm> listeSaePagm = this.getListeSaePagm(idClient);

      List<SaePrmd> listeSaePrmd = this.findPrmd(listeSaePagm);

      List<FormatControlProfil> listeFormatControlProfil = this
            .getListeFcp(listeSaePagm);

      SaeContratService saeContrat = new SaeContratService();
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

   private List<FormatControlProfil> getListeFcp(List<SaePagm> saePagms) {
      List<String> listeCodes = new ArrayList<String>();

      for (SaePagm saePagm : saePagms) {
         if (saePagm.getPagmf() != null) {
            String codeFcp = saePagm.getPagmf().getFormatProfile();
            if (!listeCodes.contains(codeFcp)) {
               listeCodes.add(codeFcp);
            }
         }

      }

      List<FormatControlProfil> listeFcp = new ArrayList<FormatControlProfil>(
            listeCodes.size());
      FormatControlProfil fcp;
      for (String code : listeCodes) {
         try {
            fcp = formatControlProfilsCache.getUnchecked(code);
         } catch (InvalidCacheLoadException e) {
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

   private List<SaePrmd> findPrmd(List<SaePagm> saePagms) {
      List<String> listeCodes = new ArrayList<String>();

      for (SaePagm saePagm : saePagms) {
         String codePrmd = saePagm.getPagmp().getPrmd();
         if (!listeCodes.contains(codePrmd)) {
            listeCodes.add(codePrmd);
         }
      }

      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>(listeCodes.size());
      Prmd prmd;
      for (String code : listeCodes) {
         try {
            prmd = prmdsCache.getUnchecked(code);
         } catch (InvalidCacheLoadException e) {
            throw new PrmdReferenceException("Le PRMD " + code
                  + " n'a pas été trouvé dans la famille de colonne DroitPrmd",
                  e);
         }
         SaePrmd saePrmd = new SaePrmd();
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

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SaePagm> getListeSaePagm(String idContratService) {

      List<SaePagm> listeSaePagm = new ArrayList<SaePagm>();
      List<Pagm> pagms;
      try {
         pagms = pagmsCache.getUnchecked(idContratService);
      } catch (InvalidCacheLoadException e) {
         pagms = new ArrayList<Pagm>();
      }

      for (Pagm pagm : pagms) {

         Pagma pagma = pagmasCache.getUnchecked(pagm.getPagma());
         SaePagma saePagma = new SaePagma();
         saePagma.setCode(pagma.getCode());
         saePagma.setActionUnitaires(pagma.getActionUnitaires());

         Pagmp pagmp = pagmpsCache.getUnchecked(pagm.getPagmp());
         SaePagmp saePagmp = new SaePagmp();
         saePagmp.setCode(pagmp.getCode());
         saePagmp.setDescription(pagmp.getDescription());
         saePagmp.setPrmd(pagmp.getPrmd());

         SaePagm saePagm = new SaePagm();
         saePagm.setCode(pagm.getCode());
         saePagm.setDescription(pagm.getDescription());
         saePagm.setParametres(pagm.getParametres());
         saePagm.setPagma(saePagma);
         saePagm.setPagmp(saePagmp);

         if (pagm.getPagmf() != null) {
            Pagmf pagmf = getPagmf(pagm.getPagmf());
            if (pagmf != null) {
               SaePagmf saePagmf = new SaePagmf();
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
   public final void ajouterPagmContratService(String idContratService,
         SaePagm saePagm) {

      // Vérification de l'existence du contrat de service
      testExistenceCS(idContratService);

      // Vérification de la non existence du PAGM à créer
      try {
         List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
         boolean pagmExist = false;
         for (Pagm pagm : pagms) {
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

      } catch (InvalidCacheLoadException e) {
         LOGGER
               .debug(
                     "impossible de trouver des pagms associés au contrat de service {}, on peut ajouter le pagm {}",
                     idContratService, saePagm.getCode());
      }

      // Vérification que les actions unitaires souhaitées existent bien en bdd, sinon on ne peut pas créer le PAGM
      SaePagma saePagma = saePagm.getPagma();
      List<String> listeActionsUnitaires = saePagma.getActionUnitaires();
      String actionInexistante = "";
      for (String action : listeActionsUnitaires) {
         if (pagmaSupport.find(action) == null) {
            actionInexistante = action;
            break;
         }
      }
      if (!StringUtils.isEmpty(actionInexistante)) {
         throw new PagmReferenceException(MESSAGE_PAGM + saePagm.getCode()
               + " ne peut pas être créé : l'action unitaire " + actionInexistante + " n'existe pas !");
      } else {
         LOGGER
         .debug(
               "Toutes les actions unitaire du PAGM {} existent, on peut l'ajouter au contrat de service",
               saePagm.getCode());
      }
      
      // Création du Mutator
      Mutator<String> mutator = createMutator();

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
   public final void supprimerPagmContratService(String idContratService,
         String codePagm) {

      // Vérification de l'existence du contrat de service
      testExistenceCS(idContratService);

      // Vérification de l'existence du PAGM à supprimer
      try {
         List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
         Pagm pagmASupprimer = null;
         boolean pagmExist = false;
         for (Pagm pagm : pagms) {
            if (pagm.getCode().equals(codePagm)) {
               pagmExist = true;
               pagmASupprimer = pagm;
            }
         }

         if (pagmExist) {
            LOGGER.debug("le pagm {} existe, suppression possible", codePagm);

            // Création du Mutator
            Mutator<String> mutator = createMutator();

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

      } catch (InvalidCacheLoadException e) {
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
   public final void modifierPagmContratService(String idContratService,
         SaePagm saePagm) {

      // Vérification de l'existence du contrat de service
      testExistenceCS(idContratService);

      // Vérification de l'existence du PAGM à modifier
      try {
         List<Pagm> pagms = pagmsCache.getUnchecked(idContratService);
         Pagm pagmASupprimer = null;
         boolean pagmExist = false;
         for (Pagm pagm : pagms) {
            if (pagm.getCode().equals(saePagm.getCode())) {
               pagmExist = true;
               pagmASupprimer = pagm;
            }
         }

         if (pagmExist) {
            LOGGER.debug("le pagm {} existe, modification possible", saePagm
                  .getCode());

            // Création du Mutator
            Mutator<String> mutator = createMutator();

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

      } catch (InvalidCacheLoadException e) {
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
   private void creerPagm(String idContratService, SaePagm saePagm,
         Mutator<String> mutator) {

      // Ajout du PAGMa
      SaePagma saePagma = saePagm.getPagma();
      Pagma pagma = new Pagma();
      pagma.setActionUnitaires(saePagma.getActionUnitaires());
      pagma.setCode(saePagma.getCode());
      pagmaSupport.create(pagma, clockSupport.currentCLock(), mutator);

      // Ajout du PAGMp
      SaePagmp saePagmp = saePagm.getPagmp();
      Pagmp pagmp = new Pagmp();
      pagmp.setCode(saePagmp.getCode());
      pagmp.setDescription(saePagmp.getDescription());
      pagmp.setPrmd(saePagmp.getPrmd());
      pagmpSupport.create(pagmp, clockSupport.currentCLock(), mutator);

      // Ajout du PAGMf
      SaePagmf saePagmf = saePagm.getPagmf();
      if (saePagmf != null) {
         Pagmf pagmf = new Pagmf();
         pagmf.setCodePagmf(saePagmf.getCodePagmf());
         pagmf.setDescription(saePagmf.getDescription());
         pagmf.setCodeFormatControlProfil(saePagmf.getFormatProfile());

         pagmfSupport.create(pagmf, clockSupport.currentCLock());
      }

      // Ajout du PAGM
      Pagm pagm = new Pagm();
      pagm.setCode(saePagm.getCode());
      pagm.setDescription(saePagm.getDescription());
      pagm.setPagma(saePagm.getPagma().getCode());
      pagm.setPagmp(saePagm.getPagmp().getCode());
      if (saePagm.getPagmf() != null) {
         pagm.setPagmf(saePagm.getPagmf().getCodePagmf());
      }
      pagm.setParametres(saePagm.getParametres());
      pagmSupport.create(idContratService, pagm, clockSupport.currentCLock(),
            mutator);

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
   private void supprimerPagm(String idContratService, Pagm pagmASupprimer,
         Mutator<String> mutator) {

      try {
         Pagma pagma = pagmasCache.getUnchecked(pagmASupprimer.getPagma());
         // Suppression du PAGMa
         pagmaSupport.delete(pagma.getCode(), clockSupport.currentCLock(),
               mutator);
      } catch (InvalidCacheLoadException e) {
         LOGGER
               .debug(
                     "Pas de PAGMa pour le pagm {}, aucune suppression de PAGMa à effectuer",
                     pagmASupprimer.getCode());
      }
      try {
         Pagmp pagmp = pagmpsCache.getUnchecked(pagmASupprimer.getPagmp());
         // Ajout du PAGMp
         pagmpSupport.delete(pagmp.getCode(), clockSupport.currentCLock(),
               mutator);
      } catch (InvalidCacheLoadException e) {
         LOGGER
               .debug(
                     "Pas de PAGMp pour le pagm {}, aucune suppression de PAGMp à effectuer",
                     pagmASupprimer.getCode());
      }
      try {
         if (pagmASupprimer.getPagmf() != null) {
            Pagmf pagmf = pagmfsCache.getUnchecked(pagmASupprimer.getPagmf());
            // Ajout du PAGMf
            pagmfSupport.delete(pagmf.getCodePagmf(), clockSupport
                  .currentCLock(), mutator);
         }
      } catch (InvalidCacheLoadException e) {
         LOGGER
               .debug(
                     "Pas de PAGMf pour le pagm {}, aucune suppression de PAGMf à effectuer",
                     pagmASupprimer.getCode());
      }

      pagmSupport.delete(idContratService, pagmASupprimer.getCode(),
            clockSupport.currentCLock(), mutator);

   }

   /**
    * Teste l'existence du CS
    * 
    * @param idContratService
    *           Identifiant du CS
    */
   private void testExistenceCS(String idContratService) {
      // Vérification de l'existence du contrat de service
      try {
         contratsCache.getUnchecked(idContratService);
      } catch (InvalidCacheLoadException e) {
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
   private Pagmf getPagmf(String codePagmf) {

      Pagmf pagmf = null;
      try {
         if (!StringUtils.isBlank(codePagmf)) {
            pagmf = pagmfsCache.getUnchecked(codePagmf);
         }

      } catch (InvalidCacheLoadException e) {
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
         String codeFormatControlProfil)
         throws FormatControlProfilNotFoundException {

      FormatControlProfil formatControlProfil;
      try {
         formatControlProfil = formatControlProfilsCache
               .getUnchecked(codeFormatControlProfil);
      } catch (InvalidCacheLoadException e) {
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

}
