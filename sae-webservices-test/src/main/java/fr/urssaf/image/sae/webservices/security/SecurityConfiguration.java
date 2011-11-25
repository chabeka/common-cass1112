package fr.urssaf.image.sae.webservices.security;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;

/**
 * Classe de configuration des services sécurisés
 * 
 * 
 */
public final class SecurityConfiguration {

   private SecurityConfiguration() {

   }

   // private SaeServiceStub service;

   private static final String SECURITY_PATH = "src/main/resources/META-INF";

   /**
    * / méthode à appeller dans le before des tests
    * 
    * @return instance de {@link SaeServiceStub}
    */
   public static SaeServiceStub createSaeServiceStub() {

      Configuration config;
      try {
         config = new PropertiesConfiguration("sae-webservices-test.properties");
      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }

      try {
         ConfigurationContext ctx = ConfigurationContextFactory
               .createConfigurationContextFromFileSystem(SECURITY_PATH,
                     SECURITY_PATH + "/axis2.xml");
         return new SaeServiceStub(ctx, config.getString("urlServiceWeb"));
      } catch (AxisFault e) {
         throw new IllegalStateException(e);
      }

   }

}
