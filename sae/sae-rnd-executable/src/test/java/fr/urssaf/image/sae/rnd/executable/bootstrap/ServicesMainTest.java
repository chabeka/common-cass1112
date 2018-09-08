package fr.urssaf.image.sae.rnd.executable.bootstrap;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.rnd.executable.bootstrap.ServicesMain;

@SuppressWarnings("PMD.MethodNamingConventions")
public class ServicesMainTest {

   @Test
   public void ServicesMain_failure_emptyOperation() throws Throwable {

      try {

         ServicesMain.main(new String[0]);

         Assert
               .fail("Une IllegalArgumentException doit être levée car aucun opération n'est indiquée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est incorrect",
               "L'opération du traitement doit être renseignée.", e
                     .getMessage());
      }
   }

   @Test
   public void ServicesMain_failure_unknownOperation() throws Throwable {

      String operation = "unknownOperation";

      try {

         ServicesMain.main(new String[] { operation });

         Assert
               .fail("Une IllegalArgumentException doit être levée car le nom de l'opération '"
                     + operation + "' est inconnu");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est incorrect",
               "L'opération du traitement '" + operation + "' est inconnu.", e
                     .getMessage());
      }
   }

   @Ignore
   @Test
   public void ServicesMain_majRnd() throws Throwable {

      String[] args = new String[] { "MAJ_RND",
            "src/test/resources/config_sae.properties" };

      ServicesMain.main(args);
   }

   @Ignore
   @Test
   public void ServicesMain_majCorrespondances() throws Throwable {

      String[] args = new String[] { "MAJ_CORRESPONDANCES_RND",
            "src/test/resources/config_sae.properties" };

      ServicesMain.main(args);
   }
}
