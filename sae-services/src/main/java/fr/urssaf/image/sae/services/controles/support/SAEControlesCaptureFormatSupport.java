package fr.urssaf.image.sae.services.controles.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.FormatProfil;
import fr.urssaf.image.sae.droit.utils.EnumValidationMode;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.identification.service.IdentificationService;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.service.ValidationService;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;
import fr.urssaf.image.sae.services.controles.model.ControleFormatSucces;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;
import fr.urssaf.image.sae.services.exception.format.FormatRuntimeException;
import fr.urssaf.image.sae.services.exception.format.identification.FormatIdentificationRuntimeException;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de support pour les contrôles de format à la capture
 */
@Component
public final class SAEControlesCaptureFormatSupport {

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAEControlesCaptureFormatSupport.class);

   private static final String MESSAGE_EXCEPTION = "Le fichier à archiver ne correspond pas au format spécifié.";

   @Autowired
   private IdentificationService identificationService;

   @Autowired
   private ValidationService validationService;

   @Autowired
   private ReferentielFormatService referentielFormatService;

   @Autowired
   private TracesControlesSupport tracesSupport;

   /**
    * Méthode chargée d'appeler le service de contrôle des formats.
    * 
    * @param contexte
    *           contexte d'appel de la vérification du format
    * @param saeDocument
    *           Objet représentant un document
    * @param controlProfilSet
    *           Liste des profils de contrôle à appliquer
    * @return des éléments sur les étapes de contrôle qui ont été effectué
    * @throws UnknownFormatException
    *            Le format est inconnu du référentiel des formats.
    * @throws ValidationExceptionInvalidFile
    *            Erreur dans la validation du fichier.
    */
   public ControleFormatSucces checkFormat(String contexte,
         SAEDocument saeDocument, List<FormatControlProfil> controlProfilSet)
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Traces debug - entrée méthode
      String prefixeTrc = "checkFormat()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // Objet de résultat
      ControleFormatSucces resultatControle = new ControleFormatSucces();

      // Récupère la valeur de la métadonnée "FormatFichier"
      LOGGER.debug("{} - Lecture de la métadonnée FormatFichier", prefixeTrc);
      String fileFormat = findMetadataValue("FormatFichier", saeDocument
            .getMetadatas());
      if (StringUtils.isBlank(fileFormat)) {
         throw new UnknownFormatException(
               "La métadonnée FormatFichier n'est pas renseignée : impossible de continuer le contrôle sur le format de fichier");
      }
      LOGGER.debug("{} - Valeur de la métadonnée FormatFichier : {}",
            prefixeTrc, fileFormat);

      // Vérifie que le format existe dans le référentiel des formats
      LOGGER
            .debug(
                  "{} - Vérifie que la valeur de la métadonnée FormatFichier existe dans le référentiel des formats",
                  prefixeTrc);
      if (!referentielFormatService.exists(fileFormat)) {
         throw new UnknownFormatException(ResourceMessagesUtils.loadMessage(
               "capture.format.format.inconnnu", fileFormat));
      }

      // On ne continue que s'il y a au moins 1 profil de contrôle dans la liste
      if (CollectionUtils.isNotEmpty(controlProfilSet)) {

         // Sélection du profil
         LOGGER.debug(
               "{} - Détermine le profil de contrôle qu'il faut appliquer",
               prefixeTrc);
         FormatControlProfil formatControlProfil = selectProfil(fileFormat,
               controlProfilSet);

         // Selon si on a trouvé 1 profil de contrôle à appliquer dans la liste
         if (formatControlProfil == null) {

            // Aucun profil de contrôle de la liste ne correspond au format de
            // fichier déclaré par le client
            // => on ne fait donc aucun contrôle de format
            LOGGER
                  .debug(
                        "{} - Aucun profil de contrôle de la liste ne correspond au format de fichier déclaré par le client. On ne fait donc aucun contrôle de format.",
                        prefixeTrc);

         } else if (formatControlProfil.getControlProfil() == null) {

            // cas atypique et improbable => on lève une RuntimeException
            LOGGER
                  .debug(
                        "{} - Erreur technique, l'objet formatControlProfil.getControlProfil() est null",
                        prefixeTrc);
            throw new FormatRuntimeException(
                  "Erreur technique: la variable formatControlProfil.getControlProfil() est null");

         } else {

            // Récupère le profil de contrôle
            FormatProfil formatProfil = formatControlProfil.getControlProfil();

            // Enrichissement de l'objet résultat de la méthode
            resultatControle.setIdFormatDuProfilControle(formatProfil
                  .getFileFormat());

            // Trace applicative
            LOGGER
                  .debug(
                        "{} - Identifiant du format du profil de contrôle de format à appliquer : {}",
                        prefixeTrc, resultatControle
                              .getIdFormatDuProfilControle());

            // Détermine si l'objet document s'appuie sur un chemin de fichier
            // ou un flux
            boolean isCheminFichier = isControleSurCheminFichier(saeDocument,
                  resultatControle);

            // Identification si demandée
            if (formatProfil.isFormatIdentification()) {

               // Enrichissement de l'objet résultat de la méthode
               resultatControle.setIdentificationActivee(Boolean.TRUE);

               // Trace applicative
               LOGGER
                     .debug(
                           "{} - L'identification est activée sur le profil de contrôle de format",
                           prefixeTrc);

               // Appel de la sous-méthode qui réalise l'identification
               applyIdentification(contexte, saeDocument, formatProfil,
                     isCheminFichier, resultatControle);

            } else {
               LOGGER
                     .debug(
                           "{} - L'identification n'est pas activée sur le profil de contrôle de format",
                           prefixeTrc);
            }

            // Validation si demandée
            if (formatProfil.isFormatValidation()) {

               // Enrichissement de l'objet résultat de la méthode
               resultatControle.setValidationActivee(Boolean.TRUE);

               // Trace applicative
               LOGGER
                     .debug(
                           "{} - La validation est activée sur le profil de contrôle de format",
                           prefixeTrc);

               // Appel de la sous-méthode qui réalise la validation
               applyValidation(contexte, saeDocument, formatProfil,
                     isCheminFichier, resultatControle);

            } else {
               LOGGER
                     .debug(
                           "{} - La validation n'est pas activée sur le profil de contrôle de format",
                           prefixeTrc);
            }

         }

      } else {
         LOGGER
               .debug(
                     "{} - La liste de profils de contrôle de format à appliquer est vide, donc aucune vérification de format à effectuer",
                     prefixeTrc);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie l'objet résultat
      return resultatControle;

   }

   private void applyIdentification(String contexte, SAEDocument saeDocument,
         FormatProfil formatProfil, boolean isCheminFichier,
         ControleFormatSucces resultatControle) throws UnknownFormatException {

      // Traces debug - entrée méthode
      String prefixeTrc = "applyIdentification()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      try {

         // il faut tester les valeurs renvoyées par
         // getFilePath et getContent de SAEDocument
         // pour savoir si c'est un fichier ou un flux
         // afin d'appeller la méthode d'identification

         // appel a identifyFile sinon identifyStream
         IdentificationResult result = null;
         if (isCheminFichier) {

            // On travaille avec un chemin de fichier
            File fichier = new File(saeDocument.getFilePath());
            result = identificationService.identifyFile(formatProfil
                  .getFileFormat(), fichier);
            resultatControle.setIdentificationRealisee(Boolean.TRUE);

         } else {

            // On travaille avec un flux
            InputStream inputStream = saeDocument.getContent().getInputStream();
            result = identificationService.identifyStream(formatProfil
                  .getFileFormat(), inputStream, saeDocument.getFileName());
            resultatControle.setIdentificationRealisee(Boolean.TRUE);

         }

         // si isIdentified alors le fichier est correctement identifié.
         // si l'identification échoue, alors exception levée.
         if (result.isIdentified()) {

            // L'identification a réussi
            LOGGER.debug("{} - L'identification a réussi", prefixeTrc);

         } else {

            // L'identification a échoué
            LOGGER.debug("{} - L'identification a échoué", prefixeTrc);

            // Si on est en mode strict => levée d'une exception pour
            // interrompre la capture
            String validationMode = formatProfil.getFormatValidationMode();
            if (EnumValidationMode.STRICT.toString().equalsIgnoreCase(
                  validationMode)) {
               // avant de levée l'exception, on trace dans le registre
               // de surveillance technique
               String idTraitement = (String) MDC.get("log_contexte_uuid");
               tracesSupport.traceErreurIdentFormatFichier(contexte,
                     formatProfil.getFileFormat(), result.getIdFormatReconnu(),
                     idTraitement);
               throw new UnknownFormatException(MESSAGE_EXCEPTION);
            } else {
               LOGGER
                     .debug(
                           "{} - L'identification a échoué en mode Monitor, on ne lève pas d'exception",
                           prefixeTrc);
               resultatControle.setIdentificationEchecMonitor(Boolean.TRUE);
               resultatControle.setIdFormatReconnu(result.getIdFormatReconnu());
            }

         }

      } catch (IdentifierInitialisationException except) {
         if (EnumValidationMode.MONITOR.toString().equalsIgnoreCase(
               formatProfil.getFormatValidationMode())) {
            LOGGER.info("Erreur lors de l'identification", except);
            resultatControle.setIdentificationEchecMonitor(Boolean.TRUE);
            resultatControle.setIdFormatReconnu("NON_RECONNU");
         } else {
            throw new FormatIdentificationRuntimeException(except);
         }
      } catch (IOException except) {
         throw new FormatIdentificationRuntimeException(except);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

   }

   private void applyValidation(String contexte, SAEDocument saeDocument,
         FormatProfil formatProfil, boolean isCheminFichier,
         ControleFormatSucces resultatControle)
         throws ValidationExceptionInvalidFile, UnknownFormatException {

      // Traces debug - entrée méthode
      String prefixeTrc = "applyValidation()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // il faut tester les valeurs renvoyées par
      // getFilePath et getContent de SAEDocument
      // pour savoir si c'est un fichier ou un flux
      // afin d'appeller la méthode d'identification

      // appel a identifyFile sinon identifyStream
      ValidationResult result = null;
      if (isCheminFichier) {

         // On travaille avec un chemin de fichier
         File fichier = new File(saeDocument.getFilePath());
         try {
            result = validationService.validateFile(formatProfil
                  .getFileFormat(), fichier);
         } catch (ValidatorInitialisationException e) {
            throw new ValidationExceptionInvalidFile(e);
         } catch (ValidatorUnhandledException e) {
            throw new ValidationExceptionInvalidFile(e);
         }
         resultatControle.setValidationRealisee(Boolean.TRUE);

      } else {

         // On travaille avec un flux
         InputStream inputStream;
         try {
            inputStream = saeDocument.getContent().getInputStream();
         } catch (IOException e) {
            throw new ValidationExceptionInvalidFile(e);
         }
         try {
            result = validationService.validateStream(formatProfil
                  .getFileFormat(), inputStream);
         } catch (ValidatorInitialisationException e) {
            throw new ValidationExceptionInvalidFile(e);
         } catch (IOException e) {
            throw new ValidationExceptionInvalidFile(e);
         } catch (ValidatorUnhandledException e) {
            throw new ValidationExceptionInvalidFile(e);
         }
         resultatControle.setValidationRealisee(Boolean.TRUE);
      }

      // si isValid alors le fichier est jugé conforme.
      if (result.isValid()) {

         // La validation a réussi
         LOGGER.debug("{} - La validation a réussi", prefixeTrc);

      } else {

         // La validation a échoué
         LOGGER.debug("{} - La validation a échoué", prefixeTrc);

         // Si on est en mode strict => levée d'une exception pour
         // interrompre la capture
         String validationMode = formatProfil.getFormatValidationMode();
         if (EnumValidationMode.STRICT.toString().equalsIgnoreCase(
               validationMode)) {
            // avant de levée l'exception, on trace dans le registre
            // de surveillance technique
            String idTraitement = (String) MDC.get("log_contexte_uuid");

            tracesSupport.traceErreurValidFormatFichier(contexte, formatProfil
                  .getFileFormat(), formatDetails(result), idTraitement);
            throw new UnknownFormatException(MESSAGE_EXCEPTION);
         } else {
            LOGGER
                  .debug(
                        "{} - La validation a échoué en mode Monitor, on ne lève pas d'exception",
                        prefixeTrc);
            resultatControle.setValidationEchecMonitor(Boolean.TRUE);
            resultatControle.setDetailEchecValidation(formatDetails(result));
         }

      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

   }

   /**
    * Méthode permettant de formatter le détail de la validation.
    * 
    * @param result
    *           résultat de la validation
    * @return String
    */
   private String formatDetails(ValidationResult result) {
      StringBuffer buffer = new StringBuffer();
      for (String detail : result.getDetails()) {
         buffer.append(detail);
         buffer.append('\n');
      }
      return buffer.toString();
   }

   /**
    * Selection du profil à partir de l'ensemble des profils de contrôle dont le
    * "formatCode" correspond à la valeur de la métadonnée "FormatFichier". *
    * 
    * @param formatFichier
    *           le format du fichier
    * @param controlProfilSet
    *           liste des profils de controle contenu dans le VIContenuExtrait
    * @return
    */
   private FormatControlProfil selectProfil(String formatFichier,
         List<FormatControlProfil> controlProfilSet) {

      FormatControlProfil formatControlProfil = getFormatControlProfil(
            formatFichier, controlProfilSet);
      return formatControlProfil;

   }

   private FormatControlProfil getFormatControlProfil(String formatCode,
         List<FormatControlProfil> controlProfilSet) {

      String prefixeTrc = "getFormatControlProfil()";

      // Initialise la liste des profils trouvés. Cas normal : 0 ou 1 profil
      List<FormatControlProfil> listeProfilsTrouves = new ArrayList<FormatControlProfil>();

      // Parcourt la liste des profils et recherche les profils avec un
      // identifiant de format correspondant
      for (FormatControlProfil formatControlProfil : controlProfilSet) {
         if (formatControlProfil.getControlProfil() != null
               && StringUtils.equalsIgnoreCase(formatControlProfil
                     .getControlProfil().getFileFormat(), formatCode)) {
            listeProfilsTrouves.add(formatControlProfil);
         }
      }

      // Fait le point sur le résultat de la recherche du profil
      FormatControlProfil formatControlProf;
      if (CollectionUtils.isEmpty(listeProfilsTrouves)) {

         // 0 profil trouvé
         formatControlProf = null;

      } else if (listeProfilsTrouves.size() > 1) {

         // + de 1 profil trouvé => Erreur de paramétrage probablement. Cas
         // improbable
         // On ne peut pas savoir lequel il faut utiliser => levée d'une
         // exception runtime
         LOGGER
               .debug(
                     "{} - Plusieurs profils de contrôle ({}) peuvent s'appliquer : on lève une exception car on ne sait pas lequel choisir",
                     prefixeTrc, listeProfilsTrouves.size());
         throw new FormatRuntimeException(
               String
                     .format(
                           "Erreur technique : Plusieurs profils de contrôle (%d) peuvent s'appliquer au format de fichier (%s) : on ne sait pas lequel choisir.",
                           listeProfilsTrouves.size(), formatCode));

      } else {

         // 1 seul profil trouvé
         formatControlProf = listeProfilsTrouves.get(0);
      }

      // Renvoie du profil de contrôle trouvé
      return formatControlProf;

   }

   private String findMetadataValue(String metaName,
         List<SAEMetadata> listSaeMetadata) {

      String valeur = null;

      if (CollectionUtils.isNotEmpty(listSaeMetadata)) {
         boolean trouve = false;
         int index = 0;
         do { // récupération de la valeur de la métadonnéé "FormatFichier"
            SAEMetadata saeMetada = listSaeMetadata.get(index);
            if (StringUtils.equalsIgnoreCase(saeMetada.getLongCode(), metaName)) {
               trouve = true;
               valeur = (String) saeMetada.getValue();
            }
            index++;
         } while (!trouve && index < listSaeMetadata.size());
      }

      return valeur;
   }

   private boolean isControleSurCheminFichier(SAEDocument saeDocument,
         ControleFormatSucces resultatControle) {

      String prefixeTrc = "isControleSurCheminFichier()";

      boolean result;

      if (StringUtils.isNotBlank(saeDocument.getFilePath())) {

         // Chemin de fichier

         LOGGER
               .debug(
                     "{} - Le ou les contrôles vont s'appuyer sur un fichier physique : {}",
                     prefixeTrc, saeDocument.getFilePath());

         result = true;

      } else if (saeDocument.getContent() != null) {

         // Flux

         LOGGER.debug("{} - Le ou les contrôles vont s'appuyer sur un flux",
               prefixeTrc);

         result = false;

      } else {

         // On n'est ni sur un chemin de fichier, ni sur un flux
         // => Erreur technique improbable : levée d'une RuntimeException
         throw new FormatIdentificationRuntimeException(
               "Erreur technique : dans l'objet SAEDocument, ni le chemin du fichier ni le flux ne sont renseignés.");

      }
      resultatControle.setSurFlux(!result);
      return result;
   }

}
