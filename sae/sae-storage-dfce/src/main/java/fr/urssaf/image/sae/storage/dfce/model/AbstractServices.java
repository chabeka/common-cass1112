package fr.urssaf.image.sae.storage.dfce.model;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.util.ConnexionServiceProvider;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;

/**
 * Classe abstraite contenant les attributs communs de toutes les
 * implementations:
 * <ul>
 * <li>{@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl} : Implementation de l'interface
 * InsertionService.</li>
 * <li>{@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.SearchingServiceImpl} : Implementation de l'interface
 * SearchingService</li>
 * <li>{@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.RetrievalServiceImpl}</li> : Implementation de l'interface
 * RetrievalService
 * </ul>
 * Elle contient également l'attribut :
 * <ul>
 * <li>
 * Attribut storageBase : Classe concrète contenant le nom de la base de
 * stockage</li>
 * </ul>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractServices {

   /**
    * Provider de connexion du service DFCe
    */
   @Autowired
   protected ConnexionServiceProvider connexionServiceProvider;

   /**
    * Service utilitaire de mutualisation du code des implémentations des
    * services DFCE
    */
   @Autowired
   protected StorageDocumentServiceSupport storageDocumentServiceSupport;

   /**
    * @return Les services DFCE.
    */
   public final ServiceProvider getDfceService() {
      return connexionServiceProvider
            .getServiceProviderByConnectionParams(getCnxParameters());
   }

   /**
    * @return Les paramètres de connection
    */
   public abstract DFCEConnection getCnxParameters();


   /**
    * @return Une occurrence de la base DFCE.
    */
   public Base getBaseDFCE() {
      return storageDocumentServiceSupport
            .getBaseDFCE(connexionServiceProvider
                  .getServiceProviderByConnectionParams(getCnxParameters()),
                  getCnxParameters());
   }

}
