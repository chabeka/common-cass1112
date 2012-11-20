package fr.urssaf.image.sae.webservices.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigurationUtils {

   public static String litUrlServiceWebDuFichierProperties() {

      Configuration config;
      try {
         config = new PropertiesConfiguration("sae-webservices-test.properties");
      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }
      return config.getString("urlServiceWeb");

   }
   
}
