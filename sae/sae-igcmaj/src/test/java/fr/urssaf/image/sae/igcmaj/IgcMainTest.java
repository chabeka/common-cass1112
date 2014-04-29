package fr.urssaf.image.sae.igcmaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igcmaj.exception.IgcMainException;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-igcmaj-test.xml")
public class IgcMainTest {

   private IgcMain instance;

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private final static File DIRECTORY;

   private final static File CRL;

   @Autowired
   private RegTechniqueService regTechnique;

   // private static final String IGC_CONFIG = "src/test/resources/igcConfig/";

   static {

      DIRECTORY = new File(SystemUtils.getJavaIoTmpDir().getAbsolutePath(),
            "igcmaj");

      CRL = new File(DIRECTORY, "CRL");

   }

   @BeforeClass
   public static void beforeClass() throws IOException {

      FileUtils.forceMkdir(DIRECTORY);
      FileUtils.cleanDirectory(DIRECTORY);

      FileUtils.forceMkdir(CRL);

   }

   @AfterClass
   public static void afterClass() throws IOException {

      FileUtils.deleteDirectory(DIRECTORY);

   }

   @Before
   public void before() {

      instance = new IgcMain("/applicationContext-sae-igcmaj-test.xml");

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
         newXML.addProperty("IgcConfig.id", "PKI_VAL_AED");
         newXML.addProperty("IgcConfig.certifACRacine", acRacines);
         newXML.addProperty("IgcConfig.repertoireCRL", crls);
         newXML.addProperty("IgcConfig.issuers.issuer", "CN=IGC/A");
         newXML.addProperty("IgcConfig.activerTelechargementCRL", true);

         for (String url : urls) {

            newXML.addProperty("IgcConfig.URLTelechargementCRL.url", url);
         }

         newXML.save(DIRECTORY + "/" + newConfig);

         return DIRECTORY + "/" + newConfig;

      } catch (ConfigurationException e) {

         throw new IllegalStateException(e);
      }
   }

   @Test
   public void igcMain_success() {

      String pathConfigFile = loadConfig(
            "igcConfig_success_temp.xml",
            getAbsolute("src/test/resources/certificats/ACRacine/pseudo_IGCA.crt"),
            CRL.getAbsolutePath(), "http://cer69idxpkival1.cer69.recouv/*.crl");

      String[] args = new String[] {
            "src/test/resources/sae-config.properties", pathConfigFile };

      instance.execute(args);

      Collection<File> files = FileUtils.listFiles(CRL, null, true);

      Assert.assertTrue("erreur sur le nombre d'urls à télécharger", files
            .size() > 3);

      List<String> crlUtiles = new ArrayList<String>();
      crlUtiles.add("Pseudo_Appli.crl");
      crlUtiles.add("Pseudo_ACOSS.crl");
      crlUtiles.add("Pseudo_IGC_A.crl");

      boolean trouve = false;
      for (String crl : crlUtiles) {
         trouve = false;
         for (File file : files) {
            if (file.getName().equals(crl)) {
               trouve = true;
            }
         }
         Assert.assertTrue("Le fichier " + crl + " doit être présent", trouve);
      }

   }

   @Test
   public void igcMain_failure_pathConfig_required() {

      assert_failure_saePathConfig_required(new String[] { null });
      assert_failure_saePathConfig_required(new String[] { "" });
      assert_failure_saePathConfig_required(new String[] { " " });
      assert_failure_saePathConfig_required(null);
      assert_failure_igcPathConfig_required(new String[] { "test", null });
      assert_failure_igcPathConfig_required(new String[] { "test", "" });
      assert_failure_igcPathConfig_required(new String[] { "test", " " });
   }

   private static void assert_failure_saePathConfig_required(String[] args) {

      try {
         IgcMain.main(args);

      } catch (IllegalArgumentException e) {

         assertEquals("message de l'exception incorrect",
               IgcMain.CONFIG_EMPTY, e.getMessage());
      }
   }

   private static void assert_failure_igcPathConfig_required(String[] args) {

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
            getAbsolute("/notExist/certificats/ACRacine"), CRL
                  .getAbsolutePath(),
            "http://cer69idxpkival1.cer69.recouv/*.crl");

      try {
         String[] args = new String[] {
               "src/test/resources/sae-config.properties", pathConfigFile };

         instance.execute(args);
         
         fail(FAIL_MESSAGE);
      } catch (IgcMainException e) {

         assertEquals("exception non attendue", IgcConfigException.class, e
               .getCause().getClass());

      }
   }

   @Test
   public void igcMain_failure_igcDownloadException() {

      String pathConfigFile = loadConfig(
            "igcConfig_success_temp.xml",
            getAbsolute("src/test/resources/certificats/ACRacine/pseudo_IGCA.crt"),
            CRL.getAbsolutePath(),
            "http://download.oracle.com/javase/6/docs/api/");

      String[] args = new String[] {
            "src/test/resources/sae-config.properties", pathConfigFile };

      instance.execute(args);

      // Vérification présence de la trace
      Date dateFin = new Date();
      Date dateDebut = DateUtils.addDays(dateFin, -1);
      List<TraceRegTechniqueIndex> listeTrace = regTechnique.lecture(dateDebut,
            dateFin, 1, false);
      for (TraceRegTechniqueIndex traceRegTechniqueIndex : listeTrace) {
         TraceRegTechnique trace = regTechnique.lecture(traceRegTechniqueIndex
               .getIdentifiant());
         Assert.assertEquals("Code évenement incorrect", "IGC_LOAD_CRLS|KO",
               trace.getCodeEvt());

         Assert.assertEquals("Contexte incorrect", "telechargerCRLs", trace
               .getContexte());

         Assert.assertEquals("saeServeurHostname incorrect", HostnameUtil
               .getHostname(), trace.getInfos().get("saeServeurHostname"));
         Assert.assertEquals("saeServeurHostname incorrect",
               "http://download.oracle.com/javase/6/docs/api/", trace
                     .getInfos().get("fichier"));
         Assert.assertEquals("saeServeurHostname incorrect", "PKI_VAL_AED",
               trace.getInfos().get("pki"));
      }

   }
}
