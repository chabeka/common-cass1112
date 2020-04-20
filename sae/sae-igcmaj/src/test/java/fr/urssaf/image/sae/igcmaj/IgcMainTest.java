package fr.urssaf.image.sae.igcmaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igcmaj.exception.IgcMainException;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;
import junit.framework.Assert;

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

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

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

    try {
      FileUtils.deleteDirectory(DIRECTORY);
    }
    catch (final Throwable t) {
      FileUtils.forceDeleteOnExit(DIRECTORY);
    }

  }

  @Before
  public void before() {
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
    instance = new IgcMain("/applicationContext-sae-igcmaj-test.xml");

  }

  private static String getAbsolute(final String path) {

    return new File(path).getAbsolutePath();
  }

  private static String loadConfig(final String newConfig, final String acRacines,
                                   final String crls, final String... urls) {

    final XMLConfiguration newXML = new XMLConfiguration();

    try {
      newXML
      .load(getAbsolute("src/test/resources/igcConfig/igcConfig_modele.xml"));
      newXML.addProperty("IgcConfig.id", "PKI_VAL_AED");
      newXML.addProperty("IgcConfig.certifACRacine", acRacines);
      newXML.addProperty("IgcConfig.repertoireCRL", crls);
      newXML.addProperty("IgcConfig.issuers.issuer", "CN=IGC/A");
      newXML.addProperty("IgcConfig.activerTelechargementCRL", true);

      for (final String url : urls) {

        newXML.addProperty("IgcConfig.URLTelechargementCRL.url", url);
      }

      newXML.save(DIRECTORY + "/" + newConfig);

      return DIRECTORY + "/" + newConfig;

    } catch (final ConfigurationException e) {

      throw new IllegalStateException(e);
    }
  }

  @Test
  public void igcMain_success() {

    final String pathConfigFile = loadConfig(
                                             "igcConfig_success_temp.xml",
                                             getAbsolute("src/test/resources/certificats/ACRacine/pseudo_IGCA.crt"),
                                             CRL.getAbsolutePath(), "http://cer69idxpkival1.cer69.recouv/*.crl");

    final String[] args = new String[] {
                                        "src/test/resources/sae-config.properties", pathConfigFile };

    instance.execute(args);

    final Collection<File> files = FileUtils.listFiles(CRL, null, true);

    Assert.assertTrue("erreur sur le nombre d'urls à télécharger", files
                      .size() > 3);

    final List<String> crlUtiles = new ArrayList<>();
    crlUtiles.add("Pseudo_Appli.crl");
    crlUtiles.add("Pseudo_ACOSS.crl");
    crlUtiles.add("Pseudo_IGC_A.crl");

    boolean trouve = false;
    for (final String crl : crlUtiles) {
      trouve = false;
      for (final File file : files) {
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

  private static void assert_failure_saePathConfig_required(final String[] args) {

    try {
      IgcMain.main(args);

    } catch (final IllegalArgumentException e) {

      assertEquals("message de l'exception incorrect",
                   IgcMain.CONFIG_EMPTY, e.getMessage());
    }
  }

  private static void assert_failure_igcPathConfig_required(final String[] args) {

    try {
      IgcMain.main(args);

    } catch (final IllegalArgumentException e) {

      assertEquals("message de l'exception incorrect",
                   IgcMain.IGC_CONFIG_EMPTY, e.getMessage());
    }
  }

  @Test
  public void igcMain_failure_igcConfigException() {

    final String pathConfigFile = loadConfig("igcConfig_success_temp.xml",
                                             getAbsolute("/notExist/certificats/ACRacine"), CRL
                                             .getAbsolutePath(),
        "http://cer69idxpkival1.cer69.recouv/*.crl");

    try {
      final String[] args = new String[] {
                                          "src/test/resources/sae-config.properties", pathConfigFile };

      instance.execute(args);

      fail(FAIL_MESSAGE);
    } catch (final IgcMainException e) {

      assertEquals("exception non attendue", IgcConfigException.class, e
                   .getCause().getClass());

    }
  }

  @Test
  public void igcMain_failure_igcDownloadException() {

    final String pathConfigFile = loadConfig(
                                             "igcConfig_success_temp.xml",
                                             getAbsolute("src/test/resources/certificats/ACRacine/pseudo_IGCA.crt"),
                                             CRL.getAbsolutePath(),
        "http://download.oracle.com/javase/6/docs/api/");

    final String[] args = new String[] {
                                        "src/test/resources/sae-config.properties", pathConfigFile };

    instance.execute(args);

    // Vérification présence de la trace
    final Date dateFin = new Date();
    final Date dateDebut = DateUtils.addDays(dateFin, -1);
    final List<TraceRegTechniqueIndex> listeTrace = regTechnique.lecture(dateDebut,
                                                                         dateFin, 1, false);
    for (final TraceRegTechniqueIndex traceRegTechniqueIndex : listeTrace) {
      final TraceRegTechnique trace = regTechnique.lecture(traceRegTechniqueIndex
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
