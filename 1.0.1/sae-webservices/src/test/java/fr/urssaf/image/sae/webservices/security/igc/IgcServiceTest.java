package fr.urssaf.image.sae.webservices.security.igc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.webservices.component.IgcConfigUtils;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcServiceTest {

   private static final String FAIL_MSG = "le test doit échouer";

   private static final URL CERTIFICAT;

   private static final Logger LOG = Logger.getLogger(IgcServiceTest.class);

   private static final File CRL;

   private static final File AC_RACINE;

   static {

      File repertory = IgcConfigUtils
            .createTempRepertory("sae_webservices_igcservice");

      CRL = new File(repertory.getAbsolutePath() + "/CRL/");
      AC_RACINE = new File(repertory.getAbsolutePath() + "/ACRacine/");

      IgcConfigUtils.createRepertory(CRL);
      IgcConfigUtils.createRepertory(AC_RACINE);

      CERTIFICAT = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/pseudo_appli.crt");

   }

   private IgcConfig igcConfig;

   @Before
   public void before() {

      igcConfig = new IgcConfig();

      igcConfig.setRepertoireACRacines(AC_RACINE.getAbsolutePath());
      igcConfig.setRepertoireCRLs(CRL.getAbsolutePath());

   }

   private IgcService createIgcService() {

      IgcService igcService = new IgcService(igcConfig);
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
      IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE.getAbsolutePath()
            + "/" + CERTIFICAT.getFile()));

      assertNotNull("exception dans le constructeur", createIgcService());

   }

   @Test
   public void IgcService_failure_certificateException()
         throws IgcDownloadException {

      URL pem = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/Pseudo_ACOSS.pem");

      IgcConfigUtils.download(pem, new File(AC_RACINE.getAbsolutePath() + "/"
            + "certificat.crt"));

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
                           "Aucun certificat d'AC racine de confiance trouvé dans le répertoire"));
      }
   }

   @Test
   public void getInstanceCertifsAndCrl_success()
         throws LoadCertifsAndCrlException, IgcDownloadException {

      // téléchargement d'une AC racine
      IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE.getAbsolutePath()
            + "/" + CERTIFICAT.getFile()));

      // téléchargement d'une CRL
      URL crl = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/Pseudo_ACOSS.crl");
      IgcConfigUtils.download(crl, new File(CRL.getAbsolutePath() + "/"
            + crl.getFile()));

      IgcService igcService = createIgcService();
      assertNotNull("une instance de CertifsAndCrl est attendue", igcService
            .getInstanceCertifsAndCrl());
   }

   
   @Test
   public void getInstanceCertifsAndCrl_failure() throws IgcDownloadException {

      // téléchargement d'une AC racine
      IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE.getAbsolutePath()
            + "/" + CERTIFICAT.getFile()));

      // Téléchargement d'un certificat X509 dans le répertoire des CRL, 
      // pour ensuite faire planter le chargement des CRL
      URL crl = CERTIFICAT;
      File destinationCrt = new File(CRL.getAbsolutePath() + "/" + crl.getFile());
      IgcConfigUtils.download(crl, destinationCrt);
      
      // Renommage du fichier .crt en fichier .crl pour faire planter
      // le chargement des CRL
      File destinationCrl = new File(
            CRL.getAbsolutePath() + 
            "/" + 
            FilenameUtils.getBaseName(crl.getFile()) + 
            ".crl");
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
