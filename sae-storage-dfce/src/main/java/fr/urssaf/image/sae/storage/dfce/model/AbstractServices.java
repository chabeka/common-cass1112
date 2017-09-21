package fr.urssaf.image.sae.storage.dfce.model;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
   private ConnexionServiceProvider connexionServiceProvider;

   /**
    * Parametre de connexion de DFCe
    */
   @Autowired
   @Qualifier(value = "dfceConnection")
   private DFCEConnection cnxParameters;

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
            .getServiceProviderByConnectionParams(cnxParameters);
   }

   /**
    * @return Les paramètres de connection
    */
   public final DFCEConnection getCnxParameters() {
      return cnxParameters;
   }

   /**
    * @param cnxParameters
    *           Les paramètres de connection
    */
   public final void setCnxParameters(final DFCEConnection cnxParameters) {
      this.cnxParameters = cnxParameters;
   }

   /**
    * @return Une occurrence de la base DFCE.
    */
   public Base getBaseDFCE() {
      return storageDocumentServiceSupport
            .getBaseDFCE(connexionServiceProvider
                  .getServiceProviderByConnectionParams(cnxParameters),
                  cnxParameters);
   }

}
