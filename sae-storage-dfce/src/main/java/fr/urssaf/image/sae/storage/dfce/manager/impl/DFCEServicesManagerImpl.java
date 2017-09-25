package fr.urssaf.image.sae.storage.dfce.manager.impl;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.commons.dfce.util.ConnexionServiceProvider;
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

   /**
    * Paramètres de connection
    */
   private DFCEConnection cnxParameters;


   /**
    * Service de connection à DFCE
    */
   private DFCEConnectionService dfceConnectionService;

   /**
    * Provider de connexion du service DFCe
    */
   @Autowired
   private ConnexionServiceProvider connexionServiceProvider;

   /**
    * Constructeur
    * 
    * @param dfceConnection
    *           Paramétrage DFCE
    */
   @Autowired
   public DFCEServicesManagerImpl(DFCEConnection dfceConnection,
         DFCEConnectionService dfceConnectionService) {
      this.cnxParameters = dfceConnection;
      this.dfceConnectionService = dfceConnectionService;
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
   @Override
   public final DFCEConnection getCnxParameters() {
      return cnxParameters;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void closeConnection() {
      ServiceProvider dfceService = connexionServiceProvider
            .getServiceProviderByConnectionParams(cnxParameters);
      if (dfceService.isSessionActive()) {
         dfceService.disconnect();
         connexionServiceProvider.removeServiceProvider(cnxParameters);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void getConnection() throws ConnectionServiceEx {
      getConnection(Boolean.FALSE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void getConnection(boolean forceReconnection)
         throws ConnectionServiceEx {
      try {
         String prefixLog = "getConnection()";
         // ici on synchronise l'appel de la méthode connect.
         synchronized (this) {
            if (forceReconnection || !isActive()) {
               openConnection();
            } else {
               LOGGER.debug(
                     "{} - Réutilisation de la connexion existante à DFCE",
                     prefixLog);
            }
         }
      } catch (Exception e) {
         throw new ConnectionServiceEx(StorageMessageHandler
               .getMessage(Constants.CNT_CODE_ERROR), e.getMessage(), e);
      }
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final boolean isActive() {
      ServiceProvider dfceService = connexionServiceProvider
            .getServiceProviderByConnectionParams(cnxParameters);
      return dfceService != null && dfceService.isSessionActive();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void openConnection() {
      String prefixLog = "openConnection()";
      LOGGER.debug("{} - Etablissement d'une nouvelle connexion à DFCE",
            prefixLog);
      ServiceProvider dfceService = dfceConnectionService.openConnection();

      connexionServiceProvider.addServiceProvider(cnxParameters, dfceService);
      LOGGER.debug("{} - Connexion établie", prefixLog);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ServiceProvider getDFCEService() {

      return connexionServiceProvider
            .getServiceProviderByConnectionParams(cnxParameters);
   }

   /**
    * Getter pour dfceConnectionService
    * 
    * @return the dfceConnectionService
    */
   public DFCEConnectionService getDfceConnectionService() {
      return dfceConnectionService;
   }

}
