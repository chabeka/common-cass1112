package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.data.utils.CheckDataUtils;
import fr.urssaf.image.sae.storage.dfce.mapping.DocumentForTestMapper;
import fr.urssaf.image.sae.storage.dfce.services.StorageServices;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Classe de test du service
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl
 * InsertionService}
 * 
 * @author rhofir, kenore.
 */
@SuppressWarnings({ "PMD.ExcessiveImports",
		"PMD.AvoidInstantiatingObjectsInLoops", "PMD.AvoidDuplicateLiterals" })
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
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#bulkInsertStorageDocument(StorageDocuments, boolean)
	 * bulkInsertStorageDocument} <br>
	 * Insertion en masse des documents avec la valeur allOrNothing = true.<br>
	 * 
	 * @throws ConnectionServiceEx
	 */
	@Test
	public void bulkInsertAll() throws IOException, ParseException,
			InsertionServiceEx, RetrievalServiceEx, ConnectionServiceEx {
		final BulkInsertionResults insertionResults = buildBlukStorageDocument(
				true, Constants.XML_PATH_DOC_WITHOUT_ERROR);
		Assert.assertNotNull(
				"Objet resultat de l'insertion en masse ne doit pas être null ",
				insertionResults);
		Assert.assertNotNull("Objet StorageDocument en masse sans erreur ",
				insertionResults.getStorageDocuments());
		Assert.assertEquals(
				"Vérfier que le nombre des documents insert est égale à celui insert ",
				Constants.XML_PATH_DOC_WITHOUT_ERROR.length, insertionResults
						.getStorageDocuments().getAllStorageDocuments().size());
		Assert.assertNotNull("Objet StorageDocument en masse avec erreur ",
				insertionResults.getStorageDocumentsOnError()
						.getStorageDocumentsOnError().size());
		for (StorageDocument storageDocument : Utils
				.nullSafeIterable(insertionResults.getStorageDocuments()
						.getAllStorageDocuments())) {
			Assert.assertNotNull("UUID insérer sans erreur ",
					storageDocument.getUuid());
			final UUIDCriteria uuidCriteria = new UUIDCriteria(
					storageDocument.getUuid(), new ArrayList<StorageMetadata>());
			Assert.assertNotNull(
					"Tester l'objet StorageDocument retrouvé dans la base est non null ",
					getRetrievalService().retrieveStorageDocumentByUUID(
							uuidCriteria));
		}

	}

	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#bulkInsertStorageDocument(StorageDocuments, boolean)
	 * bulkInsertStorageDocument} <br>
	 * Insertion en masse des documents avec la valeur allOrNothing = true.<br>
	 * 
	 * @throws ConnectionServiceEx
	 */
	@Test
	@Ignore("Pour eviter que la base soit corrompue")
	public void bulkInsertAllWithError() throws IOException, ParseException,
			InsertionServiceEx, RetrievalServiceEx, ConnectionServiceEx {
		final BulkInsertionResults insertionResults = buildBlukStorageDocument(
				true, Constants.XML_PATH_DOC_WITH_ERROR);
		Assert.assertNotNull(
				"Objet resultat de l'insertion en masse ne doit pas être null ",
				insertionResults);
		Assert.assertNotNull("Objet StorageDocument en masse sans erreur ",
				insertionResults.getStorageDocuments());
		Assert.assertEquals(
				"Vérfier que le nombre des documents insert est égale à celui insert ",
				0, insertionResults.getStorageDocuments()
						.getAllStorageDocuments().size());
		Assert.assertEquals("Objet StorageDocument en masse avec erreur ", 1,
				insertionResults.getStorageDocumentsOnError()
						.getStorageDocumentsOnError().size());
	}

	/**
	 * Test du service :
	 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.InsertionServiceImpl#bulkInsertStorageDocument(StorageDocuments, boolean)
	 * bulkInsertStorageDocument} <br>
	 * Insertion en masse des documents avec la valeur allOrNothing = false. <br>
	 * {@inheritDoc}
	 * 
	 * @throws ConnectionServiceEx
	 */
	@Test
	@Ignore("Pour eviter que la base soit corrompue")
	public void bulkInsertNotAll() throws IOException, ParseException,
			InsertionServiceEx, RetrievalServiceEx, ConnectionServiceEx {
		// à compléter
		final BulkInsertionResults insertionResults = buildBlukStorageDocument(
				false, Constants.XML_PATH_DOC_WITH_ERROR);
		Assert.assertNotNull(
				"Objet resultat de l'insertion en masse ne doit pas être null ",
				insertionResults);
		Assert.assertEquals(
				"Vérfier que le nombre des documents insert est égale à celui insert ",
				6, insertionResults.getStorageDocuments()
						.getAllStorageDocuments().size());
		Assert.assertEquals("Objet StorageDocument en masse avec erreur ", 1,
				insertionResults.getStorageDocumentsOnError()
						.getStorageDocumentsOnError().size());
		for (StorageDocument storageDocument : Utils
				.nullSafeIterable(insertionResults.getStorageDocuments()
						.getAllStorageDocuments())) {
			Assert.assertNotNull("UUID insérer sans erreur ",
					storageDocument.getUuid());
			final UUIDCriteria uuidCriteria = new UUIDCriteria(
					storageDocument.getUuid(), new ArrayList<StorageMetadata>());
			Assert.assertNotNull(
					"Le resultat de recherche {insertsNotThingsOrAllDocument}:",
					getRetrievalService().retrieveStorageDocumentByUUID(
							uuidCriteria).getUuid());
		}
		Assert.assertNotNull("Objet StorageDocument en masse avec erreur ",
				insertionResults.getStorageDocumentsOnError());
		for (StorageDocumentOnError storageDocumentOnError : Utils
				.nullSafeIterable(insertionResults.getStorageDocumentsOnError()
						.getStorageDocumentsOnError())) {
			Assert.assertNotNull(
					"Le code erreur non null lors de l'insertion en masse avec erreur ",
					storageDocumentOnError.getCodeError());
		}
	}

	/**
	 * Construit un ensemble de fichier pour tester l'insertion en masse. <br>
	 * 
	 * @param allOrNothing
	 *            : Flag d'insertion en masses.
	 * @param filesPath
	 *            : Chemin des fichiers xml
	 * @throws ConnectionServiceEx
	 */
	private BulkInsertionResults buildBlukStorageDocument(
			final boolean allOrNothing, final String[] filesPath)
			throws FileNotFoundException, IOException, ParseException,
			InsertionServiceEx, ConnectionServiceEx {
		final List<StorageDocument> lstDocument = new ArrayList<StorageDocument>();
		final File files[] = new File[filesPath.length];
		int numFile = 0;
		for (String pathFile : filesPath) {
			files[numFile] = new File(pathFile);
			numFile++;
		}
		// Récupération des fichiers de tests désérialisé.
		final List<SaeDocument> saeDocuments = getXmlDataService()
				.saeDocumentsReader(files);
		// Mapping entre les fichiers de tests et les StorageDocument
		for (SaeDocument saeDocument : Utils.nullSafeIterable(saeDocuments)) {
			lstDocument.add(DocumentForTestMapper
					.saeDocumentXmlToStorageDocument(saeDocument));
		}
		final StorageDocuments storageDocuments = new StorageDocuments(
				lstDocument);
		final BulkInsertionResults insertionResults = getInsertionService()
				.bulkInsertStorageDocument(storageDocuments, allOrNothing);
		return insertionResults;
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
