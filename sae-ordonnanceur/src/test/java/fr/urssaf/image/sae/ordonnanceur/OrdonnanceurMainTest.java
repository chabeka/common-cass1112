package fr.urssaf.image.sae.ordonnanceur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD.MethodNamingConventions")
public class OrdonnanceurMainTest {

   private OrdonnanceurMain instance;

   @Before
   public void before() {

      instance = new OrdonnanceurMain(
            "/applicationContext-sae-ordonnanceur-test.xml");

   }

   @Test
   public void launchTraitement_success() {

      String[] args = new String[] { "src/test/resources/config/sae-config-test.properties" };
      instance.loadOrdonnanceurApplicationContext(args);

      instance.launchTraitement();

   }

   @Test
   public void loadOrdonnanceurApplicationContext_empty_configSAE() {

      String[] args = new String[0];

      try {

         instance.loadOrdonnanceurApplicationContext(args);

         Assert
               .fail("le test doit échouer car le fichier de configuration du SAE n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.",
                     e.getMessage());
      }
   }

}
