package fr.urssaf.image.sae.mapping.validation;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.mapping.services.MappingDocumentOnErrorService;

/**
 * Service commun pour les tests sur le mapping des documents
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-mapping.xml" })
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractDocumentOnErrorService {
	@Autowired
	@Qualifier("mappingDocumentOnErrorService")
	private MappingDocumentOnErrorService docOnErrorService;

	/**
	 * @param docOnErrorService
	 *            : Le service de mapping des document en error.
	 */
	public final void setDocOnErrorService(
			final MappingDocumentOnErrorService docOnErrorService) {
		this.docOnErrorService = docOnErrorService;
	}

	/**
	 * @return Le service de mapping des document en error.
	 */
	public final MappingDocumentOnErrorService getDocOnErrorService() {
		return docOnErrorService;
	}

}
