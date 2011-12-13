package fr.urssaf.image.sae.igcmaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igcmaj.exception.IgcMainException;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcMainTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private final static String DIRECTORY;

   private final static String CRL;

   // private static final String IGC_CONFIG = "src/test/resources/igcConfig/";

   static {
      DIRECTORY = FilenameUtils.concat(SystemUtils.getJavaIoTmpDir()
            .getAbsolutePath(), "igcmaj");

      CRL = DIRECTORY + "/CRL/";
   }

   @BeforeClass
   public static void beforeClass() throws IOException {

      File directory = new File(DIRECTORY);
      FileUtils.forceMkdir(directory);
      FileUtils.cleanDirectory(directory);

      File crl = new File(CRL);
      FileUtils.forceMkdir(crl);

   }

   private static String getAbsolute(String path) {

      return new File(path).getAbsolutePath();
   }

   private static String loadConfig(String newConfig, String acRacines,
         String crls, String... urls) {

      XMLConfiguration newXML = new XMLConfiguration();

      try {
         newXML
               .load(getAbsolute("src/test/resources/igcConfig/igcConfig_modele.xml"));

         newXML.addProperty("repertoireACRacines", acRacines);
         newXML.addProperty("repertoireCRL", crls);

         for (String url : urls) {

            newXML.addProperty("URLTelechargementCRL.url", url);
         }

         newXML.save(DIRECTORY + "/" + newConfig);

         return DIRECTORY + "/" + newConfig;

      } catch (ConfigurationException e) {

         throw new IllegalStateException(e);
      }
   }

   @Test
   public void igcMain_success() {

      String pathConfigFile = loadConfig("igcConfig_success_temp.xml",
            getAbsolute("src/test/resources/certificats/ACRacine"), CRL,
            "http://cer69idxpkival1.cer69.recouv/*.crl");

      IgcMain.main(new String[] { pathConfigFile });

      Collection<File> files = FileUtils.listFiles(new File(CRL), null, true);

      assertEquals("le nombre de téléchargements est incorrect", 15, files
            .size());

   }

   @Test
   public void igcMain_failure_pathConfig_required() {

      assert_failure_pathConfig_required(new String[] { null });
      assert_failure_pathConfig_required(new String[] { "" });
      assert_failure_pathConfig_required(new String[] { " " });
      assert_failure_pathConfig_required(null);
   }

   private static void assert_failure_pathConfig_required(String[] args) {

      try {
         IgcMain.main(args);

      } catch (IllegalArgumentException e) {

         assertEquals("message de l'exception incorrect",
               IgcMain.IGC_CONFIG_EMPTY, e.getMessage());
      }
   }

   @Test
   public void igcMain_failure_igcConfigException() {

      String pathConfigFile = loadConfig("igcConfig_success_temp.xml",
            getAbsolute("/notExist/certificats/ACRacine"), CRL,
            "http://cer69idxpkival1.cer69.recouv/*.crl");

      try {
         IgcMain.main(new String[] { pathConfigFile });
         fail(FAIL_MESSAGE);
      } catch (IgcMainException e) {

         assertEquals("exception non attendue", IgcConfigException.class, e
               .getCause().getClass());

      }
   }

   @Test
   public void igcMain_failure_igcDownloadException() {

      String pathConfigFile = loadConfig("igcConfig_success_temp.xml",
            getAbsolute("src/test/resources/certificats/ACRacine"), CRL,
            "http://download.oracle.com/javase/6/docs/api/");

      try {
         IgcMain.main(new String[] { pathConfigFile });
         fail(FAIL_MESSAGE);
      } catch (IgcMainException e) {

         assertEquals("exception non attendue", IgcDownloadException.class, e
               .getCause().getClass());

      }
   }
}
