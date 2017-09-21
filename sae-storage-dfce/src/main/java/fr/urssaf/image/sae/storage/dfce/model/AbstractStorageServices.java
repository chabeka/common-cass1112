/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.model;

import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;

/**
 * Classe abstraite contient les attributs communs à toutes les implementations
 * de gestion de type storage document
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractStorageServices extends AbstractServices {

   /**
    * 
    * @return Les services DFCE
    */
   public abstract DFCEServicesManager getDfceServicesManager();
}
