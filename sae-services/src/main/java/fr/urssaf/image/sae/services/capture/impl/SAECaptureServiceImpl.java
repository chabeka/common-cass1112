package fr.urssaf.image.sae.services.capture.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
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
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Implémentation du service {@link SAECaptureService}
 * 
 */
@Service
public class SAECaptureServiceImpl implements SAECaptureService {
   private static final Logger LOG = LoggerFactory
         .getLogger(SAECaptureServiceImpl.class);
   private final StorageServiceProvider serviceProvider;

   private final EcdeServices ecdeServices;

   private final SAECommonCaptureService commonsService;
   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

   /**
    * initialisation des différents services du SAE nécessaire à la capture
    * 
    * @param serviceProvider
    *           façade des services DFCE
    * @param connectionParam
    *           configuration de la connexion à DFCE
    * 
    * @param ecdeServices
    *           les ecdes
    * @param commonsService
    *           service commun de la capture
    */
   @Autowired
   public SAECaptureServiceImpl(
         @Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider,
         DFCEConnection connectionParam, EcdeServices ecdeServices,
         SAECommonCaptureService commonsService) {

      Assert.notNull(serviceProvider);
      Assert.notNull(connectionParam);
      Assert.notNull(ecdeServices);
      Assert.notNull(commonsService);

      this.ecdeServices = ecdeServices;
      this.serviceProvider = serviceProvider;
      this.commonsService = commonsService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final CaptureResult capture(List<UntypedMetadata> metadatas,
         URI ecdeURL) throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException, UnexpectedDomainException, InvalidPagmsCombinaisonException {
      // Traces debug - entrée méthode
      String prefixeTrc = "capture()";
      CaptureResult result = new CaptureResult();
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - Liste des métadonnées : \"{}\"", prefixeTrc,
            buildMessageFromList(metadatas));
      LOG.debug("{} - URI ECDE : \"{}\"", prefixeTrc, ecdeURL.toString());
      // Fin des traces debug - entrée méthode
      controlesService.checkCaptureEcdeUrl(ecdeURL.toString());
      // chargement du document de l'ECDE
      File ecdeFile = loadEcdeFile(ecdeURL);

      UUID uuid = insertDocument(metadatas, ecdeFile, result);
      result.setIdDoc(uuid);
      // Traces debug - sortie méthode
      LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, uuid);
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode

      return result;

   }

   /**
    * {@inheritDoc}
    * 
    * @throws UnknownHashCodeEx
    * @throws RequiredArchivableMetadataEx
    * @throws EmptyDocumentEx
    * @throws UnknownCodeRndEx
    * @throws ReferentialRndException
    * @throws FileNotFoundException
    * @throws MetadataValueNotInDictionaryEx
    * @throws ValidationExceptionInvalidFile
    * @throws UnknownFormatException
    * @throws InvalidPagmsCombinaisonException 
    * @throws UnexpectedDomainException 
    */
   @Override
   public final CaptureResult captureFichier(List<UntypedMetadata> metadatas,
         String path) throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx, UnknownHashCodeEx,
         FileNotFoundException, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException, 
         UnexpectedDomainException, InvalidPagmsCombinaisonException {
      // Traces debug - entrée méthode
      String prefixeTrc = "capture()";
      CaptureResult result = new CaptureResult();
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - Liste des métadonnées : \"{}\"", prefixeTrc,
            buildMessageFromList(metadatas));
      LOG.debug("{} - Chemin du fichier : \"{}\"", prefixeTrc, path);
      // Fin des traces debug - entrée méthode
      File file = new File(path);
      UUID uuid;
      if (file.exists()) {
         uuid = insertDocument(metadatas, file, result);
         result.setIdDoc(uuid);
      } else {
         LOG.debug("{} - Fichier inexistant: \"{}\"", prefixeTrc, path);
         throw new FileNotFoundException("Le fichier à archiver n'existe pas");
      }

      // Traces debug - sortie méthode
      LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, uuid);
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode

      return result;

   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final CaptureResult captureBinaire(List<UntypedMetadata> metadatas,
         DataHandler content, String fileName) throws SAECaptureServiceEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         UnknownHashCodeEx, EmptyFileNameEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile, 
         UnexpectedDomainException, InvalidPagmsCombinaisonException {

      // Traces debug - entrée méthode
      String prefixeTrc = "captureBinaire()";
      CaptureResult result = new CaptureResult();
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - Liste des métadonnées : \"{}\"", prefixeTrc,
            buildMessageFromList(metadatas));
      LOG.debug("{} - Nom du fichier : \"{}\"", prefixeTrc, fileName);
      if (content != null) {
         LOG.debug("{} - Taille du contenu du fichier : \"{}\"", prefixeTrc,
               content);
      }
      // Fin des traces debug - entrée méthode

      controlesService.checkBinaryContent(content);
      controlesService.checkBinaryFileName(fileName);

      // Instanciation d'un untypedDocument
      UntypedDocument untypedDocument = new UntypedDocument(content, fileName,
            metadatas);

      // appel du service commun d'archivage dans la capture unitaire
      StorageDocument storageDoc;
      try {
         storageDoc = commonsService.buildBinaryStorageDocumentForCapture(
               untypedDocument, result);

      } catch (SAEEnrichmentEx e) {
         throw new SAECaptureServiceEx(e);
      }

      // archivage du document dans DFCE
      UUID uuid = insererBinaryStorageDocument(storageDoc);
      result.setIdDoc(uuid);
      // Traces debug - sortie méthode
      LOG.debug("{} - Valeur de retour archiveId: \"{}\"", prefixeTrc, uuid);
      LOG.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode

      return result;
   }

   /**
    * @param metadatas
    * @param ecdeFile
    * @return UntypedDocument
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}
    */
   private UntypedDocument createUntypedDocument(
         List<UntypedMetadata> metadatas, File ecdeFile)
         throws SAECaptureServiceEx {

      // TODO vérification que le fichier extrait de l'url ECDE existe bien!

      // conversion du fichier extrait de l'url ECDE en bytes[]
      // instanciation de la classe UntypedDocument avec la liste des
      // métadonnées et le contenu du document à archiver
      UntypedDocument untypedDocument = new UntypedDocument(null, metadatas);
      untypedDocument.setFilePath(ecdeFile.toString());
      return untypedDocument;
   }

   /**
    * 
    * @param ecdeURL
    * @return File.
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}
    */
   private File loadEcdeFile(URI ecdeURL) throws SAECaptureServiceEx {
      try {
         return ecdeServices.convertURIToFile(ecdeURL);
      } catch (EcdeBadURLException e) {
         throw new SAECaptureServiceEx(e);
      } catch (EcdeBadURLFormatException e) {
         throw new SAECaptureServiceEx(e);
      }

   }

   /**
    * @param storageDoc
    * @return UUID
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}
    */
   private UUID insererStorageDocument(StorageDocument storageDoc)
         throws SAECaptureServiceEx {
      // insertion du document à archiver dans DFCE puis fermeture de la
      // connexion DFCE
      UUID uuid;
      try {
         serviceProvider.openConnexion();
         uuid = serviceProvider.getStorageDocumentService()
               .insertStorageDocument(storageDoc).getUuid();

      } catch (ConnectionServiceEx e) {
         throw new SAECaptureServiceEx(e);
      } catch (InsertionServiceEx e) {
         throw new SAECaptureServiceEx(e);
      }
      return uuid;
   }

   /**
    * @param storageDoc
    * @return UUID
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}
    */
   private UUID insererBinaryStorageDocument(StorageDocument storageDoc)
         throws SAECaptureServiceEx {
      // insertion du document à archiver dans DFCE puis fermeture de la
      // connexion DFCE
      UUID uuid;
      try {
         serviceProvider.openConnexion();
         uuid = serviceProvider.getStorageDocumentService()
               .insertBinaryStorageDocument(storageDoc).getUuid();

      } catch (ConnectionServiceEx e) {
         throw new SAECaptureServiceEx(e);
      } catch (InsertionServiceEx e) {
         throw new SAECaptureServiceEx(e);
      }
      return uuid;
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
    * Méthode commune à captureFile et capture permettant d'archiver un document
    * qui provient soit de l'ECDE soit d'un emplacement donné
    * 
    * @param metadatas
    *           Liste des métadonnées
    * @param file
    *           fichier à archiver
    * @param captureResult
    *           résultat de la capture
    * @return UUID L'uuid de l'archivage
    * @throws SAECaptureServiceEx
    * @throws ReferentialRndException
    * @throws UnknownCodeRndEx
    * @throws RequiredStorageMetadataEx
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @throws UnknownMetadataEx
    * @throws DuplicatedMetadataEx
    * @throws NotArchivableMetadataEx
    * @throws EmptyDocumentEx
    * @throws RequiredArchivableMetadataEx
    * @throws UnknownHashCodeEx
    * @throws NotSpecifiableMetadataEx
    * @throws MetadataValueNotInDictionaryEx
    * @throws ValidationExceptionInvalidFile
    * @throws UnknownFormatException
    * @throws InvalidPagmsCombinaisonException 
    * @throws UnexpectedDomainException 
    */
   private UUID insertDocument(List<UntypedMetadata> metadatas, File file,
         CaptureResult captureResult) throws SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotArchivableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         NotSpecifiableMetadataEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile, 
         UnexpectedDomainException, InvalidPagmsCombinaisonException {

      // instanciation d'un UntypedDocument
      UntypedDocument untypedDocument = createUntypedDocument(metadatas, file);
      // appel du service commun d'archivage dans la capture unitaire
      StorageDocument storageDoc;
      try {
         storageDoc = commonsService.buildStorageDocumentForCapture(
               untypedDocument, captureResult);
      } catch (SAEEnrichmentEx e) {
         throw new SAECaptureServiceEx(e);
      }

      // archivage du document dans DFCE
      return insererStorageDocument(storageDoc);
   }
}
