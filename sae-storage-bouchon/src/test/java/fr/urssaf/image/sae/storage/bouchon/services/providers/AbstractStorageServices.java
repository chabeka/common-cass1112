package fr.urssaf.image.sae.storage.bouchon.services.providers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.services.storagedocument.InsertionService;
import fr.urssaf.image.sae.storage.services.storagedocument.RetrievalService;
import fr.urssaf.image.sae.storage.services.storagedocument.SearchingService;

/**
 * Contient tous les services à tester : <Li>Attribut insertionService : Les
 * services d'insertion</Li> <Li>Attribut retrievalService : Les services de
 * récupération</Li> <Li>Attribut searchingService :Les services de recherche</Li>
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-sae-storage-bouchon.xml"})
public abstract class AbstractStorageServices extends AbstractServiceParameters {
	@Autowired
	@Qualifier("insertionService")
	private InsertionService insertionService;
	@Autowired
	@Qualifier("retrievalService")
	private RetrievalService retrievalService;
	@Autowired
	@Qualifier("searchingService")
	private SearchingService searchingService;

	/**
	 * @return Les services d'insertion
	 */
	public final InsertionService getInsertionService() {
		return insertionService;
	}

	/**
	 * @param insertionService
	 *            : Les services d'insertion
	 */
	public final void setInsertionService(
			final InsertionService insertionService) {
		this.insertionService = insertionService;
	}

	/**
	 * @return Les services de récupération
	 */
	public final RetrievalService getRetrievalService() {
		return retrievalService;
	}

	/**
	 * @param retrievalService
	 *            : Les services de récupération
	 * 
	 */
	public final void setRetrievalService(
			final RetrievalService retrievalService) {
		this.retrievalService = retrievalService;
	}

	/**
	 * @return Les services de recherche
	 */
	public final SearchingService getSearchingService() {
		return searchingService;
	}

	/**
	 * @param searchingService
	 *            Les services de recherche
	 */
	public final void setSearchingService(
			final SearchingService searchingService) {
		this.searchingService = searchingService;
	}

}
