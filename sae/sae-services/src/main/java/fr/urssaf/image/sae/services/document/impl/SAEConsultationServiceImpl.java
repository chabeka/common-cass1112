package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.SAEConsultationService;
import fr.urssaf.image.sae.services.document.exception.SAEConsultationServiceException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;

/**
 * Implémentation du service {@link SAEConsultationService}
 * 
 */
@Service
@Qualifier("saeConsultationService")
public class SAEConsultationServiceImpl extends AbstractSAEServices implements
      SAEConsultationService {

   private final MetadataReferenceDAO metadataReferenceDAO;

   private final MappingDocumentService mappingDocumentService;

   /**
    * attribution des paramètres de l'implémentation
    * 
    * @param servicesConverter
    *           instance du service de conversion
    */
   @Autowired
   public SAEConsultationServiceImpl(MetadataReferenceDAO metadataReferenceDAO,
         MappingDocumentService mappingDocumentService) {
      super();
      this.metadataReferenceDAO = metadataReferenceDAO;
      this.mappingDocumentService = mappingDocumentService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(UUID idArchive)
         throws SAEConsultationServiceException {

      this.getStorageServiceProvider().setStorageServiceProviderParameter(
            this.getStorageConnectionParameter());

      try {
         this.getStorageServiceProvider().getStorageConnectionService()
               .openConnection();

         try {

            List<StorageMetadata> consultableMetadatas = new ArrayList<StorageMetadata>();

            for (Entry<String, MetadataReference> reference : this.metadataReferenceDAO
                  .getDefaultConsultableMetadataReferences().entrySet()) {

               consultableMetadatas.add(new StorageMetadata(reference
                     .getValue().getShortCode()));

            }

            UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive,
                  consultableMetadatas);

            StorageDocument storageDocument = this.getStorageServiceProvider()
                  .getStorageDocumentService().retrieveStorageDocumentByUUID(
                        uuidCriteria);

            UntypedDocument untypedDocument = null;

            if (storageDocument != null) {

               untypedDocument = this.mappingDocumentService
                     .storageDocumentToUntypedDocument(storageDocument);

            }

            return untypedDocument;

         } catch (RetrievalServiceEx e) {

            throw new SAEConsultationServiceException(e);

         } catch (ReferentialException e) {

            throw new SAEConsultationServiceException(e);

         } catch (InvalidSAETypeException e) {

            throw new SAEConsultationServiceException(e);

         } catch (MappingFromReferentialException e) {

            throw new SAEConsultationServiceException(e);

         } finally {

            this.getStorageServiceProvider().getStorageConnectionService()
                  .closeConnexion();
         }

      } catch (ConnectionServiceEx e) {

         throw new SAEConsultationServiceException(e);
      }

   }

}
