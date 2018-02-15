package fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria;

import java.util.List;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe abstraite contenant les attributs communs des différents critères de
 * recherche et de récupération.<BR />
 * Elle contient l'attribut :
 * <ul>
 * <li>
 * desiredStorageMetadatas : Représente la liste des métadonnées.
 * souhaitées dans le cadre d’une recherche ou d’une récupération d’un document.
 * </li>
 * </ul>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractCriteria {
	@SuppressWarnings("PMD.LongVariable")
	private List<StorageMetadata> desiredStorageMetadatas;

	/**
	 * Retourne la liste des métadonnées.
	 * 
	 * @return La liste des métadonnées.
	 */
	public final List<StorageMetadata> getDesiredStorageMetadatas() {
		return desiredStorageMetadatas;
	}

	/**
	 * Initialise la liste des métadonnées.
	 * 
	 * @param desiredStorageMetadatas
	 *            : La liste des métadonnées
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setDesiredStorageMetadatas(
			final List<StorageMetadata> desiredStorageMetadatas) {
		this.desiredStorageMetadatas = desiredStorageMetadatas;
	}

	/**
	 * Construit un {@link AbstractCriteria }.
	 * 
	 * @param desiredStorageMetadatas
	 *            : La liste des métadonnées souhaitée pour la recherche ou la
	 *            récupération d'un document.
	 */
	@SuppressWarnings("PMD.LongVariable")
	public AbstractCriteria(final List<StorageMetadata> desiredStorageMetadatas) {
		this.desiredStorageMetadatas = desiredStorageMetadatas;
	}
}
