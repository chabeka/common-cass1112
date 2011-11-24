package fr.urssaf.image.sae.mapping.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.building.services.BuildService;

/**
 * Cette classe permet de tester la validation des paramètres des services de
 * l'interface {@link BuildService}
 * 
 * @author akenore
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-mapping.xml" })
public class BuildServiceValidationTest {
	@Autowired
	@Qualifier("buildService")
	private BuildService buildService;

	/**
	 * @return Le service de construction d'objet.
	 */
	public final BuildService getBuildService() {
		return buildService;
	}

	/**
	 * @param buildService
	 *            Le service de construction d'objet.
	 */
	public final void setBuildService(final BuildService buildService) {
		this.buildService = buildService;
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.building.services.BuildService#buildStorageLuceneCriteria(String, int, java.util.List)
	 * buildStorageLuceneCriteria}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void buildStorageLuceneCriteria() {
		buildService.buildStorageLuceneCriteria(null, 0, null);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.building.services.BuildService#buildStorageUuidCriteria(java.util.UUID, java.util.List)
	 * buildStorageUuidCriteria}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void buildStorageUuidCriteria() {
		buildService.buildStorageUuidCriteria(null, null);
	}

	/**
	 * Permet de tester la methode
	 * {@link fr.urssaf.image.sae.building.services.BuildService#buildUntypedDocument(byte[], java.util.Map)
	 * buildUntypedDocument}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void buildUntypedDocument() {
		buildService.buildUntypedDocument(null, null);
	}

}
