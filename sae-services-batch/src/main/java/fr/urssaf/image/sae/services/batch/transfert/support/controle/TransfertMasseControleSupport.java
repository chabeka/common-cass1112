package fr.urssaf.image.sae.services.batch.transfert.support.controle;

import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
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
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.RetrievalServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Interface de controle pour le transfert de masse
 * 
 */
public interface TransfertMasseControleSupport {

   /**
    * Controle document pour la suppression
    * 
    */
   public boolean controleSAEDocumentSuppression(UntypedDocument item)
         throws SearchingServiceEx, ConnectionServiceEx;

   /**
    * Controle du document pour le transfert de masse.
    * 
    * @param item
    *           document
    * @param idTraitementMasse
    *           identifiant du traitement de masse
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
   public StorageDocument controleSAEDocumentTransfert(UntypedDocument item,
         UUID idTraitementMasse)
         throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException,
         MappingFromReferentialException, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         RequiredArchivableMetadataEx, MetadataValueNotInDictionaryEx,
         ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx,
         TraitementRepriseAlreadyDoneException;

   /**
    * Controle du document pour la reprise du transfert de masse.
    * 
    * @param item
    *           document
    * @param idTraitementMasse
    *           identifiant du traitement de masse repris
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
    * 
    */
   public StorageDocument controleSAEDocumentRepriseTransfert(
         UntypedDocument item, UUID idTraitementMasse)
         throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException,
         MappingFromReferentialException, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         RequiredArchivableMetadataEx, MetadataValueNotInDictionaryEx,
         ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx,
         TraitementRepriseAlreadyDoneException;
   
   /**
    * Méthode permettant de générer la liste des métadonnées storage contenant
    * les metadonnées de modification et la métadonnée GEL.
    * 
    * @param idArchive
    *           Identifiant document
    * @return la liste des metadonnées storage
    * @throws ReferentialException
    * @{@link ReferentialException}
    * @throws RetrievalServiceEx
    * @{@link RetrievalServiceEx}
    */
   public List<StorageMetadata> getListeStorageMetadatasWithGel(UUID idArchive)
         throws ReferentialException, RetrievalServiceEx;
   
}
