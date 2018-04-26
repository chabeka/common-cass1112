package fr.urssaf.image.sae.storage.bouchon.model;

import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;

/**
 * Classe abstraite contenant les attributs communs à toutes les implementations
 * des interfaces StorageConnectionService et StorageServiceProvider. <li>
 * Attribut storageConnectionParameter : Classe concrète contenant les
 * paramètres de connexion à la base de stockage</li>
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractConnectionServiceProvider {
	@SuppressWarnings("PMD.LongVariable")
	private StorageConnectionParameter storageConnectionParameter;

	/**
	 * 
	 * @return Retourne les paramètres de connexion à la base de stockage
	 */
	public final StorageConnectionParameter getStorageConnectionParameter() {
		return storageConnectionParameter;
	}

	/**
	 * 
	 * @param storageConnectionParameter
	 *            : Initialise les paramètres de connexion à la base de stockage
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageConnectionParameter(
			final StorageConnectionParameter storageConnectionParameter) {
		this.storageConnectionParameter = storageConnectionParameter;
	}

	/**
	 * Constructeur
	 * 
	 * @param storageConnectionParameter
	 *            : les paramètres de connexion à la base de stockage
	 */
	@SuppressWarnings("PMD.LongVariable")
	public AbstractConnectionServiceProvider(
			final StorageConnectionParameter storageConnectionParameter) {
		this.storageConnectionParameter = storageConnectionParameter;
	}

	/**
	 * Constructeur
	 */
	public AbstractConnectionServiceProvider() {
		// Ici on ne fait rien
	}
}
