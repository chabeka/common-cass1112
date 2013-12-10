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
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmpNotFoundException;
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
   
   private static final String TRC_CREATE = "createPagmp()";
   private static final String TRC_MODIFIER = "modifierPagmp()";
   
   private static final String PAGMP = "Le PAGMp ";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaePagmpServiceImpl.class);

   private static final String PREFIXE_PAGMP = "/DroitPagmp/";

   private final CuratorFramework curatorClient;

   private final PagmpSupport pagmpSupport;

   private final PrmdSupport prmdSupport;

   private final JobClockSupport clockSupport;
   
   /**
    * constructeur
    * @param pagmp {@link PagmpSupport}
    * @param prmd {@link PrmdSupport}
    * @param clock {@link JobClockSupport}
    * @param curator {@link CuratorFramework}
    */   
   @Autowired
   public SaePagmpServiceImpl(PagmpSupport pagmp, PrmdSupport prmd, JobClockSupport clock, CuratorFramework curator){
      this.pagmpSupport = pagmp;
      this.prmdSupport = prmd;
      this.clockSupport = clock;
      this.curatorClient = curator;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createPagmp(Pagmp pagmp) {

      LOGGER.debug("{} - Début de la création du pagmp {}", TRC_CREATE, pagmp.getCode());
      String resourceName = PREFIXE_PAGMP + pagmp.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         LOGGER.debug("{} - Vérification que le pagmp {} n'existe pas", TRC_CREATE, pagmp.getCode());
         checkPagmpInexistant(pagmp);
         LOGGER.debug("{} - Vérification que le prmd rattaché au pagmp {} existe", TRC_CREATE, pagmp.getCode());
         checkPrmdExiste(pagmp);

         pagmpSupport.create(pagmp, clockSupport.currentCLock());

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
   public final void modifierPagmp(Pagmp pagmp) throws PagmpNotFoundException {

      LOGGER.debug("{} - Début de la modification du pagmp {}", TRC_MODIFIER, pagmp.getCode());
      String resourceName = PREFIXE_PAGMP + pagmp.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         LOGGER.debug("{} - Vérification que le pagmp {} existe bien", TRC_MODIFIER, pagmp.getCode());
         checkPagmpExistant(pagmp);
         LOGGER.debug("{} - Vérification que le prmd rattaché au pagmp {} existe", TRC_MODIFIER, pagmp.getCode());
         checkPrmdExiste(pagmp);

         pagmpSupport.create(pagmp, clockSupport.currentCLock());

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
   public boolean isPagmpExiste(Pagmp pagmp) {
      Pagmp storedPagmp = pagmpSupport.find(pagmp.getCode());
      if (storedPagmp != null) {
         return true;
      } else {
         return false;
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
    * Vérifie que le pagmp existe déjà. S'il n'existe pas, une exception est levée
    * 
    * @param pagmp
    *           le pagmp à modifier
    * @throws PagmpNotFoundException 
    */
   private void checkPagmpExistant(Pagmp pagmp) throws PagmpNotFoundException {

      Pagmp storedPagmp = pagmpSupport.find(pagmp.getCode());

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
