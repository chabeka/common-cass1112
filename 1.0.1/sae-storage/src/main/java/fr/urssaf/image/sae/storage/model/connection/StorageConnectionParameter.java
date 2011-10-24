package fr.urssaf.image.sae.storage.model.connection;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète contenant les caractéristiques de l’utilisateur mit à
 * disposition pour se connecter à la base de stockage. <BR />
 * Elle contient les attributs :
 * <ul>
 * <li>
 * storageBase : Représente les paramètres de la base de stockage</li>
 * <li>
 * storageHost : Représente les paramètres de la machine où est localise la base
 * </li>
 * <li>
 * storageUser : Représente les paramètres de l’utilisateur de connexion</li>
 * </ul>
 */
public class StorageConnectionParameter {
	// Les attributs
	private StorageBase storageBase;
	private StorageHost storageHost;
	private StorageUser storageUser;

	/**
	 * Retourne la base de stockage
	 * 
	 * @return La base de stockage
	 */
	public final StorageBase getStorageBase() {
		return storageBase;
	}

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
	 * Retourne la machine où se trouve la base de stockage
	 * 
	 * @return La machine qui héberge la base de stockage
	 */
	public final StorageHost getStorageHost() {
		return storageHost;
	}

	/**
	 * Initialise les paramètres d'accès à la base de stockage
	 * 
	 * @param storageHost
	 *            : Les paramètres d'accès à la base de stockage
	 */
	public final void setStorageHost(final StorageHost storageHost) {
		this.storageHost = storageHost;
	}

	/**
	 * Retourne les paramètres de l'utilisateur de connexion à la base de
	 * stockage
	 * 
	 * @return L'utilisateur
	 */
	public final StorageUser getStorageUser() {
		return storageUser;
	}

	/**
	 * Initialise les paramètres de l'utilisateur de connexion à la base de
	 * stockage
	 * 
	 * @param storageUser
	 *            : L'utilisateur de connexion à la base de stockage
	 */
	public final void setStorageUser(final StorageUser storageUser) {
		this.storageUser = storageUser;
	}

	/**
	 * Construit un nouveau {@link StorageConnectionParameter }
	 * 
	 * @param storageBase
	 *            : Le nom de la base de donnée de stockage
	 * @param storageHost
	 *            : Les paramètres de connexion à la base de stockage
	 * @param storageUser
	 *            : les paramètres de l'utilisateur de connexion à la base de
	 *            stockage
	 */
	public StorageConnectionParameter(final StorageBase storageBase,
			final StorageHost storageHost, final StorageUser storageUser) {
		this.storageBase = storageBase;
		this.storageHost = storageHost;
		this.storageUser = storageUser;
	}

	/**
	 * Construit un nouveau {@link StorageConnectionParameter } par défaut
	 * 
	 */
	public StorageConnectionParameter() {
		// ici on ne fait rien
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return new ToStringBuilder(this)
				.append("storageBase", storageBase.toString())
				.append("storageHost", storageHost.toString())
				.append("storageUser", storageUser.toString()).toString();
	}
}
