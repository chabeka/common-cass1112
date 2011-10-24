package fr.urssaf.image.sae.storage.bouchon.services.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Contient les paramètres des services
 * 
 * @author akenore
 * 
 */


@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractServiceParameters {
	private StorageDocument storageDocument;
	private List<StorageMetadata> metadatas;
	private StorageDocuments storageDocuments;
	@SuppressWarnings("PMD.LongVariable")
	private List<StorageDocument> lisStorageDocument;
	private LuceneCriteria luceneCriteria;
	private UUIDCriteria uuidCriteria;

	/**
	 * Permet d'initialiser les paramètres
	 */
	@Before
	public final void initParameters() {
		metadatas = new ArrayList<StorageMetadata>();
		metadatas.add(new StorageMetadata("CodeRND", "3.1.3.1.1"));
		storageDocument = new StorageDocument(metadatas, null, null);
		lisStorageDocument = new ArrayList<StorageDocument>();
		lisStorageDocument.add(storageDocument);
		storageDocuments = new StorageDocuments(lisStorageDocument);
		luceneCriteria = new LuceneCriteria("query", 2, metadatas);
		uuidCriteria = new UUIDCriteria(
				UUID.fromString("110E8400-E29B-11D4-A716-446655440000"),
				metadatas);

	}

	

	/**
	 * @return Le document(flux binaire et les métadonnées)
	 */
	public final StorageDocument getStorageDocument() {
		return storageDocument;
	}

	/**
	 * @param storageDocument Le document(flux binaire et les métadonnées)
	 */
	public final void setStorageDocument(final StorageDocument storageDocument) {
		this.storageDocument = storageDocument;
	}

	/**
	 * @return Les métadonnées
	 */
	public final List<StorageMetadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas Les métadonnées
	 */
	public  final void setMetadatas(final List<StorageMetadata> metadatas) {
		this.metadatas = metadatas;
	}

	/**
	 * @return Les documents
	 */
	public final StorageDocuments getStorageDocuments() {
		return storageDocuments;
	}

	/**
	 * @param storageDocuments Les documents
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setStorageDocuments(final StorageDocuments storageDocuments) {
		this.storageDocuments = storageDocuments;
	}

	/**
	 * @return La liste des documents
	 */
	public final List<StorageDocument> getLisStorageDocument() {
		return lisStorageDocument;
	}

	/**
	 * @param lisStorageDocument : La liste des documents
	 */
	@SuppressWarnings("PMD.LongVariable")
	public final void setLisStorageDocument(final List<StorageDocument> lisStorageDocument) {
		this.lisStorageDocument = lisStorageDocument;
	}

	/**
	 * @return Le critère lucène
	 */
	public final LuceneCriteria getLuceneCriteria() {
		return luceneCriteria;
	}

	/**
	 * @param luceneCriteria : Le critère lucène
	 */
	public final void setLuceneCriteria(final LuceneCriteria luceneCriteria) {
		this.luceneCriteria = luceneCriteria;
	}

	/**
	 * @return Le critère uuid
	 */
	public final UUIDCriteria getUuidCriteria() {
		return uuidCriteria;
	}

	/**
	 * @param uuidCriteria : Le critère uuid
	 */
	public final void setUuidCriteria(final UUIDCriteria uuidCriteria) {
		this.uuidCriteria = uuidCriteria;
	}

	
}