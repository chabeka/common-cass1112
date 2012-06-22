package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe de test du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl
 * InsertionService}
 * 
 */
public class InsertionServiceTest extends StorageServices {

	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
	 * insertStorageDocument} <br>
	 * Insérer deux fois le même document et vérifier que les UUIDs sont
	 * différents.
	 * 
	 * @throws ConnectionServiceEx
	 *             Exception lévée lorsque la connexion n'aboutie pas.
	 */
	@Test
	public void insertOneDocument() throws IOException, ParseException,
			InsertionServiceEx, ConnectionServiceEx {
		final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
				new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
		final StorageDocument storageDocument = DocumentForTestMapper
				.saeDocumentXmlToStorageDocument(saeDocument);
		getDfceServicesManager().getConnection();
		getInsertionService().setInsertionServiceParameter(
				getDfceServicesManager().getDFCEService());
		final StorageDocument firstDocument = getInsertionService()
				.insertStorageDocument(storageDocument);
		Assert.assertNotNull(firstDocument);
	}

	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
	 * insertStorageDocument} <br>
	 * Insérer deux fois le même document et vérifier que les UUIDs sont
	 * différents.
	 * 
	 * @throws ConnectionServiceEx
	 */
	@Test
	public void insertTwiceSameDocument() throws IOException, ParseException,
			InsertionServiceEx, ConnectionServiceEx {
		getDfceServicesManager().getConnection();
		getInsertionService().setInsertionServiceParameter(
				getDfceServicesManager().getDFCEService());
		final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
				new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
		final StorageDocument storageDocument = DocumentForTestMapper
				.saeDocumentXmlToStorageDocument(saeDocument);
		final StorageDocument firstDocument = getInsertionService()
				.insertStorageDocument(storageDocument);
		final StorageDocument secondDocument = getInsertionService()
				.insertStorageDocument(storageDocument);
		// si la valeur de la comparaison est égale à 1, c'est que les deux UUID
		// sont différent.
		Assert.assertEquals(
				"Les deux UUID du même document doivent être différent :",
				true,
				secondDocument.getUuid().getLeastSignificantBits() != firstDocument
						.getUuid().getMostSignificantBits());
	}

	
	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#insertStorageDocument(StorageDocument)
	 * insertStorageDocument} <br>
	 * <p>
	 * Tests réaliser :
	 * <ul>
	 * <li>Insérer un document et vérifier son UUID.</li>
	 * <li>Récupère le document par uuid.</li>
	 * <li>Compare les métadonnée insérées dans DFCE et les métadonnées du
	 * document xml en entrée.</li>
	 * <li>Compare sha de Dfce et le sha1 calculé</li>
	 * </ul>
	 * </p>
	 */
	@Test
	public void insertStorageDocument() throws IOException, ParseException,
			StorageException, NoSuchAlgorithmException {
		final SaeDocument saeDocument = getXmlDataService().saeDocumentReader(
				new File(Constants.XML_PATH_DOC_WITHOUT_ERROR[0]));
		final StorageDocument storageDocument = DocumentForTestMapper
				.saeDocumentXmlToStorageDocument(saeDocument);
		final StorageDocument document = getInsertionService()
				.insertStorageDocument(storageDocument);
		Assert.assertNotNull("UUID après insertion ne doit pas être null ",
				document.getUuid());
		final UUIDCriteria uuid = new UUIDCriteria(document.getUuid(), null);
		Assert.assertTrue("Les deux SHA1 doivent être identique",
				CheckDataUtils.checkDocumentSha1(storageDocument.getContent(),
						getRetrievalService()
								.retrieveStorageDocumentContentByUUID(uuid)));
	}
}
