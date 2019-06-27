package fr.urssaf.image.sae.services.executable.traitementmasse;

import java.util.UUID;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.executable.service.SAEServiceProvider;
import fr.urssaf.image.sae.services.executable.traitementmasse.exception.TraitementMasseMainException;

@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementMasseMainFailureTest {

   private TraitementMasseMain instance;

   private TraitementAsynchroneService traitementService;

   @Before
   public void before() {

      instance = new TraitementMasseMain(
            "/applicationContext-sae-services-executable-test.xml");

      traitementService = SAEServiceProvider
            .getInstanceTraitementAsynchroneService();

   }

   @After
   public void after() {

      EasyMock.reset(traitementService);

   }

   private void mockThrowable(Throwable expectedThrowable) {

      try {

         traitementService.lancerJob(EasyMock.anyObject(UUID.class));

         EasyMock.expectLastCall().andThrow(expectedThrowable);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(traitementService);
   }

   private void callService(UUID uuid) {

      String[] args = new String[] { uuid.toString(),
            "src/test/resources/config_sae.properties", "context_log" };

      instance.execute(args);
   }

   @Test
   public void traitementMasseMain_failure_JobInexistantException() {

      UUID uuid = UUID.randomUUID();

      mockThrowable(new JobInexistantException(uuid));

      try {

         callService(uuid);

         Assert
               .fail("le service doit une lever une exception de type TraitementMasseMainException");

      } catch (TraitementMasseMainException e) {

         Assert.assertEquals("exception non attendue",
               JobInexistantException.class, e.getCause().getClass());
      }

   }

   @Test
   public void traitementMasseMain_failure_JobNonReserveException() {

      UUID uuid = UUID.randomUUID();

      mockThrowable(new JobNonReserveException(uuid));

      try {

         callService(uuid);

         Assert
               .fail("le service doit une lever une exception de type TraitementMasseMainException");

      } catch (TraitementMasseMainException e) {

         Assert.assertEquals("exception non attendue",
               JobNonReserveException.class, e.getCause().getClass());
      }

   }

}
