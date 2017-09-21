package fr.urssaf.image.sae.metadata.dfce;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.administration.StorageAdministrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

/**
 * Support pour la gestion de la connexion à DFCE
 * 
 * 
 */
@Component
public class ServiceProviderSupportMetadata {

   private ServiceProvider serviceProvider;

   private final DFCEConnectionService dfceConnectionService;

   /**
    * 
    * @param dfceConnectionService
    *           service de connexion à DFCE
    */
   @Autowired
   public ServiceProviderSupportMetadata(DFCEConnectionService dfceConnectionService) {

      this.dfceConnectionService = dfceConnectionService;
   }

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
    * @return le {@link BaseAdministrationService}
    */
   public final BaseAdministrationService getBaseAdministrationService() {

      return serviceProvider.getBaseAdministrationService();
   }
   
   /**
    * @return le {@link StorageAdministrationService}
    */
   public final StorageAdministrationService getStorageAdministrationService() {

      return serviceProvider.getStorageAdministrationService();
   }
   

}
