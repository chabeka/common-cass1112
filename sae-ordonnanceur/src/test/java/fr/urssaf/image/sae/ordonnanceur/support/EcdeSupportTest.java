package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml"})
@SuppressWarnings("PMD.MethodNamingConventions")
public class EcdeSupportTest {

   @Autowired
   private EcdeSupport ecdeSupport;

   /**
    * Les tests s'appuie sur la configuration dans le fichier
    * applicationContext-sae-ordonnanceur-service-test.xml
    */
   @Test
   public void isLocal() {

      // ecde.cer69.recouv est configuré à true
      String urlEcde_cer69 = "ecde://ecde.cer69.recouv/sommaire.xml";

      Assert.assertTrue(urlEcde_cer69 + "' est local", ecdeSupport.isLocal(URI
            .create(urlEcde_cer69)));

      // ecde.cer44.recouv est configuré à false
      String urlEcde_cer44 = "ecde://ecde.cer44.recouv/sommaire.xml";

      Assert.assertFalse(urlEcde_cer44 + "' n'est pas local", ecdeSupport
            .isLocal(URI.create(urlEcde_cer44)));

      // ecde.cer34.recouv est configuré à true
      String urlEcde_cer34 = "ecde://ecde.cer34.recouv/sommaire.xml";

      Assert.assertTrue(urlEcde_cer34 + "' est local", ecdeSupport.isLocal(URI
            .create(urlEcde_cer34)));

      // ecde.cer75.recouv n'est pas configuré
      String urlEcde_cer75 = "ecde://ecde.cer75.recouv/sommaire.xml";

      Assert.assertFalse(urlEcde_cer75 + "' n'est pas local", ecdeSupport
            .isLocal(URI.create(urlEcde_cer75)));

   }

}
