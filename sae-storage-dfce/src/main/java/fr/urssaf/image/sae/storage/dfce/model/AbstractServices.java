package fr.urssaf.image.sae.storage.dfce.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import net.docubase.toolkit.model.base.Base;

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

   @Autowired
   @Qualifier("dfceServices")
   protected DFCEServices dfceServices;

   /**
    * Service utilitaire de mutualisation du code des implémentations des
    * services DFCE
    */
   @Autowired
   protected StorageDocumentServiceSupport storageDocumentServiceSupport;


   /**
    * @return Une occurrence de la base DFCE.
    */
   public Base getBaseDFCE() {
      return dfceServices.getBase();
   }

   public DFCEServices getDfceServices() {
      return dfceServices;
   }

   public void setDfceServices(final DFCEServices dfceServices) {
      this.dfceServices = dfceServices;
   }

}
