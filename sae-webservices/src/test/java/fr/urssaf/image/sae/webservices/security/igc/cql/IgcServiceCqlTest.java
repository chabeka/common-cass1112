package fr.urssaf.image.sae.webservices.security.igc.cql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.modele.IssuerList;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;
import fr.urssaf.image.sae.trace.utils.TraceDestinataireCqlUtils;
import fr.urssaf.image.sae.webservices.component.IgcConfigUtils;
import fr.urssaf.image.sae.webservices.security.igc.IgcService;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;
import fr.urssaf.image.sae.webservices.support.TracesWsSupport;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-traces-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcServiceCqlTest {

  private static final String FAIL_MSG = "le test doit échouer";

  private static final URL CERTIFICAT;

  private static final Logger LOG = LoggerFactory
      .getLogger(IgcServiceCqlTest.class);

  private static final File TMP_DIR;

  private static final File CRL;

  private static final File AC_RACINE;

  private IgcConfigs igcConfigs;

  @Autowired
  private TracesWsSupport tracesWsSupport;

  @Autowired
  private RegTechniqueService regTechnique;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  @Autowired
  TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  static {

    TMP_DIR = IgcConfigUtils
        .createTempRepertory("sae_webservices_igcservice");

    CRL = new File(TMP_DIR, "CRL");
    AC_RACINE = new File(TMP_DIR, "ACRacine");

    IgcConfigUtils.createRepertory(CRL);
    IgcConfigUtils.createRepertory(AC_RACINE);

    CERTIFICAT = IgcConfigUtils
        .createURL("http://cer69idxpkival1.cer69.recouv/pseudo_appli.crt");

  }

  @AfterClass
  public static void afterClass() throws IOException {

    FileUtils.deleteDirectory(TMP_DIR);

  }

  @Before
  public void before() {
    modeApiCqlSupport.initTables(MODE_API.DATASTAX);
    createAllTraceDestinataire();
    igcConfigs = new IgcConfigs();

    final IgcConfig igcConfig = new IgcConfig();

    igcConfig.setPkiIdent("PKI_VAL_AED");
    igcConfig.setAcRacine(AC_RACINE.getAbsolutePath() + File.separator
                          + CERTIFICAT.getFile());
    igcConfig.setCrlsRep(CRL.getAbsolutePath());

    final IssuerList issuerList = new IssuerList();
    issuerList
    .setIssuers(Arrays.asList(new String[] { "CN=AC Application" }));
    igcConfig.setIssuerList(issuerList);

    igcConfigs.setIgcConfigs(Arrays.asList(new IgcConfig[] { igcConfig }));

  }

  private IgcService createIgcService() {

    final IgcService igcService = new IgcService(igcConfigs, tracesWsSupport, false);
    igcService.chargementCertificatsACRacine();

    return igcService;
  }

  @After
  public void after() {

    IgcConfigUtils.cleanDirectory(AC_RACINE);
    IgcConfigUtils.cleanDirectory(CRL);
  }

  @Test
  public void IgcService_success() throws IgcDownloadException {

    // téléchargement d'une AC racine
    IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE, CERTIFICAT
                                                 .getFile()));

    assertNotNull("exception dans le constructeur", createIgcService());

  }

  @Test
  public void IgcService_failure_certificateException()
      throws IgcDownloadException {

    final URL pem = IgcConfigUtils
        .createURL("http://cer69idxpkival1.cer69.recouv/Pseudo_ACOSS.pem");

    IgcConfigUtils.download(pem, new File(AC_RACINE, "pseudo_appli.crt"));

    try {
      createIgcService();
      fail(FAIL_MSG);
    } catch (final IllegalArgumentException e) {
      LOG.debug(e.getCause().getMessage());
      assertTrue(
                 "Exception non attendue de type " + e.getCause().getClass(),
                 CertificateException.class.isAssignableFrom(e.getCause()
                                                             .getClass()));
    }
  }

  @Test
  public void IgcService_failure_ac_racine_empty() throws IOException,
  IgcDownloadException {

    try {
      createIgcService();
      fail(FAIL_MSG);
    } catch (final IllegalArgumentException e) {

      LOG.debug(e.getMessage());
      assertTrue(
                 "Message d'exception non attendue de type " + e.getMessage(),
                 e
                 .getMessage()
                 .startsWith(
                     "Aucun certificat d'AC racine de confiance trouvé pour le fichier"));
    }
  }

  @Test
  public void getInstanceCertifsAndCrl_success()
      throws LoadCertifsAndCrlException, IgcDownloadException {

    // téléchargement d'une AC racine
    IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE, CERTIFICAT
                                                 .getFile()));

    // téléchargement d'une CRL
    final URL crl = IgcConfigUtils
        .createURL("http://cer69idxpkival1.cer69.recouv/Pseudo_ACOSS.crl");
    IgcConfigUtils.download(crl, new File(CRL, crl.getFile()));

    final IgcService igcService = createIgcService();
    assertNotNull("une instance de CertifsAndCrl est attendue", igcService
                  .getInstanceCertifsAndCrl());
  }

  @Test
  public void getInstanceCertifsAndCrl_failure() throws IgcDownloadException {

    // téléchargement d'une AC racine
    IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE, CERTIFICAT
                                                 .getFile()));

    // Téléchargement d'un certificat X509 dans le répertoire des CRL,
    // pour ensuite faire planter le chargement des CRL
    final URL crl = CERTIFICAT;
    final File destinationCrt = new File(CRL, crl.getFile());
    IgcConfigUtils.download(crl, destinationCrt);

    // Renommage du fichier .crt en fichier .crl pour faire planter
    // le chargement des CRL
    final File destinationCrl = new File(CRL, FilenameUtils.getBaseName(crl
                                                                        .getFile())
                                         + ".crl");
    destinationCrt.renameTo(destinationCrl);

    final IgcService igcService = createIgcService();

    try {
      igcService.getInstanceCertifsAndCrl();

      // Plus d'exception mais on doit trouver une trace
      // fail(FAIL_MSG);
      // Vérification présence de la trace
      final Date dateFin = new Date();
      final Date dateDebut = DateUtils.addDays(dateFin, -1);
      final List<TraceRegTechniqueIndex> listeTrace = regTechnique.lecture(
                                                                           dateDebut, dateFin, 10, false);
      boolean traceTrouve = false;
      for (final TraceRegTechniqueIndex traceRegTechniqueIndex : listeTrace) {
        final TraceRegTechnique trace = regTechnique
            .lecture(traceRegTechniqueIndex.getIdentifiant());
        if ("WS_LOAD_CRLS|KO".equals(trace.getCodeEvt())) {

          traceTrouve = true;
          Assert.assertEquals("Contexte incorrect", "ChargementCRL",
                              trace.getContexte());

          Assert
          .assertEquals("saeServeurHostname incorrect", HostnameUtil
                        .getHostname(), trace.getInfos().get(
                            "saeServeurHostname"));
          Assert.assertTrue("CRL incorrect", StringUtils.contains(trace
                                                                  .getInfos().get("fichiers").toString(),
              "sae_webservices_igcservice"));
        }
      }

      Assert.assertTrue("Une trace de type WS_LOAD_CRLS|KO doit être trouvée", traceTrouve);
      // Lorsque le chargement des crl va planter, le fichier aui à fait planter le chargement
      // devrait être supprimer
      // on verifie que le fichier a bien été supprimer du repertoire
      // Evolution #232951
      Assert.assertFalse("Le fichier ne devrait plus exister dand le repertoire", destinationCrl.exists());

    } catch (final LoadCertifsAndCrlException e) {
      LOG.debug(e.getCause().getMessage());
      assertTrue(
                 "Exception non attendue de type " + e.getCause().getClass(),
                 CRLException.class.isAssignableFrom(e.getCause().getClass()));
    }
  }

  /**
   * Création des données TraceDestinataire pour effectuer les tests des services en Cql
   */
  private void createAllTraceDestinataire() {
    final URL url = this.getClass().getResource("/cassandra-local-dataset-sae-traces.xml");
    final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "TraceDestinataire");

    final List<TraceDestinataire> listTraceDestinataire = TraceDestinataireCqlUtils.convertRowsToTraceDestinataires(list);
    for (final TraceDestinataire traceDestinataire : listTraceDestinataire) {

      traceDestinataireCqlSupport.create(traceDestinataire, new Date().getTime());
    }
  }
}
