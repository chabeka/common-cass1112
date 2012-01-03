package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Classe concrète représentant le résultat de l’insertion en masse<BR>
 * Elle contient les attributs :
 * <ul>
 * <li>
 * storageDocuments : la liste des documents archivés</li>
 * <li>
 * storageDocumentsOnError : la liste des documents en erreur</li>
 * </ul>
 */

public class BulkInsertionResults {

	private StorageDocuments storageDocuments;
	@SuppressWarnings("PMD.LongVariable")
	private StorageDocumentsOnError storageDocumentsOnError;

	/**
	 * Retourne la liste des documents archivés
	 * 
	 * @return Liste des documents archivés
	 */
	public final StorageDocuments getStorageDocuments() {
		return storageDocuments;
	}

	/**
	 * Retourne la liste des documents en erreur
	 * 
	 * @param storageDocuments
	 *            : Liste des documents en erreur
	 */
	public final void setStorageDocuments(
			final StorageDocuments storageDocuments) {
		this.storageDocuments = storageDocuments;
	}

	/**
	 * Retourne la liste des documents en erreur
	 * 
	 * @return la liste des documents en erreur
	 */
	public final StorageDocumentsOnError getStorageDocumentsOnError() {
		return storageDocumentsOnError;
	}

	/**
	 * Initialise la liste des documents en erreur
	 * 
	 * @param storageDocumentsOnError
	 *            : Liste des documents en erreur
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageDocumentsOnError(
			final StorageDocumentsOnError storageDocumentsOnError) {
		this.storageDocumentsOnError = storageDocumentsOnError;
	}

	/**
	 * Retourne la liste des UUID des documents dont l’insertion en masse s’est
	 * bien déroulée
	 * 
	 * @return liste des UUIDs
	 */
	public final List<UUID> getUuids() {
		final List<UUID> listUuids = new ArrayList<UUID>();
		if (storageDocuments != null
				&& (storageDocuments.getAllStorageDocuments() != null)) {
			// Ici on parcour tous les documents pour récuperer les uuids
			for (StorageDocument document : storageDocuments
					.getAllStorageDocuments()) {
				listUuids.add(document.getUuid());
			}
		}
		return listUuids;
	}

	/**
	 * Construit un nouveau {@link BulkInsertionResults }.
	 * 
	 * @param storageDocuments
	 *            : Les documents archivés
	 * @param storageDocumentsOnError
	 *            : Les documents en erreur
	 * 
	 */
	@SuppressWarnings("PMD.LongVariable")
	public BulkInsertionResults(final StorageDocuments storageDocuments,
			final StorageDocumentsOnError storageDocumentsOnError) {
		this.storageDocuments = storageDocuments;
		this.storageDocumentsOnError = storageDocumentsOnError;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		final StringBuffer strBufferDocOnEr = new StringBuffer();
		final StringBuffer stringBufferDoc = new StringBuffer();
		if (storageDocumentsOnError != null) {
			strBufferDocOnEr.append(storageDocumentsOnError.toString());
		}
		if (storageDocuments != null) {
			stringBufferDoc.append(storageDocuments.toString());
		}
		return new ToStringBuilder(this)
				.append("storageDocuments", stringBufferDoc.toString())
				.append("storageDocumentsOnError", strBufferDocOnEr.toString())
				.toString();
	}
}
