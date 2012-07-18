package fr.urssaf.image.sae.regionalisation.support.impl;

import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

/**
 * Classe d'implémentation du service {@link ServiceProviderSupport}
 * 
 * 
 */
@Component
public class ServiceProviderSupportImpl implements ServiceProviderSupport {

   private ServiceProvider serviceProvider;

   private final DFCEConnectionService dfceConnectionService;

   /**
    * 
    * @param dfceConnectionService
    *           service de connexion à DFCE
    */
   @Autowired
   public ServiceProviderSupportImpl(DFCEConnectionService dfceConnectionService) {

      this.dfceConnectionService = dfceConnectionService;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void connect() {
      serviceProvider = dfceConnectionService.openConnection();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void disconnect() {

      serviceProvider.disconnect();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SearchService getSearchService() {

      return serviceProvider.getSearchService();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final StoreService getStoreService() {

      return serviceProvider.getStoreService();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final BaseAdministrationService getBaseAdministrationService() {

      return serviceProvider.getBaseAdministrationService();
   }

}
