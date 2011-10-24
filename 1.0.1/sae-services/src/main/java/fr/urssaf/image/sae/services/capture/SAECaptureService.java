package fr.urssaf.image.sae.services.capture;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
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
    * @throws UnknownHashCodeEx {@link UnknownHashCodeEx}
    */
   UUID capture(List<UntypedMetadata> metadatas, URI ecdeURL)
         throws SAECaptureServiceEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx;
}
