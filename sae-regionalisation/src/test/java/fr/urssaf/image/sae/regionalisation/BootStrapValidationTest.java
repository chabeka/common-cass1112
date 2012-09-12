package fr.urssaf.image.sae.regionalisation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;

@SuppressWarnings("PMD.MethodNamingConventions")
public class BootStrapValidationTest {

   private BootStrap bootStrap;

   private static final String DFCE_CONFIG = "src/test/resources/dfce/test-dfce.properties";

   private static final String POSTGRESQL_CONFIG = "src/test/resources/database/test-postgresql.properties";

   private static final String FIRST_INDEX = "0";

   private static final String COUNT_RECORD = "100";

   private static final String BASE_SOURCE = "BASE";

   private static final String CSV_SOURCE = "CSV";

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

      assertExecute(args, "La source de données doit être indiquée BASE/CSV");
   }

   @Test
   public void execute_failure_source_inexistante() {
      String[] args = new String[] { "SOURCE_INEXISTANTE" };

      assertExecute(args, "La source soit être valide : BASE / CSV");
   }

   @Test
   public void execute_failure_required_dfce() {

      String[] args = new String[] { BASE_SOURCE };

      assertExecute(
            args,
            "Le chemin complet du fichier de configuration connexion DFCE doit être renseigné.");

   }

   @Test
   public void execute_failure_required_postgresql() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG };

      assertExecute(
            args,
            "Le chemin complet du fichier de configuration connexion POSTGRESQL doit être renseigné.");

   }

   @Test
   public void execute_failure_required_first_index() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG };

      assertExecute(args,
            "L'index de l'enregistrement de départ doit être renseigné.");

   }

   @Test
   public void execute_failure_false_first_index() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, "zzzz" };

      assertExecute(args,
            "L'index de l'enregistrement de départ doit être un nombre.");

   }

   @Test
   public void execute_failure_false_first_negatif() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, "-1" };

      assertExecute(
            args,
            "L'index de l'enregistrement de départ doit être un nombre supérieur ou égal à 0.");

   }

   @Test
   public void execute_failure_required_count_record() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, FIRST_INDEX };

      assertExecute(args,
            "Le nombre d'enregistrement à traiter doit être renseigné.");

   }

   @Test
   public void execute_failure_false_count_record() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, FIRST_INDEX, "aaaa" };

      assertExecute(args,
            "Le nombre d'enregistrement à traiter doit être un nombre.");

   }

   @Test
   public void execute_failure_false_count_negatif() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, FIRST_INDEX, "0" };

      assertExecute(args,
            "Le nombre d'enregistrement à traiter doit être un nombre supérieur à 0.");

   }

   @Test
   public void execute_failure_required_mode() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, FIRST_INDEX, COUNT_RECORD };

      assertExecute(args,
            "Le mode TIR_A_BLANC/MISE_A_JOUR doit être renseigné.");

   }

   @Test
   public void execute_failure_bad_mode() {

      String[] args = new String[] { BASE_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, FIRST_INDEX, COUNT_RECORD, "BAD_MODE" };

      assertExecute(args, "Le mode doit être TIR_A_BLANC ou MISE_A_JOUR.");

   }

   @Test
   public void execute_failure_database_required() {
      String[] args = new String[] { CSV_SOURCE, DFCE_CONFIG };

      assertExecute(
            args,
            "Le chemin complet du fichier de configuration connexion POSTGRESQL doit être renseigné.");
   }

   @Test
   public void execute_failure_file_required() {
      String[] args = new String[] { CSV_SOURCE, DFCE_CONFIG, POSTGRESQL_CONFIG };

      assertExecute(args,
            "Le fichier doit être indiqué si la source est un fichier");
   }

   @Test
   public void execute_failure_file_must_exist() {
      String[] args = new String[] { CSV_SOURCE, DFCE_CONFIG,
            POSTGRESQL_CONFIG, "src/test/fichierInexistant.txt" };

      assertExecute(args, "Le fichier spécifié doit exister");
   }
}
