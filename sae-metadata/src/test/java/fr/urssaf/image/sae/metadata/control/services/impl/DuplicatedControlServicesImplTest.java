package fr.urssaf.image.sae.metadata.control.services.impl;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkDuplicateMetadata(List)}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class DuplicatedControlServicesImplTest{
   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices controlService;
	/**
	 * Fournit des données pour valider la méthode
	 * {@link MetadataControlServicesImpl#checkDuplicateMetadata(List) }
	 * 
	 * @param withoutFault
	 *            : boolean qui permet de prendre en compte un intrus.
	 * @return La liste des métadonnées.
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public final List<UntypedMetadata> duplicateData(final boolean withoutFault)
			throws FileNotFoundException {
		List<UntypedMetadata> metadatas = null;
		if (withoutFault) {
			metadatas = MetadataDataProviderUtils
					.getUntypedMetadata(Constants.DUPLICATE_FILE_1);
		} else {
			metadatas = MetadataDataProviderUtils
					.getUntypedMetadata(Constants.DUPLICATE_FILE_2);
		}
		return metadatas;
	}

	/**
	 * Vérifie que la liste ne contenant pas d'intrus est valide
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkDuplicateMetadataWithoutDuplicateMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(controlService.checkDuplicateMetadata(
				duplicateData(true)).isEmpty());
	}

	/**
	 * Vérifie que la liste contenant un intrus n'est valide.
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkDuplicateMetadataWithDuplicateMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(!controlService.checkDuplicateMetadata(
				duplicateData(false)).isEmpty());
	}

	/**
	 * Vérifie que la liste contenant un intrus n'est valide en comparant le
	 * nombre de metadataError.
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkDuplicateMetadataCount() throws FileNotFoundException {
		Assert.assertTrue(controlService.checkDuplicateMetadata(
				duplicateData(false)).size() == 2);
	}

}
