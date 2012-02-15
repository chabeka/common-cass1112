package fr.urssaf.image.sae.storage.dfce.services;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;
import fr.urssaf.image.sae.storage.services.storagedocument.DeletionService;
import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Classe de base pour les tests unitaires.
 * 
 */
/**
 * Classe de base pour les tests unitaires.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/appliContext-sae-storage-dfce-test.xml" })
@SuppressWarnings({ "PMD.ExcessiveImports", "PMD.LongVariable" })
public class StorageServices extends CommonsServices {
	@Autowired
	@Qualifier("insertionService")
	private InsertionService insertionService;
	@Autowired
	@Qualifier("retrievalService")
	private RetrievalService retrievalService;
	@Autowired
	@Qualifier("searchingService")
	private SearchingService searchingService;
	@Autowired
	@Qualifier("deletionService")
	private DeletionService deletionService;

	/**
	 * @return : Le service d'insertion.
	 */
	public final InsertionService getInsertionService() {
		return insertionService;
	}

	/**
	 * @param insertionService
	 *            : Le service d'insertion.
	 */
	public final void setInsertionService(
			final InsertionService insertionService) {
		this.insertionService = insertionService;
	}

	/**
	 * @return Le service de suppression.
	 */
	public final DeletionService getDeletionService() {
		return deletionService;
	}

	/**
	 * @param deletionService
	 *            :Le service de suppression.
	 */
	public final void setDeletionService(final DeletionService deletionService) {
		this.deletionService = deletionService;
	}

	/**
	 * Ferme la connexion. {@inheritDoc}
	 */

	/**
	 * @return Le service de récupération de document DFCE.
	 */
	public final RetrievalService getRetrievalService() {
		return retrievalService;
	}

	/**
	 * @param retrievalService
	 *            : Le service de récupération de document DFCE.
	 */
	public final void setRetrievalService(
			final RetrievalService retrievalService) {
		this.retrievalService = retrievalService;
	}

	/**
	 * @return Le service de recherche.
	 */
	public final SearchingService getSearchingService() {
		return searchingService;
	}

	/**
	 * @param searchingService
	 *            : Le service de recherche.
	 */
	public final void setSearchingService(
			final SearchingService searchingService) {
		this.searchingService = searchingService;
	}

	/**
	 * Initialise les paramètres pour les services.
	 * 
	 * @throws ConnectionServiceEx
	 *             Exception lévée lorsque la connexion n'aboutie pas.
	 */
	@Before
	public final void initServicesParameters() throws ConnectionServiceEx {
		getDfceServicesManager().getConnection();
		getInsertionService().setInsertionServiceParameter(
				getDfceServicesManager().getDFCEService());
		getRetrievalService().setRetrievalServiceParameter(
				getDfceServicesManager().getDFCEService());
		getDeletionService().setDeletionServiceParameter(
				getDfceServicesManager().getDFCEService());
		getSearchingService().setSearchingServiceParameter(
				getDfceServicesManager().getDFCEService());
		
		getInsertionService().setJmxIndicator(new JmxIndicator());
	}

	/**
	 * Libère les ressources
	 */
	@After
	public final void closeServicesParameters() {
		getDfceServicesManager().closeConnection();
	}

}
