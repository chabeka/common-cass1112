package fr.urssaf.image.sae.metadata.performance;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;
import fr.urssaf.image.sae.metadata.utils.Utils;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata.xml" })
public class SearchLongCodeTest {

	private List<UntypedMetadata> metadatas;

	@Autowired
	@Qualifier("metadataReferenceDAO")
	private MetadataReferenceDAO referenceDAO;

	/**
	 * @return Le service de manipulation du référentiel des métadonnées.
	 */
	public final MetadataReferenceDAO getMetadataReferenceDAO() {
		return referenceDAO;
	}

	/**
	 * @param referenceDAO
	 *            : Le service de manipulation du référentiel des métadonnées.
	 */
	public final void setMetadataReferenceDAO(
			final MetadataReferenceDAO referenceDAO) {
		this.referenceDAO = referenceDAO;
	}

	/**
	 * @return the metadatas
	 */
	public final List<UntypedMetadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas the metadatas to set
	 */
	public final void setMetadatas(final List<UntypedMetadata> metadatas) {
		this.metadatas = metadatas;
	}

	/**
	 * @return Le service de manipulation des métadonnées de référentiel
	 */
	public final MetadataReferenceDAO getReferenceDAO() {
		return referenceDAO;
	}

	/**
	 * @param referenceDAO : de manipulation des métadonnées de référentiel
	 */
	public final void setReferenceDAO(final MetadataReferenceDAO referenceDAO) {
		this.referenceDAO = referenceDAO;
	}

	/**
	 * Fournit les données de test pour la méthode
	 * {@link MetadataReferenceDAO#getByLongCode(String)}
	 * 
	 * @throws FileNotFoundException
	 *             Exception levée lorsque le fichier n'existe pas.
	 */
	@Before
	public void getUntypedMetadata() throws FileNotFoundException {
		metadatas = MetadataDataProviderUtils
				.getUntypedMetadata(Constants.SHORT_LONG_CODE);
	}

	/**
	 * Permet de tester la méthode
	 * {@link MetadataReferenceDAO#getByLongCode(String)}
	 * 
	 * @throws ReferentialException
	 *             Exception lever lorsqu'il y'a un dysfonctionnement lié au
	 *             référentiel.
	 */
	@Test
	public void getByLongCode() throws ReferentialException {
		for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
			Assert.assertNotNull(referenceDAO.getByLongCode(metadata
					.getLongCode()));
		}
	}
}
