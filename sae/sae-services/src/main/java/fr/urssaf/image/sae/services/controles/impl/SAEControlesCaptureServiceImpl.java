package fr.urssaf.image.sae.services.controles.impl;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.controles.SaeControleMetadataService;
import fr.urssaf.image.sae.services.controles.model.ControleFormatSucces;
import fr.urssaf.image.sae.services.controles.support.SAEControlesCaptureFormatSupport;
import fr.urssaf.image.sae.services.enrichment.dao.impl.SAEMetatadaFinderUtils;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceRuntimeException;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.services.util.WriteUtils;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;

/**
 *Classe de contrôle pour la capture unitaire et la capture en masse.
 * 
 * */
@Service
@Qualifier("saeControlesCaptureService")
public class SAEControlesCaptureServiceImpl implements
      SAEControlesCaptureService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAEControlesCaptureServiceImpl.class);

   private static final List<String> ALGO_HASH = Arrays.asList("SHA-1");

   @Autowired
   private EcdeServices ecdeServices;

   @Autowired
   private SaeControleMetadataService controleService;

   @Autowired
   private SAEControlesCaptureFormatSupport controleFormatSupport;

   private static final String LOG_DEBUT = "{} - début";
   private static final String LOG_FIN = "{} - fin";
   private static final String LOG_FIN_VERIF = "{} - Fin de la vérification : ";
   private static final String LOG_DEBUT_VERIF = "{} - Début de la vérification : ";
   private static final String LOG_HASH = "Equivalence entre le hash fourni en métadonnée et le hash recalculé à partir du fichier";
   private static final String CAPTURE_HASH_ERREUR = "capture.hash.erreur";
   private static final String CAPTURE_URL_ECDE_ERREUR = "capture.url.ecde.incorrecte";

   /**
    * {@inheritDoc}
    */
   public final void checkSaeMetadataForCapture(SAEDocument saeDocument)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkSaeMetadataForCapture()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      controleService.checkSaeMetadataForCapture(saeDocument.getMetadatas());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkSaeMetadataForStorage(SAEDocument sAEDocument)
         throws RequiredStorageMetadataEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkSaeMetadataForCapture()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      List<SAEMetadata> metadatas = controleService.checkMetadataForStorage(sAEDocument.getMetadatas());
      sAEDocument.setMetadatas(metadatas);
      
      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkHashCodeMetadataForStorage(SAEDocument saeDocument)
         throws UnknownHashCodeEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkHashCodeMetadataForStorage()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      // Fin des traces debug - entrée méthode
      String hashCodeValue = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeDocument.getMetadatas(), SAEArchivalMetadatas.HASH_CODE
                  .getLongCode());
      LOGGER.debug("{} - Hash du document à archiver: {}", prefixeTrc,
            hashCodeValue);
      String algoHashCode = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeDocument.getMetadatas(), SAEArchivalMetadatas.TYPE_HASH
                  .getLongCode());
      LOGGER.debug("{} - Algorithme du document à archiver: {}", prefixeTrc,
            algoHashCode);
      // FIXME vérifier que l'algorithme passer fait partie d'une liste
      // pré-définit.

      InputStream content = null;
      String fileName = getFileName(saeDocument);

      try {
         content = getInputStream(saeDocument);

         checkHashCode(algoHashCode, fileName);

         // FIXME à partir de l'algorithme calculer le hashCode.
         checkHashValue(content, hashCodeValue, fileName, algoHashCode);

      } finally {
         close(content, fileName);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkHashCodeMetadataListForStorage(
         List<SAEMetadata> saeMetadatas, String refHash)
         throws UnknownHashCodeEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkHashCodeMetadataListForStorage()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      // Fin des traces debug - entrée méthode

      String fileName = SAEMetatadaFinderUtils.codeMetadataFinder(saeMetadatas,
            SAEArchivalMetadatas.NOM_FICHIER.getLongCode());

      String hashCodeValue = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeMetadatas, SAEArchivalMetadatas.HASH_CODE.getLongCode());
      LOGGER.debug("{} - Hash du document à archiver: {}", prefixeTrc,
            hashCodeValue);

      String algoHashCode = SAEMetatadaFinderUtils.codeMetadataFinder(
            saeMetadatas, SAEArchivalMetadatas.TYPE_HASH.getLongCode());
      LOGGER.debug("{} - Algorithme du document à archiver: {}", prefixeTrc,
            algoHashCode);

      // File docFile = new File(saeDocument.getFilePath());
      checkHashCode(algoHashCode, fileName);

      LOGGER.debug(LOG_DEBUT_VERIF + LOG_HASH, prefixeTrc);
      if (!StringUtils.equalsIgnoreCase(refHash, hashCodeValue.trim())) {
         LOGGER.debug(
               "{} - Hash du document {} est différent que celui recalculé {}",
               new Object[] { prefixeTrc, refHash, hashCodeValue });
         throw new UnknownHashCodeEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_HASH_ERREUR, fileName));
      }
      LOGGER.debug(LOG_FIN_VERIF + LOG_HASH, prefixeTrc);

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkUntypedDocument(UntypedDocument untypedDocument)
         throws EmptyDocumentEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkUntypedDocument()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      // Fin des traces debug - entrée méthode
      File docFile = new File(untypedDocument.getFilePath());
      LOGGER
            .debug(
                  LOG_DEBUT_VERIF
                        + "La taille du document fournie par l'application cliente est supérieure à 0 octet",
                  prefixeTrc);
      if (docFile.exists() && (docFile.length() == 0)) {

         LOGGER.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "capture.fichier.vide", docFile.getName()));
         throw new EmptyDocumentEx(ResourceMessagesUtils.loadMessage(
               "capture.fichier.vide", docFile.getName()));

      }
      LOGGER
            .debug(
                  LOG_FIN_VERIF
                        + "La taille du document fournie par l'application cliente est supérieure à 0 octet",
                  prefixeTrc);
      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkUntypedMetadata(UntypedDocument untypedDocument)
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx,
         MetadataValueNotInDictionaryEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "checkUntypedDocument()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      // Fin des traces debug - entrée méthode

      controleService.checkUntypedMetadatas(untypedDocument.getUMetadatas());
      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   @Override
   public final void checkBulkCaptureEcdeUrl(String urlEcde)
         throws CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         CaptureEcdeWriteFileEx {
      boolean ecdePermission = true;
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "checkBulkCaptureEcdeUrl()";
         LOGGER.debug(LOG_DEBUT, prefixeTrc);
         LOGGER.debug("{} - Début des vérifications sur "
               + "l'URL ECDE envoyée au service de capture de masse",
               prefixeTrc);
         // Fin des traces debug - entrée méthode

         File fileEcde = ecdeServices
               .convertSommaireToFile(convertToEcdeUri(urlEcde));
         checkExistingEcdeFile(fileEcde, urlEcde);
         LOGGER.debug(
               "{} - Début de la vérification sur les droits d'écriture "
                     + "du SAE dans le répertoire de traitement ECDE",
               prefixeTrc);

         File parentFile = fileEcde.getParentFile();
         // Dans le cas du système d'exploitation Windows
         // "parentFile.canWrite()" ne fonctionne pas, d'où le faite qu'il y a
         // deux implementations pour vérifier les permissions ECDE.
         // Implementation autre que Windows
         if (!parentFile.canWrite()) {
            ecdePermission = false;
         }

         // Implementation pour windows
         if (ecdePermission) {
            try {
               String uuid = UUID.randomUUID().toString();

               File tmpfile = new File(parentFile, "bulkFlagPermission_" + uuid
                     + ".tmp");
               WriteUtils.writeFile(tmpfile, null, null);

               if (tmpfile.isFile() && tmpfile.exists()) {
                  tmpfile.delete();
               } else {
                  ecdePermission = false;
               }

            } catch (Exception e) {
               ecdePermission = false;
            }
         }

         if (!ecdePermission) {
            throw new CaptureEcdeWriteFileEx(ResourceMessagesUtils.loadMessage(
                  "capture.ecde.droit.ecriture", urlEcde));
         }
         LOGGER.debug("{} - Le répertoire de traitement ECDE est {}",
               prefixeTrc, parentFile.getAbsoluteFile());
         LOGGER.debug("{} - Fin de la vérification sur les droits d'écriture "
               + "du SAE dans le répertoire de traitement ECDE  ", prefixeTrc);

         LOGGER
               .debug(
                     "{} - Fin des vérifications sur l'URL ECDE envoyée au service de capture de masse",
                     prefixeTrc);
         // Traces debug - sortie méthode
         LOGGER.debug(LOG_FIN, prefixeTrc);
         // Fin des traces debug - sortie méthode
      } catch (EcdeBadURLException except) {
         throw new CaptureBadEcdeUrlEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_URL_ECDE_ERREUR, urlEcde), except);
      } catch (EcdeBadURLFormatException except) {
         throw new CaptureBadEcdeUrlEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_URL_ECDE_ERREUR, urlEcde), except);
      }
   }

   @Override
   public final void checkCaptureEcdeUrl(String urlEcde)
         throws CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkCaptureEcdeUrl()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      // Fin des traces debug - entrée méthode
      try {
         LOGGER
               .debug(
                     "{} - Début des vérifications sur l'URL ECDE envoyée au service de capture unitaire",
                     prefixeTrc);
         File fileEcde = ecdeServices
               .convertURIToFile(convertToEcdeUri(urlEcde));
         checkExistingEcdeFile(fileEcde, urlEcde);
         LOGGER
               .debug(
                     "{} - Fin des vérifications sur l'URL ECDE envoyée au service de capture unitaire",
                     prefixeTrc);
      } catch (EcdeBadURLException badUrlEx) {
         throw new CaptureBadEcdeUrlEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_URL_ECDE_ERREUR, urlEcde), badUrlEx);
      } catch (EcdeBadURLFormatException e) {
         throw new CaptureBadEcdeUrlEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_URL_ECDE_ERREUR, urlEcde), e);
      }
      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * Convertit l'URL ECDE vers une URI.
    * 
    * @param url
    *           : url de l'ECDE.
    * @throws CaptureBadEcdeUrlEx
    *            si l'URL ECDE n'est pas incorrecte.
    */
   private URI convertToEcdeUri(String url) throws CaptureBadEcdeUrlEx {
      try {
         return new URI(url);
      } catch (URISyntaxException except) {
         throw new CaptureBadEcdeUrlEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_URL_ECDE_ERREUR, url), except);
      }
   }

   /**
    * Vérifie l'existance du fichier à archiver ou le sommaire.xml dans l'ECDE.
    * 
    * @param ecdeFile
    *           : Fichier à archiver pour le cas de la capture unitaire ou le
    *           sommaire.xml pour le cas de la capture en masse.
    * @param urlEcde
    *           : url de l'ECDE.
    * @throws CaptureEcdeUrlFileNotFoundEx
    *            si le fichier à archiver ou le sommaire.xml n'est pas présent.
    */
   private void checkExistingEcdeFile(File ecdeFile, String urlEcde)
         throws CaptureEcdeUrlFileNotFoundEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkExistingEcdeFile()";
      // Fin des traces debug - entrée méthode
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      LOGGER
            .debug(
                  "{} - Début de la vérification sur l'existence du fichier pointé par l'URL ECDE ({})",
                  prefixeTrc, ecdeFile.getAbsoluteFile());
      if (!(ecdeFile.isFile() && ecdeFile.exists())) {
         LOGGER.debug("{} - {} ", prefixeTrc, ResourceMessagesUtils
               .loadMessage("capture.url.ecde.fichier.introuvable", urlEcde));
         throw new CaptureEcdeUrlFileNotFoundEx(ResourceMessagesUtils
               .loadMessage("capture.url.ecde.fichier.introuvable", urlEcde));
      }
      LOGGER
            .debug(
                  "{} - Fin de la vérification sur l'existence du fichier pointé par l'URL ECDE ({})",
                  prefixeTrc, ecdeFile.getAbsoluteFile());
      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkUntypedBinaryDocument(UntypedDocument untypedDocument)
         throws EmptyDocumentEx, EmptyFileNameEx {

      DataHandler content = untypedDocument.getContent();
      String fileName = untypedDocument.getFileName();

      checkBinaryContent(content);
      checkBinaryFileName(fileName);
   }

   /**
    * Permet de vérifier si le contenu du fichier n'est pas null
    * 
    * @param content
    *           contenu du fichier
    * 
    * @throws EmptyDocumentEx
    *            erreur levée lorsquele document est vide
    */
   public final void checkBinaryContent(DataHandler content)
         throws EmptyDocumentEx {
      String trcPrefix = "checkBinaryContent()";
      boolean isOk = true;
      InputStream stream = null;

      try {
         stream = content.getInputStream();
         if (stream.read() == -1) {
            isOk = false;
         }

      } catch (IOException exception) {
         LOGGER.warn("{} - Erreur de lecture du flux", trcPrefix, exception);
         isOk = false;

      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOGGER.warn("{} - Impossible de fermer le flux", trcPrefix);
            }
         }
      }

      if (!isOk) {
         throw new EmptyDocumentEx(ResourceMessagesUtils
               .loadMessage("capture.fichier.binaire.vide"));
      }
   }

   /**
    * Permet de vérifier que le nom de fichier est bien renseigné.
    * 
    * @param fileName
    *           nom du fichier
    * 
    * @throws EmptyFileNameEx
    *            erreur levée lorsque le nom de fichier est vide
    */
   public final void checkBinaryFileName(String fileName)
         throws EmptyFileNameEx {

      if (StringUtils.isBlank(fileName)) {
         throw new EmptyFileNameEx(ResourceMessagesUtils
               .loadMessage("nomfichier.vide"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkSaeMetadataListForCapture(List<SAEMetadata> metadatas)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "checkSaeMetadataForCapture()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      controleService.checkSaeMetadataForCapture(metadatas);

      // Traces debug - sortie méthode
      LOGGER.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ControleFormatSucces checkFormat(String contexte,
         SAEDocument saeDocument, List<FormatControlProfil> controlProfilSet)
         throws UnknownFormatException, ValidationExceptionInvalidFile {

      // Traces debug - entrée méthode
      String prefixeTrc = "checkFormat()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // Appel du support
      ControleFormatSucces retour = controleFormatSupport.checkFormat(contexte,
            saeDocument, controlProfilSet);

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);
      return retour;

   }

   private void close(Closeable closeable, String name) {
      String trcPrefixe = "close()";

      if (closeable != null) {
         try {
            closeable.close();

         } catch (IOException e) {
            LOGGER.info("{} - Impossible de fermer le flux {}", new Object[] {
                  trcPrefixe, name });
         }
      }
   }

   /**
    * Récupère le stream du document
    * 
    * @param saeDocument
    *           le document SAE
    * @return le stream du contenu
    */
   private InputStream getInputStream(SAEDocument saeDocument) {
      InputStream content;

      try {
         if (saeDocument.getFilePath() == null) {
            content = saeDocument.getContent().getInputStream();

         } else {
            File docFile = new File(saeDocument.getFilePath());
            content = new FileInputStream(docFile);
         }

      } catch (IOException e) {
         throw new SAECaptureServiceRuntimeException(e);
      }

      return content;
   }

   /**
    * @param saeDocument
    *           le document SAE
    * @return le nom du fichier
    */
   private String getFileName(SAEDocument saeDocument) {
      String fileName = null;
      if (saeDocument.getFilePath() == null) {
         fileName = saeDocument.getFileName();
      } else {
         fileName = FilenameUtils.getBaseName(saeDocument.getFilePath());
      }

      return fileName;
   }

   private void checkHashCode(String algoHashCode, String fileName)
         throws UnknownHashCodeEx {
      String prefixeTrc = "checkHashCode()";

      LOGGER.debug("{} - Début de la vérification : Le type de hash est SHA-1",
            prefixeTrc);

      if (!ALGO_HASH.contains(algoHashCode)) {
         LOGGER
               .debug(
                     "{} - L'algorithme du document à archiver est différent de SHA-1",
                     prefixeTrc);
         throw new UnknownHashCodeEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_HASH_ERREUR, fileName));
      }
      LOGGER.debug(LOG_FIN_VERIF + "Le type de hash est SHA-1", prefixeTrc);
   }

   /**
    * Vérifie la valeur du hash par rapport à un hash calculé en direct
    * 
    * @param content
    *           le stream du contenu
    * @param hashCodeValue
    *           la valeur de référence
    * @param fileName
    *           le nom du fichier
    * @param algoHash
    *           algo de hash
    * @throws UnknownHashCodeEx
    *            erreur levée si le code calculé est différent de celui de
    *            référence
    */
   private void checkHashValue(InputStream content, String hashCodeValue,
         String fileName, String algoHash) throws UnknownHashCodeEx {
      String prefixeTrc = "checkHashValue()";

      LOGGER.debug(LOG_DEBUT_VERIF + LOG_HASH, prefixeTrc);
      String hashCalculated = null;

      try {
         hashCalculated = HashUtils.hashHex(content, algoHash);

      } catch (IOException exception) {
         throw new SAECaptureServiceRuntimeException(exception);

      } catch (NoSuchAlgorithmException exception) {
         throw new SAECaptureServiceRuntimeException(exception);
      }

      if (!StringUtils.equalsIgnoreCase(hashCalculated, hashCodeValue.trim())) {
         LOGGER.debug(
               "{} - Hash du document {} est différent que celui recalculé {}",
               new Object[] { prefixeTrc, hashCodeValue, hashCalculated });
         throw new UnknownHashCodeEx(ResourceMessagesUtils.loadMessage(
               CAPTURE_HASH_ERREUR, fileName));
      }
      LOGGER.debug(LOG_FIN_VERIF + LOG_HASH, prefixeTrc);
   }

}
