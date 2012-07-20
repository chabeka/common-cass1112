package fr.urssaf.image.sae.regionalisation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD.MethodNamingConventions")
public class BootStrapValidationTest {

   private BootStrap bootStrap;

   private static final String DFCE_CONFIG = "src/test/resources/dfce/test-dfce.properties";

   private static final String POSTGRESQL_CONFIG = "src/test/resources/database/test-postgresql.properties";

   private static final String FIRST_INDEX = "0";

   private static final String COUNT_RECORD = "100";

   @Before
   public void before() {

      bootStrap = new BootStrap(
            "/applicationContext-sae-regionalisation-test.xml");
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
   public void execute_failure_required_dfce() {

      String[] args = new String[0];

      assertExecute(
            args,
            "Le chemin complet du fichier de configuration connexion DFCE doit être renseigné.");

   }

   @Test
   public void execute_failure_required_postgresql() {

      String[] args = new String[] { DFCE_CONFIG };

      assertExecute(
            args,
            "Le chemin complet du fichier de configuration connexion POSTGRESQL doit être renseigné.");

   }

   @Test
   public void execute_failure_required_first_index() {

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG };

      assertExecute(args,
            "L'index de l'enregistrement de départ doit être renseigné.");

   }

   @Test
   public void execute_failure_false_first_index() {

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG, "zzzz" };

      assertExecute(args,
            "L'index de l'enregistrement de départ doit être un nombre.");

   }

   @Test
   public void execute_failure_required_count_record() {

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG,
            FIRST_INDEX };

      assertExecute(args,
            "Le nombre d'enregistrement à traiter doit être renseigné.");

   }

   @Test
   public void execute_failure_false_count_record() {

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG,
            FIRST_INDEX, "aaaa" };

      assertExecute(args,
            "Le nombre d'enregistrement à traiter doit être un nombre.");

   }

   @Test
   public void execute_failure_required_mode() {

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG,
            FIRST_INDEX, COUNT_RECORD };

      assertExecute(args,
            "Le mode TIR_A_BLANC/MISE_A_JOUR doit être renseigné.");

   }

   @Test
   public void execute_failure_bad_mode() {

      String[] args = new String[] { DFCE_CONFIG, POSTGRESQL_CONFIG,
            FIRST_INDEX, COUNT_RECORD, "BAD_MODE" };

      assertExecute(args, "Le mode doit être TIR_A_BLANC ou MISE_A_JOUR.");

   }
}
