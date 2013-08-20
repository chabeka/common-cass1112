package fr.urssaf.image.sae.regionalisation;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;

@SuppressWarnings("PMD.MethodNamingConventions")
public class BootStrapValidationTest {

   /**
    * 
    */
   private static final String UUID = "12";

   private BootStrap bootStrap;

   private static final String DFCE_CONFIG = "src/test/resources/dfce/test-dfce.properties";

   private static final String FIRST_INDEX = "1";

   private static final String COUNT_RECORD = "100";

   private static final String FICHIER_SOURCE = "csv/fichier_format_correct";

   @Before
   public void before() {

      bootStrap = new BootStrap(
            "/applicationContext-sae-regionalisation-test.xml",
            new AuthenticateSupport());
   }

   private void assertExecute(String[] args, String message) {

      try {
         bootStrap.validate(args);
         Assert.fail("Une IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est incorrect",
               message, e.getMessage());
      }
   }

   @Test
   public void execute_failure_required_source() {
      String[] args = new String[0];

      assertExecute(args, "L'identifiant de traitement doit être renseigné.");
   }

   @Test
   public void execute_failure_required_dfce() {

      String[] args = new String[] { UUID };

      assertExecute(
            args,
            "Le chemin complet du fichier de configuration connexion DFCE doit être renseigné.");

   }

   @Test
   public void execute_failure_required_file() {

      String[] args = new String[] { UUID, DFCE_CONFIG };

      assertExecute(args, "Le fichier source doit être indiqué");

   }

   @Test
   public void execute_failure_exists_file() {

      String[] args = new String[] { UUID, DFCE_CONFIG, "dd" };

      assertExecute(args, "Le fichier spécifié doit exister");

   }

   @Test
   public void execute_failure_required_first_index() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath() };

      assertExecute(args,
            "L'index de l'enregistrement de départ doit être renseigné.");

   }

   @Test
   public void execute_failure_false_first_index() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            "zzzz" };

      assertExecute(args,
            "L'index de l'enregistrement de départ doit être un nombre.");

   }

   @Test
   public void execute_failure_false_first_negatif() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            "-1" };

      assertExecute(
            args,
            "L'index de l'enregistrement de départ doit être un nombre supérieur ou égal à 1.");

   }

   @Test
   public void execute_failure_required_count_record() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            FIRST_INDEX };

      assertExecute(args,
            "L'index du dernier enregistrement à traiter doit être renseigné.");

   }

   @Test
   public void execute_failure_false_count_record() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            FIRST_INDEX, "aaaa" };

      assertExecute(args,
            "L'index du dernier enregistrement à traiter doit être un nombre.");

   }

   @Test
   public void execute_failure_last_lt_than_first() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            FIRST_INDEX, "-1" };

      assertExecute(
            args,
            "L'index du dernier enregistrement doit être supérieur ou égal à l'index de l'enregistrement de départ.");

   }

   @Test
   public void execute_failure_repertoire_required() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            FIRST_INDEX, COUNT_RECORD };

      assertExecute(args,
            "Le répertoire de génération des fichiers de résultats doit être renseigné.");

   }

   @Test
   public void execute_failure_required_mode() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            FIRST_INDEX, COUNT_RECORD, "c:/" };

      assertExecute(args,
            "Le mode TIR_A_BLANC/MISE_A_JOUR doit être renseigné.");

   }

   @Test
   public void execute_failure_bad_mode() throws IOException {

      String[] args = new String[] { UUID, DFCE_CONFIG,
            new ClassPathResource(FICHIER_SOURCE).getFile().getAbsolutePath(),
            FIRST_INDEX, COUNT_RECORD, "c:/", "BAD_MODE" };

      assertExecute(args, "Le mode doit être TIR_A_BLANC ou MISE_A_JOUR.");

   }
}
