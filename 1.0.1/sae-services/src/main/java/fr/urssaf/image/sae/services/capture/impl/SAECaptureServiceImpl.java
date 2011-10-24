package fr.urssaf.image.sae.services.capture.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
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
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Implémentation du service {@link SAECaptureService}
 * 
 */
@Service
public class SAECaptureServiceImpl implements SAECaptureService {

   private final StorageServiceProvider serviceProvider;

   //private final EcdeFileService ecdeFileService;
   private final EcdeServices ecdeServices;

   private final SAECommonCaptureService commonsService;

   /**
    * initialisation des différents services du SAE nécessaire à la capture
    * 
    * @param serviceProvider
    *           façade des services DFCE
    * @param connectionParam
    *           configuration de la connexion à DFCE
    * @param ecdeFileService
    *           service de l'ECDE
    * @param commonsService
    *           service commun de la capture
    */
   @Autowired
   public SAECaptureServiceImpl(
         @Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider,
         @Qualifier("storageConnectionParameter") StorageConnectionParameter connectionParam,
         EcdeServices ecdeServices, SAECommonCaptureService commonsService) {

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
    * 
    */
   @Override
   public final UUID capture(List<UntypedMetadata> metadatas, URI ecdeURL)
         throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx {

      // chargement du document de l'ECDE
      File ecdeFile = loadEcdeFile(ecdeURL);

      // instanciation d'un UntypedDocument
      UntypedDocument untypedDocument = createUntypedDocument(metadatas,
            ecdeFile);

      // appel du service commun d'archivage dans la capture unitaire

      StorageDocument storageDoc;
      try {
         storageDoc = commonsService
               .buildStorageDocumentForCapture(untypedDocument);
      } catch (SAEEnrichmentEx e) {
         throw new SAECaptureServiceEx(e);
      }

      // archivage du document dans DFCE
      UUID uuid = insererStorageDocument(storageDoc);

      return uuid;

   }
   /**
    * @param metadatas
    * @param ecdeFile
    * @return UntypedDocument
    * @throws SAECaptureServiceEx {@link SAECaptureServiceEx}
    */
   private UntypedDocument createUntypedDocument(
         List<UntypedMetadata> metadatas, File ecdeFile)
         throws SAECaptureServiceEx {

      // TODO vérification que le fichier extrait de l'url ECDE existe bien!

      // conversion du fichier extrait de l'url ECDE en bytes[]
      byte[] fileContent;
      try {
         fileContent = FileUtils.readFileToByteArray(ecdeFile);
      } catch (IOException e) {
         throw new SAECaptureServiceEx(e);
      }
      // instanciation de la classe UntypedDocument avec la liste des
      // métadonnées et le contenu du document à archiver
      UntypedDocument untypedDocument = new UntypedDocument(fileContent,
            metadatas);
      untypedDocument.setFilePath(ecdeFile.getName());
      return untypedDocument;
   }
   /**
    * 
    * @param ecdeURL
    * @return File.
    * @throws SAECaptureServiceEx {@link SAECaptureServiceEx}
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
    * @throws SAECaptureServiceEx {@link SAECaptureServiceEx}
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
}
