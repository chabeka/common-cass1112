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
public abstract class AbstractServiceProvider extends AbstractServices {
	@SuppressWarnings("PMD.LongVariable")
	@Autowired
	@Qualifier("dfceServicesManager")
	private DFCEServicesManager dfceServicesManager;
	/**
	 * 
	 * @param dfceServices
	 *            : Les services DFCE
	 */
	public void setDfceServicesManager(final DFCEServicesManager dfceServices) {
		this.dfceServicesManager = dfceServices;
	}

	/**
	 * 
	 * @return Les services DFCE
	 */
	public final DFCEServicesManager getDfceServicesManager() {
		return dfceServicesManager;
	}
}
