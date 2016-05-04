package fr.urssaf.image.sae.igc.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.StreamException;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.util.ConfigurationUtils;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-igc-test.xml")
public class IgcConfigServiceImplTest {
   
   @Autowired
   private IgcConfigServiceImpl service;

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private final static String DIRECTORY;

   static {
      DIRECTORY = FilenameUtils.concat(SystemUtils.getJavaIoTmpDir()
            .getAbsolutePath(), "certificats/PKI");
   }

   @BeforeClass
   public static void beforeClass() throws IOException {

      File directory = new File(DIRECTORY);
      FileUtils.forceMkdir(directory);
      FileUtils.cleanDirectory(directory);

   }

   @AfterClass
   public static void afterClass() throws IOException {

      File directory = new File(DIRECTORY);

      FileUtils.deleteDirectory(directory);

   }

   private static String loadConfig(String newConfig, String acRacines,
         String crls, String... urls) {

      XMLConfiguration newXML = new XMLConfiguration();

      try {
         newXML.load(ConfigurationUtils.getIgcConfig("igcConfig_modele.xml"));

         newXML.addProperty("IgcConfig.id", "PKI_TEST");
         newXML.addProperty("IgcConfig.certifACRacine", acRacines);
         newXML.addProperty("IgcConfig.repertoireCRL", crls);

         for (String url : urls) {

            newXML.addProperty("IgcConfig.URLTelechargementCRL.url", url);
         }

         newXML.addProperty("IgcConfig.issuers.issuer", "CN=IGC/A");

         newXML.save(DIRECTORY + "/" + newConfig);

         return DIRECTORY + "/" + newConfig;

      } catch (ConfigurationException e) {

         throw new IllegalStateException(e);
      }
   }

   @Test
   public void loadConfig_success() throws IgcConfigException,
         MalformedURLException {

      String pathConfigFile = loadConfig(
            "igcConfig_success_temp.xml",
            ConfigurationUtils
                  .getAbsolute("src/test/resources/certificats/PKI_TEST/ACRacine/pseudo_IGCA.crt"),
            ConfigurationUtils
                  .getAbsolute("src/test/resources/certificats/PKI_TEST/CRL/"),
            "http://cer69idxpkival1.cer69.recouv/*.crl");

      IgcConfigs configs = service.loadConfig(pathConfigFile);

      assertEquals(
            "erreur sur le repertoire acRacine",
            ConfigurationUtils
                  .getAbsolute("src/test/resources/certificats/PKI_TEST/ACRacine/pseudo_IGCA.crt"),
            ConfigurationUtils.getAbsolute(configs.getIgcConfigs().get(0)
                  .getAcRacine()));
      assertEquals("erreur sur le repertoire des crls", ConfigurationUtils
            .getAbsolute("src/test/resources/certificats/PKI_TEST/CRL/"),
            ConfigurationUtils.getAbsolute(configs.getIgcConfigs().get(0)
                  .getCrlsRep()));

      URL url = new URL("http://cer69idxpkival1.cer69.recouv/*.crl");

      List<URL> urls = new ArrayList<URL>();
      urls.add(url);

      String expected = StringUtils.join(urls, ",");
      String actual = StringUtils.join(configs.getIgcConfigs().get(0)
            .getUrlList().getUrls(), ",");

      assertEquals("erreur sur les urls de téléchargement", expected, actual);
   }

   @Test
   public void loadConfig_failure_urls_crl_badformat() {

      String pathConfigFile = loadConfig(
            "igcConfig_failure_urls_crl_badformat.xml", ConfigurationUtils
                  .getAbsolute("src/test/resources/certificats/ACRacine"),
            ConfigurationUtils
                  .getAbsolute("src/test/resources/certificats/CRL/"),
            "bmauvaise url");

      try {
         service.loadConfig(pathConfigFile);
         fail(FAIL_MESSAGE);
      } catch (IgcConfigException e) {

         assertEquals("erreur sur la cause de l'exception",
               ConversionException.class, e.getCause().getClass());
         assertEquals("erreur sur le message de l'exception",
               IgcConfigException.MESSAGE, e.getMessage());

         String url = "mauvaise url";
         assertTrue("erreur sur:" + url, ((ConversionException) e.getCause())
               .getMessage().contains(url));
      }

   }

   @Test
   public void loadConfig_failure_xml() {

      String pathConfigFile = ConfigurationUtils
            .getIgcConfig("igcConfig_failure.properties");

      try {
         service.loadConfig(pathConfigFile);
         fail(FAIL_MESSAGE);
      } catch (IgcConfigException e) {

         assertEquals("erreur la cause de l'exception", StreamException.class,
               e.getCause().getClass());
      }

   }

}
