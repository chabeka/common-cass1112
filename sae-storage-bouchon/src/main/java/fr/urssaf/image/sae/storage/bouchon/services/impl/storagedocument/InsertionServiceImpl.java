package fr.urssaf.image.sae.storage.bouchon.services.impl.storagedocument;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.bouchon.data.provider.InsertionDataProvider;
import fr.urssaf.image.sae.storage.bouchon.model.AbstractDfceService;
import fr.urssaf.image.sae.storage.bouchon.services.messages.Message;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageBase;
import fr.urssaf.image.sae.storage.model.storagedocument.BulkInsertionResults;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;

/**
 * Implémente les services de l'interface insertionService.
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("insertionService")
public class InsertionServiceImpl extends AbstractDfceService implements
		InsertionService {
	/**
	 * Constructeur de la classe
	 * 
	 * @param storageBase
	 *            : La base de stockage
	 */
	public InsertionServiceImpl(final StorageBase storageBase) {
		super(storageBase);
	}

	/**
	 * Construction par défaut
	 **/
	public InsertionServiceImpl() {
		super();
	}
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BulkInsertionResults bulkInsertStorageDocument(
			final StorageDocuments storageDocuments, final boolean allOrNothing) {

		return InsertionDataProvider.getBulkInsertionData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final UUID insertStorageDocument(final StorageDocument storageDocument)
			throws InsertionServiceEx {
		try {
			return InsertionDataProvider.getInsertionData();
		} catch (RuntimeException runtimeException) {
			throw new InsertionServiceEx(
					Message.INSERTION_STORAGEDOCUMENT_FAILED.getMessage(),
					runtimeException);
		}

	}

}
