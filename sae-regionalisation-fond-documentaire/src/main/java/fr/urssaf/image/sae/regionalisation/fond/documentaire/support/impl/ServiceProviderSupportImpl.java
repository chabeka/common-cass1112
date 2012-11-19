package fr.urssaf.image.sae.regionalisation.fond.documentaire.support.impl;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.ServiceProviderSupport;

/**
 * Classe d'implémentation du service {@link ServiceProviderSupport}
 * 
 * 
 */
@Component
public class ServiceProviderSupportImpl implements ServiceProviderSupport {

   private ServiceProvider serviceProvider;

   private final DFCEConnectionService dfceConnectionService;

   private final String baseName;

   /**
    * 
    * @param dfceConnectionService
    *           service de connexion à DFCE
    * @param baseName
    *           nom de la base
    */
   @Autowired
   public ServiceProviderSupportImpl(
         DFCEConnectionService dfceConnectionService,
         @Qualifier("base_regionalisation") String baseName) {

      this.dfceConnectionService = dfceConnectionService;
      this.baseName = baseName;

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
   public final Base getBase() {

      return serviceProvider.getBaseAdministrationService().getBase(baseName);
   }

}
