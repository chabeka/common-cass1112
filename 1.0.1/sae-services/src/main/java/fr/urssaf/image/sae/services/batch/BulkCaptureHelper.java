package fr.urssaf.image.sae.services.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.enrichment.dao.impl.SAEMetatadaFinderUtils;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEBulkErrors;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Cette classe a pour rôle de contrôler, de convertir et d’archiver les
 * documents contenus dans le sommaire en mode « tout ou rien ».<br>
 * Le sommaire est passé en paramètre de la méthode bulkCapture.
 * 
 * @author Rhofir
 */
@Component
@Qualifier("bulkCaptureHelper")
@SuppressWarnings({ "PMD.LongVariable", "PMD.ExcessiveImports",
		"PMD.AvoidInstantiatingObjectsInLoops", "PMD.CyclomaticComplexity" })
public class BulkCaptureHelper {

	private static final String TOUT_OU_RIEN = "TOUT_OU_RIEN";
	@Autowired
	@Qualifier("saeCommonCaptureService")
	private SAECommonCaptureService commonCapture;
	@Autowired
	@Qualifier("mappingDocumentOnErrorService")
	private MappingDocumentOnErrorService mappingOnError;

	private List<UntypedDocumentOnError> untypedDocumentsOnError;
	private List<MetadataError> errors;

	/**
	 * Enrichissement des métadonnées avec un ID traitement.
	 * 
	 * @param storageDocs
	 *            : Une liste de type {@link StorageDocument}.
	 * @return Une liste de type {@link StorageDocument}.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public final List<StorageDocument> addIdTreatementToStorageDoc(
			List<StorageDocument> storageDocs) {
		UUID idtreatement = UUID.randomUUID();
		for (StorageDocument storageDocument : Utils
				.nullSafeIterable(storageDocs)) {
			storageDocument.getMetadatas().add(
					new StorageMetadata("itm", idtreatement.toString()));
		}
		return storageDocs;
	}

	/**
	 * Construit une liste de type {@link StorageDocument}.
	 * 
	 * @param untypedDocs
	 *            : Une liste de type {@link UntypedDocument}
	 * @return Une liste de type {@linkStorageDocument}
	 */
	// CHECKSTYLE:OFF
	public final List<StorageDocument> buildStorageDocuments(
			List<UntypedDocument> untypedDocs) {
		List<StorageDocument> storageDocs = new ArrayList<StorageDocument>();
		UntypedDocument currentDocument = null;
		untypedDocumentsOnError = new ArrayList<UntypedDocumentOnError>();
		errors = new ArrayList<MetadataError>();
		try {
			for (UntypedDocument untypedDoc : Utils
					.nullSafeIterable(untypedDocs)) {
				currentDocument = untypedDoc;
				storageDocs.add(commonCapture
						.buildStorageDocumentForCapture(untypedDoc));
			}
		} catch (SAEEnrichmentEx except) {
			buildTechnicalErrors(currentDocument, except);
		} catch (ReferentialRndException except) {
			buildTechnicalErrors(currentDocument, except);
		} catch (UnknownCodeRndEx except) {
			buildErrors(currentDocument, except);
		} catch (SAECaptureServiceEx except) {
			buildTechnicalErrors(currentDocument, except);
		} catch (RequiredStorageMetadataEx except) {
			buildTechnicalErrors(currentDocument, except);
		} catch (InvalidValueTypeAndFormatMetadataEx except) {
			buildErrors(currentDocument, except);
		} catch (UnknownMetadataEx except) {
			buildErrors(currentDocument, except);
		} catch (DuplicatedMetadataEx except) {
			buildErrors(currentDocument, except);
		} catch (NotArchivableMetadataEx except) {
			buildErrors(currentDocument, except);
		} catch (EmptyDocumentEx except) {
			buildErrors(currentDocument, except);
		} catch (RequiredArchivableMetadataEx except) {
			buildErrors(currentDocument, except);
		} catch (UnknownHashCodeEx except) {
			buildErrors(currentDocument, except);
		} catch (NotSpecifiableMetadataEx except) {
			buildErrors(currentDocument, except);
		}
		return storageDocs;
	}

	// CHECKSTYLE:ON

	/**
	 * Construit une liste d'erreur.
	 * 
	 * @param badDocument
	 *            {@link StorageDocumentOnError}
	 * @param exception
	 *            : {@link Exception}
	 * @return Une Liste de type {@link UntypedDocumentOnError}
	 */
	public final List<UntypedDocumentOnError> buildErrors(
			UntypedDocument badDocument, Exception exception) {

		String filePath = badDocument.getFilePath();
		// SAE-CA-BUL002
		buildSaeErros(exception, filePath, SAEBulkErrors.FUNCTIONAL_ERROR);
		UntypedDocumentOnError untypedDocumentOnError = new UntypedDocumentOnError(
				null, badDocument.getUMetadatas(), errors);
		untypedDocumentOnError.setFilePath(filePath);
		untypedDocumentsOnError.add(untypedDocumentOnError);
		return untypedDocumentsOnError;
	}

	/**
	 * Construit une liste d'erreur.
	 * 
	 * @param badDocument
	 *            {@link StorageDocumentOnError}
	 * @param exception
	 *            : {@link Exception}
	 * @return Une Liste de type {@link UntypedDocumentOnError}
	 */
	public final List<UntypedDocumentOnError> buildTechnicalErrors(
			UntypedDocument badDocument, Exception exception) {
		String filePath = badDocument.getFilePath();
		// SAE-CA-BUL001
		buildSaeErros(exception, filePath, SAEBulkErrors.TECHNICAL_ERROR);
		untypedDocumentsOnError.add(new UntypedDocumentOnError(badDocument
				.getContent(), badDocument.getUMetadatas(), errors));
		return untypedDocumentsOnError;
	}

	/**
	 * Construit une liste d'erreur.
	 * 
	 * @param storageDocuments
	 *            {@link StorageDocuments}
	 * @param exception
	 *            : {@link Exception}
	 * @return Une Liste de type {@link UntypedDocumentOnError}
	 */
	public final List<UntypedDocumentOnError> buildTechnicalErrors(
			StorageDocuments storageDocuments, Exception exception) {
		List<UntypedMetadata> untypedMetadatas = new ArrayList<UntypedMetadata>();
		for (StorageDocument storageDocument : Utils
				.nullSafeIterable(storageDocuments.getAllStorageDocuments())) {
			String filePath = storageDocument.getFilePath();
			buildSaeErros(exception, filePath, SAEBulkErrors.TECHNICAL_ERROR);
			untypedDocumentsOnError.add(new UntypedDocumentOnError(
					storageDocument.getContent(), untypedMetadatas, errors));
		}
		return untypedDocumentsOnError;
	}

	/**
	 * @param exception
	 * @param filePath
	 * @param errorType
	 */
	private void buildSaeErros(Exception exception, String filePath,
			SAEBulkErrors errorType) {

		MetadataError error = new MetadataError();
		error.setCode(ResourceMessagesUtils.loadMessage(errorType
				.getErrorType()));
		error.setMessage(ResourceMessagesUtils.loadMessage(
				errorType.getMessage(), new File(filePath).getName(),
				exception.getMessage()));

		errors.add(error);
	}

	/**
	 * Construit une liste d'erreur.
	 * 
	 * @param badDocument
	 *            {@link StorageDocumentOnError}
	 * @param exception
	 *            : {@link Exception}
	 * @return Une Liste de type {@link UntypedDocumentOnError}
	 */
	public final List<UntypedDocumentOnError> buildErrors(
			StorageDocumentOnError badDocument, Exception exception) {
		String filePath = badDocument.getFilePath();
		List<UntypedMetadata> untypedMetadatas = new ArrayList<UntypedMetadata>();
		buildSaeErros(exception, filePath, SAEBulkErrors.FUNCTIONAL_ERROR);
		untypedMetadatas.add(new UntypedMetadata("TypeHash",
				SAEMetatadaFinderUtils.valueMetadataFinder(
						badDocument.getMetadatas(),
						"version.1.digest.alogorithm")));
		untypedMetadatas.add(new UntypedMetadata("Hash", SAEMetatadaFinderUtils
				.valueMetadataFinder(badDocument.getMetadatas(),
						"version.1.digest")));
		untypedDocumentsOnError.add(new UntypedDocumentOnError(null,
				untypedMetadatas, errors));
		return untypedDocumentsOnError;
	}

	/**
	 * Construit un résultat du traitement de la capture en masse.
	 * 
	 * @param bulkInsertionResults
	 *            : Un objet de type {@link BulkInsertionResults}
	 * @param initialDocumentsCount
	 *            : le nombre de documents initial à archiver.
	 * @param sommaire
	 *            : Chemin de fichier sommaire.
	 * @return Un objet de type {@link Resultats}
	 */
	public final Resultats buildResultatsSuccess(
			BulkInsertionResults bulkInsertionResults,
			int initialDocumentsCount, Sommaire sommaire) {
		StorageDocumentOnError badDocumentError = null;
		Resultats resultats = new Resultats();
		resultats.setBatchMode(TOUT_OU_RIEN);
		resultats.setInitialDocumentsCount(initialDocumentsCount);
		resultats.setEcdeDirectory(sommaire.getEcdeDirectory());
		resultats.setNonIntegratedVirtualDocumentsCount(0);
		// Traitement archivage en masse a échoué.
		List<StorageDocumentOnError> documentsErrorFromStorage = bulkInsertionResults
				.getStorageDocumentsOnError().getStorageDocumentsOnError();
		List<UntypedDocumentOnError> untypedDocmentsErrorFromStorage = new ArrayList<UntypedDocumentOnError>();
		// Cas de la liste StorageDocumentOnError.
		if (!CollectionUtils.isEmpty(documentsErrorFromStorage)) {
			for (StorageDocumentOnError documentError : documentsErrorFromStorage) {
				try {
					badDocumentError = documentError;
					untypedDocmentsErrorFromStorage
							.add(mappingOnError
									.storageDocumentOnErrorToUntypedDocumentOnError(documentError));
				} catch (InvalidSAETypeException except) {
					buildErrors(badDocumentError, except);
				}
			}
			untypedDocmentsErrorFromStorage
					.addAll(getUntypedDocumentsOnError());
			resultats
					.setNonIntegratedDocumentsCount(untypedDocmentsErrorFromStorage
							.size());

			resultats
					.setNonIntegratedDocuments(untypedDocmentsErrorFromStorage);
			resultats.setIntegratedDocumentsCount(0);
		}
		// Traitement archivage en masse s'est bien passé.
		List<StorageDocument> storageDocumentsFromStorage = bulkInsertionResults
				.getStorageDocuments().getAllStorageDocuments();
		if (!CollectionUtils.isEmpty(storageDocumentsFromStorage)) {
			resultats.setIntegratedDocumentsCount(storageDocumentsFromStorage
					.size());
		}
		return resultats;
	}

	/**
	 * Construit un résultat erreur du traitement de la capture en masse.
	 * 
	 * @param initialDocumentsCount
	 *            : le nombre de documents initial à archiver.
	 * @param sommaire
	 *            : Chemin de fichier sommaire.
	 * @param untypedDocumentsOnError
	 *            : Une liste d'objet de type {@link UntypedDocumentsOnError}<br>
	 *            les cas d'erreur sont :
	 *            <ul>
	 *            <li>Enrichissement</li>
	 *            <li>Code RND non existant</li>
	 *            <li>Chargement du référentiel des codes RND</li>
	 *            <li>Lors du contrôle de présence des métadonnées obligatoires
	 *            à l'archivage</li>
	 *            <li>Lors de la vérification du type et le format</li>
	 *            <li>Lors de la vérification de l’existence des métadonnées</li>
	 *            <li>Lors de la vérification de duplication des métadonnées</li>
	 *            <li>Lors de la vérification des métadonnées archivables</li>
	 *            <li>Si la taille du document est égale à 0 octet</li>
	 *            <li>Lors du contrôle de présence des métadonnées obligatoires.
	 *            </li>
	 *            <li>Lors de la vérification du hash code.</li>
	 *            <li>Lors de la vérification des métadonnées spécifiables sont
	 *            présentes</li>
	 *            <li>Lors du mapping
	 *            storageDocumentOnErrorToUntypedDocumentOnError</li>
	 *            <li>Lors de la connexion à DFCE.</li>
	 *            <li>Lors de l'insertion</li>
	 *            </ul>
	 * 
	 * @return Un objet de type {@link Resultats}
	 */
	public final Resultats buildResultatsError(int initialDocumentsCount,
			Sommaire sommaire,
			List<UntypedDocumentOnError> untypedDocumentsOnError) {
		Resultats resultats = new Resultats();
		resultats.setBatchMode(sommaire.getBatchMode());
		resultats.setInitialDocumentsCount(initialDocumentsCount);
		resultats.setEcdeDirectory(sommaire.getEcdeDirectory());
		resultats.setNonIntegratedVirtualDocumentsCount(0);
		resultats.setNonIntegratedDocuments(buildNoIntegratedDocuments(
				sommaire, untypedDocumentsOnError));
		resultats.setNonIntegratedDocumentsCount(initialDocumentsCount);
		return resultats;
	}

	/**
	 * @return Liste de type {@link UntypedDocumentsOnError}.
	 */
	public final List<UntypedDocumentOnError> getUntypedDocumentsOnError() {
		return untypedDocumentsOnError;
	}

	/**
	 * @param untypedDocumentsOnError
	 *            : Liste de type {@link UntypedDocumentsOnError}.
	 */
	public final void setUntypedDocumentsOnError(
			List<UntypedDocumentOnError> untypedDocumentsOnError) {
		this.untypedDocumentsOnError = untypedDocumentsOnError;
	}

	/**
	 * Permet de construire la liste des documents non intégrés suivant l'ordre
	 * des dit documents du sommaire
	 * 
	 * @param sommaire
	 *            : Le sommaire.
	 * @param untypedDocumentsOnError
	 *            : Le liste des documents.
	 * @return
	 */
	private List<UntypedDocumentOnError> buildNoIntegratedDocuments(
			final Sommaire sommaire,
			final List<UntypedDocumentOnError> untypedDocumentsOnError) {
		List<UntypedDocumentOnError> documentsOnError = new ArrayList<UntypedDocumentOnError>();
		final List<UntypedDocument> untypedDocuments = sommaire.getDocuments();
		for (UntypedDocument doc : untypedDocuments) {
			UntypedDocumentOnError docOnError = findDocument(doc,
					untypedDocumentsOnError);
			if (docOnError == null) {
				UntypedDocumentOnError newDocOnError = new UntypedDocumentOnError(
						doc.getContent(), doc.getUMetadatas(), null);
				newDocOnError.setFilePath(doc.getFilePath());
				documentsOnError.add(newDocOnError);
			} else {
				documentsOnError.add(docOnError);
			}
		}

		return documentsOnError;
	}

	/**
	 * Permet de retrouver un document en erreur parmi la liste des documents en
	 * erreurs
	 * 
	 * @param doc
	 *            : Le document recherché
	 * @param docsOnError
	 *            : La liste des documents en erreurs
	 * @return Le document en erreur
	 */
	private UntypedDocumentOnError findDocument(final UntypedDocument doc,
			final List<UntypedDocumentOnError> docsOnError) {
		UntypedDocumentOnError docFound = null;
		for (UntypedDocumentOnError docOnError : docsOnError) {
			if (compareDocument(doc, docOnError)) {
				docFound = docOnError;
				break;
			}
		}
		return docFound;
	}

	/**
	 * Permet de comparer deux liste d'objets de type {@link UntypedMetadata}
	 * 
	 * @param first
	 *            : La première liste.
	 * @param second
	 *            : La deuxième liste.
	 * @return True si les deux listes sont égale
	 */
	private boolean compareMetadatas(final List<UntypedMetadata> first,
			final List<UntypedMetadata> second) {

		if (first == null || second == null) {
			return false;
		}
		if (first.size() != second.size()) {
			return false;
		}
		return compareMetadataCodeAndValue(first, second);
	}

	/**
	 * Permet de comparer deux liste de metadonnées.
	 * 
	 * @param first
	 *            : La première liste.
	 * @param second
	 *            : La second liste.
	 * @return True si les deux listes sont égaux.
	 */
	private boolean compareMetadataCodeAndValue(
			final List<UntypedMetadata> first,
			final List<UntypedMetadata> second) {
		boolean find = true;
		for (UntypedMetadata uMetadata : first) {
			find = false;
			for (UntypedMetadata vMetadata : second) {
				if (uMetadata.getLongCode().equals(vMetadata.getLongCode())) {
					if (uMetadata.getValue() == vMetadata.getValue()) {
						find = true;
						break;
					}
				}
			}

		}
		return find;
	}

	/**
	 * Permet de comparer un document de type UntypedDocument et un autre de
	 * type UntypedDocumentOnError
	 * 
	 * @param doc
	 *            : Le document de type UntypedDocument
	 * @param docOnError
	 *            : Le document de type UntypedDocumentOnError
	 * @return True si les deux documents sont egaux.
	 */
	private boolean compareDocument(final UntypedDocument doc,
			final UntypedDocumentOnError docOnError) {
		boolean equal = true;

		if ((!doc.getFilePath().equals(docOnError.getFilePath()))
								|| (!compareMetadatas(doc.getUMetadatas(),
						docOnError.getUMetadatas()))) {
			equal = false;
		}

		return equal;
	}
}
