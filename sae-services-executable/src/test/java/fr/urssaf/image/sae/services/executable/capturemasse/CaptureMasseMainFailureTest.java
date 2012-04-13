package fr.urssaf.image.sae.services.executable.capturemasse;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.executable.capturemasse.exception.CaptureMasseMainException;
import fr.urssaf.image.sae.services.executable.service.SAEServiceProvider;

@SuppressWarnings("PMD.MethodNamingConventions")
public class CaptureMasseMainFailureTest {

   private CaptureMasseMain instance;

   private SAEBulkCaptureService bulkCapture;

   @Before
   public void before() {

      instance = new CaptureMasseMain(
            "/applicationContext-sae-services-executable-test.xml");

      bulkCapture = SAEServiceProvider.getInstanceSAEBulkCaptureService();

   }

   @After
   public void after() {

      EasyMock.reset(bulkCapture);

   }

   private void mockThrowable(Throwable expectedThrowable) {

      try {

         bulkCapture.bulkCapture(EasyMock.anyObject(String.class));

         EasyMock.expectLastCall().andThrow(expectedThrowable);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(bulkCapture);
   }

   private void callService() {

      String[] args = new String[] {
            "ecde://ecde.cer69.recouv/DCL001/19991231/3/sommaire.xml",
            "src/test/resources/config_sae.properties", "context_log" };

      instance.captureMasse(args);
   }

   @Test
   public void captureMasseMain_failure_CaptureBadEcdeUrlEx() {

      mockThrowable(new CaptureBadEcdeUrlEx(null));

      try {

         callService();

         Assert
               .fail("le service doit une lever une exception de type CaptureMasseMainException");

      } catch (CaptureMasseMainException e) {

         Assert.assertEquals("exception non attendue",
               CaptureBadEcdeUrlEx.class, e.getCause().getClass());
      }

   }

   @Test
   public void captureMasseMain_failure_CaptureEcdeUrlFileNotFoundEx() {

      mockThrowable(new CaptureEcdeUrlFileNotFoundEx(null));

      try {

         callService();

         Assert.fail("le service doit une lever une exception");

         Assert
               .fail("le service doit une lever une exception de type CaptureMasseMainException");

      } catch (CaptureMasseMainException e) {

         Assert.assertEquals("exception non attendue",
               CaptureEcdeUrlFileNotFoundEx.class, e.getCause().getClass());
      }

   }

   @Test
   public void captureMasseMain_failure_CaptureEcdeWriteFileEx() {

      mockThrowable(new CaptureEcdeWriteFileEx(null));

      try {

         callService();

         Assert
               .fail("le service doit une lever une exception de type CaptureMasseMainException");

      } catch (CaptureMasseMainException e) {

         Assert.assertEquals("exception non attendue",
               CaptureEcdeWriteFileEx.class, e.getCause().getClass());
      }

   }

}
