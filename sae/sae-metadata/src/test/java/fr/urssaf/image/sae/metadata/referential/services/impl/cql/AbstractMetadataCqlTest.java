/**
 *   (AC75095351) Classe abstraite pour configuration commune de tests
 */
package fr.urssaf.image.sae.metadata.referential.services.impl.cql;

import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.facade.SaeMetadataSupportFacade;
import fr.urssaf.image.sae.metadata.utils.Constantes;
import fr.urssaf.image.sae.metadata.utils.MetadataUtils;

/**
 * Classe abstraite (ne peut être instanciée) pour gérer l'implentation commune des tests
 * des services liées aux metadata
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
@SuppressWarnings("PMD")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractMetadataCqlTest {
  @Autowired
  private CassandraServerBean server;

  @Autowired
  SaeMetadataSupportFacade saeMetadataSupportFacade;

  static boolean init = false;

  private final String cfName = Constantes.CF_METADATA;

  @BeforeClass
  public static void init() {
    init = false;
  }

  @Before
  public void setup() throws Exception {

    if (server.getStartLocal()) {
      // Si l'initialisation a eu lieu on supprime les données
      if (init) {
        server.clearTables();
      } else {
        // On effectue l'initialisation en recréant le Keyspace cql
        server.resetData(true, ModeGestionAPI.MODE_API.DATASTAX);
        init = true;
      }
      GestionModeApiUtils.setModeApiCql(cfName);
      createAllMetadata();
    }
  }


  /**
   * Création des données Metadata pour effectuer les tests des services en Cql
   */
  private void createAllMetadata() {

    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-metadonnees.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "Metadata");
    final List<MetadataReference> listMetaData = MetadataUtils.convertRowsToMetadata(list);
    int i = 0;
    for (final MetadataReference metadataReference : listMetaData) {
      saeMetadataSupportFacade.create(metadataReference);
      i++;
    }
  }

  @Test
  public void z_end() throws Exception {

    if (server.isCassandraStarted()) {
      server.resetData();
    }
    Assert.assertTrue(true);
  }

}
