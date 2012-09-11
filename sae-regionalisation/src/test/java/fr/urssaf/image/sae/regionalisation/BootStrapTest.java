package fr.urssaf.image.sae.regionalisation;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.regionalisation.mock.ServiceMock;
import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.util.AuthentificationUtils;

@SuppressWarnings("PMD.MethodNamingConventions")
public class BootStrapTest {

   private BootStrap bootStrap;

   private static final String BASE_SOURCE = "BASE";

   private static final String FILE_SOURCE = "CSV";

   private static final String DFCE_CONFIG = "src/test/resources/dfce/test-dfce.properties";

   private static final String POSTGRESQL_CONFIG = "src/test/resources/database/test-postgresql.properties";

   private ProcessingService processingService;

   @Before
   public void before() {

      AuthenticateSupport authenticateSupport = AuthentificationUtils
            .createAuthenticateSupport("regionalisation");

      bootStrap = new BootStrap(
            "/applicationContext-sae-regionalisation-test.xml",
            authenticateSupport);

      processingService = ServiceMock.createProcessingService();
   }

   @After
   public void after() {

      EasyMock.reset(processingService);
   }

   private void assertService() {

      EasyMock.verify(processingService);
   }

   @Test
   public void execute_success_TIR_A_BLANC() {

      processingService.launch(false, 5, 100);

      EasyMock.replay(processingService);

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, "5", "100", "TIR_A_BLANC" };
      bootStrap.execute(args);

      assertService();
   }

   @Test
   public void execute_success_MISE_A_JOUR() {

      processingService.launch(true, 12, 50);

      EasyMock.replay(processingService);

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, "12", "50", "MISE_A_JOUR" };
      bootStrap.execute(args);

      assertService();
   }

   @Test
   public void execute_validate_failure() {

      EasyMock.replay(processingService);

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG };
      bootStrap.execute(args);

      assertService();
   }

   @Test
   public void execute_authentification_failure() {

      AuthenticateSupport authenticateSupport = AuthentificationUtils
            .createAuthenticateSupport("tata");

      bootStrap = new BootStrap(
            "/applicationContext-sae-regionalisation-test.xml",
            authenticateSupport);

      EasyMock.replay(processingService);

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, "12", "50", "MISE_A_JOUR" };
      bootStrap.execute(args);

      assertService();
   }

}
