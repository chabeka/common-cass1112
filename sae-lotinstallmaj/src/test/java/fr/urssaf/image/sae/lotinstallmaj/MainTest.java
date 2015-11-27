package fr.urssaf.image.sae.lotinstallmaj;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Tests unitaires de la classe {@link Main}
 */
public class MainTest {

   @Test
   public void startContextSpring_success() {

      String saeConfig = "src/test/resources/config/test-sae-config.properties";
      ApplicationContext context = Main.startContextSpring(saeConfig);

      Assert.assertNotNull("Un contexte Spring doit être créé", context);

   }

}
