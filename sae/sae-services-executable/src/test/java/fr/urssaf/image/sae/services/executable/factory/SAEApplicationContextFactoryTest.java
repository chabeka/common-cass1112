package fr.urssaf.image.sae.services.executable.factory;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEApplicationContextFactoryTest {

   @Test
   public void createSAEApplicationContext_success() {

      String contextConfig = "/applicationContext-sae-services-executable.xml";
      String saeConfig = "src/test/resources/config_sae.properties";
      ApplicationContext context = SAEApplicationContextFactory
            .createSAEApplicationContext(contextConfig, saeConfig);

      Assert.assertNotNull("Un context spring doit être créé", context);
   }
}
