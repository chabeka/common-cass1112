package fr.urssaf.image.sae.rnd.executable;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.rnd.executable.service.RndServiceProvider;
import fr.urssaf.image.sae.rnd.service.MajRndService;

@SuppressWarnings("PMD.MethodNamingConventions")
public class MajRndMainTest {

   private MajRndMain instance;

   private MajRndService majRndService;

   @Before
   public void before() {

      instance = new MajRndMain(
            "/applicationContext-sae-rnd-executable-test.xml");

      majRndService = RndServiceProvider.getInstanceMajRndService();
   }

   @After
   public void after() {

      EasyMock.reset(majRndService);

   }

   @Test
   public void traitementMasseMain_success() {

      String[] args = new String[] {
            "src/test/resources/config_sae.properties", "context_log" };

      instance.execute(args);
   }

   @Test
   public void majRndMain_failure_empty_configSAE() {

      String[] args = new String[0];

      try {

         instance.execute(args);

         Assert
               .fail("le test doit échouer car le fichier de configuration du SAE n'est pas renseigné");

      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.",
                     e.getMessage());
      }
      
      args = new String[1];
      args[0] = "";

      try {

         instance.execute(args);

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
