/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;

/**
 * Classe de contrôle des métadonnées.
 * 
 * @author rhofir.
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
    * <li>Vérifier que les valeurs des métadonnées obligatoire sont saisies.</li><br>
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
    * @throws RequiredArchivableMetadataEx {@link RequiredArchivableMetadataEx} 
    */
   void checkUntypedMetadata(UntypedDocument untypedDocument)
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx;

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
    * Cette méthode permet de vérifier que l'ensemble des métadonnées
    * obligatoires lors du stockage sont présentes. Cette méthode doit être
    * appelée après <b>l’enrichissement</b> des métadonnées.
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
    * Cette méthode permet de vérifier la valeur du Hash du document à archiver.
    * 
    * @param saeDocument
    *           : Classe représentant un document typé de type
    *           {@link SAEDocument} .
    * @throws UnknownHashCodeEx
    *            {@link UnknownHashCodeEx}
    */
   void checkHashCodeMetadataForStorage(SAEDocument saeDocument)
         throws UnknownHashCodeEx;

}
