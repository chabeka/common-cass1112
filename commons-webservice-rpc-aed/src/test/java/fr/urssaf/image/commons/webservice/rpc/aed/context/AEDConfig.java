package fr.urssaf.image.commons.webservice.rpc.aed.context;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Classe de récupération des paramètres situés dans
 * src/test/resources/aed.properties
 * 
 */
public final class AEDConfig {

   private static final Logger LOG = Logger.getLogger(AEDConfig.class);

   private AEDConfig() {

   }

   public static final String P12;

   public static final String PASSWORD;

   public static final String PACKAGE;

   public static final String WSDL;

   static {
      try {
         Configuration config = new PropertiesConfiguration(
               "src/test/resources/aed.properties");
         P12 = config.getString("aed.p12");
         LOG.debug("P12:" + P12);
         PASSWORD = config.getString("aed.password");
         LOG.debug("PASSWORD:" + PASSWORD);
         WSDL = config.getString("aed.wsdl");
         LOG.debug("WSDL:" + WSDL);
         PACKAGE = config.getString("aed.package");
         LOG.debug("PACKAGE:" + PACKAGE);
      } catch (ConfigurationException e) {
         LOG.error(e.getMessage(), e);
         throw new IllegalArgumentException(e);
      }
   }

}
