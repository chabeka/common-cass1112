package fr.urssaf.image.sae.storage.dfce.manager.impl;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Permet de fabriquer et détruire les services DFCE.
 * 
 */
@Service
@Qualifier("dfceServicesManager")
public class DFCEServicesManagerImpl implements DFCEServicesManager {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DFCEServicesManagerImpl.class);

   @Autowired
   private DFCEConnection cnxParameters;

   private ServiceProvider dfceService;

   /**
    * @param service
    *           the service to set
    */
   public final void setDfceService(final ServiceProvider service) {
      this.dfceService = service;
   }

   /**
    * @return the service
    */
   public final ServiceProvider getDfceService() {
      return dfceService;
   }

   /**
    * @param cnxParameters
    *           the cnxParameters to set
    */
   public final void setCnxParameters(final DFCEConnection cnxParameters) {
      this.cnxParameters = cnxParameters;
   }

   /**
    * @return the cnxParameters
    */
   public final DFCEConnection getCnxParameters() {
      return cnxParameters;
   }

   /**
    * {@inheritDoc}
    */
   public final void closeConnection() {
      if (dfceService.isServerUp() || dfceService.isSessionActive()) {
         dfceService.disconnect();
      }

   }

   /**
    * {@inheritDoc}
    */
   public final ServiceProvider getDFCEService() {

      return dfceService;
   }

   /**
    * {@inheritDoc}
    */
   public final void getConnection() throws ConnectionServiceEx {
      getConnection(Boolean.FALSE);
   }

   /**
    * {@inheritDoc}
    */
   public final void getConnection(boolean forceReconnection)
         throws ConnectionServiceEx {
      try {
         String prefixLog = "getConnection()";
         // ici on synchronise l'appel de la méthode connect.
         synchronized (this) {
            if (forceReconnection || !isDFCEServiceValid()) {
               LOGGER.debug(
                     "{} - Etablissement d'une nouvelle connexion à DFCE",
                     prefixLog);
               dfceService = ServiceProvider.newServiceProvider();
               dfceService.connect(cnxParameters.getLogin(), cnxParameters
                     .getPassword(), cnxParameters.getServerUrl().toString(),
                     cnxParameters.getTimeout());
            } else {
               LOGGER.debug(
                     "{} - Réutilisation de la connexion existante à DFCE",
                     prefixLog);
            }
         }
      } catch (Exception except) {
         throw new ConnectionServiceEx(StorageMessageHandler
               .getMessage(Constants.CNT_CODE_ERROR), except.getMessage(),
               except);
      }
   }

   /**
    * {@inheritDoc}
    * 
    */
   public final boolean isActive() {
      return dfceService.isSessionActive();
   }

   /**
    * 
    * @return True si le service DFCE est valide.
    */
   private boolean isDFCEServiceValid() {
      return dfceService != null && dfceService.isServerUp()
            && dfceService.isSessionActive();
   }

}
