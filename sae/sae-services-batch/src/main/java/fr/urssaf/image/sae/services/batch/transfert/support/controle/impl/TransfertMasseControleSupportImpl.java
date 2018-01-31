package fr.urssaf.image.sae.services.batch.transfert.support.controle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.batch.transfert.support.controle.TransfertMasseControleSupport;
import fr.urssaf.image.sae.services.controles.SAEControlesModificationService;
import fr.urssaf.image.sae.services.documentExistant.SAEDocumentExistantService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.services.reprise.exception.TraitementRepriseAlreadyDoneException;
import fr.urssaf.image.sae.services.transfert.SAETransfertService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageTransfertService;
import fr.urssaf.image.sae.trace.dao.support.ServiceProviderSupport;

/**
 * Implémentation du support {@link TransfertMasseControleSupport}
 * 
 */
@Component
public class TransfertMasseControleSupportImpl implements
      TransfertMasseControleSupport {

   /**
    * Logger
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(TransfertMasseControleSupportImpl.class);

   /**
    * SAETransfertService
    */
   @Autowired
   private SAETransfertService transfertService;

   /**
    * SAEDocumentExistantService
    */
   @Autowired
   private SAEDocumentExistantService docExistant;

   /**
    * ServiceProviderSupport
    */
   @Autowired
   private ServiceProviderSupport traceServiceSupport;

   /**
    * MappingDocumentService
    */
   @Autowired
   private MappingDocumentService mappingDocumentService;

   /**
    * ControleModificationService
    */
   @Autowired
   private SAEControlesModificationService controleModification;

   /**
    * Provider de service pour la connexion DFCE de la GNS
    */
   @Autowired
   private StorageTransfertService storageTransfertService;

   /**
    * Provider de service pour la connexion DFCE de la GNT
    */
   @Autowired
   private StorageServiceProvider storageServiceProvider;

   /**
    * {@inheritDoc}
    */
   public boolean controleSAEDocumentSuppression(UntypedDocument item)
         throws SearchingServiceEx, ConnectionServiceEx {

      return storageServiceProvider.getStorageDocumentService()
            .searchMetaDatasByUUIDCriteria(
                  new UUIDCriteria(item.getUuid(), null)) != null;
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public StorageDocument controleSAEDocumentTransfert(UntypedDocument item,
         UUID idTraitementMasse)
         throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException,
         MappingFromReferentialException, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         RequiredArchivableMetadataEx, MetadataValueNotInDictionaryEx,
         NotModifiableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         NotSpecifiableMetadataEx, UnknownHashCodeEx,
         TraitementRepriseAlreadyDoneException {

      String trcPrefix = "controleSAEDocumentTransfert()";
      LOGGER.debug("{} - début", trcPrefix);

      return controleSAEDocumentTransfertCommon(item, false, idTraitementMasse);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public StorageDocument controleSAEDocumentRepriseTransfert(
         UntypedDocument item, UUID idTraitementMasse) throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException,
         MappingFromReferentialException, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         RequiredArchivableMetadataEx, MetadataValueNotInDictionaryEx,
         ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx,
         TraitementRepriseAlreadyDoneException {
      String trcPrefix = "controleSAEDocumentRepriseTransfert()";
      LOGGER.debug("{} - début", trcPrefix);

      return controleSAEDocumentTransfertCommon(item, true, idTraitementMasse);
   }

   /**
    * Controle du document pour le transfert de masse.
    * 
    * @param item
    *           document
    * @param isReprise
    *           Mode reprise, true actif, false sinon
    * @param idTraitementMasse
    *           Identifiant traitement de masse
    * @return Le document controlé.
    * @throws TransfertException
    * @{@link TransfertException}
    * @throws MappingFromReferentialException
    * @{@link MappingFromReferentialException}
    * @throws InvalidSAETypeException
    * @{@link InvalidSAETypeException}
    * @throws MetadataValueNotInDictionaryEx
    * @{@link MetadataValueNotInDictionaryEx}
    * @throws RequiredArchivableMetadataEx
    * @{@link RequiredArchivableMetadataEx}
    * @throws InvalidValueTypeAndFormatMetadataEx
    * @{@link InvalidValueTypeAndFormatMetadataEx}
    * @throws DuplicatedMetadataEx
    * @{@link DuplicatedMetadataEx}
    * @throws UnknownMetadataEx
    * @{@link UnknownMetadataEx}
    * @throws NotModifiableMetadataEx
    * @{@link NotModifiableMetadataEx}
    * @throws UnknownHashCodeEx
    * @{@link UnknownHashCodeEx}
    * @throws NotSpecifiableMetadataEx
    * @{@link NotSpecifiableMetadataEx}
    * @throws UnknownCodeRndEx
    * @{@link UnknownCodeRndEx}
    * @throws ReferentialRndException
    * @{@link ReferentialRndException}
    * @throws TraitementRepriseAlreadyDoneException
    * @{@link TraitementRepriseAlreadyDoneException}
    */
   private StorageDocument controleSAEDocumentTransfertCommon(
         UntypedDocument item, boolean isReprise, UUID idTraitementMasse)
         throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException,
         MappingFromReferentialException, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         RequiredArchivableMetadataEx, MetadataValueNotInDictionaryEx,
         NotModifiableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         NotSpecifiableMetadataEx, UnknownHashCodeEx,
         TraitementRepriseAlreadyDoneException {

      String trcPrefix = "controleSAEDocumentTransfert()";
      LOGGER.debug("{} - début", trcPrefix);

      List<StorageMetadata> storageMetas = new ArrayList<StorageMetadata>();
      if (!CollectionUtils.isEmpty(item.getUMetadatas())) {
         if (!item.getBatchActionType().equals("SUPPRESSION")) {
            controleModification.checkSaeMetadataForTransfertMasse(item.getUMetadatas());
         }
         
         try {
            List<SAEMetadata> modifiedSaeMetadatas = mappingDocumentService
                  .untypedMetadatasToSaeMetadatas(item.getUMetadatas());
            storageMetas = mappingDocumentService
                  .saeMetadatasToStorageMetadatas(modifiedSaeMetadatas);
         } catch (InvalidSAETypeException e) {
            throw new InvalidSAETypeException(e);
         } catch (MappingFromReferentialException e) {
            throw new MappingFromReferentialException(e);
         }
      }

      StorageDocument document = transfertService
            .controleDocumentTransfertMasse(item.getUuid(), storageMetas,
                  isReprise, idTraitementMasse);
      // Charger le typeAction dans le storageDoc
      document.setBatchTypeAction(item.getBatchActionType());
      return document;

   }
}
