package fr.urssaf.image.sae.webservices.component;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.modele.IssuerList;
import fr.urssaf.image.sae.igc.modele.URLList;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.igc.service.impl.IgcDownloadServiceImpl;

/**
 * Classe d'instance de {@link IgcConfig}
 * 
 * 
 */
public final class IgcConfigFactory {

   public static final String DOWNLOAD_URL = "http://cer69idxpkival1.cer69.recouv/";

   private IgcConfigFactory() {

   }

   public static final File DIRECTORY;

   public static final File CRL;

   public static final File AC_RACINE;

   public static final URL CRL_DOWNLOAD;

   public static final URL CERTIFICAT;

   public static final String ID_PKI = "PKI_TEST";

   static {

      DIRECTORY = IgcConfigUtils
            .createTempRepertory("sae_webservices/PKI_TEST");

      CRL = new File(DIRECTORY + "/CRL/");
      AC_RACINE = new File(DIRECTORY + "/ACRacine/");

      IgcConfigUtils.createRepertory(DIRECTORY);
      IgcConfigUtils.createRepertory(CRL);
      IgcConfigUtils.createRepertory(AC_RACINE);

      CRL_DOWNLOAD = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/*.crl");

      CERTIFICAT = IgcConfigUtils
            .createURL("http://cer69idxpkival1.cer69.recouv/pseudo_IGCA.crt");

   }

   /**
    * 
    * 
    * 
    * @return instance de {@link IgcConfig}
    * @throws IgcDownloadException
    *            exception lors du téléchargement des crl
    */
   public static IgcConfigs createIgcConfig() throws IgcDownloadException {

      IgcConfigs igcConfigs = new IgcConfigs();

      IgcConfig igcConfig = new IgcConfig();

      IssuerList issuerList = new IssuerList();
      issuerList.setIssuers(Arrays.asList(new String[] { "CN=AC Applications,O=ACOSS,L=Paris,ST=France,C=FR" }));
      igcConfig.setIssuerList(issuerList);

      igcConfig.setPkiIdent(ID_PKI);

      igcConfig.setAcRacine(AC_RACINE.getAbsolutePath() + File.separator
            + CERTIFICAT.getFile());
      igcConfig.setCrlsRep(CRL.getAbsolutePath());

      URLList urlList = new URLList();
      urlList.setUrls(Arrays.asList(new URL[] { CRL_DOWNLOAD }));

      igcConfig.setUrlList(urlList);

      // téléchargement d'une AC racine
      IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE.getAbsolutePath()
            + "/" + CERTIFICAT.getFile()));

      igcConfigs.setIgcConfigs(Arrays.asList(new IgcConfig[] { igcConfig }));

      // téléchargement des CRL
      IgcDownloadService downaloadService = new IgcDownloadServiceImpl();
      downaloadService.telechargeCRLs(igcConfigs);

      return igcConfigs;

   }
}
