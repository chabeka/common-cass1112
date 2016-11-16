package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
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
import fr.urssaf.image.sae.services.document.SAENoteService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Fournit l'implémentation des services pour la recherche.
 */
@Service
@Qualifier("saeNoteService")
public class SAENoteServiceImpl extends AbstractSAEServices implements
      SAENoteService {

   private static final String SEPARATOR_STRING = ", ";

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

   private static final Logger LOG = LoggerFactory
         .getLogger(SAENoteServiceImpl.class);

   @Override
   public void addDocumentNote(UUID docUuid, String contenu, String login)
         throws SAEDocumentNoteException, ArchiveInexistanteEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "addDocumentNote()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID du document : {}", prefixeTrc, docUuid);
      LOG.debug("{} - Contenu de la note : {}", prefixeTrc, contenu);
      LOG.debug("{} - Login : {}", prefixeTrc, login);

      try {
         getStorageServiceProvider().openConnexion();

         // On récupère la liste de toutes les méta du référentiel
         List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
         Map<String, MetadataReference> listeAllMeta = referenceDAO
               .getAllMetadataReferences();
         for (String mapKey : listeAllMeta.keySet()) {
            if (!StorageTechnicalMetadatas.NOTE.getShortCode().equals(
                  listeAllMeta.get(mapKey).getShortCode())) {
               allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
                     .getShortCode()));
            }
         }

         UUIDCriteria uuidCriteria = new UUIDCriteria(docUuid, allMeta);

         // On récupère les métadonnées du document sur lequel on souhaite
         // ajouter la note pour
         // vérifier les droits
         List<StorageMetadata> listeStorageMeta = this
               .getStorageServiceProvider().getStorageDocumentService()
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
         List<SaePrmd> saePrmds = token.getSaeDroits().get("ajout_note");
         LOG.debug("{} - Vérification des droits", prefixeTrc);
         boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

         if (!isPermitted) {
            throw new AccessDeniedException(
                  "L'ajout de note est refusé car les droits sont insuffisants");
         }

         // On ajoute la note au document
         LOG.debug("{} - Ajout de la note au document", prefixeTrc);
         storageService.addDocumentNote(docUuid, contenu, login);

      } catch (ConnectionServiceEx except) {
         throw new SAEDocumentNoteException(
               "Erreur de connection au service de gestion des notes", except);
      } catch (DocumentNoteServiceEx e) {
         throw new SAEDocumentNoteException(
               "Une erreur a eu lieu lors de l'ajout d'une note", e);
      } catch (ReferentialException e) {
         throw new SAEDocumentNoteException(
               "Une erreur a eu lieu lors de l'ajout d'une note", e);
      } catch (RetrievalServiceEx e) {
         throw new SAEDocumentNoteException(
               "Une erreur a eu lieu lors de l'ajout d'une note", e);
      } catch (InvalidSAETypeException e) {
         throw new SAEDocumentNoteException(
               "Une erreur a eu lieu lors de l'ajout d'une note", e);
      } catch (MappingFromReferentialException e) {
         throw new SAEDocumentNoteException(
               "Une erreur a eu lieu lors de l'ajout d'une note", e);
      }

      LOG.debug("{} - Sortie", prefixeTrc);

   }

   @Override
   public List<StorageDocumentNote> getDocumentNotes(UUID docUuid)
         throws SAEDocumentNoteException {

      // Traces debug - entrée méthode
      String prefixeTrc = "getDocumentNotes()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID du document : {}", prefixeTrc, docUuid);

      try {
         getStorageServiceProvider().openConnexion();
      } catch (ConnectionServiceEx except) {
         throw new SAEDocumentNoteException(
               "Erreur de connection au service de gestion des notes", except);
      }

      LOG.debug("{} - Sortie", prefixeTrc);

      return getStorageServiceProvider().getStorageDocumentService()
            .getDocumentsNotes(docUuid);

   }

}
