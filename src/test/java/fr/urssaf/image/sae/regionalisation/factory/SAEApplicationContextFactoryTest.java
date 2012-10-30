package fr.urssaf.image.sae.regionalisation.factory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEApplicationContextFactoryTest {

   @Test
   public void load_success() {

      String contextConfig = "/applicationContext-sae-regionalisation.xml";

      String dfceConfig = "src/test/resources/dfce/test-dfce.properties";
      File dirParent = new File(FileUtils.getTempDirectory(), "suivi");
      dirParent.mkdir();

      ApplicationContext context = SAEApplicationContextFactory.load(
            contextConfig, dfceConfig, dirParent.getAbsolutePath());

      Assert.assertNotNull("Un context spring doit être créé", context);

      FileUtils.deleteQuietly(dirParent);
   }
}
