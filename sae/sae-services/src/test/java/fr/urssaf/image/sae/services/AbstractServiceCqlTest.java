/**
 *   (AC75095351) Classe abstraite pour configuration commune de tests
 */
package fr.urssaf.image.sae.services;

import java.net.URL;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.format.referentiel.dao.support.facade.ReferentielFormatSupportFacade;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.FormatFichierUtils;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.facade.SaeMetadataSupportFacade;
import fr.urssaf.image.sae.metadata.utils.MetadataUtils;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.utils.TraceDestinataireCqlUtils;

/**
 * Classe abstraite (ne peut être instanciée) pour gérer l'implentation commune des tests
 * des services liées aux metadata
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-services-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractServiceCqlTest {
  @Autowired
  protected CassandraServerBean server;

  @Autowired
  protected
  SaeMetadataSupportFacade saeMetadataSupportFacade;

  @Autowired
  private ReferentielFormatSupportFacade referentielFormatSupportFacade;

  @Autowired
  TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  protected static boolean init = false;

  // private final String cfName = Constantes.CF_METADATA;

  /*
   * @BeforeClass
   * public static void init() {
   * init = false;
   * }
   */

  /*
   * @Before
   * public void setup() throws Exception {
   * initMetadata();
   * }
   */

  /**
   * @throws InterruptedException
   * @throws Exception
   */
  protected void initMetadata() throws InterruptedException, Exception {
    if (server.getStartLocal()) {
      // Si l'initialisation a eu lieu on supprime les données
      if (init) {
        server.clearTables();
      } else {
        // On effectue l'initialisation en recréant le Keyspace cql
        server.resetData(true, ModeGestionAPI.MODE_API.DATASTAX);
        init = true;
      }
      createAllMetadata();
      createReferentielFormat();
      createAllTraceDestinataire();
    }
  }


  /**
   * Création des données Metadata pour effectuer les tests des services en Cql
   */
  private void createAllMetadata() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-metadonnees-services.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "Metadata");
    final List<MetadataReference> listMetaData = MetadataUtils.convertRowsToMetadata(list);
    int i = 0;
    for (final MetadataReference metadataReference : listMetaData) {
      saeMetadataSupportFacade.create(metadataReference);
      i++;
    }
  }

  private void createReferentielFormat() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-format-services.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "ReferentielFormat");
    final List<FormatFichier> listFormatFichier = FormatFichierUtils.convertRowsToFormatFichier(list);
    for (final FormatFichier formatFichier : listFormatFichier) {
      referentielFormatSupportFacade.create(formatFichier);
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
