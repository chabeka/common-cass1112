package fr.urssaf.image.sae.ordonnanceur.factory;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Tests unitaires de la classe {@link OrdonnanceurContexteFactory}
 */
public class OrdonnanceurContexteFactoryTest {

   @Test
   public void creerContext_success() {

      String contextConfig = "/applicationContext-sae-ordonnanceur.xml";
      String saeConfig = "src/test/resources/config/sae-config-test.properties";
      ApplicationContext context = OrdonnanceurContexteFactory.creerContext(
            contextConfig, saeConfig);

      Assert.assertNotNull("Un context spring doit être créé", context);

   }

}
