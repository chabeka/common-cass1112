package fr.urssaf.image.sae.mapping.validation;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.mapping.services.MappingDocumentService;

/**
 * Service commun pour les tests sur le mapping des documents
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-mapping.xml" })
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractDocumentService {
	@Autowired
	@Qualifier("mappingDocumentService")
	private MappingDocumentService mappingDocService;
	/**
	 * @param mappingDocService
	 *            : Le service de mapping des documents
	 */
	public final void setMappingDocService(final MappingDocumentService mappingDocService) {
		this.mappingDocService = mappingDocService;
	}

	/**
	 * @return Le service de mapping des documents
	 */
	public final MappingDocumentService getMappingDocService() {
		return mappingDocService;
	}

}
