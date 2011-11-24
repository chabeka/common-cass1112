package fr.urssaf.image.sae.services.consultation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.factory.SAEStorageFactory;
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
   private static final Logger LOG = LoggerFactory
         .getLogger(SAEConsultationServiceImpl.class);
   private final MetadataReferenceDAO referenceDAO;

   private final MappingDocumentService mappingService;

   /**
    * attribution des paramètres de l'implémentation
    * 
    * @param referenceDAO
    *           instance du service des métadonnées de référence
    * @param mappingService
    *           instance du service de mapping du SAE
    * 
    */
   @Autowired
   public SAEConsultationServiceImpl(MetadataReferenceDAO referenceDAO,
         MappingDocumentService mappingService) {
      super();
      this.referenceDAO = referenceDAO;
      this.mappingService = mappingService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(UUID idArchive)
         throws SAEConsultationServiceException {
      // Traces debug - entrée méthode
      String prefixeTrc = "consultation()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            idArchive);
      // Fin des traces debug - entrée méthode
      try {
         this.getStorageServiceProvider().openConnexion();

         try {

            List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();

            for (Entry<String, MetadataReference> reference : this.referenceDAO
                  .getDefaultConsultableMetadataReferences().entrySet()) {

               metadatas.add(SAEStorageFactory.createStorageMetadata(reference
                     .getValue().getShortCode()));

            }
            LOG.debug("{} - Liste des métadonnées consultable : \"{}\"",
                  prefixeTrc, buildMessageFromList(metadatas));
            UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, metadatas);

            StorageDocument storageDocument = this.getStorageServiceProvider()
                  .getStorageDocumentService().retrieveStorageDocumentByUUID(
                        uuidCriteria);

            UntypedDocument untypedDocument = null;

            if (storageDocument != null) {
               untypedDocument = this.mappingService
                     .storageDocumentToUntypedDocument(storageDocument);
            }
            LOG.debug("{} - Sortie", prefixeTrc);
            // Fin des traces debug - sortie méthode
            return untypedDocument;

         } catch (RetrievalServiceEx e) {

            throw new SAEConsultationServiceException(e);

         } catch (ReferentialException e) {

            throw new SAEConsultationServiceException(e);

         } catch (InvalidSAETypeException e) {

            throw new SAEConsultationServiceException(e);

         } catch (MappingFromReferentialException e) {

            throw new SAEConsultationServiceException(e);

         }
      } catch (ConnectionServiceEx e) {

         throw new SAEConsultationServiceException(e);
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
}
