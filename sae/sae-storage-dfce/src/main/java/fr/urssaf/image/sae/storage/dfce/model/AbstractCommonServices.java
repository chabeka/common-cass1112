package fr.urssaf.image.sae.storage.dfce.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;

/**
 * Classe abstraite contenant l'attribut commun de connexion de toutes les
 * implementations:
 * <ul>
 * <li>
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.InsertionServiceImpl}
 * : Implementation de l'interface InsertionService.</li>
 * <li>
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.SearchingServiceImpl}
 * : Implementation de l'interface SearchingService</li>
 * <li>
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.DeletionServiceImpl}
 * </li> : Implementation de l'interface DeletionService
 * <li>
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.UpdateServiceImpl}
 * </li> : Implementation de l'interface UpdateService
 * </ul>
 * Elle contient également l'attribut :
 * <ul>
 * <li>
 * Attribut storageBase : Classe concrète contenant le nom de la base de
 * stockage</li>
 * </ul>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractCommonServices extends AbstractServices {

   /**
    * Parametre de connexion de DFCe
    */
   @Autowired
   @Qualifier(value = "dfceConnection")
   protected DFCEConnection cnxParameters;

   /**
    * {@inheritDoc}
    */
   @Override
   public DFCEConnection getCnxParameters() {
      return cnxParameters;
   }
}
