package fr.urssaf.image.sae.ordonnanceur.util;

import java.io.IOException;

import org.junit.Test;

@SuppressWarnings("PMD.MethodNamingConventions")
public class LauncherUtilsTest {

   private Process process;

   public void after() {

      if (process != null) {

         process.destroy();
      }
   }

   @Test
   public void launch_failure_notexist_executable() throws IOException {

      process = LauncherUtils.launch("java -jar exemple.jar");

      // Assert
      // .assertTrue(
      // "Par convention le processus doit échouer et indiquer une autre valeur que 0",
      // process.exitValue() != 0);

   }

   @Test(expected = IOException.class)
   public void launch_failure_badcommand() throws IOException {

      process = LauncherUtils.launch("badcommand");

   }
}
