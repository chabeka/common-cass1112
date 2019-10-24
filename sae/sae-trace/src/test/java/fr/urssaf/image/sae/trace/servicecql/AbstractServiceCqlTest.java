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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.commons.utils.ModeApiAllUtils;
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

  protected static boolean init = false;

  @BeforeClass
  public static void init() {
    init = false;
  }

  @Before
  public void setup() throws Exception {
    initData();
    ModeApiAllUtils.setAllModeAPICql();
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
      e.printStackTrace();
    }
  }

}
