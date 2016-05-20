/**
 * 
 */
package fr.urssaf.image.sae.services.suppression.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe implémentant l'interface {@link SAESuppressionService}. Cette classe
 * est un singleton et peut être accessible par le système d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Service
public class SAESuppressionServiceImpl extends AbstractSAEServices implements
      SAESuppressionService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAESuppressionServiceImpl.class);

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageService;

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   private PrmdService prmdService;

   @Autowired
   private MappingDocumentService mappingService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void suppression(UUID idArchive) throws SuppressionException,
         ArchiveInexistanteEx {
      String trcPrefix = "suppression";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de suppression du document {}", new Object[] {
            trcPrefix, idArchive.toString() });

      try {
         this.getStorageServiceProvider().openConnexion();

         try {
            LOG.debug("{} - recherche du document", trcPrefix);
            List<StorageMetadata> allMeta = new ArrayList<StorageMetadata>();
            Map<String, MetadataReference> listeAllMeta = referenceDAO
                  .getAllMetadataReferences();
            for (String mapKey : listeAllMeta.keySet()) {
               allMeta.add(new StorageMetadata(listeAllMeta.get(mapKey)
                     .getShortCode()));
            }
            UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

            // On récupère les métadonnées du document à partir de l'UUID, avec
            // toutes les
            // métadonnées du référentiel
            List<StorageMetadata> listeStorageMeta = this
                  .getStorageServiceProvider().getStorageDocumentService()
                  .retrieveStorageDocumentMetaDatasByUUID(uuidCriteria);
            if (listeStorageMeta.size() == 0) {
               String message = StringUtils
                     .replace(
                           "Il n'existe aucun document pour l'identifiant d'archivage '{0}'",
                           "{0}", idArchive.toString());
               throw new ArchiveInexistanteEx(message);
            }
            List<UntypedMetadata> listeUMeta = mappingService
                  .storageMetadataToUntypedMetadata(listeStorageMeta);

            // Vérification des droits
            LOG.debug("{} - Récupération des droits", trcPrefix);
            AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                  .getContext().getAuthentication();
            List<SaePrmd> saePrmds = token.getSaeDroits().get("suppression");
            LOG.debug("{} - Vérification des droits", trcPrefix);
            boolean isPermitted = prmdService.isPermitted(listeUMeta, saePrmds);

            if (!isPermitted) {
               throw new AccessDeniedException(
                     "Le document est refusé à la suppression car les droits sont insuffisants");

            }

            LOG.debug("{} - suppression du document", trcPrefix);
            storageService.deleteStorageDocument(idArchive);

         } catch (DeletionServiceEx exception) {
            throw new SuppressionException(exception);
         } catch (ReferentialException exception) {
            throw new SuppressionException(exception);
         } catch (InvalidSAETypeException exception) {
            throw new SuppressionException(exception);
         } catch (MappingFromReferentialException exception) {
            throw new SuppressionException(exception);
         } catch (RetrievalServiceEx exception) {
            throw new SuppressionException(exception);
         }
      } catch (ConnectionServiceEx e) {
         throw new SuppressionException(e);
      }
      LOG.debug("{} - Suppression du document {} terminée", new Object[] {
            trcPrefix, idArchive.toString() });
      LOG.debug("{} - fin", trcPrefix);
   }

}
