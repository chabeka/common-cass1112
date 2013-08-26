package fr.urssaf.image.sae.regionalisation;

import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.regionalisation.mock.ServiceMock;
import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.util.AuthentificationUtils;

@SuppressWarnings("PMD.MethodNamingConventions")
@Ignore("à revoir")
public class BootStrapTest {

   private BootStrap bootStrap;

   private static final String DFCE_CONFIG = "src/test/resources/dfce/test-dfce.properties";

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
   public void execute_validate_failure() {

      EasyMock.replay(processingService);

      String[] args = new String[] { "12", DFCE_CONFIG };
      bootStrap.execute(args);

      assertService();
   }

   @Test
   public void execute_authentification_failure() throws IOException {

      AuthenticateSupport authenticateSupport = AuthentificationUtils
            .createAuthenticateSupport("tata");

      bootStrap = new BootStrap(
            "/applicationContext-sae-regionalisation-test.xml",
            authenticateSupport);

      EasyMock.replay(processingService);

      String[] args = new String[] { "12", DFCE_CONFIG, "12", "62", "chemin",
            "MISE_A_JOUR" };
      bootStrap.execute(args);

      assertService();
   }

}
