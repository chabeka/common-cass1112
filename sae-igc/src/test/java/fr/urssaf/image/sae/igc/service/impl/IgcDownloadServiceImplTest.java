package fr.urssaf.image.sae.igc.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.modele.URLList;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;

@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.VariableNamingConventions" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-igc-test.xml")
public class IgcDownloadServiceImplTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(IgcDownloadServiceImplTest.class);

   @Autowired
   private IgcDownloadServiceImpl service;

   @Autowired
   private RegTechniqueService regTechnique;

   private final static File DIRECTORY;

   private final static File CRL;

   private static URL download_exist;

   static {

      DIRECTORY = new File(SystemUtils.getJavaIoTmpDir().getAbsolutePath(),
            "certificats.download");

      CRL = new File(DIRECTORY, "CRL");

   }

   @BeforeClass
   public static void beforeClass() throws IOException, ConfigurationException {

      Configuration config = new PropertiesConfiguration("sae-igc.properties");

      download_exist = new URL(config.getString("url.download"));

      FileUtils.forceMkdir(CRL);

   }

   @AfterClass
   public static void afterClass() throws IOException {

      FileUtils.deleteDirectory(DIRECTORY);

   }

   @Test
   public void telechargeCRLs_success() throws IgcDownloadException {

      IgcConfigs igcConfigs = new IgcConfigs();
      IgcConfig igcConfig = new IgcConfig();
      igcConfig.setCrlsRep(CRL.getAbsolutePath());
      igcConfig.setDlActivated(true);

      List<URL> urls = new ArrayList<URL>();
      urls.add(download_exist);

      URLList urlList = new URLList();
      urlList.setUrls(urls);
      igcConfig.setUrlList(urlList);

      igcConfig.setPkiIdent("PKI_TEST");

      igcConfigs.setIgcConfigs(Arrays.asList(new IgcConfig[] { igcConfig }));

      service.telechargeCRLs(igcConfigs);

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

      for (File file : files) {
         LOG.debug(file.getName());
      }

   }

   @Test
   public void telechargeCRLs_success_zero_dl() throws IgcDownloadException {

      IgcConfigs igcConfigs = new IgcConfigs();
      IgcConfig igcConfig = new IgcConfig();
      igcConfig.setCrlsRep(CRL.getAbsolutePath());
      igcConfig.setDlActivated(false);

      List<URL> urls = new ArrayList<URL>();
      urls.add(download_exist);

      URLList urlList = new URLList();
      urlList.setUrls(urls);
      igcConfig.setUrlList(urlList);

      igcConfig.setPkiIdent("PKI_TEST");

      igcConfigs.setIgcConfigs(Arrays.asList(new IgcConfig[] { igcConfig }));

      service.telechargeCRLs(igcConfigs);

   }

   @Test
   public void telechargeCRLs_failure() throws IgcDownloadException,
         MalformedURLException {

      IgcConfigs configs = new IgcConfigs();

      IgcConfig igcConfig = new IgcConfig();
      igcConfig.setCrlsRep(CRL.getAbsolutePath());
      igcConfig.setDlActivated(true);

      List<URL> urls = new ArrayList<URL>();
      urls.add(new URL("http://download.oracle.com/javase/6/docs/api/"));

      URLList urlList = new URLList();
      urlList.setUrls(urls);
      igcConfig.setUrlList(urlList);

      igcConfig.setPkiIdent("PKI_TEST");
      configs.setIgcConfigs(Arrays.asList(new IgcConfig[] { igcConfig }));

      service.telechargeCRLs(configs);

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

         Assert.assertEquals("saeServeurHostname incorrect", HostnameUtil.getHostname(), trace.getInfos().get("saeServeurHostname"));
         Assert.assertEquals("CRL incorrect", "http://download.oracle.com/javase/6/docs/api/", trace.getInfos().get("fichier"));
         Assert.assertEquals("PKI incorrect", "PKI_TEST", trace.getInfos().get("pki"));
         
      }

   }
}
