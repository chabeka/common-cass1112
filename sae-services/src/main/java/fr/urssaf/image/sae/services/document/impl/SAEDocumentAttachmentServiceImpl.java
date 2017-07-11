package fr.urssaf.image.sae.services.document.impl;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentAttachment;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.services.SAEControlMetadataService;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.document.SAEDocumentAttachmentService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentAttachmentEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Fournit l'implémentation des services pour la recherche.
 */
@Service
@Qualifier("saeDocumentAttachmentService")
public class SAEDocumentAttachmentServiceImpl extends AbstractSAEServices
      implements SAEDocumentAttachmentService {

   private static final String SEPARATOR_STRING = ", ";

   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesService;

   @Autowired
   private SAEConvertMetadataService convertService;

   @Autowired
   private SAEControlMetadataService controlService;

   @Autowired
   private PrmdService prmdService;

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   private MappingDocumentService mappingService;

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageService;

   @Autowired
   private EcdeServices ecdeServices;

   private static final Logger LOG = LoggerFactory
         .getLogger(SAEDocumentAttachmentServiceImpl.class);

   @Override
   public void addDocumentAttachmentBinaire(UUID docUuid, String docName,
         String extension, DataHandler contenu) throws SAEDocumentAttachmentEx,
         ArchiveInexistanteEx, EmptyDocumentEx, EmptyFileNameEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "addDocumentAttachmentBinaire()";

      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID document de rattachement : \"{}\"", prefixeTrc,
            docUuid);
      LOG.debug("{} - Nom du fichier : \"{}\"", prefixeTrc, docName);
      LOG.debug("{} - Extension : \"{}\"", prefixeTrc, extension);

      if (contenu != null) {
         LOG.debug("{} - Taille du contenu du fichier : \"{}\"", prefixeTrc,
               contenu);
      }
      // Fin des traces debug - entrée méthode

      try {
         controlesService.checkBinaryContent(contenu);

         controlesService.checkBinaryFileName(docName);

         verificationDroit(docUuid, prefixeTrc);

         storageService.addDocumentAttachment(docUuid, docName, extension,
               contenu);

         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

      } catch (ConnectionServiceEx e) {
         throw new SAEDocumentAttachmentEx(
               "Erreur de connection au service de gestion des documents attachés",
               e);
      } catch (ReferentialException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (UnknownDesiredMetadataEx e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (RetrievalServiceEx e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (InvalidSAETypeException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (MappingFromReferentialException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (StorageDocAttachmentServiceEx e) {
         throw new SAEDocumentAttachmentEx(e);
      }

   }

   @Override
   public void addDocumentAttachmentUrl(UUID docUuid, URI ecdeURL)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx,
         CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx, EmptyDocumentEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "addDocumentAttachmentUrl()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID document de rattachement : \"{}\"", prefixeTrc,
            docUuid);
      LOG.debug("{} - URI ECDE : \"{}\"", prefixeTrc, ecdeURL.toString());
      // Fin des traces debug - entrée méthode

      try {
         verificationDroit(docUuid, prefixeTrc);

         controlesService.checkCaptureEcdeUrl(ecdeURL.toString());
         // chargement du document de l'ECDE
         File ecdeFile = loadEcdeFile(ecdeURL);
         controlesService.checkDocumentAttache(ecdeFile);

         String docName = FilenameUtils.getBaseName(ecdeFile.getName());
         String extension = FilenameUtils.getExtension(ecdeFile.getName());

         DataHandler contenu = new DataHandler(new FileDataSource(ecdeFile));

         storageService.addDocumentAttachment(docUuid, docName, extension,
               contenu);

         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

      } catch (ConnectionServiceEx e) {
         throw new SAEDocumentAttachmentEx(
               "Erreur de connection au service de gestion des documents attachés",
               e);
      } catch (ReferentialException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (UnknownDesiredMetadataEx e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (RetrievalServiceEx e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (InvalidSAETypeException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (MappingFromReferentialException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (StorageDocAttachmentServiceEx e) {
         throw new SAEDocumentAttachmentEx(e);
      }

   }

   private void verificationDroit(UUID docUuid, String prefixeTrc)
         throws ConnectionServiceEx, ReferentialException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         RetrievalServiceEx, ArchiveInexistanteEx, InvalidSAETypeException,
         MappingFromReferentialException {

      getStorageServiceProvider().openConnexion();

      // On récupère le document sur lequel on souhaite ajouter un document
      // attaché pour
      // vérifier les droits
      List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
      Map<String, MetadataReference> listeAllMeta = referenceDAO
            .getAllMetadataReferences();
      for (String mapKey : listeAllMeta.keySet()) {
         allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
               .getShortCode()));
      }
      UUIDCriteria uuidCriteria = new UUIDCriteria(docUuid, allMeta);

      List<StorageMetadata> listeStorageMeta = this.getStorageServiceProvider()
            .getStorageDocumentService()
            .retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
      if (listeStorageMeta.size() == 0) {
         String message = StringUtils
               .replace(
                     "Il n'existe aucun document pour l'identifiant d'archivage '{0}'",
                     "{0}", docUuid.toString());
         throw new ArchiveInexistanteEx(message);
      }
      List<UntypedMetadata> listeUMeta = mappingService
            .storageMetadataToUntypedMetadata(listeStorageMeta);

      // Vérification des droits
      LOG.debug("{} - Récupération des droits", prefixeTrc);
      AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
            .getContext().getAuthentication();
      List<SaePrmd> saePrmds = token.getSaeDroits().get("ajout_doc_attache");
      LOG.debug("{} - Vérification des droits", prefixeTrc);
      boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

      if (!isPermitted) {
         throw new AccessDeniedException(
               "L'ajout de document attaché est refusé car les droits sont insuffisants");
      }
   }

   /**
    * 
    * @param ecdeURL
    * @return File.
    * @throws SAECaptureServiceEx
    *            {@link SAECaptureServiceEx}
    */
   private File loadEcdeFile(URI ecdeURL) throws SAEDocumentAttachmentEx {
      try {
         return ecdeServices.convertURIToFile(ecdeURL);
      } catch (EcdeBadURLException e) {
         throw new SAEDocumentAttachmentEx(e);
      } catch (EcdeBadURLFormatException e) {
         throw new SAEDocumentAttachmentEx(e);
      }

   }

   @Override
   public UntypedDocumentAttachment getDocumentAttachment(UUID docUuid)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "getDocumentAttachment()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID document de rattachement : \"{}\"", prefixeTrc,
            docUuid);
      // Fin des traces debug - entrée méthode

      try {

         this.getStorageServiceProvider().openConnexion();

         List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
         Map<String, MetadataReference> listeAllMeta = referenceDAO
               .getAllMetadataReferencesPourVerifDroits();
         for (String mapKey : listeAllMeta.keySet()) {
               allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
                     .getShortCode()));
         }

         UUIDCriteria uuidCriteria = new UUIDCriteria(docUuid, allMeta);

         // On récupère le document à partir de l'UUID, avec toutes les
         // métadonnées du référentiel
         StorageDocument storageDocument = this.getStorageServiceProvider()
               .getStorageDocumentService()
               .retrieveStorageDocumentByUUID(uuidCriteria);

         UntypedDocument untypedDocument = null;

         if (storageDocument != null) {
            untypedDocument = this.mappingService
                  .storageDocumentToUntypedDocument(storageDocument);

            LOG.debug("{} - Récupération des droits", prefixeTrc);
            AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                  .getContext().getAuthentication();
            List<SaePrmd> saePrmds = token.getSaeDroits().get("consultation");
            LOG.debug("{} - Vérification des droits", prefixeTrc);
            boolean isPermitted = prmdService.isPermitted(
                  untypedDocument.getUMetadatas(), saePrmds);

            if (!isPermitted) {
               throw new AccessDeniedException(
                     "Le document est refusé à la consultation car les droits sont insuffisants");
            }

            StorageDocumentAttachment documentAttache = storageService
                  .getDocumentAttachment(docUuid);

            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode

            if (documentAttache != null) {
               UntypedDocumentAttachment uDocumentAttache = this.mappingService
                     .storageDocumentAttachmentToUntypedDocumentAttachment(documentAttache);
               return uDocumentAttache;
            } else {
               return null;
            }

         } else {
            String message = StringUtils
                  .replace(
                        "Il n'existe aucun document pour l'identifiant d'archivage '{0}'",
                        "{0}", docUuid.toString());
            throw new ArchiveInexistanteEx(message);
         }

      } catch (StorageDocAttachmentServiceEx e) {
         throw new SAEDocumentAttachmentEx(
               "Une exception a eu lieu lors de la récupération du document au format d'origine",
               e);
      } catch (ConnectionServiceEx e) {
         throw new SAEDocumentAttachmentEx(
               "Une exception a eu lieu lors de la récupération du document au format d'origine",
               e);
      } catch (ReferentialException e) {
         throw new SAEDocumentAttachmentEx(
               "Une exception a eu lieu lors de la récupération du document au format d'origine",
               e);
      } catch (RetrievalServiceEx e) {
         throw new SAEDocumentAttachmentEx(
               "Une exception a eu lieu lors de la récupération du document au format d'origine",
               e);
      } catch (InvalidSAETypeException e) {
         throw new SAEDocumentAttachmentEx(
               "Une exception a eu lieu lors de la récupération du document au format d'origine",
               e);
      } catch (MappingFromReferentialException e) {
         throw new SAEDocumentAttachmentEx(
               "Une exception a eu lieu lors de la récupération du document au format d'origine",
               e);
      }

   }

   @Override
   public void addDocumentAttachmentBinaireRollbackParent(UUID docUuid,
         String docName, String extension, DataHandler contenu)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx, EmptyDocumentEx,
         EmptyFileNameEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "addDocumentAttachmentBinaireRollbackParent()";

      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID document de rattachement : \"{}\"", prefixeTrc,
            docUuid);
      LOG.debug("{} - Nom du fichier : \"{}\"", prefixeTrc, docName);
      LOG.debug("{} - Extension : \"{}\"", prefixeTrc, extension);

      if (contenu != null) {
         LOG.debug("{} - Taille du contenu du fichier : \"{}\"", prefixeTrc,
               contenu);
      }
      // Fin des traces debug - entrée méthode

      try {
         controlesService.checkBinaryContent(contenu);

         controlesService.checkBinaryFileName(docName);

         verificationDroit(docUuid, prefixeTrc);

         storageService.addDocumentAttachment(docUuid, docName, extension,
               contenu);

         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

      } catch (ConnectionServiceEx e) {
         try {
            storageService.deleteStorageDocument(docUuid);
            throw new SAEDocumentAttachmentEx(
                  "Erreur de connection au service de gestion des documents attachés",
                  e);
         } catch (DeletionServiceEx e1) {

            String message = "Erreur lors de l'ajout d'un document attaché, ATTENTION, le rollback du document parent ("
                  + docUuid + ") n'a pas été effectué";
            throw new SAEDocumentAttachmentEx(message, e);
         }
      } catch (ReferentialException e) {
         rollBackDocumentParent(docUuid, e);
      } catch (UnknownDesiredMetadataEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (RetrievalServiceEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (InvalidSAETypeException e) {
         rollBackDocumentParent(docUuid, e);
      } catch (MappingFromReferentialException e) {
         rollBackDocumentParent(docUuid, e);
      } catch (StorageDocAttachmentServiceEx e) {
         rollBackDocumentParent(docUuid, e);
      }

   }

   @Override
   public void addDocumentAttachmentUrlRollbackParent(UUID docUuid, URI ecdeURL)
         throws SAEDocumentAttachmentEx, ArchiveInexistanteEx,
         CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx, EmptyDocumentEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "addDocumentAttachmentUrlRollbackParent()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID document de rattachement : \"{}\"", prefixeTrc,
            docUuid);
      LOG.debug("{} - URI ECDE : \"{}\"", prefixeTrc, ecdeURL.toString());
      // Fin des traces debug - entrée méthode

      try {
         verificationDroit(docUuid, prefixeTrc);

         controlesService.checkCaptureEcdeUrl(ecdeURL.toString());
         // chargement du document de l'ECDE
         File ecdeFile = loadEcdeFile(ecdeURL);
         controlesService.checkDocumentAttache(ecdeFile);

         String docName = FilenameUtils.getBaseName(ecdeFile.getName());
         String extension = FilenameUtils.getExtension(ecdeFile.getName());

         DataHandler contenu = new DataHandler(new FileDataSource(ecdeFile));

         storageService.addDocumentAttachment(docUuid, docName, extension,
               contenu);
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode

      } catch (ConnectionServiceEx e) {
         try {
            storageService.deleteStorageDocument(docUuid);
            throw new SAEDocumentAttachmentEx(
                  "Erreur de connection au service de gestion des documents attachés",
                  e);
         } catch (DeletionServiceEx e1) {

            String message = "Erreur lors de l'ajout d'un document attaché, ATTENTION, le rollback du document parent ("
                  + docUuid + ") n'a pas été effectué";
            throw new SAEDocumentAttachmentEx(message, e);
         }

      } catch (ReferentialException e) {
         rollBackDocumentParent(docUuid, e);
      } catch (UnknownDesiredMetadataEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (RetrievalServiceEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (InvalidSAETypeException e) {
         rollBackDocumentParent(docUuid, e);
      } catch (MappingFromReferentialException e) {
         rollBackDocumentParent(docUuid, e);
      } catch (StorageDocAttachmentServiceEx e) {
         rollBackDocumentParent(docUuid, e);
      } catch (CaptureEcdeUrlFileNotFoundEx e) {
         try {
            storageService.deleteStorageDocument(docUuid);
            throw e;
         } catch (DeletionServiceEx e1) {

            String message = "Erreur lors de l'ajout d'un document attaché, ATTENTION, le rollback du document parent ("
                  + docUuid + ") n'a pas été effectué";
            throw new SAEDocumentAttachmentEx(message, e);
         }
      } catch (CaptureBadEcdeUrlEx e) {
         try {
            storageService.deleteStorageDocument(docUuid);
            throw e;
         } catch (DeletionServiceEx e1) {

            String message = "Erreur lors de l'ajout d'un document attaché, ATTENTION, le rollback du document parent ("
                  + docUuid + ") n'a pas été effectué";
            throw new SAEDocumentAttachmentEx(message, e);
         }
      } catch (EmptyDocumentEx e) {
         try {
            storageService.deleteStorageDocument(docUuid);
            throw e;
         } catch (DeletionServiceEx e1) {

            String message = "Erreur lors de l'ajout d'un document attaché, ATTENTION, le rollback du document parent ("
                  + docUuid + ") n'a pas été effectué";
            throw new SAEDocumentAttachmentEx(message, e);
         }
      }

   }

   private void rollBackDocumentParent(UUID docUuid, Exception e)
         throws SAEDocumentAttachmentEx {
      try {
         storageService.deleteStorageDocument(docUuid);
         throw new SAEDocumentAttachmentEx(e);
      } catch (DeletionServiceEx e1) {

         String message = "Erreur lors de l'ajout d'un document attaché, ATTENTION, le rollback du document parent ("
               + docUuid + ") n'a pas été effectué";
         throw new SAEDocumentAttachmentEx(message, e);
      }
   }

}
