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

   private static final String PREFIXE_PRMD = "/DroitPrmd/";

   @Autowired
   private PrmdSupport prmdSupport;

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private CuratorFramework curatorClient;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createPrmd(Prmd prmd) {

      String resourceName = PREFIXE_PRMD + prmd.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         checkPrmdInexistant(prmd);

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

      boolean exists = false;
      Prmd storedPrmd = prmdSupport.find(code);

      if (storedPrmd != null) {
         exists = true;
      }

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

}
