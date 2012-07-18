package fr.urssaf.image.sae.regionalisation.support;

import net.docubase.toolkit.service.administration.BaseAdministrationService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

/**
 * Service contenant les opérations concernant les critères de recherche.
 * 
 * 
 */
public interface ServiceProviderSupport {

   /**
    * Connexion à DFCE
    */
   void connect();

   /**
    * déconnection de DFCE
    */
   void disconnect();

   /**
    * Retourne le service de sauvegarde de données
    * 
    * @return service de sauvegarde des données
    */
   StoreService getStoreService();

   /**
    * Retourne le service de recherche
    * 
    * @return service de recherche
    */
   SearchService getSearchService();

   /**
    * Retourne le service d'administration
    * 
    * @return service d'administration
    */
   BaseAdministrationService getBaseAdministrationService();
}
