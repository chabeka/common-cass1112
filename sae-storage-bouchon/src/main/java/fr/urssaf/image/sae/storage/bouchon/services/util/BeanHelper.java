package fr.urssaf.image.sae.storage.bouchon.services.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * 
 * @author akenore
 * 
 */
public final class BeanHelper {
	@SuppressWarnings("PMD.LongVariable")
	private static final String UUID_CODE_METADATA = "UUID";
	@SuppressWarnings("PMD.LongVariable")
	private static final String PATH_CODE_METADATA = "Path";

	/**
	 * Construit un StorageDocument à partir d'une liste de métadonnées
	 * 
	 * @param providedMetaData
	 *            : liste de métadonnées fournie
	 * @param withoutUUID
	 *            : boolean permet de prendre en compte L'UIID
	 * @return un StorageMetaData
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static StorageDocument storageDocumentFromData(
			final Map<String, String> providedMetaData,
			final boolean withoutUUID) {
		final String path = providedMetaData.get("Path");
		final String uuidMetadata = providedMetaData.get("UUID");
		UUID uuid = null;
		byte[] content = null;
		if (StringUtils.isNotEmpty(uuidMetadata)) {
			uuid = UUID.fromString(uuidMetadata);
		}
		if (StringUtils.isNotEmpty(path)) {

			content = Utils.fileContent();
		}
		return new StorageDocument(storageMetadataFromData(providedMetaData,
				withoutUUID), content, null, uuid);

	}

	/**
	 * Retourne la liste des métadonnées.
	 * 
	 * @param providedMetaData
	 *            : liste de métadonnées fournie
	 * @param withoutUUID
	 *            : Critère qui permet de spécifier si on prend en compte l'uuid
	 * @return la liste des métadonnées
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	private static List<StorageMetadata> storageMetadataFromData(
			final Map<String, String> providedMetaData,
			final boolean withoutUUID) {

		final List<StorageMetadata> storageMetadatas = new ArrayList<StorageMetadata>();

		for (Map.Entry<String, String> metadata : Utils.nullSafeMap(
				providedMetaData).entrySet()) {
			if (!metadata.getKey().trim().equalsIgnoreCase(PATH_CODE_METADATA)) {
				if (withoutUUID) {
					if (!metadata.getKey().trim()
							.equalsIgnoreCase(UUID_CODE_METADATA)) {
						storageMetadatas.add(new StorageMetadata(metadata
								.getKey().trim(), metadata.getValue().trim()));
					}
				} else {
					storageMetadatas.add(new StorageMetadata(metadata.getKey().trim(),
							metadata.getValue().trim()));
				}
			}

		}
		return Collections.unmodifiableList(storageMetadatas);

	}

	/**
	 * Contrôle les valeurs des métadonnées à partir d'une liste de valeurs
	 * 
	 * @param metadatas
	 *            : Liste de métadonnées
	 * @param metadatasCode
	 *            : Liste de codes
	 * @param metadatasValues
	 *            : Liste de valeurs
	 * @return True si les valeurs correspondent bien aux valeurs spécifiées
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static boolean checkMetaDatasValues(
			final List<StorageMetadata> metadatas,
			final String[] metadatasCode, final String[] metadatasValues) {
		boolean isEqual = true;
		if (metadatasCode.length == metadatasValues.length) {
			for (int i = 0; i < metadatasCode.length - 1; i++) {
				final StorageMetadata storageMetadata = storageMetadataFinder(
						metadatas, metadatasCode[i]);
				if (!storageMetadata.getValue().toString().trim()
						.equalsIgnoreCase(metadatasValues[i].trim())) {
					isEqual = false;
					break;
				}
			}
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * Permet de trouver une métadonnée à partir du code
	 * 
	 * @param metadatas
	 *            : La liste des métadonnées
	 * @param code
	 *            : Le code
	 * @return une métadonnée à partir du code
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private static StorageMetadata storageMetadataFinder(
			final List<StorageMetadata> metadatas, final String code) {
		StorageMetadata storageMetadata = null;
		for (StorageMetadata metadata : Utils.nullSafeIterable(metadatas)) {
			final String metadataCode = metadata.getCode();
			if (StringUtils.isNotEmpty(metadataCode)
					&& metadataCode.contains(code.trim())) {
				storageMetadata = metadata;
			}
		}
		return storageMetadata;
	}

	/** Cette classe n'est pas faite pour être instanciée. */
	private BeanHelper() {
		assert false;
	}
}
