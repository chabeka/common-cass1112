package fr.urssaf.image.sae.webservices.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.EcdeUrlType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.service.WSCaptureService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectArchivageUnitaireFactory;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

/**
 * Implémentation de {@link WSCaptureService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public class WSCaptureServiceImpl implements WSCaptureService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSCaptureServiceImpl.class);

   @Autowired
   private SAECaptureService captureService;

   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final ArchivageUnitaireResponse archivageUnitaire(
         ArchivageUnitaire request) throws CaptureAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "archivageUnitaire()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Vérification que la liste des métadonnées n'est pas vide
      ListeMetadonneeType listeMeta = request.getArchivageUnitaire()
            .getMetadonnees();
      verifListeMetaNonVide(listeMeta);

      // Lecture de l'URL ECDE
      URI ecdeURL = URI.create(request.getArchivageUnitaire().getEcdeUrl()
            .getEcdeUrlType().toString());
      LOG.debug("{} - URI ECDE: {}", prefixeTrc, ecdeURL);

      // Conversion de la liste des métadonnées d'un type vers un autre
      List<UntypedMetadata> metadatas = convertListeMeta(listeMeta);

      // Appel de la couche service, et transtypage des exceptions en SoapFault
      UUID uuid = capture(metadatas, ecdeURL);
      LOG.debug("{} - UUID : \"{}\"", prefixeTrc, uuid);

      // Construction de l'objet de réponse
      ArchivageUnitaireResponse response = ObjectArchivageUnitaireFactory
            .createArchivageUnitaireResponse(uuid);
      if (response == null) {
         LOG.debug("{} - Valeur de retour : null", prefixeTrc);
      } else {
         LOG.debug("{} - Valeur de retour : \"{}\"", prefixeTrc, response
               .getArchivageUnitaireResponse().getIdArchive().getUuidType());
      }

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Renvoie l'objet de réponse de la couche web service
      return response;

   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final ArchivageUnitairePJResponse archivageUnitairePJ(
         ArchivageUnitairePJ request) throws CaptureAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "archivageUnitairePJ()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Vérification que la liste des métadonnées n'est pas vide
      ListeMetadonneeType listeMeta = request.getArchivageUnitairePJ()
            .getMetadonnees();
      verifListeMetaNonVide(listeMeta);

      // Conversion de la liste des métadonnées d'un type vers un autre
      List<UntypedMetadata> metadatas = convertListeMeta(listeMeta);

      // Est-ce l'application cliente nous a envoyé une URL ECDE ou
      // un contenu de fichier ?
      UUID uuid;
      EcdeUrlType ecdeUrlType = request.getArchivageUnitairePJ()
            .getArchivageUnitairePJRequestTypeChoice_type0().getEcdeUrl();
      if (ecdeUrlType == null) {

         // On travaille avec un contenu
         LOG.debug("{} - On a reçu un contenu et non pas une URL ECDE",
               prefixeTrc);

         // Lecture du nom du fichier associé au contenu
         String nomFichier = request.getArchivageUnitairePJ()
               .getArchivageUnitairePJRequestTypeChoice_type0().getDataFile()
               .getFileName();
         LOG.debug("{} - Nom fichier: {}", prefixeTrc, nomFichier);

         // Vérifie que le nom du fichier est renseigné
         if (StringUtils.isBlank(nomFichier)) {
            throw new CaptureAxisFault("NomFichierVide",
                  wsMessageRessourcesUtils.recupererMessage("nomfichier.vide",
                        null));
         }

         // Récupération du contenu du document
         DataHandler dataHandler = request.getArchivageUnitairePJ()
               .getArchivageUnitairePJRequestTypeChoice_type0().getDataFile()
               .getFile();
         byte[] content = convertToByteArray(dataHandler);

         // Vérifie que le contenu du document n'est pas vide
         if (content == null || content.length == 0) {
            throw new CaptureAxisFault("CaptureFichierVide",
                  wsMessageRessourcesUtils.recupererMessage(
                        "capture.fichier.binaire.vide", null));
         }

         // Appel de la couche service, et transtypage des exceptions en
         // SoapFault
         uuid = capturePJ(metadatas, nomFichier, content);

      } else {

         // On travaille avec une URL ECDE
         LOG.debug("{} - On a reçu une URL ECDE et non pas un contenu",
               prefixeTrc);

         // Extraction de l'URL ECDE depuis l'objet de la couche web service
         URI ecdeURL = URI.create(ecdeUrlType.getEcdeUrlType().toString());

         // Appel de la couche service, et transtypage des exceptions en
         // SoapFault
         uuid = capture(metadatas, ecdeURL);

      }

      // Log l'UUID
      LOG.debug("{} - UUID : \"{}\"", prefixeTrc, uuid);

      // Construction de l'objet de réponse
      ArchivageUnitairePJResponse response = ObjectArchivageUnitaireFactory
            .createArchivageUnitairePJResponse(uuid);
      if (response == null) {
         LOG.debug("{} - Valeur de retour : null", prefixeTrc);
      } else {
         LOG.debug("{} - Valeur de retour : \"{}\"", prefixeTrc, response
               .getArchivageUnitairePJResponse().getIdArchive().getUuidType());
      }

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Renvoie l'objet de réponse de la couche web service
      return response;

   }

   private void verifListeMetaNonVide(ListeMetadonneeType listeMeta)
         throws CaptureAxisFault {
      String prefixeTrc = "verifListeMetaNonVide()";
      LOG
            .debug(
                  "{} - Début de la vérification : La liste des métadonnées fournies par l'application n'est pas vide",
                  prefixeTrc);
      if (listeMeta.getMetadonnee() == null) {
         LOG.debug("{} - {}", prefixeTrc, wsMessageRessourcesUtils
               .recupererMessage("ws.capture.metadata.is.empty", null));
         throw new CaptureAxisFault("CaptureMetadonneesVide",
               wsMessageRessourcesUtils.recupererMessage(
                     "ws.capture.metadata.is.empty", null));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : La liste des métadonnées fournies par l'application n'est pas vide",
                  prefixeTrc);
   }

   private List<UntypedMetadata> convertListeMeta(ListeMetadonneeType listeMeta) {

      String prefixeTrc = "convertitListeMeta()";

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      for (MetadonneeType metadonnee : listeMeta.getMetadonnee()) {
         metadatas.add(createUntypedMetadata(metadonnee));
      }
      LOG.debug("{} - Liste des métadonnées : \"{}\"", prefixeTrc,
            buildMessageFromList(metadatas));

      return metadatas;

   }

   private UntypedMetadata createUntypedMetadata(MetadonneeType metadonnee) {

      return new UntypedMetadata(metadonnee.getCode().getMetadonneeCodeType(),
            metadonnee.getValeur().getMetadonneeValeurType());
   }

   private UUID capture(List<UntypedMetadata> metadatas, URI ecdeURL)
         throws CaptureAxisFault {
      try {
         return captureService.capture(metadatas, ecdeURL);
      } catch (RequiredStorageMetadataEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture", e.getMessage(), e);

      } catch (InvalidValueTypeAndFormatMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesFormatTypeNonValide", e
               .getMessage(), e);

      } catch (UnknownMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesInconnu",
               e.getMessage(), e);

      } catch (DuplicatedMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesDoublon",
               e.getMessage(), e);

      } catch (NotSpecifiableMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesInterdites", e
               .getMessage(), e);

      } catch (EmptyDocumentEx e) {

         throw new CaptureAxisFault("CaptureFichierVide", e.getMessage(), e);

      } catch (RequiredArchivableMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesArchivageObligatoire", e
               .getMessage(), e);

      } catch (ReferentialRndException e) {

         throw new CaptureAxisFault("ErreurInterne", wsMessageRessourcesUtils
               .recupererMessage("ws.capture.error", null), e);

      } catch (UnknownCodeRndEx e) {

         throw new CaptureAxisFault("CaptureCodeRndInterdit", e.getMessage(), e);

      } catch (UnknownHashCodeEx e) {

         throw new CaptureAxisFault("CaptureHashErreur", e.getMessage(), e);

      } catch (CaptureBadEcdeUrlEx e) {

         throw new CaptureAxisFault("CaptureUrlEcdeIncorrecte", e.getMessage(),
               e);

      } catch (CaptureEcdeUrlFileNotFoundEx e) {

         throw new CaptureAxisFault("CaptureUrlEcdeFichierIntrouvable", e
               .getMessage(), e);

      } catch (SAECaptureServiceEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture",
               wsMessageRessourcesUtils.recupererMessage("ws.capture.error",
                     null), e);

      } catch (NotArchivableMetadataEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture",
               wsMessageRessourcesUtils.recupererMessage("ws.capture.error",
                     null), e);
      } catch (MetadataValueNotInDictionaryEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesValeurNonValide", e
               .getMessage());
      } catch (UnknownFormatException except) {

         throw new CaptureAxisFault("FormatFichierInconnu", except
               .getMessage());
      } catch (ValidationExceptionInvalidFile except) {

         throw new CaptureAxisFault("FormatFichierNonConforme", except
               .getMessage());
      }

   }

   private UUID capturePJ(List<UntypedMetadata> metadatas, String fileName,
         byte[] content) throws CaptureAxisFault {
      try {
         return captureService.captureBinaire(metadatas, content, fileName);
      } catch (RequiredStorageMetadataEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture", e.getMessage(), e);

      } catch (InvalidValueTypeAndFormatMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesFormatTypeNonValide", e
               .getMessage(), e);

      } catch (UnknownMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesInconnu",
               e.getMessage(), e);

      } catch (DuplicatedMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesDoublon",
               e.getMessage(), e);

      } catch (NotSpecifiableMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesInterdites", e
               .getMessage(), e);

      } catch (EmptyDocumentEx e) {

         throw new CaptureAxisFault("CaptureFichierVide", e.getMessage(), e);

      } catch (RequiredArchivableMetadataEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesArchivageObligatoire", e
               .getMessage(), e);

      } catch (ReferentialRndException e) {

         throw new CaptureAxisFault("ErreurInterne", wsMessageRessourcesUtils
               .recupererMessage("ws.capture.error", null), e);

      } catch (UnknownCodeRndEx e) {

         throw new CaptureAxisFault("CaptureCodeRndInterdit", e.getMessage(), e);

      } catch (UnknownHashCodeEx e) {

         throw new CaptureAxisFault("CaptureHashErreur", e.getMessage(), e);

      } catch (SAECaptureServiceEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture",
               wsMessageRessourcesUtils.recupererMessage("ws.capture.error",
                     null), e);

      } catch (NotArchivableMetadataEx e) {

         throw new CaptureAxisFault("ErreurInterneCapture",
               wsMessageRessourcesUtils.recupererMessage("ws.capture.error",
                     null), e);
      } catch (EmptyFileNameEx e) {

         throw new CaptureAxisFault("NomFichierVide", wsMessageRessourcesUtils
               .recupererMessage("ws.capture.error", null), e);
      } catch (MetadataValueNotInDictionaryEx e) {

         throw new CaptureAxisFault("CaptureMetadonneesValeurNonValide", e
               .getMessage());
      } catch (UnknownFormatException except) {

         throw new CaptureAxisFault("FormatFichierInconnu", except
               .getMessage());
      } catch (ValidationExceptionInvalidFile except) {

         throw new CaptureAxisFault("FormatFichierNonConforme", except
               .getMessage());
      }
   }

   /**
    * Construit une chaîne qui comprends l'ensemble des objets à afficher dans
    * les logs. <br>
    * Exemple : "UntypedMetadata[code long:=Titre,value=Attestation],
    * UntypedMetadata[code long:=DateCreation,value=2011-09-01],
    * UntypedMetadata[code long:=ApplicationProductrice,value=ADELAIDE]"
    * 
    * @param <T>
    *           le type d'objet
    * @param list
    *           : liste des objets à afficher.
    * @return Une chaîne qui représente l'ensemble des objets à afficher.
    */
   private <T> String buildMessageFromList(Collection<T> list) {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SIMPLE_STYLE);
      for (T o : Utils.nullSafeIterable(list)) {
         if (o != null) {
            toStrBuilder.append(o.toString());
         }
      }
      return toStrBuilder.toString();
   }

   /**
    * Permet de convertir un objet DataHandler en tableau de byte
    */
   private byte[] convertToByteArray(DataHandler dataHandler) {
      if (dataHandler == null) {
         return new byte[0];
      } else {
         try {
            InputStream inputStream;
            inputStream = dataHandler.getInputStream();
            byte[] content = IOUtils.toByteArray(inputStream);

            return content;
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }
   }
}
