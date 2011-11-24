package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe de test des services de recherche.
 * 
 * @author Rhofir, aknore.
 * 
 */

public class SearchingServiceTest extends StorageServices {

	/**
	 * Permet de récupérer un document à partir du critère « UUIDCriteria ».<br>
	 * {@inheritDoc}
	 * 
	 * @throws ConnectionServiceEx
	 */
	@Test
	public void searchDocumentByUUID() throws SearchingServiceEx,
			InsertionServiceEx, IOException, ParseException, DeletionServiceEx,
			ConnectionServiceEx {
		StorageDocument document = getMockData(getInsertionService());
		UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
				new ArrayList<StorageMetadata>());
		Assert.assertNotNull(
				"Recupération d'un document par UUID :",
				getSearchingService().searchStorageDocumentByUUIDCriteria(
						uuidCriteria).getUuid());
		destroyMockTest(document.getUuid(), getDeletionService());
	}

	/**
	 * Permet de récupérer un document à partir du critère « UUIDCriteria ».<br>
	 * {@inheritDoc}
	 * @throws ConnectionServiceEx 
	 */
	@Test
	public void searchDocumentByUUIDWithDesiredMetaData()
			throws SearchingServiceEx, InsertionServiceEx, IOException,
			ParseException, DeletionServiceEx, ConnectionServiceEx {
			// Initialisation des jeux de données UUID
		StorageDocument document = getMockData(getInsertionService());
		// Initialisation des jeux de données Metadata
		final DesiredMetaData metaDataFromXml = getXmlDataService()
				.desiredMetaDataReader(
						new File(Constants.XML_FILE_DESIRED_MDATA[0]));
		List<StorageMetadata> desiredMetadatas = DocumentForTestMapper
				.saeMetaDataXmlToStorageMetaData(metaDataFromXml)
				.getMetadatas();

		UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
				desiredMetadatas);
		Assert.assertNotNull(
				"Le resultat de recherche :",
				getSearchingService().searchStorageDocumentByUUIDCriteria(
						uuidCriteria).getUuid());
		Assert.assertTrue(
				"Les deux listes des métaData doivent être identique : ",
				CheckDataUtils.checkDesiredMetaDatas(
						desiredMetadatas,
						getSearchingService().searchMetaDatasByUUIDCriteria(
								uuidCriteria).getMetadatas()));
		// Suppression du document insert
		destroyMockTest(document.getUuid(), getDeletionService());
	}

	/**
	 * Récupérer les métadonnées par UUID. <br>{@inheritDoc}
	 * @throws ConnectionServiceEx 
	 */
	@Test
	public void searchMetaDatasByUUID() throws SearchingServiceEx,
			InsertionServiceEx, IOException, ParseException, DeletionServiceEx, ConnectionServiceEx {
		// Initialisation des jeux de données UUID
		StorageDocument document = getMockData(getInsertionService());
		UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
				new ArrayList<StorageMetadata>());
		Assert.assertNotNull(
				"Recupération d'une liste de métadonnées par uuid :",
				getSearchingService().searchMetaDatasByUUIDCriteria(
						uuidCriteria).getMetadatas());
		// Suppression du document insert
		destroyMockTest(document.getUuid(), getDeletionService());
	}

	/**
	 * Récupérer les métadonnées par UUID et récupération qu'une liste de
	 * métadonnées spécifique. <br>{@inheritDoc}
	 * @throws ConnectionServiceEx 
	 */
	@Test
	public void searchMetaDatasByUUIDWithDesiredMetaData()
			throws SearchingServiceEx, IOException, ParseException,
			InsertionServiceEx, DeletionServiceEx, ConnectionServiceEx {
		// Initialisation des jeux de données UUID
		StorageDocument document = getMockData(getInsertionService());
		// Initialisation des jeux de données Metadata
		final DesiredMetaData desiredMetaData = getXmlDataService()
				.desiredMetaDataReader(
						new File(Constants.XML_FILE_DESIRED_MDATA[0]));
		List<StorageMetadata> desiredMetadatas = DocumentForTestMapper
				.saeMetaDataXmlToStorageMetaData(desiredMetaData)
				.getMetadatas();
		UUIDCriteria uuidCriteria = new UUIDCriteria(document.getUuid(),
				desiredMetadatas);
		Assert.assertNotNull(
				"Le resultat de recherche :",
				getSearchingService().searchMetaDatasByUUIDCriteria(
						uuidCriteria).getMetadatas());
		Assert.assertTrue(
				"Les deux listes des métaData doivent être identique",
				CheckDataUtils.checkDesiredMetaDatas(
						desiredMetadatas,
						getSearchingService().searchMetaDatasByUUIDCriteria(
								uuidCriteria).getMetadatas()));
		// Suppression du document insert
		destroyMockTest(document.getUuid(), getDeletionService());
	}

}
