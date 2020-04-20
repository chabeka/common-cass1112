package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant la liste des documents.<BR />
 * Elle contient l'attribut :
 * <ul>
 * <li>
 * allStorageDocuments : La liste des documents.</li>
 * </ul>
 */
@SuppressWarnings("PMD.LongVariable")
public class StorageDocuments {

	private List<StorageDocument> allStorageDocuments;

	/**
	 * Retourne la liste des documents
	 * 
	 * @return La liste des documents
	 */
	public final List<StorageDocument> getAllStorageDocuments() {
		return allStorageDocuments;
	}

	/**
	 * Initialise la liste des documents
	 * 
	 * @param storageDocuments
	 *            : La liste des documents
	 */
	public final void setAllStorageDocuments(
			final List<StorageDocument> storageDocuments) {
		this.allStorageDocuments = storageDocuments;
	}

	/**
	 * Construit un {@link StorageDocuments }.
	 * 
	 * @param storageDocuments
	 *            La liste des documents
	 */
	public StorageDocuments(final List<StorageDocument> storageDocuments) {
		this.allStorageDocuments = storageDocuments;
	}

	/**
	 * Construit un {@link StorageDocuments } par défaut.
	 * 
	 */
	public StorageDocuments() {
		// ici on ne fait rien
	}

	/**
	 * {@inheritDoc}
	 */

	public final String toString() {
		final StringBuffer stringBuffer = new StringBuffer();
		if (!allStorageDocuments.isEmpty()) {
			for (StorageDocument storageDocument : allStorageDocuments) {
				if (storageDocument != null) {
					stringBuffer.append(storageDocument.toString());
				}
			}
		}
		return new ToStringBuilder(this).append("storageDocuments",
				stringBuffer.toString()).toString();
	}
}
