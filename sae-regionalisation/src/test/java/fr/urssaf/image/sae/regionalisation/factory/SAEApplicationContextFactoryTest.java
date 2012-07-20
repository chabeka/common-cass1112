package fr.urssaf.image.sae.regionalisation.factory;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEApplicationContextFactoryTest {

   @Test
   public void load_success() {

      String contextConfig = "/applicationContext-sae-regionalisation.xml";
      
      String dfceConfig = "src/test/resources/dfce/test-dfce.properties";
      String postgresqlConfig = "src/test/resources/database/test-postgresql.properties";
      ApplicationContext context = SAEApplicationContextFactory.load(
            contextConfig, dfceConfig, postgresqlConfig);

      Assert.assertNotNull("Un context spring doit être créé", context);
   }
}
