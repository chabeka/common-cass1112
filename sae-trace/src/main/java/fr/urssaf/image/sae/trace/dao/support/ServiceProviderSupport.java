package fr.urssaf.image.sae.trace.dao.support;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.commons.dfce.util.ConnexionServiceProvider;

/**
 * Support pour la gestion de la connexion à DFCE
 * 
 * 
 */
@Component
public class ServiceProviderSupport {

   /**
    * Provider de connexion du service DFCe
    */
   @Autowired
   private ConnexionServiceProvider connexionServiceProvider;

   /**
    * Paramètres de connection
    */
   private final DFCEConnection dfceConnection;

   /**
    * DFCe connection service
    */
   private final DFCEConnectionService dfceConnectionService;

   /**
    * Construteur
    * 
    * @param dfceConnectionService
    *           service de connexion à DFCE
    */
   @Autowired
   public ServiceProviderSupport(DFCEConnection dfceConnection,
         DFCEConnectionService dfceConnectionService) {

      Validate.notNull(dfceConnection, "'dfceConnection' is required");
      Validate.notNull(dfceConnectionService,
            "'dfceConnectionService' is required");

      this.dfceConnection = dfceConnection;
      this.dfceConnectionService = dfceConnectionService;
   }

   /**
    * connexion à DFCE
    */
   public final void connect() {
      if(connexionServiceProvider == null){
         connexionServiceProvider = new ConnexionServiceProvider();
      }
      ServiceProvider serviceProvider = connexionServiceProvider
            .getServiceProviderByConnectionParams(dfceConnection);
      if (serviceProvider == null
            || (serviceProvider != null && !serviceProvider.isSessionActive())) {
         connexionServiceProvider.removeServiceProvider(dfceConnection);
         serviceProvider = dfceConnectionService.openConnection();
         connexionServiceProvider.addServiceProvider(dfceConnection,
               serviceProvider);
      }
   }

   /**
    * déconnexion de DFCE
    */
   public final void disconnect() {

      // Test du null
      // Dans le cas par exemple où la connexion à DFCE n'a pas pu être établie
      if (connexionServiceProvider != null) {
         ServiceProvider dfceService = connexionServiceProvider
               .getServiceProviderByConnectionParams(dfceConnection);
         if (dfceService != null) {
            dfceService.disconnect();
         }
      }

   }

   /**
    * @return le {@link RecordManagerService}
    */
   public final RecordManagerService getRecordManagerService() {

      return connexionServiceProvider.getServiceProviderByConnectionParams(
            dfceConnection).getRecordManagerService();
   }

   /**
    * Renvoie le service d'accès aux journaux
    * 
    * @return le {@link ArchiveService}
    */
   public final ArchiveService getArchiveService() {
      return connexionServiceProvider.getServiceProviderByConnectionParams(
            dfceConnection).getArchiveService();
   }

   /**
    * Renvoie le service de recherche
    * 
    * @return le {@link SearchService}
    */
   public final SearchService getSearchService() {
      return connexionServiceProvider.getServiceProviderByConnectionParams(
            dfceConnection).getSearchService();
   }

   /**
    * Renvoie le service d'accès aux contenus
    * 
    * @return le {@link StoreService}
    */
   public final StoreService getStoreService() {
      return connexionServiceProvider.getServiceProviderByConnectionParams(
            dfceConnection).getStoreService();
   }

   /**
    * Getter pour connexionServiceProvider
    * 
    * @return the connexionServiceProvider
    */
   public ConnexionServiceProvider getConnexionServiceProvider() {
      return connexionServiceProvider;
   }

   /**
    * Setter pour connexionServiceProvider
    * 
    * @param connexionServiceProvider
    *           the connexionServiceProvider to set
    */
   public void setConnexionServiceProvider(
         ConnexionServiceProvider connexionServiceProvider) {
      this.connexionServiceProvider = connexionServiceProvider;
   }
}
