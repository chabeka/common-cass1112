package fr.urssaf.image.sae.services.capture;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Service pour l'opération : capture unitaire
 * 
 * 
 */
public interface SAECaptureService {

   /**
    * 
    * Service pour l'opération : capture unitaire
    * 
    * @param metadatas
    *           liste des métadonnées à archiver
    * @param ecdeURL
    *           url ECDE du fichier numérique à archiver
    * @return Identifiant unique du document dans le SAE
    * 
    * 
    * 
    * @throws SAECaptureServiceEx
    *            exception levée lors de la capture
    * @throws RequiredStorageMetadataEx
    *            les métadonnées obligatoires sont absentes
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            les valeurs des métadonnées ne sont pas du bon type ou au bon
    *            format
    * @throws UnknownMetadataEx
    *            les métadonnées n'existent pas dans le référentiel
    * @throws DuplicatedMetadataEx
    *            les métadonnées sont dupliquées
    * @throws NotSpecifiableMetadataEx
    *            les métadonnées ne sont pas archivables
    * @throws RequiredArchivableMetadataEx
    *            les métadonnées obligatoires pour l'archivage sont absentes
    * @throws EmptyDocumentEx
    *            Le fichier à archiver est vide
    * @throws NotArchivableMetadataEx
    *            les métadonnées ne sont pas archivables
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}
    * @throws CaptureEcdeUrlFileNotFoundEx
    *            {@link CaptureEcdeUrlFileNotFoundEx}
    * @throws CaptureBadEcdeUrlEx
    *            {@link CaptureBadEcdeUrlEx}
    * @throws CaptureBadEcdeUrlEx
    *            {@link CaptureBadEcdeUrlEx}
    * @throws CaptureEcdeUrlFileNotFoundEx
    *            {@link CaptureEcdeUrlFileNotFoundEx}
    * 
    */
   @PreAuthorize("hasRole('archivage_unitaire')")
   UUID capture(List<UntypedMetadata> metadatas, URI ecdeURL)
         throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx;

   /**
    * 
    * Service pour l'opération : capture unitaire. Le fichier a <br>
    * été initialement transmis sous forme de flux binaire.
    * 
    * @param metadatas
    *           liste des métadonnées à archiver
    * @param content
    *           contenu du fichier à archiver
    * @param fileName
    *           nom du fichier à archiver. Sera stocké dans les metadatas
    * 
    * @return Identifiant unique du document dans le SAE
    * 
    * 
    * 
    * @throws SAECaptureServiceEx
    *            exception levée lors de la capture
    * @throws RequiredStorageMetadataEx
    *            les métadonnées obligatoires sont absentes
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            les valeurs des métadonnées ne sont pas du bon type ou au bon
    *            format
    * @throws UnknownMetadataEx
    *            les métadonnées n'existent pas dans le référentiel
    * @throws DuplicatedMetadataEx
    *            les métadonnées sont dupliquées
    * @throws NotSpecifiableMetadataEx
    *            les métadonnées ne sont pas archivables
    * @throws RequiredArchivableMetadataEx
    *            les métadonnées obligatoires pour l'archivage sont absentes
    * @throws EmptyDocumentEx
    *            Le fichier à archiver est vide
    * @throws NotArchivableMetadataEx
    *            les métadonnées ne sont pas archivables
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}
    * @throws EmptyFileNameEx
    *            {@link EmptyFileNameEx}
    */
   @PreAuthorize("hasRole('archivage_unitaire')")
   UUID captureBinaire(List<UntypedMetadata> metadatas, byte[] content,
         String fileName) throws SAECaptureServiceEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         UnknownHashCodeEx, EmptyFileNameEx;

}
