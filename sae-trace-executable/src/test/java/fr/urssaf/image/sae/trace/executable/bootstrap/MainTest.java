/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.bootstrap;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;

public class MainTest {

   private String helpCmd;
   private File saeConfig;

   @Before
   public void before() throws IOException {

      ClassPathResource resource = new ClassPathResource(
            "manuel/_LISEZ_MOI.txt");
      File file = resource.getFile();
      helpCmd = FileUtils.readFileToString(file);

      saeConfig = File.createTempFile("sae-config", ".properties");

   }

   @After
   public void after() {
      FileUtils.deleteQuietly(saeConfig);
   }

   @Test
   public void testAucunArgument() throws Throwable {
      try {
         Main.main(null);
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte",
               "la commande est incorrecte.\n" + helpCmd, exception
                     .getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }

   @Test
   public void testHelpArgument() throws Throwable {
      try {
         Main.main(new String[] { "Help" });
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte", helpCmd,
               exception.getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }

   @Test
   public void testNombreArgumentsIncorrect() throws Throwable {
      try {
         Main.main(new String[] { "argument 1" });
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte",
               "la commande est incorrecte.\n" + helpCmd, exception
                     .getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }

   @Test
   public void testFichierInexistant() throws Throwable {
      try {
         Main.main(new String[] { "argument 1", "argument 2", "argument 3" });
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte",
               "le fichier de configuration du SAE est inexistant", exception
                     .getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }

   @Test
   public void testParametrePurgeIncorrect() throws Throwable {
      try {
         Main.main(new String[] { saeConfig.getAbsolutePath(), "PRUGE",
               "argument 3" });
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte",
               "la commande est incorrecte.\n" + helpCmd, exception
                     .getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }

   @Test
   public void testParametreTypePurgeIncorrect() throws Throwable {
      try {
         Main.main(new String[] { saeConfig.getAbsolutePath(), "PURGE",
               "argument 3" });
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte",
               "le registre à purger n'est pas référencé.\n" + helpCmd,
               exception.getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }

   @Test
   public void testParametreTypeJournalisationIncorrect() throws Throwable {
      try {
         Main.main(new String[] { saeConfig.getAbsolutePath(),
               "JOURNALISATION", "argument 3" });
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");

      } catch (TraceExecutableRuntimeException exception) {
         Assert.assertEquals("l'exception doit être correcte",
               "le journal n'est pas référencé.\n" + helpCmd, exception
                     .getMessage());

      } catch (Exception exception) {
         Assert
               .fail("une exception TraceExecutableRuntimeException est attendue");
      }
   }
}
