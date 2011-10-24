package fr.urssaf.image.sae.mapping.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.mapping.dataprovider.MappingDataProviderUtils;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;

/**
 * Classe contenant les éléments communs
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-mapping.xml" })
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class AbstractMappingService {
	@Autowired
	@Qualifier("mappingDocumentService")
	private MappingDocumentService mappingService;

	/**
	 * @param mappingService
	 *            : Le service de mapping
	 */
	public final void setMappingService(
			final MappingDocumentService mappingService) {
		this.mappingService = mappingService;
	}

	/**
	 * @return Le service de mapping
	 */
	public final MappingDocumentService getMappingService() {
		return mappingService;
	}

	/**
	 * Fournit les données de test
	 * 
	 * @param xmlfile
	 *            : Le fichier xml.
	 * @return Une document non typé
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings({ "PMD.DataflowAnomalyAnalysis",
			"PMD.AvoidFinalLocalVariable" })
	public final UntypedDocument getUntypedDocument(final File xmlfile)
			throws FileNotFoundException {
		List<UntypedMetadata> metadatas = null;
		final byte[] content = "fichier Test".getBytes();
		metadatas = MappingDataProviderUtils.getUntypedMetadata(xmlfile);
		return new UntypedDocument(content, metadatas);
	}

}
