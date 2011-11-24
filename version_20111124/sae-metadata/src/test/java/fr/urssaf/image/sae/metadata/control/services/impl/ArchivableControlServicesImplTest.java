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
 * {@link MetadataControlServices#checkArchivableMetadata(fr.urssaf.image.sae.bo.model.bo.SAEDocument)}
 * 
 * @author akenore
 * 
 */
public class ArchivableControlServicesImplTest extends AbstractDataProvider {
	/**
	 * Fournit des données pour valider la méthode
	 * {@link MetadataControlServicesImpl#checkArchivableMetadata(SAEDocument)}
	 * 
	 * @param withoutFault
	 *            : boolean qui permet de prendre en compte un intrus
	 * @return Un objet de type {@link SAEDocument}
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public final SAEDocument archivableData(final boolean withoutFault)
			throws FileNotFoundException {
		 List<SAEMetadata> metadatas =null;
		if (withoutFault) {
			metadatas = MetadataDataProviderUtils
					.getSAEMetadata(Constants.ARCHIVABLE_FILE_1);
		} else {
			metadatas = MetadataDataProviderUtils
					.getSAEMetadata(Constants.ARCHIVABLE_FILE_2);
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
	public void checkArchivableMetadataWithoutNotArchivaleMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(getControlService().checkArchivableMetadata(
				archivableData(true)).isEmpty());
	}
	/**
	 * Vérifie que la liste  contenant un intrus n'est valide
	 * 
	 * @throws FileNotFoundException
	 *             Exception levé lorsque le fichier n'existe pas.
	 */
	@Test
	public void checkArchivableMetadataWithNotArchivaleMetadata()
			throws FileNotFoundException {
		Assert.assertTrue(!getControlService().checkArchivableMetadata(
				archivableData(false)).isEmpty());
	}

}
