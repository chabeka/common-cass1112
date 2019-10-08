/**
 *   (AC75095351) Classe abstraite pour configuration commune de tests
 */
package fr.urssaf.image.sae.metadata.control.services.impl.cql;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.facade.SaeMetadataSupportFacade;
import fr.urssaf.image.sae.metadata.test.utils.MetadataUtils;
import fr.urssaf.image.sae.metadata.utils.Constantes;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.utils.TraceDestinataireCqlUtils;



/**
 * Classe abstraite (ne peut être instanciée) pour gérer l'implémentation commune des tests
 * des services liées aux metadata.
 * L'injection de données sefait à l'aide d'une copie du fichier de données "cassandra-local-dataset-sae-metadonnees.xml" pour thrift
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
@SuppressWarnings("PMD")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractMetadataControlCqlTest {
  @Autowired
  @Qualifier("metadataControlServices")
  protected MetadataControlServices controlService;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  SaeMetadataSupportFacade saeMetadataSupportFacade;

  @Autowired
  TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  static boolean init = false;


  @BeforeClass
  public static void init() {
    init = false;
  }

  @Before
  public void setup() throws Exception {
    final HashMap<String, String> modesApiTest = new HashMap<>();
    modesApiTest.put(Constantes.CF_METADATA, ModeGestionAPI.MODE_API.DATASTAX);
    modesApiTest.put("tracedestinatairecql", ModeGestionAPI.MODE_API.DATASTAX);
    modesApiTest.put("traceregtechnique", ModeGestionAPI.MODE_API.DATASTAX);
    ModeGestionAPI.setListeCfsModes(modesApiTest);
    if (server.getStartLocal()) {
      if (init) {
        server.clearTables();
      } else {
        server.resetData(true, ModeGestionAPI.MODE_API.DATASTAX);
        init = true;
      }

      createAllMetadata();
      createAllTraceDestinataire();

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

  /**
   * Création des données TraceDestinataire pour effectuer les tests des services en Cql
   */
  private void createAllTraceDestinataire() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-traces.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "TraceDestinataire");
    // final List<Row> list = DataCqlUtils.deserialize(url.getPath());

    int i = 0;
    final List<TraceDestinataire> listTraceDestinataire = TraceDestinataireCqlUtils.convertRowsToTraceDestinataires(list);
    for (final TraceDestinataire traceDestinataire : listTraceDestinataire) {
      traceDestinataireCqlSupport.create(traceDestinataire, new Date().getTime());
      i++;
    }
  }

  @Test
  public void z_end() {
    try {
      if (server.isCassandraStarted()) {
        server.resetData();
      }
      Assert.assertTrue(true);
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
