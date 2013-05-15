package fr.urssaf.image.sae.rnd.executable;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import exception.MajCorrespondancesMainException;
import fr.urssaf.image.sae.rnd.exception.MajCorrespondancesException;
import fr.urssaf.image.sae.rnd.exception.MajRndException;
import fr.urssaf.image.sae.rnd.executable.service.RndServiceProvider;
import fr.urssaf.image.sae.rnd.service.MajCorrespondancesService;

@SuppressWarnings("PMD.MethodNamingConventions")
public class MajCorrespondancesMainFailureTest {

   private MajCorrespondancesMain instance;

   private MajCorrespondancesService majCorrespondancesService;

   @Before
   public void before() {

      instance = new MajCorrespondancesMain(
            "/applicationContext-sae-rnd-executable-test.xml");

      majCorrespondancesService = RndServiceProvider.getInstanceMajCorrespondancesService();

   }

   @After
   public void after() {

      EasyMock.reset(majCorrespondancesService);

   }

   private void mockThrowable(Throwable expectedThrowable) {

      try {

         majCorrespondancesService.lancer();

         EasyMock.expectLastCall().andThrow(expectedThrowable);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(majCorrespondancesService);
   }

   private void callService() {

      String[] args = new String[] {
            "src/test/resources/config_sae.properties" };

      instance.execute(args);
   }

   @Test
   public void majRndMain_failure() {

      mockThrowable(new MajCorrespondancesException());

      try {

         callService();

         Assert
               .fail("le service doit une lever une exception de type MajCorrespondancesMainException");

      } catch (MajCorrespondancesMainException e) {

         Assert.assertEquals("exception non attendue", MajCorrespondancesException.class, e
               .getCause().getClass());
      }

   }

}
