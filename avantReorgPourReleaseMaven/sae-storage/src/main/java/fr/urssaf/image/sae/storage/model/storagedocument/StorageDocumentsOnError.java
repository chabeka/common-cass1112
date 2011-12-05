package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant la liste des documents en erreur.<BR />
 * Elle contient l'attribut :
 * <ul>
 * <li>
 * storageDocumentsOnError : La liste des documents qui ont causé une erreur
 * d'archivage</li>
 * </ul>
 */
public class StorageDocumentsOnError {

	private List<StorageDocumentOnError> storageDocumentsOnError;// NOPMD

	/**
	 * Retourne la liste des documents en erreur
	 * 
	 * @return La liste des documents en erreur
	 */
	public final List<StorageDocumentOnError> getStorageDocumentsOnError() {
		return storageDocumentsOnError;
	}

	/**
	 * Initialise la liste des documents en erreur
	 * 
	 * @param storageDocumentsOnError
	 *            : La liste des documents en erreur
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageDocuments(
			final List<StorageDocumentOnError> storageDocumentsOnError) {
		this.storageDocumentsOnError = storageDocumentsOnError;
	}

	/**
	 * Construit un {@link StorageDocuments }.
	 * 
	 * @param storageDocumentsOnError
	 *            : Liste des documents en erreur
	 */
	@SuppressWarnings("PMD.LongVariable")
	public StorageDocumentsOnError(final 
			List<StorageDocumentOnError> storageDocumentsOnError) {
		this.storageDocumentsOnError = storageDocumentsOnError;
	}

	/**
	 * Construit un {@link StorageDocuments } par défaut.
	 */
	public StorageDocumentsOnError() {
		// ici on ne fait rien
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		@SuppressWarnings("PMD.LongVariable")
		final StringBuffer stringBuffer = new StringBuffer();
		if (storageDocumentsOnError != null) {
			for (StorageDocumentOnError storageDocumentOnError : storageDocumentsOnError) {
				stringBuffer.append(storageDocumentOnError.toString());
			}
		}
		return new ToStringBuilder(this).append("storageDocumentsOnError",
				stringBuffer.toString()).toString();
	}
}
