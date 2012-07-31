package fr.urssaf.image.sae.services.consultation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
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
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.factory.SAEStorageFactory;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du service {@link SAEConsultationService}
 * 
 */
@Service
@Qualifier("saeConsultationService")
public class SAEConsultationServiceImpl extends AbstractSAEServices implements
      SAEConsultationService {
   /**
    * 
    */
   private static final String SEPARATOR_STRING = ", ";
   private static final Logger LOG = LoggerFactory
         .getLogger(SAEConsultationServiceImpl.class);
   private final MetadataReferenceDAO referenceDAO;

   @Autowired
   private SAEConvertMetadataService convertService;

   @Autowired
   private SAEControlMetadataService controlService;

   @Autowired
   private PrmdService prmdService;

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
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      ConsultParams consultParams = new ConsultParams(idArchive);

      return consultation(consultParams);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final UntypedDocument consultation(ConsultParams consultParams)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      UUID idArchive = consultParams.getIdArchive();

      // Traces debug - entrée méthode
      String prefixeTrc = "consultation()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            idArchive);
      // Fin des traces debug - entrée méthode
      try {
         this.getStorageServiceProvider().openConnexion();

         try {

            ConsultParams params = new ConsultParams(consultParams
                  .getIdArchive(), new ArrayList<String>(referenceDAO
                  .getAllMetadataReferences().keySet()));
            
            List<StorageMetadata> allMeta = manageMetaData(params);

            List<String> metadatas = manageMetaDataNames(consultParams);

            LOG.debug("{} - Liste des métadonnées consultable : \"{}\"",
                  prefixeTrc, buildMessageFromList(metadatas));

            // UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive,
            // metadatas);
            UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, allMeta);

            StorageDocument storageDocument = this.getStorageServiceProvider()
                  .getStorageDocumentService().retrieveStorageDocumentByUUID(
                        uuidCriteria);

            UntypedDocument untypedDocument = null;

            if (storageDocument != null) {
               untypedDocument = this.mappingService
                     .storageDocumentToUntypedDocument(storageDocument);

               AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
                     .getContext().getAuthentication();
               List<SaePrmd> saePrmds = token.getDetails().get("consultation");

               boolean isPermitted = prmdService.isPermitted(untypedDocument
                     .getUMetadatas(), saePrmds);

               if (!isPermitted) {
                  throw new AccessDeniedException(
                        "Le document est refusé à la consultation car les droits sont insuffisants");
               }

               List<UntypedMetadata> list = filterMetadatas(metadatas,
                     untypedDocument.getUMetadatas());
               untypedDocument.setUMetadatas(list);

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
    * @param metadatas
    * @param uMetadatas
    * @return
    */
   private List<UntypedMetadata> filterMetadatas(List<String> metadatas,
         List<UntypedMetadata> uMetadatas) {

      List<UntypedMetadata> metaList = uMetadatas;
      UntypedMetadata currentMetadata;

      for (int index = uMetadatas.size() - 1; index >= 0; index--) {
         currentMetadata = metaList.get(index);

         if (!metadatas.contains(currentMetadata.getLongCode())) {
            metaList.remove(index);
         }
      }

      return metaList;
   }

   /**
    * Retourne la liste des metadatas à renvoyer en fonction de celles stockées
    * dans l'objet de paramétrage
    * 
    * @param consultParams
    *           paramètres de consultation
    * @return la liste des metadatas à consulter
    * @throws UnknownDesiredMetadataEx
    *            levée lorsque la metadata paramétrée n'existe pas
    * @throws ReferentialException
    *            levée lorsqu'au moins une metadata n'existe pas
    * @throws MetaDataUnauthorizedToConsultEx
    *            levée lorsqu'au moins une metadata n'est pas consultable
    */
   private List<String> manageMetaDataNames(ConsultParams consultParams)
         throws UnknownDesiredMetadataEx, ReferentialException,
         MetaDataUnauthorizedToConsultEx {

      List<String> keyList = new ArrayList<String>();

      if (CollectionUtils.isEmpty(consultParams.getMetadonnees())) {

         Map<String, MetadataReference> map = this.referenceDAO
               .getDefaultConsultableMetadataReferences();

         for (MetadataReference metaRef : map.values()) {
            keyList.add(metaRef.getLongCode());
         }

      } else {

         try {
            controlService.controlLongCodeExist(consultParams.getMetadonnees());

         } catch (LongCodeNotFoundException longExcept) {
            String message = ResourceMessagesUtils.loadMessage(
                  "consultation.metadonnees.inexistante", StringUtils.join(
                        longExcept.getListCode(), SEPARATOR_STRING));
            throw new UnknownDesiredMetadataEx(message, longExcept);
         }

         try {
            controlService.controlLongCodeIsAFConsultation(consultParams
                  .getMetadonnees());
         } catch (LongCodeNotFoundException longNotFoundEx) {
            String message = ResourceMessagesUtils.loadMessage(
                  "consultation.metadonnees.non.consultable", StringUtils.join(
                        longNotFoundEx.getListCode(), SEPARATOR_STRING));
            throw new MetaDataUnauthorizedToConsultEx(message, longNotFoundEx);
         }

         keyList.addAll(consultParams.getMetadonnees());

      }

      return keyList;
   }

   /**
    * Retourne la liste des metadatas à renvoyer en fonction de celles stockées
    * dans l'objet de paramétrage
    * 
    * @param consultParams
    *           paramètres de consultation
    * @return la liste des metadatas à consulter
    * @throws UnknownDesiredMetadataEx
    *            levée lorsque la metadata paramétrée n'existe pas
    * @throws ReferentialException
    *            levée lorsqu'au moins une metadata n'existe pas
    * @throws MetaDataUnauthorizedToConsultEx
    *            levée lorsqu'au moins une metadata n'est pas consultable
    */
   private List<StorageMetadata> manageMetaData(ConsultParams consultParams)
         throws UnknownDesiredMetadataEx, ReferentialException,
         MetaDataUnauthorizedToConsultEx {

      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();

      List<String> keyList = new ArrayList<String>();

      if (CollectionUtils.isEmpty(consultParams.getMetadonnees())) {

         Map<String, MetadataReference> map = this.referenceDAO
               .getDefaultConsultableMetadataReferences();

         for (MetadataReference metaRef : map.values()) {
            keyList.add(metaRef.getShortCode());
         }

      } else {

         try {
            controlService.controlLongCodeExist(consultParams.getMetadonnees());

         } catch (LongCodeNotFoundException longExcept) {
            String message = ResourceMessagesUtils.loadMessage(
                  "consultation.metadonnees.inexistante", StringUtils.join(
                        longExcept.getListCode(), SEPARATOR_STRING));
            throw new UnknownDesiredMetadataEx(message, longExcept);
         }

         Map<String, String> mapShortCode = null;
         try {
            mapShortCode = convertService.longCodeToShortCode(consultParams
                  .getMetadonnees());
         } catch (LongCodeNotFoundException longExcept) {
            String message = ResourceMessagesUtils.loadMessage(
                  "consultation.metadonnees.inexistante", StringUtils.join(
                        longExcept.getListCode(), SEPARATOR_STRING));
            throw new UnknownDesiredMetadataEx(message, longExcept);
         }

         keyList.addAll(mapShortCode.keySet());

      }

      for (String shortCode : keyList) {
         metadatas.add(SAEStorageFactory.createStorageMetadata(shortCode));
      }

      return metadatas;
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
