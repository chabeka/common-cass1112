package fr.urssaf.image.sae.igc.service.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

      IgcConfig igcConfig = new IgcConfig();
      igcConfig.setRepertoireCRLs(CRL.getAbsolutePath());

      List<URL> urls = new ArrayList<URL>();
      urls.add(download_exist);

      igcConfig.setUrlsTelechargementCRLs(urls);

      Integer crlsNumber = service.telechargeCRLs(igcConfig);

      assertEquals("erreur sur le nombre d'urls à télécharger", Integer
            .valueOf(15), crlsNumber);

      Collection<File> files = FileUtils.listFiles(CRL, null, true);

      for (File file : files) {

         LOG.debug(file.getName());
      }

   }

   @Test(expected = IgcDownloadException.class)
   public void telechargeCRLs_failure() throws IgcDownloadException,
         MalformedURLException {

      IgcConfig igcConfig = new IgcConfig();
      igcConfig.setRepertoireCRLs(CRL.getAbsolutePath());

      List<URL> urls = new ArrayList<URL>();
      urls.add(new URL("http://download.oracle.com/javase/6/docs/api/"));

      igcConfig.setUrlsTelechargementCRLs(urls);

      service.telechargeCRLs(igcConfig);

   }
}
