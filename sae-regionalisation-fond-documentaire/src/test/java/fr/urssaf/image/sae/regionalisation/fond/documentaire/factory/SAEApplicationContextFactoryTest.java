package fr.urssaf.image.sae.regionalisation.fond.documentaire.factory;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEApplicationContextFactoryTest {

   @Test
   public void load_success() {

      String contextConfig = "/applicationContext-sae-regionalisation-fond-documentaire.xml";

      String dfceConfig = "src/test/resources/cassandra/cassandra-connection.properties";

      ApplicationContext context = SAEApplicationContextFactory.load(
            contextConfig, dfceConfig);

      Assert.assertNotNull("Un context spring doit être créé", context);

   }
}
