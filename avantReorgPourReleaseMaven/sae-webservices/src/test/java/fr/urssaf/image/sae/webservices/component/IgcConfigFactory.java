package fr.urssaf.image.sae.webservices.component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
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

   static {

      DIRECTORY = IgcConfigUtils.createTempRepertory("sae_webservices");

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
   public static IgcConfig createIgcConfig() throws IgcDownloadException {

      IgcConfig igcConfig = new IgcConfig();

      igcConfig.setRepertoireACRacines(AC_RACINE.getAbsolutePath());
      igcConfig.setRepertoireCRLs(CRL.getAbsolutePath());

      igcConfig.setUrlsTelechargementCRLs(new ArrayList<URL>());
      igcConfig.getUrlsTelechargementCRLs().add(CRL_DOWNLOAD);

      // téléchargement d'une AC racine
      IgcConfigUtils.download(CERTIFICAT, new File(AC_RACINE.getAbsolutePath()
            + "/" + CERTIFICAT.getFile()));

      // téléchargement des CRL
      IgcDownloadService downaloadService = new IgcDownloadServiceImpl();
      downaloadService.telechargeCRLs(igcConfig);

      return igcConfig;

   }
}
