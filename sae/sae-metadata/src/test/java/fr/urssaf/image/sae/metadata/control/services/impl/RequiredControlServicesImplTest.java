package fr.urssaf.image.sae.metadata.control.services.impl;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkRequiredMetadata(SAEDocument)}
 * 
 * @author akenore
 * 
 */
public class RequiredControlServicesImplTest extends AbstractDataProvider {

	/**
	 * Fournit des données pour valider la méthode
	 * {@link MetadataControlServicesImpl#checkRequiredMetadata(SAEDocument)}
	 * 
	 * @param withoutValue
	 *            : boolean qui permet de prendre en compte un intrus
	 * @return Un objet de type {@link SAEDocument}
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public final SAEDocument requiredData(final boolean withoutValue)
			throws FileNotFoundException {
		List<SAEMetadata> metadatas = null;
		if (withoutValue) {
			metadatas = MetadataDataProviderUtils
					.getSAEMetadata(Constants.REQUIRED_FILE_1);
		} else {
			metadatas = MetadataDataProviderUtils
					.getSAEMetadata(Constants.REQUIRED_FILE_2);
		}
		return new SAEDocument(null, metadatas);
	}

	/**
	 * Vérifie que la liste ne contenant pas d'intrus est valide
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkRequiredMetadataWithRequiredMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(!getControlService().checkRequiredMetadata(
				requiredData(true)).isEmpty());
	}

	/**
	 * Vérifie que la liste contenant un intrus n'est valide.
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkRequiredMetadataWithoutRequiredMetadataValue()
			throws FileNotFoundException {
		Assert.assertTrue(!getControlService().checkRequiredMetadata(
				requiredData(false)).isEmpty());
	}

}
