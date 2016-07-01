package fr.urssaf.image.sae.services.executable;

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
                     "L'opération du traitement doit être renseigné.", e
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
   public void ServicesMain_traitementMasse() {

      String[] args = new String[] { "traitementMasse"};

      ServicesMain.main(args);
   }
}
