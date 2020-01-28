package fr.urssaf.image.sae.services.metadata.cql;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.support.facade.SaeMetadataSupportFacade;
import fr.urssaf.image.sae.metadata.utils.MetadataUtils;
import fr.urssaf.image.sae.services.metadata.MetadataService;
import fr.urssaf.image.sae.services.metadata.impl.MetadataServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-services-test.xml"})
public class MetadataServiceCqlTest {


  @BeforeClass
  public static void beforeClass() throws IOException {
    ModeApiAllUtils.setAllModeAPICql();
  }

  @Before
  public void before() throws Exception {
    createAllMetadata();
  }

  @After
  public void end() throws Exception {
    server.resetData();
  }

  @Autowired
  protected CassandraServerBean server;

  private MetadataService metadataService;

  @Autowired
  private SaeMetaDataService saeMetaDataService;

  @Autowired
  protected SaeMetadataSupportFacade saeMetadataSupportFacade;

  @Test
  public void getClientAvailableMetadata() {

    // On créé le metadataService sans injection avec le constructeur pour que les données de la classe abstraite soient pris en compte
    metadataService = new MetadataServiceImpl(saeMetaDataService);

    final List<MetadataReference> metadatas = metadataService
        .getClientAvailableMetadata();
    Assert
    .assertNotNull(
                   "La liste des métadonnées mise à disposition du client ne doit pas être nulle",
                   metadatas);
    Assert
    .assertFalse(
                 "La liste des métadonnées  mise à disposition du client ne doit pas être vide",
                 metadatas.isEmpty());

    for (final MetadataReference metadata : metadatas) {
      Assert.assertTrue(
                        "La métadonnée n'est pas mise à disposition du client", metadata
                        .isClientAvailable());
    }
  }

  /**
   * Création des données Metadata pour effectuer les tests des services en Cql
   */
  private void createAllMetadata() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-metadonnees-services.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "Metadata");
    final List<MetadataReference> listMetaData = MetadataUtils.convertRowsToMetadata(list);

    for (final MetadataReference metadataReference : listMetaData) {
      saeMetadataSupportFacade.create(metadataReference);
    }
  }
}
