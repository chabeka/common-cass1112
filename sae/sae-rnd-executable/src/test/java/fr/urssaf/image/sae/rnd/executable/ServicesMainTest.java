package fr.urssaf.image.sae.rnd.executable;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("PMD.MethodNamingConventions")
public class ServicesMainTest {

   @Test
   public void ServicesMain_failure_emptyOperation() {

      try {

         ServicesMain.main(new String[0]);

         Assert
               .fail("Une IllegalArgumentException doit être levée car aucun opération n'est indiquée");

      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals("le message de l'exception est incorrect",
                     "L'opération du traitement doit être renseignée.", e
                           .getMessage());
      }
   }

   @Test
   public void ServicesMain_failure_unknownOperation() {

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

   @Test
   public void ServicesMain_majRnd() {

      String[] args = new String[] { "MAJ_RND" };

      ServicesMain.main(args);
   }

   @Test
   public void ServicesMain_majCorrespondances() {

      String[] args = new String[] { "MAJ_CORRESPONDANCE_RND" };

      ServicesMain.main(args);
   }
}
