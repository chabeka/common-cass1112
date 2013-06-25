package fr.urssaf.image.sae.webservices;

import java.net.URISyntaxException;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Test;
import org.opensaml.util.resource.ResourceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Tests unitaires liés au chargement du contexte Spring
 */
public class ApplicationContextTest {

   /**
    * Vérifie que le contexte Spring peut s'instancier correctement
    * 
    * @throws NamingException
    * @throws IllegalStateException
    */
   @Test
   public void creationContexteTest() throws IllegalStateException,
         NamingException, ResourceException, URISyntaxException {

      // Simulation JNDI
      simuleJndi();

      // Création du contexte Spring en chargeant le fichier principal de la
      // partie main
      String contextConfig = "/applicationContext.xml";
      ApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig });

      // Contrôle
      Assert.assertNotNull("Un contexte Spring doit être créé", context);

   }

   private void simuleJndi() throws IllegalStateException, NamingException {

      String config = "src/test/resources/config/test-sae-config.properties";

      SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
      builder.bind("java:comp/env/SAE_Fichier_Configuration", config);
      builder.activate();

   }

}
