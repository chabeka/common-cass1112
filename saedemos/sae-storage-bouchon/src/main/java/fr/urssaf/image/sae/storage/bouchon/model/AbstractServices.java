package fr.urssaf.image.sae.storage.bouchon.model;

import fr.urssaf.image.sae.storage.model.connection.StorageBase;

/**
 * Classe abstraite contenant les attributs communs � toutes les impl�mentations
 * des interfaces. <li>
 * Attribut storageBase : Classe concrète contenant le nom de la base de
 * stockage</li>
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD")
public abstract class AbstractServices {

	private StorageBase storageBase;

	/**
	 * Initialise la base de stockage
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public final void setStorageBase(final StorageBase storageBase) {
		this.storageBase = storageBase;
	}

	/**
	 * 
	 * @return Retourne la base de stockage.
	 */
	public final StorageBase getStorageBase() {
		return storageBase;
	}

	/**
	 * Constructeur de la classe
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public AbstractServices(final StorageBase storageBase) {
		this.storageBase = storageBase;
	}
	/**
	 * Constructeur par défaut
	 */
	public AbstractServices() {
			//ici on fait rien
	}
	
}
