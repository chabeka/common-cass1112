package fr.urssaf.image.sae.rnd.executable.bootstrap;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.executable.bootstrap.MajRndMain;
import fr.urssaf.image.sae.rnd.executable.exception.MajRndMainException;
import fr.urssaf.image.sae.rnd.executable.service.RndServiceProvider;
import fr.urssaf.image.sae.rnd.service.MajRndService;

@SuppressWarnings("PMD.MethodNamingConventions")
public class MajRndMainFailureTest {

   private MajRndMain instance;

   private MajRndService majRndService;

   @Before
   public void before() {

      instance = new MajRndMain(
            "/applicationContext-sae-rnd-executable-test.xml");

      majRndService = RndServiceProvider.getInstanceMajRndService();

   }

   @After
   public void after() {

      EasyMock.reset(majRndService);

   }

   private void mockThrowable(Throwable expectedThrowable) {

      try {

         majRndService.lancer();

         EasyMock.expectLastCall().andThrow(expectedThrowable);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(majRndService);
   }

   private void callService() {

      String[] args = new String[] {
            "src/test/resources/config_sae.properties", "context_log" };

      instance.execute(args);
   }

   @Test
   public void majRndMain_failure() {

      mockThrowable(new MajRndException());

      try {

         callService();

         Assert
               .fail("le service doit une lever une exception de type MajRndMainException");

      } catch (MajRndMainException e) {

         Assert.assertEquals("exception non attendue", MajRndException.class, e
               .getCause().getClass());
      }

   }

}
