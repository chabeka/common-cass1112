/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle;

import java.io.File;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentNotFoundException;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Composant de contrôle des règles métier sur les métadonnées et les fichiers
 * des documents à archiver dans un traitement de capture de masse
 * 
 */
public interface CaptureMasseControleSupport {

   /**
    * Service permettant de contrôler le fichier et les métadonnées d'un
    * document à archiver dans un traitement de capture de masse.<br>
    * Les vérifications suivantes sont effectuées :<br>
    * <ul>
    * <li>Vérification de l'existence du fichier dans l'ECDE</li>
    * <li>Vérification que le fichier n'est pas vide</li>
    * <li>Vérification que les métadonnées existent dans le référentiel des
    * métadonnées</li>
    * <li>Vérification que les métadonnées ne sont pas dupliquées</li>
    * <li>Vérification que le type ou le format de la métadonnée est conforme au
    * référentiel des métadonnées</li>
    * <li>Vérification que les métadonnées sont autorisées à l'archivage</li>
    * <li>Vérification que les métadonnées obligatoires à l'archivage sont bien
    * spécifiées</li>
    * <li>Vérification que la métadonnée <i>TypeHash</i> indique un algorithme
    * de hashage connu</li>
    * <li>Vérification que le <i>hash</i> du fichier est identique à la valeur
    * de la métadonnée <i>hash</i></li>
    * <li>Vérification que la valeur de la métadonnée <i>CodeRND</i> est
    * référencée dans le <b>SAE</b></li>
    * </ul>
    * 
    * @param document
    *           Modèle métier du document
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    * @throws CaptureMasseSommaireDocumentNotFoundException
    *            Le fichier du document n'existe pas dans l'ECDE
    * @throws EmptyDocumentEx
    *            Le fichier du document est vide
    * @throws UnknownMetadataEx
    *            Des métadonnées n'existent pas dans le référentiel des
    *            métadonnées
    * @throws DuplicatedMetadataEx
    *            Des métadonnées sont dupliquées
    * @throws InvalidValueTypeAndFormatMetadataEx
    *            Une métadonnée a un type ou un format non conforme au
    *            référentiel des métadonnées
    * @throws NotSpecifiableMetadataEx
    *            Des métadonnées ne sont pas autorisées à l'archivage
    * @throws RequiredArchivableMetadataEx
    *            Des métadonnées obligatoires à l'archivage ne sont pas
    *            spécifiées
    * @throws UnknownHashCodeEx
    *            La métadonnée TypeHash n'est pas un algorithme de hashage
    *            reconnu par le SAE
    * @throws UnknownCodeRndEx
    *            La métadonnée codeRND n'existe pas
    */
   void controleSAEDocument(UntypedDocument document, File ecdeDirectory)
         throws CaptureMasseSommaireDocumentNotFoundException, EmptyDocumentEx,
         UnknownMetadataEx, DuplicatedMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx, UnknownCodeRndEx, MetadataValueNotInDictionaryEx;

   /**
    * Service permettant de contrôler le fichier et les métadonnées d'un
    * document avant stockage
    * 
    * @param document
    *           Modèle métier du document
    * @throws RequiredStorageMetadataEx
    *            Des métadonnées obligatoires au stockage ne sont pas spécifiées
    */
   void controleSAEDocumentStockage(SAEDocument document)
         throws RequiredStorageMetadataEx;
}
