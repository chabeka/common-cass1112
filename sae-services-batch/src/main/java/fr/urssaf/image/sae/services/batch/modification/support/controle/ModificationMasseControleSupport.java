/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle;

import java.io.File;
import java.util.List;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.batch.modification.support.controle.model.ModificationMasseControlResult;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Composant de contrôle des règles métier sur les métadonnées et les fichiers
 * des documents à archiver dans un traitement de capture de masse
 * 
 */
public interface ModificationMasseControleSupport {

   /**
    * <br>Service permettant de contrôler le fichier et les métadonnées d'un
    * document à modifier dans un traitement de modification de masse.</br>
    * @param document
    *           Modèle métier du document
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    * @return Le bean de resultat du controle.
    * @return ModificationMasseControlResult résultat des controles sur la modification de masse
    * @throws MetadataValueNotInDictionaryEx @{@link MetadataValueNotInDictionaryEx}
    * @throws NotModifiableMetadataEx @{@link NotModifiableMetadataEx}
    * @throws UnknownHashCodeEx @{@link UnknownHashCodeEx}
    * @throws RequiredArchivableMetadataEx @{@link RequiredArchivableMetadataEx}
    * @throws NotSpecifiableMetadataEx @{@link NotSpecifiableMetadataEx}
    * @throws UnknownMetadataEx @{@link UnknownMetadataEx}
    * @throws InvalidValueTypeAndFormatMetadataEx @{@link InvalidValueTypeAndFormatMetadataEx}
    * @throws DuplicatedMetadataEx @{@link DuplicatedMetadataEx}
    * @throws ModificationException @{@link ModificationException}
    * @throws ArchiveInexistanteEx @{@link ArchiveInexistanteEx}
    * @throws UnknownCodeRndEx @{@link UnknownCodeRndEx}
    * @throws ReferentialRndException @{@link ReferentialRndException}
    * @throws UnexpectedDomainException @{@link UnexpectedDomainException}
    * @throws CaptureMasseSommaireDocumentNotFoundException @{@link CaptureMasseSommaireDocumentNotFoundException}
    * @throws EmptyDocumentEx @{@link EmptyDocumentEx}
    */
   ModificationMasseControlResult controleSAEDocumentMetadatas(UntypedDocument document,
         File ecdeDirectory) throws ReferentialRndException, UnknownCodeRndEx, ArchiveInexistanteEx, ModificationException, DuplicatedMetadataEx, 
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, NotSpecifiableMetadataEx, RequiredArchivableMetadataEx, UnknownHashCodeEx, 
         NotModifiableMetadataEx, MetadataValueNotInDictionaryEx, CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx;

   /**
    * <br>Service permettant de contrôler les métadonnées d'un
    * document à modifier dans un traitement de modification de masse.</br>
    * @param item Document à modifier.
    * @param listeMetadataDocument La liste des metadonnées à modifier.
    * @return Le document modifié.
    * @throws UnknownCodeRndEx @{@link UnknownCodeRndEx}
    * @throws ReferentialRndException @{@link ReferentialRndException}
    * @throws InvalidValueTypeAndFormatMetadataEx @{@link InvalidValueTypeAndFormatMetadataEx}
    * @throws UnknownMetadataEx @{@link UnknownMetadataEx}
    * @throws DuplicatedMetadataEx @{@link DuplicatedMetadataEx}
    * @throws NotSpecifiableMetadataEx @{@link NotSpecifiableMetadataEx}
    * @throws RequiredArchivableMetadataEx @{@link RequiredArchivableMetadataEx}
    * @throws UnknownHashCodeEx @{@link UnknownHashCodeEx}
    * @throws NotModifiableMetadataEx @{@link NotModifiableMetadataEx}
    * @throws MetadataValueNotInDictionaryEx @{@link MetadataValueNotInDictionaryEx}
    * @throws ModificationException @{@link ModificationException}
    */
   StorageDocument controleSAEDocumentModification(UntypedDocument item, List<StorageMetadata> listeMetadataDocument) 
         throws UnknownCodeRndEx, ReferentialRndException, InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, 
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, RequiredArchivableMetadataEx, UnknownHashCodeEx, NotModifiableMetadataEx, 
         MetadataValueNotInDictionaryEx, ModificationException;

}
