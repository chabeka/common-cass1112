package fr.urssaf.image.sae.mapping.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;

/**
 * Classe abstraite contenant les services communs des services de mapping.
 * @author akenore
 *
 */
public abstract class AbstractMappingDocumentService {
	@Autowired
	@Qualifier("metadataReferenceDAO")
	private MetadataReferenceDAO referenceDAO;
	/**
	 * @param referenceDAO
	 *            : Le service du référentiel
	 */
	public final void setReferenceDAO(final MetadataReferenceDAO referenceDAO) {
		this.referenceDAO = referenceDAO;
	}

	/**
	 * @return Le service du référentiel
	 */
	public final MetadataReferenceDAO getReferenceDAO() {
		return referenceDAO;
	}
}
