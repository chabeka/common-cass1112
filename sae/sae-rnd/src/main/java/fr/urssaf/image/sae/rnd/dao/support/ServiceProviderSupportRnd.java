package fr.urssaf.image.sae.rnd.dao.support;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;
import net.docubase.toolkit.service.ged.ArchiveService;
import net.docubase.toolkit.service.ged.RecordManagerService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

/**
 * Support pour la gestion de la connexion à DFCE
 * 
 * 
 */
@Component
public class ServiceProviderSupportRnd {

   private ServiceProvider serviceProvider;

   @Autowired
   private DFCEConnectionService dfceConnectionService;

    /**
    * connexion à DFCE
    */
   public final void connect() {
      serviceProvider = dfceConnectionService.openConnection();
   }

   /**
    * déconnexion de DFCE
    */
   public final void disconnect() {

      // Test du null
      // Dans le cas par exemple où la connexion à DFCE n'a pas pu être établie
      if (serviceProvider != null) {
         serviceProvider.disconnect();
      }

   }

   /**
    * @return le {@link RecordManagerService}
    */
   public final RecordManagerService getRecordManagerService() {

      return serviceProvider.getRecordManagerService();
   }
   
   /**
    * Renvoie le service d'accès aux journaux 
    * @return le {@link ArchiveService}
    */
   public final ArchiveService getArchiveService() {
      return serviceProvider.getArchiveService();
   }
   
   /**
    * Renvoie le service de recherche
    * @return le {@link SearchService}
    */
   public final SearchService getSearchService() {
      return serviceProvider.getSearchService();
   }
   
   /**
    * Renvoie le service d'accès aux contenus
    * @return le {@link StoreService}
    */
   public final StoreService getStoreService() {
      return serviceProvider.getStoreService();
   }
   
   /**
    * 
    * @return le {@link StorageAdministrationService}
    */
   public final StorageAdministrationService getStorageAdministrationService() {
      return serviceProvider.getStorageAdministrationService();
   }

}
