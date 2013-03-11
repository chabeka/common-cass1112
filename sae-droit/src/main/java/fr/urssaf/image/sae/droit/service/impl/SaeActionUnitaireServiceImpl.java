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
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.service.SaeActionUnitaireService;
import fr.urssaf.image.sae.droit.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link SaeActionUnitaireService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaeActionUnitaireServiceImpl implements SaeActionUnitaireService {

   private static final String CHECK = "checkActionUnitaireExiste";

   private static final String TRC_CREATE = "createActionUnitaire()";

   private static final String ACTION_UNITAIRE = "L'action unitaire ";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaePrmdServiceImpl.class);

   private static final String PREFIXE_AU = "/DroitActionUnitaire/";

   @Autowired
   private ActionUnitaireSupport actionSupport;

   @Autowired
   private JobClockSupport clockSupport;

   @Autowired
   private CuratorFramework curatorClient;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createActionUnitaire(ActionUnitaire actionUnitaire) {

      String resourceName = PREFIXE_AU + actionUnitaire.getCode();

      LOGGER
            .debug("{} - Debut de la création de l'action unitaire", TRC_CREATE);
      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curatorClient,
            resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         checkActionUnitaireExiste(actionUnitaire);

         actionSupport.create(actionUnitaire, clockSupport.currentCLock());

         checkLock(mutex, actionUnitaire);
         
         LOGGER.debug("{} - Fin de la création de l'action unitaire",
               TRC_CREATE);
      } finally {
         mutex.release();
      }
   }

   /**
    * Vérifie si l'action unitaire passée en paramètre existe déjà. Si c'est le
    * cas, soulève une exception {@link DroitRuntimeException}
    * 
    * @param actionUnitaire
    *           actionUnitaire à créer
    */
   private void checkActionUnitaireExiste(ActionUnitaire actionUnitaire) {

      ActionUnitaire storedAction = actionSupport
            .find(actionUnitaire.getCode());

      if (storedAction != null) {
         LOGGER
               .debug(
                     "{} - L'action unitaire {} existe déjà dans la famille de colonnes DroitActionUnitaire",
                     CHECK, actionUnitaire.getCode());
         throw new DroitRuntimeException(ACTION_UNITAIRE
               + actionUnitaire.getCode() + " existe déjà dans la "
               + "famille de colonnes DroitActionUnitaire");
      }

   }

   private void checkLock(ZookeeperMutex mutex, ActionUnitaire actionUnitaire) {
      if (!ZookeeperUtils.isLock(mutex)) {
         ActionUnitaire storedAction = actionSupport.find(actionUnitaire
               .getCode());

         if (storedAction == null) {
            throw new ActionUnitaireReferenceException(ACTION_UNITAIRE
                  + actionUnitaire.getCode() + " n'a pas été créée");
         }

         if (!actionUnitaire.equals(storedAction)) {
            throw new ActionUnitaireReferenceException(ACTION_UNITAIRE
                  + actionUnitaire.getCode() + " a déjà été créée");
         }
      }

   }

}
