package fr.urssaf.image.sae.storage.bouchon.data.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.urssaf.image.sae.storage.bouchon.data.model.SearchByLuceneData;
import fr.urssaf.image.sae.storage.bouchon.data.model.SearchByUUIDData;
import fr.urssaf.image.sae.storage.bouchon.data.model.SearchServiceData;
import fr.urssaf.image.sae.storage.bouchon.data.model.SearchingXml;
import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.bouchon.services.util.Utils;
import fr.urssaf.image.sae.storage.bouchon.services.util.XstreamHelper;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Cette classe fournit les données pour simuler les services de recherche
 * 
 * @author akenore
 * 
 */
public final class SearchDataProvider {
	
	private static final Logger LOGGER = Logger
			.getLogger(SearchDataProvider.class);
	private static SearchServiceData searchServiceData;

	/**
	 * Retourne les données du bouchon
	 * 
	 * @return les données du bouchon
	 */
	private static SearchServiceData searchDataLoader() {
		return XstreamHelper.loadDataProcess(Utils.getFileFromClassPath(SearchingXml.buildXmlfile()),
				SearchServiceData.class, Message.DATA_FILE_LOADER.getMessage());
	}

	/**
	 * Cette méthode retourne le résultat de la recherche par requête lucene
	 * 
	 * @return retourne le résultat de la recherche par requête lucene
	 */
	public static StorageDocuments getSearchStorageDocumentByLuceneCriteriaData() {
		searchServiceData = searchDataLoader();
		Validate.notNull(searchServiceData,
				Message.SEARCH_BY_LUCENE_DATA.getMessage());
		LOGGER.info("Chargement des données du service de recherche lucène");
		final List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
		for (SearchByLuceneData searchByLuceneData : Utils
				.nullSafeIterable(searchServiceData.getSearchByLuceneData())) {
			storageDocuments.add(BeanHelper.storageDocumentFromData(
					searchByLuceneData.getDocument(), true));
			LOGGER.info("Données chargées : "
					+ Utils.mapToString(searchByLuceneData.getDocument()));
		}
		return new StorageDocuments(
				Collections.unmodifiableList(storageDocuments));

	}

	/**
	 * Cette méthode retourne le résultat de la recherche par uuid
	 * 
	 * @param index
	 *            : index de l'element souhaité
	 * @return Retourne le résultat de la recherche par uuid
	 */
	public static StorageDocument getSearchStorageDocumentByUUIDCriteria(
			final int index) {
		searchServiceData = searchDataLoader();
		Validate.notNull(searchServiceData,
				Message.SEARCH_BY_UUID_DATA.getMessage());
		LOGGER.info("Chargement des données du service de recherche par UUID");
		final List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
		for (SearchByUUIDData searchByUUIDData : Utils
				.nullSafeIterable(searchServiceData.getSearchByUUIDData())) {
			storageDocuments.add(BeanHelper.storageDocumentFromData(
					searchByUUIDData.getDocument(), true));
			LOGGER.info("Données chargées : "
					+ Utils.mapToString(searchByUUIDData.getDocument()));
		}
		return storageDocuments.get(index);

	}

	/**
	 * Constructeur
	 */
	private SearchDataProvider() {
		assert false;
	}
}
