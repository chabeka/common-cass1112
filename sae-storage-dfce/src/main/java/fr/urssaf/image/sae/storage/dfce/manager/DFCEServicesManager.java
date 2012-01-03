package fr.urssaf.image.sae.storage.dfce.manager;

import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import net.docubase.toolkit.service.ServiceProvider;

/**
 * Permet de fabriquer et détruire les services DFCE.
 * 
 * @author akenore
 * 
 */
public interface DFCEServicesManager {
	/**
	 * 
	 * @return Les services de DFCE.
	 * 
	 */
	ServiceProvider getDFCEService();

	/**
	 * 
	 * @throws ConnectionServiceEx
	 *             Exception levée lorsque la connexion ne s'est pas bien
	 *             déroulée.
	 */
	void getConnection() throws ConnectionServiceEx;

	/**
	 * 
	 * @return True si la connection est active.
	 *           
	 * 
	 */
	boolean isActive();

	/**
	 * 
	 * Ferme la connexion des services DFCE.
	 */
	void closeConnection();
}
