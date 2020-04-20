/**
 *   (AC75095351) Classe abstraite pour configuration commune de tests
 */
package fr.urssaf.image.sae.trace.servicecql;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;

/**
 * Classe abstraite (ne peut être instanciée) pour gérer l'implentation commune des tests
 * des services liées aux metadata
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractServiceCqlTest {

  @Autowired
  protected CassandraServerBean server;


  @Autowired
  TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  protected static boolean init = false;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AbstractServiceCqlTest.class);

  @BeforeClass
  public static void init() {
    init = false;
  }

  @Before
  public void setup() throws Exception {
    initData();
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
  }

  @After
  public void after() throws Exception {
    server.clearTables();
  }
  /**
   * @throws InterruptedException
   * @throws Exception
   */
  protected void initData() throws InterruptedException, Exception {
    if (server.getStartLocal()) {
      // Si l'initialisation a eu lieu on supprime les données
      if (init) {
        server.clearTables();
      } else {
        // On effectue l'initialisation en recréant le Keyspace cql
        server.resetData(true, ModeGestionAPI.MODE_API.DATASTAX);
        init = true;
      }

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
      LOGGER.error("Une erreur s'est produite lors du reset de cassandra: {}", e.getMessage());
    }
  }

}
