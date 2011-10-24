package fr.urssaf.image.sae.metadata.control.services.impl;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * 
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkConsultableMetadata(List)}
 * 
 * @author akenore
 * 
 */

public class ConsultableControlServicesImplTest extends AbstractDataProvider {

	/**
	 * Fournit des données pour valider la méthode
	 * {@link MetadataControlServicesImpl#checkConsultableMetadata(List)}
	 * 
	 * @param withoutFault
	 *            : boolean qui permet de prendre en compte un intrus
	 * @return La liste des métadonnées.
	 * @throws FileNotFoundException Exception levé lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public final List<SAEMetadata> consultableData(final boolean withoutFault)
			throws FileNotFoundException {
		List<SAEMetadata> metadatas = null;
		if (withoutFault) {
			metadatas = MetadataDataProviderUtils
					.getSAEMetadata(Constants.CONSULTABLE_FILE_1);
		} else {
			metadatas = MetadataDataProviderUtils
					.getSAEMetadata(Constants.CONSULTABLE_FILE_2);
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
	public void checkConsultableMetadataWithoutNotConsultableMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(getControlService().checkConsultableMetadata(
				consultableData(true)).isEmpty());
	}
	/**
	 * Vérifie que la liste  contenant un intrus n'est valide
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkConsultableMetadataWithNotConsultableMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(!getControlService().checkConsultableMetadata(
				consultableData(false)).isEmpty());
	}

	
}
