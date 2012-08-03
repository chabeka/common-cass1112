package fr.urssaf.image.sae.regionalisation.service.validation;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.regionalisation.service.ProcessingService;

@SuppressWarnings("PMD.MethodNamingConventions")
public class ProcessingServiceValidationTest {

   private ProcessingService service;

   @Before
   public void before() {

      service = new ProcessingService() {

         @Override
         public void launch(boolean updateDatas, int firstRecord,
               int processingCount) {

            // aucune implémentation

         }

      };
   }

   @Test
   public void launch_failure_firstRecord() throws IOException {

      try {

         service.launch(true, -1, 5);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "le paramètre 'firstRecord' doit être supérieur ou égal à 0", e
                     .getMessage());

      }
   }

   @Test
   public void launch_failure_processingCount() throws IOException {

      try {

         service.launch(true, 0, 0);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "le paramètre 'processingCount' doit être supérieur à 0", e
                     .getMessage());

      }
   }
}
