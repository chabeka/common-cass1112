package fr.urssaf.image.sae.commons.context;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class ContextFactoryTest {

   @Test
   public void testContext() {

      String contextConfig = "/applicationContext-sae-commons-context-test.xml";
      String saeConfig = "src/test/resources/config/config_sae.properties";
      ApplicationContext context = ContextFactory.createSAEApplicationContext(
            contextConfig, saeConfig);

      Assert.assertNotNull("Un context spring doit être créé", context);

      String value = (String) context.getBean("testValue");

      Assert.assertEquals("la valeur du chemin doit etre correct",
            "src/test/resources/config/cassandra-local.properties", value);
   }

}
