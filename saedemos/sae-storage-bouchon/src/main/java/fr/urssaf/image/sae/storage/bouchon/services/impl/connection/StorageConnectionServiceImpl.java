package fr.urssaf.image.sae.storage.bouchon.services.impl.connection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.bouchon.model.AbstractConnectionServiceProvider;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;
import fr.urssaf.image.sae.storage.services.connection.StorageConnectionService;

/**
 * Fournit l’implémentation des services de connexion à la base de stockage
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("storageConnectionService")
public class StorageConnectionServiceImpl extends
		AbstractConnectionServiceProvider implements StorageConnectionService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void openConnection() throws ConnectionServiceEx {
		assert true;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void closeConnexion() {

		assert true;
	}

	/**
	 * Constructeur par défaut
	 */
	public StorageConnectionServiceImpl() {
		super();
	}

	/**
	 * 
	 * @param storageConnectionParameter
	 *            : Les paramètres de connexion à la base de stockage
	 */
	@SuppressWarnings("PMD.LongVariable")
	public StorageConnectionServiceImpl(
			final StorageConnectionParameter storageConnectionParameter) {
		super(storageConnectionParameter);
	}
}
