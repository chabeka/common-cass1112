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
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.service.SaePagmpService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaePagmpService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaePagmpServiceImpl implements SaePagmpService {

   private static final String CHECK = "checkPagmpInexistant";

   private static final String PAGMP = "Le PAGMp ";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaePagmpServiceImpl.class);

   private static final String PREFIXE_PAGMP = "/DroitPagmp/";

   @Autowired
   private CuratorFramework curatorClient;

   @Autowired
   private PagmpSupport pagmpSupport;

   @Autowired
   private PrmdSupport prmdSupport;

   @Autowired
   private JobClockSupport clockSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createPagmp(Pagmp pagmp) {

      String resourceName = PREFIXE_PAGMP + pagmp.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         checkPagmpInexistant(pagmp);
         checkPrmdExiste(pagmp);

         pagmpSupport.create(pagmp, clockSupport.currentCLock());

         checkLock(mutex, pagmp);

      } finally {
         mutex.release();
      }
   }

   /**
    * Vérifie que le pagmp n'existe pas. S'il existe, une exception est levée
    * 
    * @param pagmp
    *           le pagmp à créer
    */
   private void checkPagmpInexistant(Pagmp pagmp) {

      Pagmp storedPagmp = pagmpSupport.find(pagmp.getCode());

      if (storedPagmp != null) {
         LOGGER
               .warn(
                     "{} - Le PAGMp {} existe déjà dans la famille de colonne DroitPagmp",
                     CHECK, pagmp.getCode());
         throw new DroitRuntimeException(PAGMP + pagmp.getCode()
               + " existe déjà dans la famille de colonne DroitPagmp");
      }

   }

   /**
    * Vérifie que le Prmd existe. Si ce n'est pas le cas, exception
    * {@link PrmdReferenceException} levée
    * 
    * @param pagmp
    *           le pagmp
    */
   private void checkPrmdExiste(Pagmp pagmp) {
      Prmd prmd = prmdSupport.find(pagmp.getPrmd());

      if (prmd == null) {
         throw new PrmdReferenceException("Le PRMD " + pagmp.getPrmd()
               + " n'a pas été trouvé "
               + "dans la famille de colonnes DroitPrmd");
      }

   }

   private void checkLock(ZookeeperMutex mutex, Pagmp pagmp) {

      if (!ZookeeperUtils.isLock(mutex)) {

         Pagmp storedPagmp = pagmpSupport.find(pagmp.getCode());

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
}
