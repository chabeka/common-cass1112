package fr.urssaf.image.sae.storage.dfce.model;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.service.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;

/**
 * Classe abstraite contenant les attributs communs de toutes les
 * implementations:
 * <ul>
 * <li>{@link InsertionServiceImpl } : Implementation de l'interface
 * InsertionServiceI.</li>
 * <li>{@link SearchingServiceImpl } : Implementation de l'interface
 * SearchingService</li>
 * <li>{@link RetrievalServiceImpl }</li> : Implementation de l'interface
 * RetrievalService
 * </ul>
 * Elle contient également l'attribut :
 * <ul>
 * <li>
 * Attribut storageBase : Classe concrète contenant le nom de la base de
 * stockage</li>
 * </ul>
 * 
 * @author akenore,rhofir.
 * 
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractServices {

	
	private ServiceProvider dfceService;

	@Autowired
	private StorageConnectionParameter cnxParameters;
	/**
	 * @param dfceService
	 *            : Les services DFCE.
	 */
	public final void setDfceService(final ServiceProvider dfceService) {
		this.dfceService = dfceService;
	}

	/**
	 * @return Les services DFCE.
	 */
	public final ServiceProvider getDfceService() {
		return dfceService;
	}

	

	/**
	 * @return Les paramètres de connection
	 */
	public final StorageConnectionParameter getCnxParameters() {
		return cnxParameters;
	}

	/**
	 * @param cnxParameters
	 *            Les paramètres de connection
	 */
	public final void setCnxParameters(
			final StorageConnectionParameter cnxParameters) {
		this.cnxParameters = cnxParameters;
	}
	
	 /**
	    * @return Une occurrence de la base DFCE.
	    */
	   public final Base getBaseDFCE() {
	      return dfceService.getBaseAdministrationService().getBase(
	    		  cnxParameters.getStorageBase().getBaseName());
	   }

}
