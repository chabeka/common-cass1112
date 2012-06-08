package fr.urssaf.image.sae.anais.framework.util;

//CHECKSTYLE:OFF

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import fr.urssaf.image.sae.anais.framework.config.SaeAnaisConfig;

public final class InitFactory {

   private InitFactory() {

   }

   public static SaeAnaisConfig initConfigRechercheDocumentaire() {

      SaeAnaisConfig anaisConfig = new SaeAnaisConfig();

      try {
         AbstractConfiguration.setDefaultListDelimiter("|".charAt(0));
         Configuration config = new PropertiesConfiguration(
               "sae-anais-framework-rg-test.properties");

         anaisConfig.setCodeapp(config.getString("anais.code-application"));
         anaisConfig.setPasswd(config
               .getString("anais.compte-applicatif-password"));
         anaisConfig.setCodeenv("PROD");
         anaisConfig.setAppdn(config.getString("anais.cn"));
         anaisConfig.setPort(config.getInt("anais.port"));

         anaisConfig.setHostname(config.getStringArray("anais.host")[0]);
         anaisConfig.setTimeout(config.getString("anais.timeout"));
         anaisConfig.setUsetls(config.getBoolean("anais.tls"));

         anaisConfig.setComptePortail(config.getString("anais.comptePortail"));
         anaisConfig.setDroitsDirect(config
               .getBoolean("anais.activerDroitsDirects"));

      } catch (ConfigurationException configException) {
         throw new IllegalStateException(configException);
      }

      return anaisConfig;

   }
}

// CHECKSTYLE:ON
