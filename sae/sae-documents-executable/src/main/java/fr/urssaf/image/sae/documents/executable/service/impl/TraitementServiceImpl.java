package fr.urssaf.image.sae.documents.executable.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;
import fr.urssaf.image.sae.documents.executable.multithreading.FormatRunnable;
import fr.urssaf.image.sae.documents.executable.multithreading.FormatValidationPoolThreadExecutor;
import fr.urssaf.image.sae.documents.executable.service.DfceService;
import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.documents.executable.service.TraitementService;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;

/**
 * Classe d'implémentation du service <b>TraitementService</b>. Cette classe est
 * un singleton, et est accessible via l'annotation <b>@AutoWired</b>.
 */
@Service
public class TraitementServiceImpl implements TraitementService {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraitementServiceImpl.class);

   /**
    * Service permettant de réaliser des opérations sur DFCE.
    */
   @Autowired
   private DfceService dfceService;

   /**
    * Service permettant de réaliser des opérations sur les fichiers.
    */
   @Autowired
   private FormatFichierService formatFichierService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void identifierValiderFichiers(
         final FormatValidationParametres parametres) {
      long startTime = System.currentTimeMillis();
      getDfceService().ouvrirConnexion();
      final String requeteLucene = parametres.getRequeteLucene();
      final List<String> metadonnees = parametres.getMetadonnees();
      // initialise la liste de metadonnees si la liste est vide
      if (metadonnees.isEmpty()) {
         Collections.addAll(metadonnees, Constantes.METADONNEES_DEFAULT);
      }
      // execution de la requete dfce
      final Iterator<Document> iteratorDoc = getDfceService().executerRequete(
            requeteLucene);

      // initialise le pool de thread
      final FormatValidationPoolThreadExecutor executor = new FormatValidationPoolThreadExecutor(
            parametres);

      int nbDocTraites = 0;
      int nbDocErreurIdent = 0;
      while (iteratorDoc.hasNext()
            && (nbDocTraites < parametres.getNombreMaxDocs())
            && (!isTempsMaximumAtteind(parametres, startTime))) {
         // recupereation du contenu du document
         final Document document = iteratorDoc.next();
         final InputStream stream = getDfceService().recupererContenu(document);
         final String idFormat = MetadataUtils.getMetadataByCd(document,
               Constantes.METADONNEES_FORMAT_FICHIER).toString();

         File file = null;
         try {
            // creation du fichier temporaire
            file = createTmpFile(parametres, stream);

            if (parametres.getModeVerification() != MODE_VERIFICATION.VALIDATION) {
               if (!getFormatFichierService().identifierFichier(idFormat, file,
                     document, metadonnees)) {
                  nbDocErreurIdent++;
               }
            }

            if (parametres.getModeVerification() != MODE_VERIFICATION.IDENTIFICATION) {
               final FormatRunnable runnable = new FormatRunnable(document,
                     file, getFormatFichierService());
               executor.execute(runnable);
            }

            nbDocTraites++;

            if (parametres.getModeVerification() != MODE_VERIFICATION.VALIDATION) {
               if (nbDocTraites % parametres.getTaillePasExecution() == 0) {
                  LOGGER.info("{} documents identifiés", nbDocTraites);
               }
            }

            if (parametres.getModeVerification() == MODE_VERIFICATION.IDENTIFICATION) {
               // dans le cas de l'identification simple, la suppression du
               // fichier se fait directement
               // dans le cas de la validation ou identification et validation,
               // le fichier sera supprimé à la fin du thread
               if (file != null) {
                  file.delete();
               }
            }
         } catch (IOException e) {
            LOGGER.error(
                  "Erreur de conversion du stream en fichier temporaire : {}",
                  e.getMessage());
            if (file != null) {
               file.delete();
            }
         }
      }

      if (parametres.getModeVerification() != MODE_VERIFICATION.IDENTIFICATION) {
         executor.shutdown();
         executor.waitFinishValidation();
      }

      LOGGER.info("{} documents analysés au total", nbDocTraites);
      LOGGER.info("{} documents en erreur d'identification", nbDocErreurIdent);
      LOGGER.info("{} documents en erreur de validation", executor
            .getNombreDocsErreur());
   }

   /**
    * Methode permettant de vérifier si on a atteind la durée maximum de
    * traitement.
    * 
    * @param parametres
    *           parametres
    * @param startTime
    *           heure de debut en millisecondes
    * @return boolean
    */
   private boolean isTempsMaximumAtteind(
         final FormatValidationParametres parametres, final long startTime) {
      boolean tempsMaxAtteind = true;
      if (parametres.getTempsMaxTraitement() == 0) {
         // pas de temps maximum, donc pas atteind
         tempsMaxAtteind = false;
      } else {
         // on calcule la duree courante et on vérifie qu'on n'a pas atteind le
         // maximum
         final long dureeCouranteMinute = (System.currentTimeMillis() - startTime)
               / Constantes.CONVERT_MILLISECONDS_TO_MINUTES;
         tempsMaxAtteind = (dureeCouranteMinute < parametres
               .getTempsMaxTraitement());
      }
      return tempsMaxAtteind;
   }

   /**
    * Methode permettant de créer un fichier temporaire sur le disque à partir
    * du stream. Le fichier sera placé dans le répertoire configuré dans l'objet
    * paramètre ou dans le répertoire temporaire de l'OS si le paramètre n'est
    * pas configuré.
    * 
    * @param parametres
    *           objet paramètre
    * @param stream
    *           contenu du fichier
    * @return File
    * @throws IOException
    */
   private File createTmpFile(final FormatValidationParametres parametres,
         final InputStream stream) throws IOException {
      File tmpFile = null;
      FileOutputStream fos = null;
      try {
         File repertoireTemporaire = null;
         if (StringUtils.isNotBlank(parametres.getCheminRepertoireTemporaire())) {
            repertoireTemporaire = new File(parametres
                  .getCheminRepertoireTemporaire());
         }
         tmpFile = File.createTempFile("sae-doc-exec-", ".pdf",
               repertoireTemporaire);
         fos = new FileOutputStream(tmpFile);
         IOUtils.copy(stream, fos);
         return tmpFile;
      } finally {
         IOUtils.closeQuietly(stream);
         IOUtils.closeQuietly(fos);
      }
   }

   /**
    * Permet de récupérer le service permettant de réaliser des opérations sur
    * DFCE.
    * 
    * @return DfceService
    */
   public final DfceService getDfceService() {
      return dfceService;
   }

   /**
    * Permet de modifier le service permettant de réaliser des opérations sur
    * DFCE.
    * 
    * @param dfceService
    *           service permettant de réaliser des opérations sur DFCE.
    */
   public final void setDfceService(final DfceService dfceService) {
      this.dfceService = dfceService;
   }

   /**
    * Permet de récupérer le service permettant de réaliser des opérations sur
    * les fichiers.
    * 
    * @return FormatFichierService
    */
   public final FormatFichierService getFormatFichierService() {
      return formatFichierService;
   }

   /**
    * Permet de récupérer le service permettant de réaliser des opérations sur
    * les fichiers.
    * 
    * @param formatFichierService
    *           service permettant de réaliser des opérations sur les fichiers
    */
   public final void setFormatFichierService(
         final FormatFichierService formatFichierService) {
      this.formatFichierService = formatFichierService;
   }

}
