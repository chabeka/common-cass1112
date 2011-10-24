package fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Représente l’identifiant unique universel du document. <BR />
 * Elle contient l'attribut :
 * <ul>
 * <li>
 * uuid : L'identifiant unique universel recherché</li>
 * </ul>
 */
public class UUIDCriteria extends AbstractCriteria {
	// Attribut
	private UUID uuid;

	/**
	 * Retourne l’identifiant unique universel.
	 * 
	 * @return L'identifiant
	 */
	public final UUID getUuid() {
		return uuid;
	}

	/**
	 * Initialise l’identifiant unique universel
	 * 
	 * @param uuid
	 *            : L'identifiant
	 */
	public final void setUuid(final UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Construit un {@link UUIDCriteria}
	 * 
	 * @param uuid
	 *            : L'identifiant unique universel.
	 * @param desiredStorageMetadatas
	 *            : Les métadonnées de la recherche.
	 */
	@SuppressWarnings("PMD.LongVariable")
	public UUIDCriteria(final UUID uuid,
			final List<StorageMetadata> desiredStorageMetadatas) {
		super(desiredStorageMetadatas);
		this.uuid = uuid;
	}

}
