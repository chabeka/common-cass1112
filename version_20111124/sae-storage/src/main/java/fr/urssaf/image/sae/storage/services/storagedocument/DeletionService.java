package fr.urssaf.image.sae.storage.services.storagedocument;

import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Fournit les services de suppression document.<BR />
 * Ce services est :
 * <ul>
 * <li>deleteStorageDocument : service qui permet de supprimer un
 * StorageDocument à partir de l'UUIDCriteria.</li>
 * </ul>
 */
public interface DeletionService {

	/**
	 * Permet de supprimer un StorageDocument à partir du critère « UUIDCriteria
	 * ».
	 * 
	 * @param uuidCriteria
	 *            : L'identifiant unique du document
	 * 
	 * 
	 * 
	 * @throws DeletionServiceEx
	 *             Runtime exception
	 */

	void deleteStorageDocument(final UUIDCriteria uuidCriteria)
			throws DeletionServiceEx;

		

	/**
	 * Permet de faire un rollback à partir d'un identifiant de traitement. 
	 * 
	 * 
	 * @param processId
	 *            : L'identifiant du traitement
	 * 
	 * 
	 * 
	 * @throws DeletionServiceEx
	 *             Runtime exception
	 */

	void rollBack(final String processId)
			throws DeletionServiceEx;
	/**
	 * 
	 * @param <T> : Le type générique.
	 * @param parameter : Le paramètre du service {@link DeletionService}
	 */
	 <T> void setDeletionServiceParameter(T parameter);

}
