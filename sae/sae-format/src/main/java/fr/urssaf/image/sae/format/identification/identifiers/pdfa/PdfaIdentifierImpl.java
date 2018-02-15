package fr.urssaf.image.sae.format.identification.identifiers.pdfa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.commons.droid.service.impl.FormatIdentificationServiceImpl;
import fr.urssaf.image.sae.format.format.compatible.model.SaeFormatCompatible;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.identifiers.Identifier;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.model.EtapeEtResultat;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Implémentation des appels à l'outil d'identification pour les PDF/A-1b
 * 
 */
@Service
public final class PdfaIdentifierImpl implements Identifier {

   public static final String PUUID = "PUUID : ";
   public static final String PUUID_EGAL_IDFORMAT = "PUUID = IDFORMAT.";

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(PdfaIdentifierImpl.class);

   /**
    * Objet utilisé pour permettre de recupérer le PUUID correspondant à
    * l'idFormat
    */
   @Autowired
   private FormatIdentificationServiceImpl formatIdentificationService;

   /**
    * pour charger les différents formats compatibles.
    */
   @Autowired
   private SaeFormatCompatible saeFormatCompatible;

   /**
    * {@inheritDoc}
    */
   @Override
   public IdentificationResult identifyFile(String idFormat, File fichier)
         throws IOException {

      // Traces debug - entrée méthode
      String prefixeTrc = "identifyFile()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      LOGGER
            .debug(
                  "{} - Demande d'identification du fichier \"{}\" par rapport à l'identifiant de format {}",
                  new Object[] { prefixeTrc, fichier.getAbsolutePath(),
                        idFormat });

      // Contrôle de idFormat => seule la valeur fmt/354 est autorisée
      checkIdFormat(idFormat);

      // Appel de la sous-méthode qui ne travaille que sur le fmt/354
      IdentificationResult result = identifyFile(fichier);
      LOGGER.debug("{} - Résultat de l'identification: {}", prefixeTrc, result
            .isIdentified());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie le résultat de l'identification
      return result;

   }

   private IdentificationResult identifyFile(File fichier) throws IOException {

      String prefixeTrc = "identifyFile()";

      // Initialisation des résultats de l'analyse
      IdentificationResult identificationResult = new IdentificationResult();
      List<EtapeEtResultat> listeEtapeResult = new ArrayList<EtapeEtResultat>();
      identificationResult.setDetails(listeEtapeResult);

      // Etape 1: Appel de DROID pour identifier le fichier
      String puuid;
      try {
         puuid = formatIdentificationService.identifie(fichier);
      } catch (RuntimeException ex) {
         throw new IdentificationRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.outil.identification"), ex);
      }
      LOGGER.debug("{} - Identifiant PRONOM détecté par DROID : {}",
            prefixeTrc, puuid);
      listeEtapeResult.add(new EtapeEtResultat(SaeFormatMessageHandler
            .getMessage("identify.file.etape1"), String.format("%s%s", PUUID,
            puuid)));

      // On poursuit l'algo uniquement si Droid a réussi à identifier le fichier
      if (StringUtils.isBlank(puuid)) {

         // Droid n'a pas réussi à identifier le fichier => l'identification
         // n'est pas valide
         LOGGER.debug("{} - Droid n'a pas réussi à identifier le fichier",
               prefixeTrc);
         identificationResult.setIdentified(Boolean.FALSE);

      } else {

         // Etape 2: Comparaison du résultat de DROID avec "fmt/354"
         if (StringUtils.equalsIgnoreCase(Constantes.FMT_354, puuid)) {

            // On a une égalité entre le format détecté par DROID et le format
            // en entrée de la méthode
            LOGGER
                  .debug(
                        "{} - L'identifiant PRONOM détecté par DROID ({}) correspond directement à {}",
                        new Object[] { prefixeTrc, puuid, Constantes.FMT_354 });
            listeEtapeResult.add(new EtapeEtResultat(SaeFormatMessageHandler
                  .getMessage("identify.file.etape2"), PUUID_EGAL_IDFORMAT));

            // Résultat de l'identification => OK
            identificationResult.setIdentified(Boolean.TRUE);
            identificationResult.setIdFormatReconnu(puuid);

         } else {

            // Le format détecté par DROID est différent du format d'entrée de
            // la méthode. On va regarder s'ils sont compatibles.
            LOGGER
                  .debug(
                        "{} - L'identifiant PRONOM détecté par DROID ({}) n'est pas identique à {}. On cherche dans les formats compatibles.",
                        new Object[] { prefixeTrc, puuid, Constantes.FMT_354 });

            // Récupération de tous les types compatibles à partir de l'idFormat
            List<String> compatibles = saeFormatCompatible
                  .getFormatsCompatibles(Constantes.FMT_354);

            // Contrôle technique: vérifie que la liste des formats compatibles
            // n'est pas vide
            if (CollectionUtils.isEmpty(compatibles)) {

               // Erreur technique, on est censé avoir paramétré des formats
               // compatibles

               LOGGER
                     .debug(
                           "{} - La liste des identifiants de formats compatibles est vide",
                           prefixeTrc);

               throw new IdentificationRuntimeException(
                     "Erreur technique: la liste des formats compatibles au fmt/354 est vide.");

            } else {

               // Il y a une liste de formats compatibles

               LOGGER.debug(
                     "{} - Liste des identifiants de format compatibles : {}",
                     prefixeTrc, compatibles);

               // Recherche du format détecté par Droid dans la liste des
               // formats
               // comptabiles
               boolean compatible = compatibles.contains(puuid);
               if (compatible) {

                  // Le format détecté par Droid est compatible

                  LOGGER
                        .debug(
                              "{} - L'identifiant de format détecté par DROID ({}) fait partie de la liste des identifiants compatibles avec {}",
                              new Object[] { prefixeTrc, puuid,
                                    Constantes.FMT_354 });

                  listeEtapeResult
                        .add(new EtapeEtResultat(
                              SaeFormatMessageHandler
                                    .getMessage("identify.file.etape2"),
                              SaeFormatMessageHandler
                                    .getMessage("identify.file.puuid.diff.id.format.mais.compatible")));

                  identificationResult.setIdentified(Boolean.TRUE);
                  identificationResult.setIdFormatReconnu(puuid);

               } else {

                  // Le format détecté par Droid n'est pas compatible

                  LOGGER
                        .debug(
                              "{} - L'identifiant de format détecté par DROID ({}) ne fait pas partie de la liste des identifiants compatibles avec {}",
                              new Object[] { prefixeTrc, puuid,
                                    Constantes.FMT_354 });

                  listeEtapeResult
                        .add(new EtapeEtResultat(
                              SaeFormatMessageHandler
                                    .getMessage("identify.file.etape2"),
                              SaeFormatMessageHandler
                                    .getMessage("identify.file.puuid.diff.id.format.non.compatible")));

                  identificationResult.setIdentified(Boolean.FALSE);
                  identificationResult.setIdFormatReconnu(puuid);

               }

            }

         }
      }

      // Renvoie le résultat de l'identification
      return identificationResult;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IdentificationResult identifyStream(String idFormat,
         InputStream stream, String nomFichier) {

      // TODO commons-droid devrait proposer un service d'identification par
      // Flux

      // Traces debug - entrée méthode
      String prefixeTrc = "identifyStream()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      LOGGER
            .debug(
                  "{} - Demande d'identification d'un flux avec le nom de fichier \"{}\" par rapport à l'identifiant de format {}",
                  new Object[] { prefixeTrc, nomFichier, idFormat });

      // Contrôle de idFormat => seule la valeur fmt/354 est autorisée
      checkIdFormat(idFormat);

      try {

         // Création d'un fichier temporaire dans lequel on écrit le flux
         // Récupère l'extension à partir du nom du fichier passé en paramètre
         String extension = FilenameUtils.getExtension(nomFichier);
         if (StringUtils.isBlank(extension)) {
            extension = "tmp";
         }
         extension = ".".concat(extension);
         // Création d'un fichier temporaire vide avec un nom unique
         File createdFile = File.createTempFile("sae_identifyStream_pdfa_",
               extension);
         // Ecrit le flux à identifié dans le fichier temporaire
         FileUtils.copyInputStreamToFile(stream, createdFile);
         // stream.close();
         // Trace applicative
         LOGGER.debug("{} - Fichier temporaire généré : {}", prefixeTrc,
               createdFile.getAbsolutePath());

         // try/finally pour toujours supprimer le fichier temporaire
         IdentificationResult identificationResult;
         try {

            // Appel de la méthode d'identification par fichier
            identificationResult = identifyFile(idFormat, createdFile);

         } finally {
            // Suppression du fichier temporaire
            LOGGER.debug("{} - Suppression du fichier temporaire {}",
                  prefixeTrc, createdFile.getAbsolutePath());
            FileUtils.forceDelete(createdFile);
         }

         // Traces debug - sortie méthode
         LOGGER.debug(LOG_FIN, prefixeTrc);

         // Renvoie le résultat de l'identification
         return identificationResult;

      } catch (IOException except) {
         throw new IdentificationRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.outil.identification"), except);
      }
   }

   private void checkIdFormat(String idFormat) {

      if (!StringUtils.equalsIgnoreCase(idFormat, "fmt/354")) {
         throw new IdentificationRuntimeException(
               String
                     .format(
                           "Erreur technique: Le bean d'identification des PDF/A 1b (fmt/354) a été sollicité pour identifier un autre format (%s)",
                           idFormat));
      }

   }

}
