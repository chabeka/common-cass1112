package fr.urssaf.image.sae.regionalisation.service.validation;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
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
         public int launchWithFile(boolean updateDatas, File source,
               String uuid, int firstRecord, int lastRecord, String dirPath) {
            // pas d'implémentation
            return 0; 
         }

      };
   }

   @Test
   public void launchWithFile_failure_file_null() throws IOException {

      try {

         service.launchWithFile(true, null, null, -1, -1, null);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "le paramètre fichier doit être renseigné", e.getMessage());

      } 
   }

   @Test
   public void launchWithFile_failure_uuid_null() throws IOException {

      try {

         service.launchWithFile(true, new File(""), null, -1, -1, null);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "l'identifiant unique doit être renseigné", e.getMessage());

      } catch (Throwable throwable) {
         Assert.fail("le type d'exception levé n'est pas le bon");
      }
   }

   @Test
   public void launchWithFile_failure_plage_incorrecte() throws IOException {

      try {

         service.launchWithFile(true, new File(""), null, 12, 1, null);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "l'identifiant unique doit être renseigné", e.getMessage());

      } catch (Throwable throwable) {
         Assert
               .fail("l'index de départ doit être inférieur ou égal à l'index de fin");
      }
   }

   @Test
   public void launchWithFile_failure_repertoire_null() throws IOException {

      try {

         service.launchWithFile(true, new File(""), null, 1, 10, null);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "l'identifiant unique doit être renseigné", e.getMessage());

      } catch (Throwable throwable) {
         Assert.fail("le répertoire parent doit être renseigné");
      }
   }

}
