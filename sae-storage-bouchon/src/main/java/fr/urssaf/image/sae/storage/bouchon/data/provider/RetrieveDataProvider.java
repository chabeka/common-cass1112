package fr.urssaf.image.sae.storage.bouchon.data.provider;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.urssaf.image.sae.storage.bouchon.data.model.RetrievalXml;
import fr.urssaf.image.sae.storage.bouchon.data.model.RetrieveDoc;
import fr.urssaf.image.sae.storage.bouchon.data.model.RetrieveDocMetaData;
import fr.urssaf.image.sae.storage.bouchon.data.model.RetrieveMetaData;
import fr.urssaf.image.sae.storage.bouchon.data.model.RetrieveServiceData;
import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.bouchon.services.util.Utils;
import fr.urssaf.image.sae.storage.bouchon.services.util.XstreamHelper;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Cette classe fournit les données pour simuler les services de récupération des documents
 * 
 * @author akenore
 * 
 */
public final class RetrieveDataProvider {
	  /** Le composant pour les traces */
		private static final Logger LOGGER = Logger.getLogger(RetrieveDataProvider.class);
	  @SuppressWarnings("PMD.LongVariable")
	private static RetrieveServiceData retrieveServiceData;
	
	/**
	 * Retourne les données du bouchon
	 * 
	 * @return les données du bouchon
	 */
	private static RetrieveServiceData retrieveDataLoader() {
		return XstreamHelper.loadDataProcess(Utils.getFileFromClassPath(RetrievalXml.buildXmlfile()),
				RetrieveServiceData.class,
				Message.DATA_FILE_LOADER.getMessage());
	}

	/**
	 * Cette méthode retourne le contenu du document et ses métadonnées
	 * 
	 * @param index
	 *            : index de l'element souhaité
	 * @return retourne le contenu du document et ses métadonnées
	 */
	public static StorageDocument retrieveStorageDocumentByUUIDCriteriaData(
			final int index) {
		retrieveServiceData = retrieveDataLoader();
		Validate.notNull(retrieveServiceData,
				Message.RETRIEVE_BY_UUID_DATA.getMessage());
		LOGGER.info("Chargement des données du service de récupération du binaire et des métadonnées ");
	final	List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
		for (RetrieveDocMetaData retrieveDocMetaData : Utils
				.nullSafeIterable(retrieveServiceData.getRetrieveDocMetaData())) {
			storageDocuments
					.add(BeanHelper.storageDocumentFromData(retrieveDocMetaData
							.getDocument(),true));
			LOGGER.info("Données chargées : " + Utils.mapToString(retrieveDocMetaData.getDocument()));
		}
		return storageDocuments.get(0);

	}

	/**
	 * Cette méthode retourne le contenu du document
	 * 
	 * @param index
	 *            : index de l'element souhaité
	 * @return retourne le contenu du document
	 */
	public static byte[] retrieveStorageContentByUUIDCriteriaData(
			final int index) {
		retrieveServiceData = retrieveDataLoader();
		Validate.notNull(retrieveServiceData,
				Message.RETRIEVE_BY_UUID_DATA.getMessage());
		LOGGER.info("Chargement des données du service de récupération du binaire  ");
	final	List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
		for (RetrieveDoc retrieveDoc : Utils
				.nullSafeIterable(retrieveServiceData.getRetrieveDoc())) {
			storageDocuments.add(BeanHelper.storageDocumentFromData(retrieveDoc
					.getDocument(),false));
			LOGGER.info("Données chargées : " + Utils.mapToString(retrieveDoc.getDocument()));
		}
		return storageDocuments.get(index).getContent();

	}
	/**
	 * Cette méthode retourne le contenu du document
	 * 
	 * @param index
	 *            : index de l'element souhaité
	 * @return retourne le contenu du document
	 */
	public static List<StorageMetadata> retrieveStorageMetaDataByUUIDCriteriaData(
			final int index) {
		retrieveServiceData = retrieveDataLoader();
		Validate.notNull(retrieveServiceData,
				Message.RETRIEVE_BY_UUID_DATA.getMessage());
		LOGGER.info("Chargement des données du service de récupération des métadonnées ");
	final	List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
		for (RetrieveMetaData retrieveMetaData : Utils
				.nullSafeIterable(retrieveServiceData.getRetrieveMetaData())) {
			storageDocuments.add(BeanHelper.storageDocumentFromData(retrieveMetaData
					.getDocument(),false));
			LOGGER.info("Données chargées : " + Utils.mapToString(retrieveMetaData.getDocument()));
		}
		return storageDocuments.get(index).getMetadatas();

	}
	/**
	 * Constructeur
	 */
	private RetrieveDataProvider(){
		assert false;
	}
}
