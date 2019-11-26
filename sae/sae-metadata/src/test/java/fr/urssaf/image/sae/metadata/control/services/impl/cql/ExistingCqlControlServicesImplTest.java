package fr.urssaf.image.sae.metadata.control.services.impl.cql;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.control.services.impl.MetadataControlServicesImpl;
import fr.urssaf.image.sae.metadata.test.constants.Constants;
import fr.urssaf.image.sae.metadata.test.dataprovider.MetadataDataProviderUtils;

/**
 * Cette classe permet de tester le service
 * {@link MetadataControlServices#checkExistingMetadata(UntypedDocument)}
 * Elle utilise la classe abstraite AbstractMetadataCqlTest pour la configuration et l'injection de données
 */

public class ExistingCqlControlServicesImplTest extends AbstractMetadataControlCqlTest {

  /**
   * Fournit des données pour valider la méthode
   * {@link MetadataControlServicesImpl#checkExistingMetadata(UntypedDocument) }
   * 
   * @param withoutFault
   *          : boolean qui permet de prendre en compte un intrus
   * @return Un objet de type {@link UntypedDocument}
   * @throws FileNotFoundException
   *           Exception levé lorsque le fichier n'existe pas.
   */
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  public final UntypedDocument existingData(final boolean withoutFault)
      throws FileNotFoundException {
    List<UntypedMetadata> metadatas = null;
    if (withoutFault) {
      metadatas = MetadataDataProviderUtils
          .getUntypedMetadata(Constants.EXISTING_FILE_1);
    } else {
      metadatas = MetadataDataProviderUtils
          .getUntypedMetadata(Constants.EXISTING_FILE_2);
    }
    return new UntypedDocument(null, metadatas);
  }

  /**
   * Vérifie que la liste ne contenant pas d'intrus est valide
   * 
   * @throws FileNotFoundException
   *             Exception levé lorsque le fichier n'existe pas.
   */
  @Test
  public void checkExistingMetadataWithoutNotExistingMetadata()
      throws FileNotFoundException {
    Assert.assertTrue(controlService.checkExistingMetadata(
                                                           existingData(true)).isEmpty());
  }
  /**
   * Vérifie que la liste contenant un intrus n'est valide.
   * 
   * @throws FileNotFoundException
   *             Exception levé lorsque le fichier n'existe pas.
   */
  @Test
  public void checkExistingMetadataWithExistingMetadata()
      throws FileNotFoundException {
    Assert.assertTrue(!controlService.checkExistingMetadata(
                                                            existingData(false)).isEmpty());
  }

}