package fr.urssaf.image.sae.services.controles.cql;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.util.resource.ResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.services.AbstractServiceCqlTest;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import junit.framework.Assert;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })*/
public class SAEControlesCaptureServiceImplMetaCqlTest extends AbstractServiceCqlTest {

  @Autowired
  SAEControlesCaptureService saeControlesCaptureService;

  @BeforeClass
  public static void beforeClass() throws IOException {
    ModeApiAllUtils.setAllModeAPICql();
  }

  @Before
  public void before() throws Exception {
    initMetadata();

  }

  /**
   * Test permettant que vérifier que des valeurs de métadonnées non associées
   * à un dictionnaire sont rejettes
   *
   * @throws UnknownMetadataEx
   * @throws DuplicatedMetadataEx
   * @throws InvalidValueTypeAndFormatMetadataEx
   * @throws SAECaptureServiceEx
   * @throws IOException
   * @throws ParseException
   * @throws RequiredArchivableMetadataEx
   * @throws MetadataValueNotInDictionaryEx
   * @throws URISyntaxException
   * @throws DictionaryNotFoundException
   * @throws ResourceException
   */
  @Test
  public final void checkUntypedMetadataNotInDictionary()
      throws UnknownMetadataEx, DuplicatedMetadataEx,
      InvalidValueTypeAndFormatMetadataEx, SAECaptureServiceEx, IOException,
      ParseException, RequiredArchivableMetadataEx, URISyntaxException,
      DictionaryNotFoundException, ResourceException {

    final ClassPathResource ressource = new ClassPathResource("doc/doc1.PDF");
    final List<UntypedMetadata> metas = new ArrayList<>();
    metas.add(new UntypedMetadata("CodeRND", "1.6"));
    metas.add(new UntypedMetadata("Hash", "hash"));
    final DataHandler dataHandler = new DataHandler(new InputStreamSource(FileUtils
                                                                          .openInputStream(ressource.getFile())));
    final UntypedDocument untypedDocument = new UntypedDocument(dataHandler, metas);
    try {
      saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
    } catch (final MetadataValueNotInDictionaryEx ex) {
      Assert
      .assertEquals(
                    "La valeur de la métadonnée CodeRND est incorrecte: elle n'est pas comprise dans le dictionnaire de données associé",
                    ex.getMessage());
    }

  }

  /**
   * Test permettant de vérifier qu'une exception est levée si le dictionnaire
   * n'est pas trouvé
   *
   * @throws UnknownMetadataEx
   * @throws DuplicatedMetadataEx
   * @throws InvalidValueTypeAndFormatMetadataEx
   * @throws SAECaptureServiceEx
   * @throws IOException
   * @throws ParseException
   * @throws RequiredArchivableMetadataEx
   * @throws URISyntaxException
   * @throws MetadataValueNotInDictionaryEx
   * @throws ResourceException
   */

  @Test(expected = MetadataRuntimeException.class)
  @Ignore("TODO : pour la release, en attendant l'analyse du problème")
  public final void checkUntypedMetadataDictionaryNotExist()
      throws UnknownMetadataEx, DuplicatedMetadataEx,
      InvalidValueTypeAndFormatMetadataEx, SAECaptureServiceEx, IOException,
      ParseException, RequiredArchivableMetadataEx, URISyntaxException,
      MetadataValueNotInDictionaryEx, ResourceException {

    final ClassPathResource ressource = new ClassPathResource("doc/doc1.PDF");

    final List<UntypedMetadata> metas = new ArrayList<>();
    metas.add(new UntypedMetadata("CodeRND", "1.1"));
    metas.add(new UntypedMetadata("Hash", "hash"));
    // Dans le jeu de données cassandra-local-dataset-metadata-dictionary.xml, la métadonnée Siret
    // est déclarée comme associée à un dictionnaire dicCodeRND, mais celui-ci n'existe pas. Du coup,
    // une exception doit être levée.
    metas.add(new UntypedMetadata("Siret", "siret"));
    final DataHandler dataHandler = new DataHandler(new InputStreamSource(FileUtils
                                                                          .openInputStream(ressource.getFile())));
    final UntypedDocument untypedDocument = new UntypedDocument(dataHandler, metas);
    saeControlesCaptureService.checkUntypedMetadata(untypedDocument);
  }

}
