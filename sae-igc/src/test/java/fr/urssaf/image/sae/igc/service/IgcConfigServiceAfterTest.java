package fr.urssaf.image.sae.igc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.util.ConfigurationUtils;
import fr.urssaf.image.sae.igc.util.TextUtils;

/**
 * Test sur l'instance de {@link IgcConfig} renvoyé par {@link IgcConfigService}
 * 
 * 
 */
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcConfigServiceAfterTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private IgcConfigService service;

   private XMLConfiguration config;

   @Before
   public void before() throws MalformedURLException {

      service = new IgcConfigService() {

         @Override
         public IgcConfigs loadConfig(String pathConfigFile)
               throws IgcConfigException {

            final XStream xstream = new XStream();
            xstream.processAnnotations(IgcConfigs.class);
            FileInputStream stream = null;
            IgcConfigs configs;

            try {
               stream = new FileInputStream(pathConfigFile);
               configs = IgcConfigs.class.cast(xstream.fromXML(stream));
            } catch (FileNotFoundException e) {
               throw new IgcConfigException(e);
            }

            for (IgcConfig igcConfig : configs.getIgcConfigs()) {
               // transformation des chemin en chemin absolu
               igcConfig.setAcRacine(ConfigurationUtils.getAbsolute(igcConfig
                     .getAcRacine()));
               igcConfig.setCrlsRep(ConfigurationUtils.getAbsolute(igcConfig
                     .getCrlsRep()));
            }

            return configs;
         }

         @Override
         public IgcConfigs loadConfig(Resource configFile)
               throws IgcConfigException {
            return null;
         }
      };

   }

   @Test
   public void loadConfig_success() throws IgcConfigException {

      String path = "igcConfig_success.xml";

      this.config = ConfigurationUtils.createConfig(path);

      assertNotNull("les arguments doivent être valide", service
            .loadConfig(config.getFileName()));

   }

   private static void assertLoadConfig_failure(IgcConfigService service,
         XMLConfiguration config, String cause, String... args) {

      try {
         service.loadConfig(config.getFileName());
         fail(FAIL_MESSAGE);
      } catch (IgcConfigException e) {

         assertEquals("erreur la cause de l'exception", TextUtils.getMessage(
               cause, args), e.getMessage());

      }

   }

   @Test
   public void loadConfig_failure_ACracines_notexist() {

      String path = "igcConfig_failure_ac_racines_notexist.xml";

      this.config = ConfigurationUtils.createConfig(path);

      assertLoadConfig_failure(
            service,
            config,
            IgcConfigService.AC_RACINES_NOTEXIST,
            ConfigurationUtils
                  .getAbsolute("notExist/certificats/ACRacine/pseudo_IGCA.crt"),
            ConfigurationUtils.getIgcConfig(path));

   }

   @Test
   public void loadConfig_failure_ACracines_required() {

      String path = "igcConfig_failure_ac_racines_required.xml";

      this.config = ConfigurationUtils.createConfig(path);

      assertLoadConfig_failure(service, config,
            IgcConfigService.AC_RACINES_REQUIRED, ConfigurationUtils
                  .getIgcConfig(path));

   }

   @Test
   public void loadConfig_failure_crls_notexist() {

      String path = "igcConfig_failure_crls_notexist.xml";

      this.config = ConfigurationUtils.createConfig(path);

      assertLoadConfig_failure(service, config, IgcConfigService.CRLS_NOTEXIST,
            ConfigurationUtils.getAbsolute("notExist/certificats/CRL/"),
            ConfigurationUtils.getIgcConfig(path));

   }

   @Test
   public void loadConfig_failure_crls_required() {

      String path = "igcConfig_failure_crls_required.xml";

      this.config = ConfigurationUtils.createConfig(path);

      assertLoadConfig_failure(service, config, IgcConfigService.CRLS_REQUIRED,
            ConfigurationUtils.getIgcConfig(path));

   }

   @Test
   public void loadConfig_failure_urls_crl_required() {

      String path = "igcConfig_failure_urls_crl_required.xml";

      this.config = ConfigurationUtils.createConfig(path);

      assertLoadConfig_failure(service, config,
            IgcConfigService.URLS_CRL_REQUIRED, ConfigurationUtils
                  .getIgcConfig(path));

   }

}
