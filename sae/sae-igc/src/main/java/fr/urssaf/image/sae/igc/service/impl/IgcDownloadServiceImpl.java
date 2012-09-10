package fr.urssaf.image.sae.igc.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.igc.util.URLUtils;

/**
 * Classe d'implémentation {@link IgcDownloadService}
 * 
 * 
 */
public class IgcDownloadServiceImpl implements IgcDownloadService {

   private static final Logger LOG = LoggerFactory
         .getLogger(IgcDownloadServiceImpl.class);

   @Override
   public final void telechargeCRLs(IgcConfigs igcConfigs)
         throws IgcDownloadException {

      List<Exception> exceptions = new ArrayList<Exception>();
      for (IgcConfig igcConfig : igcConfigs.getIgcConfigs()) {

         try {

            for (URL url : igcConfig.getUrlList().getUrls()) {

               int downloads = this.download(url, igcConfig.getCrlsRep());

               LOG
                     .info(
                           "Mise a jour des CRL pour la PKI {} : {} CRL telechargees",
                           igcConfig.getPkiIdent(), downloads);

            }

         } catch (IOException e) {
            exceptions.add(e);
         }
      }

      if (!CollectionUtils.isEmpty(exceptions)) {
         // TODO FBON - Comment retourner l'ensemble des exceptions ?
         throw new IgcDownloadException(exceptions.get(0));
      }

   }

   private int download(URL url, String repertory) throws IOException {

      List<URL> urls = URLUtils.findLinks(new URL(url.getProtocol(), url
            .getHost(), url.getPort(), "/"));

      return this.download(urls, repertory, FilenameUtils.getExtension(url
            .getFile()));

   }

   private int download(List<URL> urls, String repertory, String extension)
         throws IOException {

      int downloads = 0;

      for (URL url : urls) {

         if (this.download(url, repertory, extension)) {

            downloads++;

         }

      }

      return downloads;

   }

   private boolean download(URL url, String repertory, String extension)
         throws IOException {

      boolean isDownload = false;

      File destination = new File(repertory + url.getFile());

      if (FileFilterUtils.suffixFileFilter(extension).accept(destination)) {

         LOG.debug("downloading from " + url.toString());

         FileUtils.copyURLToFile(url, destination);

         isDownload = true;

      }

      return isDownload;

   }

}
