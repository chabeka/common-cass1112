package fr.urssaf.image.sae.igc.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.modele.URLList;

@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.VariableNamingConventions" })
public class IgcDownloadServiceImplTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(IgcDownloadServiceImplTest.class);

   private IgcDownloadServiceImpl service;

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

   @Before
   public void before() {

      this.service = new IgcDownloadServiceImpl();
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

   @Test(expected = IgcDownloadException.class)
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

   }
}
