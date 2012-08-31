package fr.urssaf.image.sae.webservices.security.igc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.modele.IssuerList;
import fr.urssaf.image.sae.webservices.component.IgcConfigUtils;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcServiceTest {

   private static final String FAIL_MSG = "le test doit échouer";

   private static final URL CERTIFICAT;

   private static final Logger LOG = LoggerFactory
         .getLogger(IgcServiceTest.class);

   private static final File TMP_DIR;

   private static final File CRL;

   private static final File AC_RACINE;

   private IgcConfigs igcConfigs;

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

      igcConfigs = new IgcConfigs();

      IgcConfig igcConfig = new IgcConfig();

      igcConfig.setPkiIdent("PKI_VAL_AED");
      igcConfig.setAcRacine(AC_RACINE.getAbsolutePath() + File.separator
            + CERTIFICAT.getFile());
      igcConfig.setCrlsRep(CRL.getAbsolutePath());

      IssuerList issuerList = new IssuerList();
      issuerList
            .setIssuers(Arrays.asList(new String[] { "CN=AC Application" }));
      igcConfig.setIssuerList(issuerList);

      igcConfigs.setIgcConfigs(Arrays.asList(new IgcConfig[] { igcConfig }));

   }

   private IgcService createIgcService() {

      IgcService igcService = new IgcService(igcConfigs);
      igcService.afterPropertiesSet();

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

      URL pem = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/Pseudo_ACOSS.pem");

      IgcConfigUtils.download(pem, new File(AC_RACINE, "pseudo_appli.crt"));

      try {
         createIgcService();
         fail(FAIL_MSG);
      } catch (IllegalArgumentException e) {
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
      } catch (IllegalArgumentException e) {

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
      URL crl = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/Pseudo_ACOSS.crl");
      IgcConfigUtils.download(crl, new File(CRL, crl.getFile()));

      IgcService igcService = createIgcService();
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
      URL crl = CERTIFICAT;
      File destinationCrt = new File(CRL, crl.getFile());
      IgcConfigUtils.download(crl, destinationCrt);

      // Renommage du fichier .crt en fichier .crl pour faire planter
      // le chargement des CRL
      File destinationCrl = new File(CRL, FilenameUtils.getBaseName(crl
            .getFile())
            + ".crl");
      destinationCrt.renameTo(destinationCrl);

      IgcService igcService = createIgcService();

      try {
         igcService.getInstanceCertifsAndCrl();
         fail(FAIL_MSG);
      } catch (LoadCertifsAndCrlException e) {
         LOG.debug(e.getCause().getMessage());
         assertTrue(
               "Exception non attendue de type " + e.getCause().getClass(),
               CRLException.class.isAssignableFrom(e.getCause().getClass()));
      }
   }
}
