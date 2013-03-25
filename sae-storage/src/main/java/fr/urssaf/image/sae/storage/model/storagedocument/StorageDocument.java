package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant un document contenant un identifiant unique
 * suite à une insertion qui s’est bien déroulée. Elle contient l'attribut :
 * <ul>
 * <li>
 * uuid : L'uuid du document</li>
 * </ul>
 */

public class StorageDocument extends AbstractStorageDocument {
	// L'attribut
	private UUID uuid;

	/**
	 * Retourne l’identifiant unique universel
	 * 
	 * @return UUID du document
	 */
	public final UUID getUuid() {
		return uuid;
	}

	/**
	 * Initialise l’identifiant unique universel.
	 * 
	 * @param uuid
	 *            : L'identifiant universel unique
	 */
	public final void setUuid(final UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Construit un {@link StorageDocument }.
	 * 
	 * @param metadatas
	 *            : Les métadonnées du document
	 * @param content
	 *            : Le contenu du document
	 * 
	 */
	public StorageDocument(final List<StorageMetadata> metadatas,
			final byte[] content) {
		super(metadatas, content, null);

	}

	/**
	 * Construit un {@link StorageDocument }.
	 * 
	 * @param metadatas
	 *            : Les métadonnées du document
	 * 
	 */
	public StorageDocument(final List<StorageMetadata> metadatas) {
		super(metadatas, new byte[1], null);

	}

	/**
	 * Construit un {@link StorageDocument }.
	 * 
	 * @param storageDocument
	 *            : Un storageDocument
	 * 
	 */
	@SuppressWarnings("PMD.CallSuperInConstructor")
	public StorageDocument(final StorageDocument storageDocument) {
		setContent(storageDocument.getContent());
		setCreationDate(storageDocument.getCreationDate());
		setFilePath(storageDocument.getFilePath());
		setMetadatas(storageDocument.getMetadatas());
		setTitle(storageDocument.getTitle());
		setTypeDoc(storageDocument.getTypeDoc());
		setUuid(storageDocument.getUuid());

	}

	/**
	 * 
	 * Construit un {@link StorageDocument } par défaut.
	 */
	public StorageDocument() {
		super(new ArrayList<StorageMetadata>(), new byte[1], null);
	}
	/**
	 * Construit un {@link StorageDocument }.
	 * 
	 * @param metadatas
	 *            : Les métadonnées du document
	 * @param content
	 *            : Le contenu du document
	 * @param uuid
	 *            : l'uuid
	 * 
	 */
	public StorageDocument(final List<StorageMetadata> metadatas,
			final byte[] content, final UUID uuid) {
		super(metadatas, content, null);
		this.uuid = uuid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		@SuppressWarnings("PMD.LongVariable")
		final StringBuffer stringBuffer = new StringBuffer();
		if (getMetadatas() != null) {
			for (StorageMetadata metadata : getMetadatas()) {
				stringBuffer.append(metadata.toString());
			}
		}
		return new ToStringBuilder(this)
				.append("creationDate", getCreationDate())
				.append("content", getContent()).append("uuid", uuid)
				.append("filePath", getFilePath())
				.append("metadatas", stringBuffer.toString()).toString();
	}



}
