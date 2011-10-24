package fr.urssaf.image.sae.storage.services.storagedocument;

import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Fournit les services d’insertion de document.<BR />
 * Ces services sont :
 * <ul>
 * <li>insertStorageDocument : service qui permet d'insérer un document unique.</li>
 * <li>bulkInsertStorageDocument : service qui permet de réaliser une insertion
 * en masse de documents.</li>
 * </ul>
 * 
 */
public interface InsertionService {
	// CHECKSTYLE:OFF
	/**
	 * Permet d'insérer un document unique
	 * 
	 * @param storageDocument
	 *            : Le document à stocker
	 * 
	 * @return  Le document
	 * 
	 * @throws InsertionServiceEx
	 *             Exception lévée lorsque l'insertion d'un document ne se déroule pas bien.
	 */
	StorageDocument insertStorageDocument(StorageDocument storageDocument)
			throws InsertionServiceEx;

	/**
	 * Permet de réaliser une insertion en masse de documents
	 * 
	 * @param storageDocuments
	 *            : Les documents à stocker
	 * @param allOrNothing
	 *            : boolean qui permet de faire une insertion en masse en mode
	 *            tout ou rien
	 * @return Le résultat des insertions réussies et échouées
	 * @throws InsertionServiceEx
	 *             : Exception lévée lors de l'insertion en masse.
	 */
	BulkInsertionResults bulkInsertStorageDocument(
			StorageDocuments storageDocuments, final boolean allOrNothing)
			throws InsertionServiceEx;
	
	/**
	 * 
	 * @param <T> : Le type générique.
	 * @param parameter : Le paramètre du service {@link InsertionService}
	 */
	 <T> void setInsertionServiceParameter(T parameter);

}
