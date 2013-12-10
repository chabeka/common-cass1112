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
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmaNotFoundException;
import fr.urssaf.image.sae.droit.service.SaePagmaService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaePagmaService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaePagmaServiceImpl implements SaePagmaService {

   private static final String CHECK_NOT_EXISTS = "checkPagmaNotExists";
   private static final String CHECK_EXISTS = "checkPagmaNotExists";
   private static final String TRC_CREATE = "createPagma";
   private static final String TRC_MODIFIER = "modifierPagma";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaePagmaServiceImpl.class);

   private static final String PREFIXE_PAGMA = "/DroitPagma/";

   private final CuratorFramework curatorClient;

   private final PagmaSupport pagmaSupport;

   private final ActionUnitaireSupport actionSupport;

   private final JobClockSupport clockSupport;

   
   /**
    * constructeur
    * @param action {@link ActionUnitaireSupport}
    * @param pagma {@link PagmaSupport}
    * @param clock {@link JobClockSupport}
    * @param curator {@link CuratorFramework}
    */   
   @Autowired
   public SaePagmaServiceImpl(ActionUnitaireSupport action, PagmaSupport pagma, JobClockSupport clock, CuratorFramework curator){
      this.pagmaSupport = pagma;
      this.actionSupport = action;
      this.clockSupport = clock;
      this.curatorClient = curator;
   }

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void createPagma(Pagma pagma) {

      LOGGER.debug("{} - Début de la création du pagma {}", TRC_CREATE, pagma.getCode());
      
      String resourceName = PREFIXE_PAGMA + pagma.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         LOGGER.debug("{} - Vérification que le pagma {} n'existe pas", TRC_CREATE, pagma.getCode());
         checkPagmaNotExists(pagma);
         LOGGER.debug("{} - vérification que les actions unitaires rattachées au pagma {} existent", TRC_CREATE, pagma.getCode());
         checkActionsUnitairesExist(pagma);

         pagmaSupport.create(pagma, clockSupport.currentCLock());

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
   public void modifierPagma(Pagma pagma) throws PagmaNotFoundException {
      LOGGER.debug("{} - Début de la modification du pagma {}", TRC_MODIFIER, pagma.getCode());
      
      String resourceName = PREFIXE_PAGMA + pagma.getCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         LOGGER.debug("{} - Vérification que le pagma {} existe", TRC_MODIFIER, pagma.getCode());
         checkPagmaExists(pagma);
         LOGGER.debug("{} - vérification que les actions unitaires rattachées au pagma {} existent", TRC_MODIFIER, pagma.getCode());
         checkActionsUnitairesExist(pagma);

         pagmaSupport.create(pagma, clockSupport.currentCLock());

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
   public boolean isPagmaExiste(Pagma pagma) {
      if (pagmaSupport.find(pagma.getCode()) != null) {
         return true;
      } else {
         return false;
      }
   }
   
   private void checkLock(ZookeeperMutex mutex, Pagma pagma) {
      if (!ZookeeperUtils.isLock(mutex)) {

         String code = pagma.getCode();

         Pagma storedPagma = pagmaSupport.find(code);
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
   private void checkActionsUnitairesExist(Pagma pagma) {
      for (String action : pagma.getActionUnitaires()) {
         if (actionSupport.find(action) == null) {
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
   private void checkPagmaNotExists(Pagma pagma) {
      if (pagmaSupport.find(pagma.getCode()) != null) {
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
   private void checkPagmaExists(Pagma pagma) throws PagmaNotFoundException {
      if (pagmaSupport.find(pagma.getCode()) == null) {
         LOGGER
               .warn(
                     "{} - Le PAGMa {} n'existe pas dans la famille de colonne DroitPagma",
                     CHECK_EXISTS, pagma.getCode());
         throw new PagmaNotFoundException("Le PAGMa " + pagma.getCode()
               + " n'existe pas dans la famille de colonne DroitPagma");
      }

   }


}
