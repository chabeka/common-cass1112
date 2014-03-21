package fr.urssaf.image.sae.services.capture;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;

import javax.activation.DataHandler;

import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
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
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

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
    * @return CaptureResult : objet contenant l'identifiant unique du document
    *         dans le SAE
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
    * @throws MetadataValueNotInDictionaryEx
    *            {@link MetadataValueNotInDictionaryEx}
    * @throws UnknownFormatException
    *            {@link UnknownFormatException}
    * @throws ValidationExceptionInvalidFile
    *            {@link ValidationExceptionInvalidFile}
    * 
    */
   @PreAuthorize("hasRole('archivage_unitaire')")
   CaptureResult capture(List<UntypedMetadata> metadatas, URI ecdeURL)
         throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
         CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
         MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
         UnknownFormatException;

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
    * @return CaptureResult : objet contenant l'identifiant unique du document
    *         dans le SAE
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
    * @throws MetadataValueNotInDictionaryEx
    *            {@link MetadataValueNotInDictionaryEx}
    * @throws UnknownFormatException
    *            {@link UnknownFormatException}
    * @throws ValidationExceptionInvalidFile
    *            {@link ValidationExceptionInvalidFile}
    */
   @PreAuthorize("hasRole('archivage_unitaire')")
   CaptureResult captureBinaire(List<UntypedMetadata> metadatas,
         DataHandler content, String fileName) throws SAECaptureServiceEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         UnknownHashCodeEx, EmptyFileNameEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile;

   /**
    * 
    * Service pour l'opération : capture unitaire
    * 
    * @param metadatas
    *           liste des métadonnées à archiver
    * @param path
    *           chemin absolu du fichier
    * 
    * @return CaptureResult : objet contenant l'identifiant unique du document
    *         dans le SAE
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
    * @throws NotArchivableMetadataEx
    *            les métadonnées ne sont pas archivables
    * @throws RequiredArchivableMetadataEx
    *            les métadonnées obligatoires pour l'archivage sont absentes
    * @throws EmptyDocumentEx
    *            Le fichier à archiver est vide
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}
    * @throws FileNotFoundException
    *            {@link FileNotFoundException}
    * @throws MetadataValueNotInDictionaryEx
    *            {@link MetadataValueNotInDictionaryEx}
    * @throws UnknownFormatException
    *            {@link UnknownFormatException}
    * @throws ValidationExceptionInvalidFile
    *            {@link ValidationExceptionInvalidFile}
    */
   @PreAuthorize("hasRole('archivage_unitaire')")
   CaptureResult captureFichier(List<UntypedMetadata> metadatas, String path)
         throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx, UnknownHashCodeEx,
         FileNotFoundException, MetadataValueNotInDictionaryEx,
         ValidationExceptionInvalidFile, UnknownFormatException;

}
