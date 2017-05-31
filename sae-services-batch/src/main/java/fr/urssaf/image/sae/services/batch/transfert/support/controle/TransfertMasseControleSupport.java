package fr.urssaf.image.sae.services.batch.transfert.support.controle;

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
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

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
    * Controle document pour le transfert
    * @throws NotModifiableMetadataEx 
    * @throws UnknownHashCodeEx 
    * @throws NotSpecifiableMetadataEx 
    * @throws UnknownCodeRndEx 
    * @throws ReferentialRndException 
    * @throws NotModifiableMetadataEx
    * @throws UnknownHashCodeEx
    * @throws NotSpecifiableMetadataEx
    * @throws UnknownCodeRndEx
    * @throws ReferentialRndException
    * 
    */
   public StorageDocument controleSAEDocumentTransfert(UntypedDocument item)
         throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException,
         MappingFromReferentialException, UnknownMetadataEx,
         DuplicatedMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         RequiredArchivableMetadataEx, MetadataValueNotInDictionaryEx,
         ReferentialRndException, UnknownCodeRndEx, NotSpecifiableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx;
}
