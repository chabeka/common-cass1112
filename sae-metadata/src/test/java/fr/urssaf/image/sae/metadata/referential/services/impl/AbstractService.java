package fr.urssaf.image.sae.metadata.referential.services.impl;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;

/**
 * Contient les tests sur les services de désérialisation du referentiel des
 * métadonnées
 * 
 * @author akenore
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata.xml" })
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractService {
	@Autowired
	@Qualifier("metadataReferenceDAO")
	private MetadataReferenceDAO referenceDAO;
	

	
	
	/**
	 * @return Le service de manipulation du referentiel des métadonnées.
	 */
	public final MetadataReferenceDAO getMetadataReferenceDAO() {
		return referenceDAO;
	}

	/**
	 * @param referenceDAO
	 *            : Le service de manipulation du referentiel des métadonnées.
	 */
	public final void setMetadataReferenceDAO(
			final MetadataReferenceDAO referenceDAO) {
		this.referenceDAO = referenceDAO;
	}

	/**
	 * @return Le service d'accès au référentiel des métadonnées.
	 */
	public final MetadataReferenceDAO getReferenceDAO() {
		return referenceDAO;
	}

	/**
	 * @param referenceDAO Le service d'accès au référentiel des métadonnées.
	 */
	public final void setReferenceDAO(final MetadataReferenceDAO referenceDAO) {
		this.referenceDAO = referenceDAO;
	}
}
