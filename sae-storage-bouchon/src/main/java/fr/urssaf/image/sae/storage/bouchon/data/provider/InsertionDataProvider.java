package fr.urssaf.image.sae.storage.bouchon.data.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.urssaf.image.sae.storage.bouchon.data.model.BulkInsertionData;
import fr.urssaf.image.sae.storage.bouchon.data.model.InsertionServiceData;
import fr.urssaf.image.sae.storage.bouchon.data.model.InsertionXml;
import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.bouchon.services.util.BeanHelper;
import fr.urssaf.image.sae.storage.bouchon.services.util.Utils;
import fr.urssaf.image.sae.storage.bouchon.services.util.XstreamHelper;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Cette classe fournit les données pour simuler les services d'insertion
 * 
 * @author akenore
 * 
 */
public final class InsertionDataProvider {
	
	  @SuppressWarnings("PMD.LongVariable")
	private static InsertionServiceData insertionServiceData;
	  /** Le composant pour les traces */
		private static final Logger LOGGER = Logger.getLogger(InsertionDataProvider.class);
	/**
	 * Retourne les données du bouchon
	 * 
	 * @return les données du bouchon
	 */
	private static InsertionServiceData insertionDataLoader() {
		return XstreamHelper.loadDataProcess(Utils.getFileFromClassPath(InsertionXml.buildXmlfile()),
				InsertionServiceData.class,
				Message.DATA_FILE_LOADER.getMessage());
	}

	/**
	 * Cette méthode retourne l'UUID
	 * 
	 * @return retourne l'UUID
	 */
	public static UUID getInsertionData() {
		LOGGER.info("Chargement des données du service d'insertion");
		insertionServiceData = insertionDataLoader();
		Validate.notNull(insertionServiceData,
				Message.INSERTION_DATA.getMessage());
		LOGGER.info("UUID chargée : " + insertionServiceData.getInsertionData());
		return UUID.fromString(insertionServiceData.getInsertionData());
	}

	/**
	 * Cette méthode retourne le résultat de l'insertion en masse
	 * @return Le résultat de l'insertion en masse
	 * 
	 */
	public static   BulkInsertionResults getBulkInsertionData() {
		LOGGER.info("Chargement des données du service d'insertion en masse");
		insertionServiceData = insertionDataLoader();
		Validate.notNull(insertionServiceData,
				Message.BULK_INSERTION_DATA.getMessage());
	final	List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();
		for (BulkInsertionData bulkInsertionData : Utils
				.nullSafeIterable(insertionServiceData.getBulkInsertionData())) {
			storageDocuments.add(BeanHelper
					.storageDocumentFromData(bulkInsertionData.getDocument(),true));
			LOGGER.info("Données chargées : " + Utils.mapToString(bulkInsertionData.getDocument()));
		}
		return new BulkInsertionResults(new StorageDocuments(
				Collections.unmodifiableList(storageDocuments)), null);
	}

	/**
	 * Cette classe ne peut pas être d'instancier
	 */
	private InsertionDataProvider() {
		assert false;
	}

}
