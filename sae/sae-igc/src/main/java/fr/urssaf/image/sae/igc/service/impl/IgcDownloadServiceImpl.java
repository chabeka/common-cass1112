package fr.urssaf.image.sae.igc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcDownloadService;
import fr.urssaf.image.sae.igc.util.URLUtils;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;

/**
 * Classe d'implémentation {@link IgcDownloadService}
 * 
 * 
 */
@Service
public class IgcDownloadServiceImpl implements IgcDownloadService {

   private DispatcheurService dispatcheurService;

   private static final String TRACE_CODE_EVT_ECHEC_CHARGEMENT_CRL = "IGC_LOAD_CRLS|KO";

   private static final Logger LOG = LoggerFactory
         .getLogger(IgcDownloadServiceImpl.class);

   /**
    * Constructeur
    * 
    * @param dispatcheurService
    *           Le service de dispatch des traces
    */
   @Autowired
   public IgcDownloadServiceImpl(DispatcheurService dispatcheurService) {
      this.dispatcheurService = dispatcheurService;
   }

   @Override
   public final void telechargeCRLs(IgcConfigs igcConfigs)
         throws IgcDownloadException {

      for (IgcConfig igcConfig : igcConfigs.getIgcConfigs()) {

         if (igcConfig.isDlActivated()) {

            // Création d'un répertoire temporaire
            String repCrls = igcConfig.getCrlsRep();
            String[] split = repCrls.split(Pattern.quote(File.separator));
            String repTemp = repCrls.replace(split[split.length - 1],
                  "repCrlsTemp");

            for (URL url : igcConfig.getUrlList().getUrls()) {
               boolean erreur = false;

               try {
                  // Téléchargement des CRLs
                  int downloads = this.download(url, repTemp);
                  LOG
                        .info(
                              "Mise a jour des CRL pour la PKI {} : {} CRL telechargees",
                              igcConfig.getPkiIdent(), downloads);

                  // Test de chargement des CRL
                  File repertoire = new File(repTemp);
                  String[] listeCRL = repertoire.list();

                  for (String crl : listeCRL) {
                     InputStream input = new FileInputStream(repTemp + "/"
                           + crl);
                     CertificateFactory certifFactory;
                     try {
                        certifFactory = CertificateFactory.getInstance("X.509");
                        certifFactory.generateCRL(input);
                     } catch (GeneralSecurityException e) {
                        erreur = true;
                        LOG.error(
                              "erreur de chargement du fichier CRL: " + crl, e);
                        ecrireTraces(TRACE_CODE_EVT_ECHEC_CHARGEMENT_CRL, crl,
                              igcConfig.getPkiIdent());
                     }
                  }

               } catch (IOException e) {
                  erreur = true;
                  LOG.error("erreur de téléchargement des CRLs : " + url, e);
                  ecrireTraces(TRACE_CODE_EVT_ECHEC_CHARGEMENT_CRL, url
                        .toString(), igcConfig.getPkiIdent());
               }

               // Si pas de pb de chargement, on déplace les CRL du
               // répertoire temporaire au répertoire définitif
               if (!erreur) {
                  // Liste des éléments du répertoire temporaire
                  File repertoireTemp = new File(repTemp);
                  String[] tabCRLTemp = repertoireTemp.list();
                  List<String> listeCRLTemp = Arrays.asList(tabCRLTemp);
                  // Liste des éléments du répertoire définitif
                  File repertoireDef = new File(repCrls);
                  String[] tabCRLDef = repertoireDef.list();
                  // Suppression des éléments présents dans le répertoire
                  // définitif mais pas dans le répertoire temporaire
                  for (String crl : tabCRLDef) {
                     if (!listeCRLTemp.contains(crl)) {
                        File crlToDelete = new File(repCrls + "/" + crl);
                        try {
                        crlToDelete.delete();
                        } catch (Exception e) {
                           System.out.println("exeption");
                          System.out.println(e.getMessage());
                        }
                        
                     }
                  }
                  // Copie de tous les fichiers du répertoire temporaire au
                  // répertoire définitif
                  for (String crl : tabCRLTemp) {
                     LOG.debug("Copie du fichier : " + crl);
                     System.out.println("Copie du fichier : " + crl);
                     File source = new File(repTemp + "/" + crl);
                     File destination = new File(repCrls + "/" + crl);
                     boolean res = copier(source, destination);
                     if (!res) {
                        LOG.debug("Fichier non copié");
                     }
                  }
               }

               // Suppression du répertoire temporaire
               File repToDelete = new File(repTemp);
               String[] tabCRLToDelete = repToDelete.list();
               if (tabCRLToDelete != null) {
                  List<String> listeCRLToDelete = Arrays.asList(tabCRLToDelete);
                  for (String crlToDelete : listeCRLToDelete) {
                     File source = new File(repTemp + "/" + crlToDelete);
                     if (source.exists()) {
                        source.delete();
                     }
                  }
               }
               
               if (repToDelete.exists()) {
                  repToDelete.delete();
               }
            }

         } else {
            LOG.info("mise à jour des CRL désactivée pour la PKI {}", igcConfig
                  .getPkiIdent());
         }
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

   private void ecrireTraces(String codeEvenement, String crl, String pki) {
      try {
         // Instantiation de l'objet TraceToCreate
         TraceToCreate traceToCreate = new TraceToCreate();

         // Code de l'événement
         traceToCreate.setCodeEvt(codeEvenement);

         // Contexte
         traceToCreate.setContexte("telechargerCRLs");

         // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
         // ce code
         traceToCreate.getInfos().put("saeServeurHostname",
               HostnameUtil.getHostname());
         traceToCreate.getInfos().put("fichier", crl);
         traceToCreate.getInfos().put("pki", pki);

         // Appel du dispatcheur
         dispatcheurService.ajouterTrace(traceToCreate);
      } catch (Throwable ex) {
         LOG
               .error(
                     "Une erreur s'est produite lors de l'écriture de la trace d'erreur de chargement des CRLs",
                     ex);
      }
   }

   /**
    * copie le fichier source dans le fichier resultat retourne vrai si cela
    * réussit
    */
   private boolean copier(File source, File destination) {
      boolean resultat = false;

      // Declaration des flux
      java.io.FileInputStream sourceFile = null;
      java.io.FileOutputStream destinationFile = null;

      try {
         // Création du fichier :
         destination.createNewFile();

         // Ouverture des flux
         sourceFile = new java.io.FileInputStream(source);
         destinationFile = new java.io.FileOutputStream(destination);

         // Lecture par segment de 0.5Mo
         byte buffer[] = new byte[512 * 1024];
         int nbLecture;

         while ((nbLecture = sourceFile.read(buffer)) != -1) {
            destinationFile.write(buffer, 0, nbLecture);
         }

         // Copie réussie
         resultat = true;
      } catch (java.io.FileNotFoundException f) {

      } catch (java.io.IOException e) {

      } finally {
         // Quoi qu'il arrive, on ferme les flux
         try {
            sourceFile.close();
         } catch (Exception e) {
         }
         try {
            destinationFile.close();
         } catch (Exception e) {
         }
      }
      return (resultat);
   }
}
