package fr.urssaf.image.sae.regionalisation.support;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

/**
 * Service contenant les opérations DFCE.
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
    * Retourne la base de la régionalisation
    * 
    * @return base DFCE
    */
   Base getBase();

   /**
    * Modifie la métadonnée d'un document
    * 
    * @param document
    *           document SAE
    * @param key
    *           code court de la métadonnée
    * @param value
    *           valeur de la métadonnée
    */
   void updateCriterion(Document document, String key, Object value);
}
