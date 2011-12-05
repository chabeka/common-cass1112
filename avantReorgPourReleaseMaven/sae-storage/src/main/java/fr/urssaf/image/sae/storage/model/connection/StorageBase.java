package fr.urssaf.image.sae.storage.model.connection;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète contenant le nom de la base de stockage. <br/>
 * Elle contient l'attribut : <br/>
 * <ul>
 * <li>
 * baseName : Représente le nom de la base de stockage.</li>
 * </ul>
 */
public class StorageBase {
	// Les attributs
	private String baseName;

	/**
	 * Retourne le nom de la base de stockage
	 * 
	 * @return Le nom de la base de stockage
	 */
	public final String getBaseName() {
		return baseName;
	}

	/**
	 * Initialise nom de la base de stockage
	 * 
	 * @param baseName
	 *            : Le nom de la base de stockage
	 */
	public final void setBaseName(final String baseName) {
		this.baseName = baseName;
	}

	/**
	 * Construit un nouveau {@link StorageBase } avec e nom de la base de
	 * stockage
	 * 
	 * @param baseName
	 *            : Le nom de la base de stockage
	 */
	public StorageBase(final String baseName) {
		this.baseName = baseName;
	}

	/**
	 * Construit un nouveau {@link StorageBase } par défaut.
	 */
	public StorageBase() {
		// ici on ne fait rien
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return new ToStringBuilder(this).append("baseName", baseName)
				.toString();
	}
}
