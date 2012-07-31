package fr.urssaf.image.sae.regionalisation;

import java.io.IOException;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.regionalisation.mock.ServiceMock;
import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;
import fr.urssaf.image.sae.regionalisation.security.RegionalisationCallbackHandler;
import fr.urssaf.image.sae.regionalisation.security.RegionalisationLoginModule;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;

@SuppressWarnings("PMD.MethodNamingConventions")
public class BootStrapTest {

   private BootStrap bootStrap;

   private static final String DFCE_CONFIG = "src/test/resources/dfce/test-dfce.properties";

   private static final String POSTGRESQL_CONFIG = "src/test/resources/database/test-postgresql.properties";

   private ProcessingService processingService;

   @Before
   public void before() {

      CallbackHandler callbackHandler = new RegionalisationCallbackHandler() {

         protected String loadPassword(PasswordCallback passwordCallback)
               throws IOException {

            return "toto";

         }

      };

      AuthenticateSupport authenticateSupport = new AuthenticateSupport(
            callbackHandler, RegionalisationLoginModule.class);

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

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG, "5",
            "100", "TIR_A_BLANC" };
      bootStrap.execute(args);

      assertService();
   }

   @Test
   public void execute_success_MISE_A_JOUR() {

      processingService.launch(true, 12, 50);

      EasyMock.replay(processingService);

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG, "12",
            "50", "MISE_A_JOUR" };
      bootStrap.execute(args);

      assertService();
   }

}
