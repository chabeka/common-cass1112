package fr.urssaf.image.sae.storage.bouchon.model;

import fr.urssaf.image.sae.storage.model.connection.StorageBase;

/**
 * Classe abstrait contenant les attributs communs à toutes les implementations
 * des interfaces InsertionServiceImpl, SearchingServiceImpl,
 * RetrievalServiceImpl
 * 
 * @author akenore
 * 
 */
public abstract class AbstractDfceService extends AbstractServices {
	/**
	 * Constructeur de la classe
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public AbstractDfceService(final StorageBase storageBase) {
		super(storageBase);
	}
	
	/**
	 * Constructeur par défaut
	 * 
	 *
	 */
	public AbstractDfceService() {
		super();
	}
	
}
