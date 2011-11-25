package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.jmx.BulkProgress;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentsOnError;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * Implémente les services de l'interface {@link InsertionService}.
 * 
 * @author Akenore, Rhofir
 * 
 */
@Service
@Qualifier("insertionService")
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.CyclomaticComplexity"})
public class InsertionServiceImpl extends AbstractServices implements
		InsertionService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(InsertionServiceImpl.class);
	@Autowired
	@Qualifier("deletionService")
	private DeletionService deletionService;
	private int jmxStorageIndex;
	private int totalDocument;
	private JmxIndicator indicator;

	/**
	 * @return : Le service de suppression
	 */
	public final DeletionService getDeletionService() {
		return deletionService;
	}

	/**
	 * @param deletionService
	 *            : Le service de suppression.
	 */
	public final void setDeletionService(final DeletionService deletionService) {
		this.deletionService = deletionService;
	}

	/**
	 * @param indicator
	 *            : Les indicateurs de l'archivage de masse.
	 */
	public final void setIndicator(final JmxIndicator indicator) {
		this.indicator = indicator;
	}

	/**
	 * @return Les indicateurs de l'archivage de masse.
	 */
	public final JmxIndicator getIndicator() {
		return indicator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws InsertionServiceEx
	 *             Exception levée lorsque l'insertion ne se déroule pas bien
	 */
	@Loggable(LogLevel.TRACE)
	@ServiceChecked
	@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops","PMD.CyclomaticComplexity", "PMD.LongVariable"})
	public final BulkInsertionResults bulkInsertStorageDocument(
			final StorageDocuments storageDocuments, final boolean allOrNothing)
			throws InsertionServiceEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "bulkInsertStorageDocument()";
      LOGGER.debug("{} - Début", prefixeTrc);
      LOGGER.debug("{} - Début de la boucle d'insertion des documents dans DFCE", prefixeTrc);
      // Fin des traces debug - entrée méthode
		final List<StorageDocument> storageDocDone = new ArrayList<StorageDocument>();
		final List<StorageDocumentOnError> storageDocFailed = new ArrayList<StorageDocumentOnError>();
		jmxStorageIndex = 0;
		totalDocument = 0;
		int indexDocument = 0;
		if (storageDocuments != null
				&& storageDocuments.getAllStorageDocuments() != null) {
			totalDocument = storageDocuments.getAllStorageDocuments().size();
		}
		if (indicator != null) {
			indicator.setJmxCountDocument(totalDocument);
			indicator.setJmxStorageIndex(jmxStorageIndex);
			indicator.setJmxTreatmentState(BulkProgress.INSERTION_DOCUMENTS);
		}
		for (StorageDocument storageDocument : Utils
				.nullSafeIterable(storageDocuments.getAllStorageDocuments())) {
			try {
	         LOGGER.debug("{} - Stockage du document #{} ({})",
	               new Object[]{prefixeTrc, ++indexDocument,storageDocument.getFilePath()});
				storageDocument.setUuid(insertStorageDocument(storageDocument)
						.getUuid());
				storageDocDone.add(storageDocument);
				jmxStorageIndex++;
				if (indicator != null) {
					indicator.setJmxStorageIndex(jmxStorageIndex);
				}
			} catch (InsertionServiceEx insertExcp) {
				if (storageDocument != null) {
					StorageDocumentOnError storageDocumentOnError = new StorageDocumentOnError(
							storageDocument.getMetadatas(),
							storageDocument.getContent() ==  null ? "DOCERROR!".getBytes() : storageDocument.getContent() ,
							storageDocument.getFilePath(), "INSERROR : "
									+ insertExcp.getMessage());
					storageDocumentOnError.setMessageError("INSERROR");
					storageDocFailed.add(storageDocumentOnError);
				}
				if (allOrNothing) {
				   LOGGER.debug("{} - Déclenchement de la procédure de rollback", prefixeTrc);
					rollback(storageDocDone);
					// Les documents "rollbackés" ne doivent pas apparaitre dans
					// BulkInsertionResults
					
					LOGGER.debug("{} - La procédure de rollback est terminée", prefixeTrc);
					storageDocDone.clear();
					break;
				}
			}
			LOGGER.debug("{} - Fin de la boucle d'insertion des documents dans DFCE", prefixeTrc);
			LOGGER.debug("{} - Sortie", prefixeTrc);
		}
		return new BulkInsertionResults(new StorageDocuments(storageDocDone),
				new StorageDocumentsOnError(storageDocFailed));
	}

	/**
	 * Supprime les documents qui ont déjà été insérés avec succès. Appelée dans
	 * le cadre d'un traitement "tout ou rien" qui serait en erreur.
	 * 
	 * @param storageDocDone
	 * @throws InsertionServiceEx
	 *             Levée si le rollback (donc la suppression) échoue.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	private void rollback(final List<StorageDocument> storageDocDone)
			throws InsertionServiceEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "rollback()";
      LOGGER.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
		jmxStorageIndex = 0;
		totalDocument = 0;
		int indexDocument = 0;
		if (storageDocDone != null) {
			totalDocument = storageDocDone.size();
		}
		if (indicator != null) {
			indicator.setJmxCountDocument(totalDocument);
			indicator.setJmxStorageIndex(jmxStorageIndex);
			indicator.setJmxTreatmentState(BulkProgress.DELETION_DOCUMENTS);
		}
		for (StorageDocument strDocument : Utils
				.nullSafeIterable(storageDocDone)) {
			try {
			   LOGGER.debug("{} - Rollback du document #{} ({})",
                  new Object[]{prefixeTrc, ++indexDocument,strDocument.getUuid()});
				deletionService.setDeletionServiceParameter(getDfceService());
				deletionService.deleteStorageDocument(new UUIDCriteria(
						strDocument.getUuid(), null));
				jmxStorageIndex++;
				if (indicator != null) {
					indicator.setJmxStorageIndex(jmxStorageIndex);
				}
			} catch (DeletionServiceEx delSerEx) {
				// FIXME: lever une exception plus parlante pour l'appelant
				// (par exemple : AllOrNothingRollbackException).
				// Pour l'instant on laisse cette exception pour ne pas casser
				// les
				// signatures.

				// TODO : il faut continuer le rollback sur les autres
				// documents.
				// L'appelant pourrait obtenir une liste des documents non
				// rollbackés via un attribut
				// de l'exception AllOrNothingRollbackException
			   LOGGER.debug("{} - Une exception a été levée lors du rollback : {}", prefixeTrc,delSerEx.getMessage());
				throw new InsertionServiceEx(
						StorageMessageHandler
								.getMessage(Constants.DEL_CODE_ERROR),
						delSerEx.getMessage(), delSerEx);
			}
		}
		LOGGER.debug("{} - Sortie", prefixeTrc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Loggable(LogLevel.TRACE)
	@ServiceChecked
	public final StorageDocument insertStorageDocument(
			final StorageDocument storageDocument) throws InsertionServiceEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "insertStorageDocument()";
      LOGGER.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
	   try {
			Document docDfce = BeanMapper.storageDocumentToDfceDocument(
					getBaseDFCE(), storageDocument);
			// ici on récupère le chemin du fichier.

			final InputStream docContent = new ByteArrayInputStream(
					FileUtils.readFileToByteArray(new File(storageDocument
							.getFilePath())));
			final String[] file = BeanMapper.findFileNameAndExtension(
					storageDocument, StorageTechnicalMetadatas.NOM_FICHIER
							.getShortCode().toString());
			LOGGER.debug("{} - Enrichissement des métadonnées : "
					+ "ajout de la métadonnée NomFichier valeur : {}.{}",
					new Object[] { prefixeTrc, file[0], file[1] });
			LOGGER.debug("{} - Début insertion du document dans DFCE",
					prefixeTrc);
			docDfce = getDfceService().getStoreService().storeDocument(docDfce,
					file[0], file[1], docContent);
			LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})",
					prefixeTrc, docDfce.getUuid());
			LOGGER.debug("{} - Fin insertion du document dans DFCE", prefixeTrc);
	      LOGGER.debug("{} - Sortie", prefixeTrc);
			return BeanMapper.dfceDocumentToStorageDocument(docDfce, null,
					getDfceService(), false);
		} catch (TagControlException tagCtrlEx) {
			throw new InsertionServiceEx(
					StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
					tagCtrlEx.getMessage(), tagCtrlEx);
		} catch (Exception except) {
			throw new InsertionServiceEx(
					StorageMessageHandler.getMessage(Constants.INS_CODE_ERROR),
					except.getMessage(), except);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final <T> void setInsertionServiceParameter(final T parameter) {
		setDfceService((ServiceProvider) parameter);

	}

	/**
	 * @return L'indice du document stocké.
	 */
	public final int getJmxStorageIndex() {
		return jmxStorageIndex;
	}

	/**
	 * @param jmxStorageIndex
	 *            : Indicateur jmx qui permet de retourner l'indice du document
	 *            stocké.
	 */
	public final void setJmxStorageIndex(int jmxStorageIndex) {
		this.jmxStorageIndex = jmxStorageIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	public JmxIndicator retrieveJmxStorageIndicator() {
		return indicator;
	}

	/**
	 * @param totalDocument
	 *            : Le nombre total de document à insérer.
	 */
	public void setTotalDocument(int totalDocument) {
		this.totalDocument = totalDocument;
	}

	/**
	 * @return Le nombre total de document à insérer.
	 */
	public int getTotalDocument() {
		return totalDocument;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setJmxIndicator(JmxIndicator indicator) {
		setIndicator(indicator);
	}
}
