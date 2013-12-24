package fr.urssaf.image.sae.services.controles;

import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

/**
 * Classe de contrôle pour la capture unitaire et la capture en masse.
 * 
 */
public interface SAEControlesCaptureService {

   /**
    * Cette méthode permet de vérifier que la taille du contenu est supérieure à
    * 0 octet.
    * 
    * @param untypedDocument
    *           Classe représentant un document non typé.
    * @throws EmptyDocumentEx
    *            {@link EmptyDocumentEx}
    */
   void checkUntypedDocument(UntypedDocument untypedDocument)
         throws EmptyDocumentEx;

   /**
    * Cette méthode permet de faire les contrôles suivant : <br>
    * <ul>
    * <br>
    * <li>Vérifier l’existence des métadonnées.</li><br>
    * <li>Vérifier le type/format des métadonnées</li><br>
    * <li>Vérifier la duplication des métadonnées</li><br>
    * <li>Vérifier que les valeurs des métadonnées obligatoire sont saisies.</li>
    * <br>
    * </ul>
    * 
    * @param untypedDocument
    *           {@link UntypedDocument}
    * @throws UnknownMetadataEx
    *            {@link UnknownMetadataEx}
    * @throws DuplicatedMetadataEx
    *            {@link DuplicatedMetadataEx}
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            {@link InvalidValueTypeAndFormatMetadataEx}
    * @throws RequiredArchivableMetadataEx
    *            {@link RequiredArchivableMetadataEx}
    * @throws MetadataValueNotInDictionaryEx
    *            {@link MetadataValueNotInDictionaryEx}
    */
   void checkUntypedMetadata(UntypedDocument untypedDocument)
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx,
         MetadataValueNotInDictionaryEx;

   /**
    * Cette méthode permet de faire les contrôles suivant :<br>
    * <ul>
    * <br>
    * <li>Vérifier lors de l’archivage si les métadonnées spécifiables sont
    * présentes.</li><br>
    * <li>Vérifier que l'ensemble des métadonnées obligatoires lors de
    * l'archivage sont présentes</li><br>
    * </ul>
    * 
    * @param saeDocument
    *           {@link SAEDocument}
    * @throws NotSpecifiableMetadataEx
    *            {@link NotSpecifiableMetadataEx}
    * @throws RequiredArchivableMetadataEx
    *            {@link RequiredArchivableMetadataEx}
    */
   void checkSaeMetadataForCapture(SAEDocument saeDocument)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx;

   /**
    * Cette méthode permet de faire les contrôles suivant :<br>
    * <ul>
    * <br>
    * <li>Vérifier lors de l’archivage si les métadonnées spécifiables sont
    * présentes.</li><br>
    * <li>Vérifier que l'ensemble des métadonnées obligatoires lors de
    * l'archivage sont présentes</li><br>
    * </ul>
    * 
    * @param metadatas
    *           la liste des métadonnées
    * @throws NotSpecifiableMetadataEx
    *            {@link NotSpecifiableMetadataEx}
    * @throws RequiredArchivableMetadataEx
    *            {@link RequiredArchivableMetadataEx}
    */
   void checkSaeMetadataListForCapture(List<SAEMetadata> metadatas)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx;

   /**
    * Vérifie l'ensemble des métadonnées obligatoires lors du stockage sont
    * présentes. Cette méthode doit être appelée après <b>l’enrichissement</b>
    * des métadonnées.
    * 
    * @param sAEDocument
    *           : Classe représentant un document typé de type
    *           {@link SAEDocument} .
    * @throws RequiredStorageMetadataEx
    *            {@link RequiredStorageMetadataEx}
    */
   void checkSaeMetadataForStorage(SAEDocument sAEDocument)
         throws RequiredStorageMetadataEx;

   /**
    * Vérifie la valeur du Hash du document à archiver.
    * 
    * @param saeDocument
    *           : Classe représentant un document typé de type
    *           {@link SAEDocument} .
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}
    */
   void checkHashCodeMetadataForStorage(SAEDocument saeDocument)
         throws UnknownHashCodeEx;

   /**
    * Vérifie l'URL ECDE envoyée au service de <b>Capture de masse</b>.
    * 
    * @param urlEcde
    *           : L'URL ECDE du sommaire.xml.
    * @throws CaptureBadEcdeUrlEx
    *            si l'URL ECDE fournit est incorrecte.
    * @throws CaptureEcdeUrlFileNotFoundEx
    *            si l'URL ECDE fournit pointe sur un fichier inexistant.
    * @throws CaptureEcdeWriteFileEx
    *            si le SAE n'a pas les droits d’écriture.
    */
   void checkBulkCaptureEcdeUrl(String urlEcde) throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx, CaptureEcdeWriteFileEx;

   /**
    * Vérifie l'URL ECDE envoyée au service de <b>Capture unitaire.</b>
    * 
    * @param urlEcde
    *           : L'URL ECDE du fichier à archiver.
    * @throws CaptureBadEcdeUrlEx
    *            si l'URL ECDE fournit est incorrecte.
    * @throws CaptureEcdeUrlFileNotFoundEx
    *            si l'URL ECDE fournit pointe sur un fichier inexistant.
    */
   void checkCaptureEcdeUrl(String urlEcde) throws CaptureBadEcdeUrlEx,
         CaptureEcdeUrlFileNotFoundEx;

   /**
    * Permet de vérifier que la taille du contenu est supérieure<br>
    * à 0 octet et que le nom du fichier est renseigné.
    * 
    * @param untypedDocument
    *           le document à traiter
    * 
    * @throws EmptyDocumentEx
    *            si le contenu est vide (0 octet)
    * @throws EmptyFileNameEx
    *            si le nom de fichier est vide ou rempli d'espaces
    * 
    */
   void checkUntypedBinaryDocument(UntypedDocument untypedDocument)
         throws EmptyDocumentEx, EmptyFileNameEx;

   /**
    * Permet de vérifier si le contenu du fichier n'est pas null
    * 
    * @param content
    *           le contenu du fichier
    * 
    * @throws EmptyDocumentEx exception levée si le document est vide
    */
   void checkBinaryContent(byte[] content) throws EmptyDocumentEx;

   /**
    * Permet de vérifier que le nom de fichier est bien renseigné.
    * 
    * @param fileName
    *           le nom du fichier
    * 
    * @throws EmptyFileNameEx exception levée si le document est vide
    */
   void checkBinaryFileName(String fileName) throws EmptyFileNameEx;

   /**
    * Permet de comparer le hash de référence et celui contenu dans les
    * métadonnées
    * 
    * @param saeMetadatas
    *           la liste des métadonnées
    * @param refHash
    *           le hash de référence
    * @throws UnknownHashCodeEx
    *            Erreur levée si les deux hash ne correspondent pas
    */
   void checkHashCodeMetadataListForStorage(List<SAEMetadata> saeMetadatas,
         String refHash) throws UnknownHashCodeEx;
   
   /**
    * Méthode chargée d'appeler le service de contrôle des formats.
    * 
    * @param saeDocument
    *          classe representant un document - paramètre obligatoire
    * @param listControlProfil
    *          Liste des profils de contrôle à appliquer
    * @throws UnknownFormatException : Le format est inconnu du référentiel des formats.
    * @throws ValidationExceptionInvalidFile : Erreur dans la validation du fichier.
    */
   void checkFormat(SAEDocument saeDocument, List<FormatControlProfil> listControlProfil) throws UnknownFormatException, ValidationExceptionInvalidFile;
   

}
