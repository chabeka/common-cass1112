/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;

/**
 * Service centralisant les contrôles des métadonnées
 * 
 */
public interface SaeControleMetadataService {

   /**
    * Cette méthode permet de faire les contrôles suivants :<br>
    * <ul>
    * <li>Vérifier l'existence des métadonnées</li>
    * <li>Vérifier le type/format des métadonnées</li>
    * <li>Vérifier la duplication des métadonnées</li>
    * <li>Vérifier que les valeurs des métadonnées obligatoires sont saisies</li>
    * </ul>
    * 
    * @param metadatas
    *           la liste des métadonnées à vérifier
    * @throws UnknownMetadataEx
    *            Exception levée lorsqu'une métadonnée n'existe pas
    * @throws DuplicatedMetadataEx
    *            Exception levée lorsqu'une métadonnée est définie plusieurs
    *            fois pour le même document
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            Exception levée lorsqu'une métadonnée ne possède pas le bon
    *            format ou le type de valeur
    * @throws RequiredArchivableMetadataEx
    *            Exception levée si une métadonnée obligatoire à l'archivage est
    *            manquante
    * @throws MetadataValueNotInDictionaryEx
    *            Erreur levée si la valeur de la métadonnée ne correspond pas à
    *            une valeur du dictionnaire associé
    */
   void checkUntypedMetadatas(List<UntypedMetadata> metadatas)
         throws UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, RequiredArchivableMetadataEx,
         MetadataValueNotInDictionaryEx;

   /**
    * Cette méthode permet de faire les contrôles suivants :<br/>
    * <ul>
    * <li>vérifier que les métadonnées passées en paramètre sont spécifiables</li>
    * <li>vérifier que l'ensemble des métadonnées obligatoires lors de
    * l'archivage sont présentes</li>
    * </ul>
    * 
    * @param metadatas
    *           Liste des métadonnées à vérifier
    * @throws NotSpecifiableMetadataEx
    *            Exception levée si une métadonnée non spécifiable est présente
    * @throws RequiredArchivableMetadataEx
    *            Exception levée si une métadonnée obligatoire à l'archivage est
    *            manquante
    */
   void checkSaeMetadataForCapture(List<SAEMetadata> metadatas)
         throws NotSpecifiableMetadataEx, RequiredArchivableMetadataEx;

   /**
    * Vérifie l'ensemble des métadonnées obligatoires lors du stockage sont
    * présentes. Cette méthode doit être appelée après l'enrichissement des
    * métadonnées
    * 
    * @param metadatas
    *           la liste des métadonnées à vérifier
    * @return La liste des métadonnées trimées si besoin
    * @throws RequiredStorageMetadataEx
    *            exception levée lorsqu'une métadonnée obligatoire au stockage
    *            n'est pas présente
    */
   List<SAEMetadata> checkMetadataForStorage(List<SAEMetadata> metadatas)
         throws RequiredStorageMetadataEx;
}
