package fr.urssaf.image.sae.documents.executable.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.documents.executable.model.AddMetadatasParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;
import fr.urssaf.image.sae.documents.executable.multithreading.AddMetadatasPoolThreadExecutor;
import fr.urssaf.image.sae.documents.executable.multithreading.AddMetadatasRunnable;
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
   public final int identifierValiderFichiers(
         final FormatValidationParametres parametres) {
      long startTime = System.currentTimeMillis();
      LOGGER.debug("Lancement du traitement d'identification et de validation");
      getDfceService().ouvrirConnexion();
      final String requeteLucene = parametres.getRequeteLucene();
      final List<String> metadonnees = getMetadonnees(parametres);

      int nbDocTraites = 0;
      try {
         // execution de la requete dfce
         final Iterator<Document> iteratorDoc = getDfceService()
               .executerRequete(requeteLucene);

         // initialise le pool de thread
         final FormatValidationPoolThreadExecutor executor = new FormatValidationPoolThreadExecutor(
               parametres);

         int nbDocErreurIdent = 0;
         while (isTraitementTermine(parametres, startTime, iteratorDoc,
               nbDocTraites)) {
            // recupereation du contenu du document
            final Document document = iteratorDoc.next();
            final InputStream stream = getDfceService().recupererContenu(
                  document);
            final String idFormat = MetadataUtils.getMetadataByCd(document,
                  Constantes.METADONNEES_FORMAT_FICHIER).toString();

            File file = null;
            try {
               // creation du fichier temporaire
               file = createTmpFile(parametres, stream);

               if (!lancerIdentifierFichier(parametres, metadonnees, document,
                     idFormat, file)) {
                  nbDocErreurIdent++;
               }

               lancerValiderFichier(parametres, executor, document, file);

               nbDocTraites++;

               // trace l'avancement de l'identification
               tracerIdentification(parametres, nbDocTraites);

               if ((parametres.getModeVerification() == MODE_VERIFICATION.IDENTIFICATION)
                     && (file != null)) {
                  LOGGER.debug("Suppression du fichier temporaire {}", file
                        .getAbsolutePath());
                  if (!file.delete()) {
                     LOGGER.error(
                           "Impossible de supprimer le fichier temporaire {}",
                           file.getAbsolutePath());
                  }
               }
            } catch (IOException e) {
               LOGGER
                     .error(
                           "Erreur de conversion du stream en fichier temporaire : {}",
                           e.getMessage());
            }
         }

         if (parametres.getModeVerification() != MODE_VERIFICATION.IDENTIFICATION) {
            executor.shutdown();
            executor.waitFinishValidation();
         }

         LOGGER.info("{} documents analysés au total", nbDocTraites);
         LOGGER.info("{} documents en erreur d'identification",
               nbDocErreurIdent);
         LOGGER.info("{} documents en erreur de validation", executor
               .getNombreDocsErreur());

      } catch (SearchQueryParseException ex) {
         LOGGER.error("La syntaxe de la requête n'est pas valide : {}", ex
               .getMessage());
      }

      // ferme la connexion a dfce
      getDfceService().fermerConnexion();

      return nbDocTraites;
   }

   /**
    * Methode permettant de récupérer la liste des métadonnées en paramètres. Si
    * la liste est vide, on initalise la liste avec les métadonnées par défaut.
    * 
    * @param parametres
    *           paramètres
    * @return List<String>
    */
   private List<String> getMetadonnees(
         final FormatValidationParametres parametres) {
      final List<String> metadonnees = parametres.getMetadonnees();
      // initialise la liste de metadonnees si la liste est vide
      if (metadonnees.isEmpty()) {
         Collections.addAll(metadonnees, Constantes.METADONNEES_DEFAULT);
      }
      return metadonnees;
   }

   /**
    * Methode permettant de lancer l'identification d'un fichier.
    * 
    * @param parametres
    *           parameters
    * @param metadonnees
    *           metadonnees
    * @param document
    *           document
    * @param idFormat
    *           format
    * @param file
    *           fichier
    * @return boolean
    */
   private boolean lancerIdentifierFichier(
         final FormatValidationParametres parametres,
         final List<String> metadonnees, final Document document,
         final String idFormat, File file) {
      boolean identificationOk = true;
      if ((parametres.getModeVerification() != MODE_VERIFICATION.VALIDATION)
            && (!getFormatFichierService().identifierFichier(idFormat, file,
                  document, metadonnees))) {
         identificationOk = false;
      }
      return identificationOk;
   }

   /**
    * Methode permettant de lancer la validation d'un fichier.
    * 
    * @param parametres
    *           parameters
    * @param executor
    *           executeur de thread
    * @param document
    *           document
    * @param file
    *           fichier
    * @return boolean
    */
   private void lancerValiderFichier(
         final FormatValidationParametres parametres,
         final FormatValidationPoolThreadExecutor executor,
         final Document document, File file) {
      if (parametres.getModeVerification() != MODE_VERIFICATION.IDENTIFICATION) {
         final FormatRunnable runnable = new FormatRunnable(document, file,
               getFormatFichierService());
         executor.execute(runnable);
      }
   }

   /**
    * Methode permettant de vérifier le traitement est terminé. Pour se faire,
    * il faut vérifier qu'il y a encore des documents à traiter, et qu'on a pas
    * atteind le nombre max de documents, et qu'on a pas atteind le temps
    * maximum.
    * 
    * @param parametres
    *           parametres
    * @param startTime
    *           heure de début en millisecondes
    * @param iteratorDoc
    *           iterateur de document
    * @param nbDocTraites
    *           nombre de doc traités
    * @return
    */
   private boolean isTraitementTermine(
         final FormatValidationParametres parametres, long startTime,
         final Iterator<Document> iteratorDoc, int nbDocTraites) {
      return iteratorDoc.hasNext()
            && (nbDocTraites < parametres.getNombreMaxDocs())
            && (!isTempsMaximumAtteind(parametres, startTime));
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
         tempsMaxAtteind = (dureeCouranteMinute >= parametres
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
         LOGGER.debug("Création du fichier temporaire {}", tmpFile
               .getAbsolutePath());
         return tmpFile;
      } finally {
         IOUtils.closeQuietly(stream);
         IOUtils.closeQuietly(fos);
      }
   }

   /**
    * Methode permettant de tracer l'avancement de l'identification.
    * 
    * @param parametres
    *           parametres
    * @param nbDocTraites
    *           nombre de documents traités
    */
   private void tracerIdentification(
         final FormatValidationParametres parametres, int nbDocTraites) {
      if ((parametres.getModeVerification() != MODE_VERIFICATION.VALIDATION)
            && (nbDocTraites % parametres.getTaillePasExecution() == 0)) {
         LOGGER.info("{} documents identifiés", nbDocTraites);
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

   @Override
   public void addMetadatasToDocuments(AddMetadatasParametres parametres) {

      // -- Overture connexion dfce
      getDfceService().ouvrirConnexion();

      String requeteLucene = parametres.getRequeteLucene();
      Map<String, String> metas = parametres.getMetadonnees();

      try {
         Iterator<Document> it = getDfceService()
               .executerRequete(requeteLucene);

         AddMetadatasPoolThreadExecutor poolThead;
         poolThead = new AddMetadatasPoolThreadExecutor(parametres);

         while (it.hasNext()) {
            Document doc = (Document) it.next();
            AddMetadatasRunnable addMetasRun;
            addMetasRun = new AddMetadatasRunnable(getDfceService(), doc, metas);
            poolThead.execute(addMetasRun);
         }

         poolThead.shutdown();

         // -- On attend la fin de l'execution du poolThead
         poolThead.waitFinishAddMetadata();

         LOGGER.info("{} documents traités au total", poolThead
               .getNombreTraites());
      } catch (SearchQueryParseException ex) {
         LOGGER.error("La syntaxe de la requête n'est pas valide : {}", ex
               .getMessage());
      }

      // -- Fermeture connexion dfce
      getDfceService().fermerConnexion();
   }

}
