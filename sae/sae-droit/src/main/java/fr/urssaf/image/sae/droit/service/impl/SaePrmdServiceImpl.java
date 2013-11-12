/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.service.SaePrmdService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaePrmdService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaePrmdServiceImpl implements SaePrmdService {

   private static final String CHECK = "checkPrmdInexistant";

   private static final String PRMD = "Le PRMD ";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaePrmdServiceImpl.class);

   private static final String TRC_CREATE = "createPrmd()";
   private static final String TRC_EXISTS = "prmdExists()";

   private static final String TRC_FIND = "getPrmd()";

   private static final String PREFIXE_PRMD = "/DroitPrmd/";

   private final PrmdSupport prmdSupport;

   private final JobClockSupport clockSupport;

   private final CuratorFramework curatorClient;

   /**
    * constructeur
    * 
    * @param prmd
    *           {@link PrmdSupport}
    * @param clock
    *           {@link JobClockSupport}
    * @param curator
    *           {@link CuratorFramework}
    */
   @Autowired
   public SaePrmdServiceImpl(PrmdSupport prmd, JobClockSupport clock,
         CuratorFramework curator) {
      this.prmdSupport = prmd;
      this.clockSupport = clock;
      this.curatorClient = curator;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createPrmd(Prmd prmd) {

      String resourceName = PREFIXE_PRMD + prmd.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
         ZookeeperUtils.acquire(mutex, resourceName);

         LOGGER.debug("{} - Vérification PRMD inexistant", TRC_CREATE);
         checkPrmdInexistant(prmd);

         LOGGER.debug("{} - Création PRMD", TRC_CREATE);
         prmdSupport.create(prmd, clockSupport.currentCLock());

         checkLock(mutex, prmd);

      } finally {
         mutex.release();
      }
   }

   @Override
   public void modifyPrmd(Prmd prmd) {
      String resourceName = PREFIXE_PRMD + prmd.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
         ZookeeperUtils.acquire(mutex, resourceName);

         LOGGER.debug("{} - Vérification PRMD existant", TRC_CREATE);
         checkPrmdExistant(prmd);

         LOGGER.debug("{} - Création PRMD", TRC_CREATE);
         prmdSupport.create(prmd, clockSupport.currentCLock());

         checkLock(mutex, prmd);

      } finally {
         mutex.release();
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean prmdExists(String code) {

      LOGGER.debug("{} - Début de recherche du PRMD", TRC_EXISTS);
      boolean exists = false;
      Prmd storedPrmd = prmdSupport.find(code);

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
   private void checkPrmdInexistant(Prmd prmd) {

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
   private void checkPrmdExistant(Prmd prmd) {

      if (!prmdExists(prmd.getCode())) {

         LOGGER.warn("{} - Le PRMD à modifier {} n'existe pas dans la "
               + "famille de colonnes DroitPRMD", CHECK, prmd.getCode());
         throw new DroitRuntimeException(PRMD + prmd.getCode()
               + " à modifier n'existe pas dans la " + "famille de colonnes DroitPRMD");
      }

   }

   private void checkLock(ZookeeperMutex mutex, Prmd prmd) {

      if (!ZookeeperUtils.isLock(mutex)) {

         Prmd storedPrmd = prmdSupport.find(prmd.getCode());

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
   public final Prmd getPrmd(String code) {

      LOGGER.debug("{} - Début de la récupération du PRMD", TRC_FIND);
      Prmd storedPrmd = prmdSupport.find(code);
      LOGGER.debug("{} - Fin de la récupération du PRMD", TRC_FIND);

      return storedPrmd;
   }

}
