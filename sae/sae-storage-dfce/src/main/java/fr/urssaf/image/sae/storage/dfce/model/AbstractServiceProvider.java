package fr.urssaf.image.sae.storage.dfce.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;

/**
 * Classe abstraite contient les attributs communs à toutes les implementations
 * de l'interface {@link fr.urssaf.image.sae.storage.services.StorageServiceProvider}.
 * <ul>
 * <li>
 * storageConnectionParameter : Classe concrète contenant les paramètres de
 * connexion à la base de stockage</li>
 * </ul>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractServiceProvider extends AbstractStorageServices {
   @SuppressWarnings("PMD.LongVariable")
   @Autowired
   @Qualifier("dfceServicesManager")
   protected DFCEServicesManager dfceServicesManager;

}
