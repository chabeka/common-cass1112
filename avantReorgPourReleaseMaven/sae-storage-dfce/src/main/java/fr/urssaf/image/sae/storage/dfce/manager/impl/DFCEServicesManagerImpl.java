package fr.urssaf.image.sae.storage.dfce.manager.impl;

import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;

/**
 * Permet de fabriquer et détruire les services DFCE.
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("dfceServicesManager")
public class DFCEServicesManagerImpl implements DFCEServicesManager {
	@Autowired
	private StorageConnectionParameter cnxParameters;

	private ServiceProvider dfceService;

	/**
	 * @param service
	 *            the service to set
	 */
	public final void setDfceService(final ServiceProvider service) {
		this.dfceService = service;
	}

	/**
	 * @return the service
	 */
	public final ServiceProvider getDfceService() {
		return dfceService;
	}

	/**
	 * @param cnxParameters
	 *            the cnxParameters to set
	 */
	public final void setCnxParameters(
			final StorageConnectionParameter cnxParameters) {
		this.cnxParameters = cnxParameters;
	}

	/**
	 * @return the cnxParameters
	 */
	public final StorageConnectionParameter getCnxParameters() {
		return cnxParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void closeConnection() {
		if (dfceService.isServerUp()||dfceService.isSessionActive()) {
			dfceService.disconnect();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public final ServiceProvider getDFCEService() {

		return dfceService;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void getConnection() throws ConnectionServiceEx {
		try {
			// ici on synchronise l'appel de la méthode connect.
			synchronized (this) {
				if (!isDFCEServiceValid()) {
					dfceService = ServiceProvider.newServiceProvider();
					dfceService.connect(cnxParameters.getStorageUser()
							.getLogin(), cnxParameters.getStorageUser()
							.getPassword(), Utils
							.buildUrlForConnection(cnxParameters));
				}
			}
		} catch (Exception except) {
			throw new ConnectionServiceEx(
					StorageMessageHandler.getMessage(Constants.CNT_CODE_ERROR),
					except.getMessage(), except);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public final boolean isActive() {
		return dfceService.isSessionActive();
	}

	/**
	 * 
	 * @return True si le service DFCE est valide.
	 */
	private boolean isDFCEServiceValid() {
		return dfceService != null && dfceService.isServerUp()
				&& dfceService.isSessionActive();
	}

}
